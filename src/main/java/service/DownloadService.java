package service;

import bean.Pack;
import client.dao.PackDao;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import util.FilePathUtils;
import util.HttpDownloader;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 下载服务
 * 将 CachePool 中的 fileUpdateList 开始下载，完成后删除 fileUpdateList 中的记录，
 * Created by QPing on 2015/5/5.
 */
public class DownloadService {
    protected final Logger log = Logger.getLogger(DownloadService.class);

    boolean isClose; // 是否关闭下载服务
    boolean isBusy; //是否正在很努力的干活


    private String PATH_LOCAL_WEBROOT;    // 文件下载保存的路径
    private String PATH_DOWNLOAD = "upload/packlist/create/";

    // 记录文件是否重复下载
    private int preFileNo = -1;
    private int preDownloadTime = 0;

    /**
     * 通过这个方法对下载服务说：
     *          “ 睡你MB，起来嗨 ”
     */
    public void wakeUpHi() {
        log.info("开始下载进程");
        if(isBusy || isClose)
            return;

        isBusy = true;

        preFileNo = -1;
        preDownloadTime = 0;

        try{
            if(!CachePool.needDownload()){
                log.info("并没有需要下载的东西。");
            }
            while(CachePool.needDownload()){
                startupWork();
            }
        }catch (Exception e){
            log.error(e);
        }

        // 忙完了
        isBusy = false;

    }

    private void startupWork() throws Exception {
        Pack p = CachePool.getInstance().getUpdate();
        log.info("start download file: " + p.getNo() + " | " + p.getFilename());

        // 判断列表中的记录是否正确
        if(StringUtils.isBlank(p.getFilename()) || p.getNo() < 0){
            throw new Exception("file cannot download without filename or no");
        }

        // 记录文件下载次数
        if(preFileNo == p.getNo()){
            preDownloadTime ++;
        }else{
            preFileNo = p.getNo();
        }


        // 准备请求路径和下载路径
        String ip = CachePool.self.getParentip();
        int port = CachePool.self.getParentport();

        // 将文件名转码，防止有中文和空格
        String filename = URLEncoder.encode(p.getFilename(), "utf-8").replaceAll("\\+", "%20");
        String destUrl = "http://" + ip + ":"+ port + "/transfer/" + PATH_DOWNLOAD + p.getNo() + "/" + filename;

        String savePath = PATH_LOCAL_WEBROOT + PATH_DOWNLOAD + p.getNo() + File.separator +  p.getFilename();

        // 开始下载
        HttpDownloader downloader = new HttpDownloader(destUrl, savePath);
        boolean downSuccess = downloader.download();
        if(downSuccess){

            PackDao dao = new PackDao();
            try {
                dao.release(p);
                CachePool.getInstance().removeUpdate(p);
                CachePool.getInstance().addRelease(p);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dao.close();
            }
        }else{
            // 下载失败5次后停止下载，直到下一次唤醒下载服务
            if(preDownloadTime > 5){
                throw new Exception("下次文件连续5次发生错误，停止本次下载。 ");
            }
        }
    }

    public static class DownloadServiceHolder{
        public static DownloadService instance = new DownloadService();
    }

    public static DownloadService getInstance(){
        return DownloadServiceHolder.instance;
    }

    private DownloadService(){
        PATH_LOCAL_WEBROOT = FilePathUtils.getInstance().getProjectClassPath() + "webapp/";

        log.info("正在开启下载服务...");
        log.info("文件更新下载保存的路径为：" + PATH_LOCAL_WEBROOT + "   " + PATH_DOWNLOAD);
        if(!CachePool.isClient()){
            log.info("节点不是一个标准客户端，无法享受下载服务!");
            isClose = true;
        }
    }

    public boolean isBusy(){
        return isBusy;
    }
}
