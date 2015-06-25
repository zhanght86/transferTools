package server.servlet;

import bean.Node;
import bean.Pack;
import client.bean.FileListResult;
import client.dao.NodeSelfDao;
import client.dao.PackDao;
import client.httpclient.common.FormPost;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import schedule.impl.UpdateFileTask;
import service.CachePool;
import service.DownloadService;
import util.FilePathUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by QPing on 2015/6/15.
 */
public class CommandServlet  extends BaseServlet {


    private String PATH_PACKLIST_CREATE;        // 文件上传的路径
    private String PATH_TEMP;        // 文件上传的路径

    @Override
    public void init() throws ServletException {
        super.init();
        PATH_PACKLIST_CREATE = getServletContext().getRealPath("/upload/packlist/create");
        PATH_TEMP = getServletContext().getRealPath("/temp");
    }

    protected final Logger log = Logger.getLogger(CommandServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
       this.doPost(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String method = req.getParameter("method");

        if(method.equals("register")){
            // 接收请求，更新本节点的父节点IP和父节点端口
            register(req, resp);
        }else if(method.equals("update")){
            // 接收请求，更新指定的文件
            update(req, resp);
        }else if(method.equals("checkUpdate")){
            // 检查更新是否完成
            checkUpdate(req, resp);
        }else if(method.equals("deploy")){
            // 部署
            deploy(req, resp);
        }else if(method.equals("execBat")){
            // 执行脚本
            execBat(req, resp);
        }else if(method.equals("ls")){
            // 显示节点中的文件路径
            ls(req, resp);
        }
    }

    private void ls(HttpServletRequest req, HttpServletResponse resp) {

    }

    private void execBat(HttpServletRequest req, HttpServletResponse resp) {

    }

    private void deploy(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String webRoot = req.getParameter("webRoot");
            Pack pack = (Pack) this.parseForm(req, Pack.class);
            int no = pack.getNo();
            String fileName = pack.getFilename();

            if(fileName.indexOf(".zip") == -1){
                throw new Exception("无法解压非zip文件");
            }

            String systemName = CachePool.self.getOsname();
            String transferProjectPath =  FilePathUtils.getInstance().getProjectClassPath();

            String tempDir = PATH_TEMP + File.separator;

            if(systemName.toUpperCase().contains("WINDOW")) {
                runBat(transferProjectPath, no, fileName, webRoot, tempDir) ;
            } else{
                runShell(transferProjectPath, no, fileName, webRoot, tempDir) ;
            }

            // 标志增量包已经执行
            PackDao dao = new PackDao();
            try {
                dao.deploy(pack);
            } catch (Exception e) {
                e.printStackTrace();
                this.returnError(resp, e.getMessage());
            }finally {
                dao.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            this.returnError(resp, e.getMessage());
        }
    }


    private void checkUpdate(HttpServletRequest req, HttpServletResponse resp) {

        try {
            log.info("检查下载状态");

            Pack pack = (Pack) this.parseForm(req, Pack.class);

            int flag = CachePool.getInstance().checkUpdate(pack);
            if(flag == 1){
                returnSuccess(resp);
            }else{
                returnError(resp, flag + "" , flag == 2 ? "正在下载" : "未加入下载列表");
            }

        } catch (Exception e) {
            e.printStackTrace();
            returnError(resp, "失败" + e.getMessage());
        }

    }

    private void update(HttpServletRequest req, HttpServletResponse resp) {


        String parentip = req.getParameter("parentip");
        String parentport = req.getParameter("parentport");

        Node self = CachePool.getInstance().self;
        String ip = self.getParentip();
        String port = self.getParentport() + "";
        String inviteCode = self.getInvitecode();

        if(!parentip.equals(ip) || !parentport.equals(port)){
            returnError(resp, "不符合当前注册的父节点，请重新调用注册接口");
        }

        PackDao dao = new PackDao();

        try {

            List<Pack> list = new ArrayList<Pack>();
            Pack pack = (Pack) this.parseForm(req, Pack.class);
            list.add(pack);

            log.info("请求文件列表成功,回写状态");
            // 增加到下载队列中
            List<Pack> listSure = CachePool.getInstance().addDownloadList(list);
            if(null == listSure){
                log.info("文件列表没有发生更新");
            }else{
                log.info("新增下载文件数:" +listSure.size());
            }
            // 保存到数据库中
            dao.insertList(listSure);
            // 通知下载服务开始干活
            DownloadService.getInstance().wakeUpHi();

            returnSuccess(resp);
        } catch (Exception e) {
            e.printStackTrace();
            returnError(resp, "失败" + e.getMessage());
        } finally {
            dao.close();
        }
    }

    private void register(HttpServletRequest req, HttpServletResponse resp) {
        String parentid = req.getParameter("parentid");
        String parentip = req.getParameter("parentip");
        String parentport = req.getParameter("parentport");
        String invitecode = "from_command_servlet";

        NodeSelfDao dao = new NodeSelfDao();
        try {
            int port = Integer.parseInt(parentport);
            dao.updateParent(parentid, parentip, port, invitecode);
            returnSuccess(resp, "注册成功!");
        } catch (Exception e) {
            e.printStackTrace();
            returnError(resp, e.getMessage());
        }finally {
            dao.close();
        }
    }


    private void runShell(String transferProjectPath, int no, String zipFileName, String webRoot, String tempDir) throws IOException, InterruptedException {
        String filePath =   PATH_PACKLIST_CREATE + File.separator + no + File.separator + zipFileName;
        String fileNameWithOutExt = zipFileName.substring(0, zipFileName.lastIndexOf("."));

        System.out.println("zipfilePath: " + filePath)  ;
        System.out.println("fileNameWithOutExt: " + fileNameWithOutExt)  ;
        System.out.println("tempDir: " + tempDir)  ;
        System.out.println("webRoot: " + webRoot)  ;
        System.out.println("transferProjectPath: " + transferProjectPath)  ;
        System.out.println("updateLogPath: " + transferProjectPath)  ;

        // 解压压缩包到临时目录
        StringBuffer shell = new StringBuffer();

        shell.append("unzip -o " + filePath + " -d " + tempDir + "\n")  ;
        shell.append("cp -rf " + tempDir + fileNameWithOutExt + "/*  " + webRoot + "\n")  ;
        shell.append("echo \"update success\" `date -d now +%Y%m%d%H%M%S`>> " + transferProjectPath + "update.log")  ;

        wirteFile(tempDir, "update.sh", shell.toString());
        Runtime.getRuntime().exec( "chmod +x " + tempDir + "update.sh").waitFor();
        Runtime.getRuntime().exec(tempDir + "update.sh").waitFor();
    }

    private void runBat(String transferProjectPath, int no, String zipFileName, String webRoot, String tempDir) throws IOException, InterruptedException {
        String filePath =   PATH_PACKLIST_CREATE + File.separator + no + File.separator + zipFileName;
        String fileNameWithOutExt = zipFileName.substring(0, zipFileName.lastIndexOf("."));
        System.out.println("zipfilePath: " + filePath)  ;
        System.out.println("fileNameWithOutExt: " + fileNameWithOutExt)  ;
        System.out.println("tempDir: " + tempDir)  ;
        System.out.println("webRoot: " + webRoot)  ;
        System.out.println("transferProjectPath: " + transferProjectPath)  ;
        System.out.println("updateLogPath: " + transferProjectPath)  ;

        // 解压压缩包到临时目录
        StringBuffer shell = new StringBuffer();

        shell.append("unzip -o " + filePath + " -d " + tempDir + "\n")  ;
        shell.append("cp -rf " + tempDir + fileNameWithOutExt + "/*  " + webRoot + "\n")  ;
        shell.append("echo \"update success\" `date -d now +%Y%m%d%H%M%S`>> " + transferProjectPath + "update.log")  ;
        shell.append("exit");
        //  shell.append("echo \"update success\" `date -d now +%Y%m%d%H%M%S`>> " + transferProjectPath + "update.log")  ;
        wirteFile(tempDir, "update.bat", shell.toString());
        //  String cmd = "cmd /c start F:\\database_backup\\ngx_backup\\"+ batName + ".bat";// pass
        Runtime.getRuntime().exec(tempDir + "update.bat").waitFor();
        System.out.println("child thread donn");
    }

    private boolean wirteFile(String path,String fileName, String batStr) throws IOException {
        File unzipShellFile = new File(path + fileName);
        FileOutputStream in = new FileOutputStream(unzipShellFile);
        byte bt[] = new byte[1024];
        bt = batStr.toString().getBytes();
        in.write(bt, 0, bt.length);
        in.close();
        return true;
    }



}
