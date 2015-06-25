package schedule.impl;

import bean.Node;
import bean.Pack;
import client.bean.FileListResult;
import client.dao.PackDao;
import client.httpclient.common.FormPost;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import schedule.TaskBase;
import service.CachePool;
import service.DownloadService;

import java.util.ArrayList;
import java.util.List;

/**
 * 定时执行更新增量包的任务
 * Created by QPing on 2015/1/19.
 */
public class UpdateFileTask extends TaskBase {

    protected final Logger log = Logger.getLogger(UpdateFileTask.class);

    public UpdateFileTask(){
        super();
    }

    public UpdateFileTask(String name) {
        super();
        this.setName(name);
    }

    @Override
    public void execute() {
        // 如果没有配置好父节点，端口，注册码，本节点的名称，部署类别，节点类型，区域，则不发起请求
        // 如果节点类型是中心，也会返回false，因为中心是没有父节点的，不需要请求更新
        if(!CachePool.isClient()){
            log.info("当前节点类型无法下载");
            return;
        }

        downloadList();

    }

    private void downloadList(){
        PackDao dao = new PackDao();

        Node self = CachePool.getInstance().self;
        String ip = self.getParentip();
        String port = self.getParentport() + "";
        String inviteCode = self.getInvitecode();
        int fileNo = CachePool.getInstance().getLastUpdateFileNo();

        try {

            log.info("向父节点请求更新文件列表:" + ip);

            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("nodeid", self.getNodeid()));
            nvps.add(new BasicNameValuePair("name", self.getName()));
            nvps.add(new BasicNameValuePair("area", self.getArea()));
            nvps.add(new BasicNameValuePair("ip", self.getIp()));
            nvps.add(new BasicNameValuePair("port", self.getPort() + ""));
            nvps.add(new BasicNameValuePair("mac", self.getMac()));
            nvps.add(new BasicNameValuePair("invitecode", inviteCode));
            nvps.add(new BasicNameValuePair("fileno", fileNo + ""));

            FileListResult result = (FileListResult) FormPost.send(ip, port, "/transfer/client/servlet/DownloadServlet?method=downloadList", nvps, FileListResult.class);

            if (result != null && (result.code.equals("1"))){
                log.info("请求文件列表成功,回写状态");
                // 增加到下载队列中
                List<Pack> list = CachePool.getInstance().addDownloadList(result.list);
                if(null == list){
                    log.info("文件列表没有发生更新");
                }else{
                    log.info("新增下载文件数:" +list.size());
                }
                // 保存到数据库中
                dao.insertList(list);
            } else {

                log.error(result.msg);
            }

            // 通知下载服务开始干活
            DownloadService.getInstance().wakeUpHi();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
    }


}
