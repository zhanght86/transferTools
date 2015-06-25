package client.servlet;

import bean.Pack;
import client.dao.PackDao;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import schedule.impl.UpdateFileTask;
import service.CachePool;
import service.DownloadService;
import util.FilePathUtils;
import util.Sequence;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 * Created by QPing on 2015/3/27.
 */
public class SvnPackServlet extends BaseServlet {

    private String PATH_PACKLIST_CREATE;        // 文件上传的路径
    private long UPLOAD_SIZE_MAX = -1;          // 文件上传最大为2G


    UpdateFileTask task = new UpdateFileTask("手动请求更新");
    private String batStr;

    @Override
    public void init() throws ServletException {
        super.init();
        PATH_PACKLIST_CREATE = getServletContext().getRealPath("/upload/packlist/create");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String method = req.getParameter("method");

        if ("queryPage".equals(method)) {
            queryPage(req, resp);
        } else if ("create".equals(method)) {
            create(req, resp);
        } else if("delete".equals(method)) {
            delete(req, resp);
        } else if ("release".equals(method)){
            release(req, resp);
        } else if("queryDownloadProgress".equals(method)){
            queryDownloadProgress(req, resp);
        } else if("refreshDownload".equals(method)){
            refreshDownload(req, resp);
        } else if("sysemOverview".equals(method)){
            sysemOverview(req, resp);
        }   else if("deploy".equals(method)){
            deploy(req, resp);
        }
    }

    private void runShell(String projectPath, int no, String zipFileName, String webRoot, String tempDir) throws IOException, InterruptedException {
        String filePath =   PATH_PACKLIST_CREATE + File.separator + no + File.separator + zipFileName;
        String fileNameWithOutExt = zipFileName.substring(0, zipFileName.lastIndexOf("."));


        System.out.println("zipfilePath: " + filePath)  ;
        System.out.println("fileNameWithOutExt: " + fileNameWithOutExt)  ;
        System.out.println("tempDir: " + tempDir)  ;
        System.out.println("webRoot: " + webRoot)  ;
        System.out.println("projectPath: " + projectPath)  ;
        System.out.println("updateLogPath: " + projectPath)  ;

        // 解压压缩包到临时目录
        StringBuffer shell = new StringBuffer();

        shell.append("unzip -o " + filePath + " -d " + tempDir + "\n")  ;
        shell.append("cp -rf " + tempDir + fileNameWithOutExt + "/*  " + webRoot + "\n")  ;
        shell.append("echo \"update success\" `date -d now +%Y%m%d%H%M%S`>> " + projectPath + "update.log")  ;

        wirteFile(tempDir, "update.sh", shell.toString());
        Runtime.getRuntime().exec( "chmod +x " + tempDir + "update.sh").waitFor();
        Runtime.getRuntime().exec(tempDir + "update.sh").waitFor();
    }

    private void runBat(String projectPath, int no, String zipFileName, String webRoot, String tempDir) throws IOException, InterruptedException {
        String filePath =   PATH_PACKLIST_CREATE + File.separator + no + File.separator + zipFileName;
        String fileNameWithOutExt = zipFileName.substring(0, zipFileName.lastIndexOf("."));
        System.out.println("zipfilePath: " + filePath)  ;
        System.out.println("fileNameWithOutExt: " + fileNameWithOutExt)  ;
        System.out.println("tempDir: " + tempDir)  ;
        System.out.println("webRoot: " + webRoot)  ;
        System.out.println("projectPath: " + projectPath)  ;
        System.out.println("updateLogPath: " + projectPath)  ;

        // 解压压缩包到临时目录
        StringBuffer shell = new StringBuffer();

        shell.append("unzip -o " + filePath + " -d " + tempDir + "\n")  ;
        shell.append("cp -rf " + tempDir + fileNameWithOutExt + "/*  " + webRoot + "\n")  ;
        shell.append("echo \"update success\" `date -d now +%Y%m%d%H%M%S`>> " + projectPath + "update.log")  ;
        shell.append("exit");
      //  shell.append("echo \"update success\" `date -d now +%Y%m%d%H%M%S`>> " + projectPath + "update.log")  ;
        wirteFile(tempDir, "update.bat", shell.toString());
      //  String cmd = "cmd /c start F:\\database_backup\\ngx_backup\\"+ batName + ".bat";// pass
        Runtime.getRuntime().exec(tempDir + "update.bat").waitFor();
        System.out.println("child thread donn");


    }


    private void deploy(HttpServletRequest req, HttpServletResponse resp) {
        try {
            Pack pack = (Pack) this.parseForm(req, Pack.class);
            int no = pack.getNo();
            String fileName = pack.getFilename();

            if(fileName.indexOf(".zip") == -1){
                throw new Exception("无法解压非zip文件");
            }

            String systemName = CachePool.self.getOsname();
            String projectPath =  FilePathUtils.getInstance().getProjectClassPath();

            String filePath =   PATH_PACKLIST_CREATE + File.separator + no + File.separator + fileName;
            String tempDir = FilePathUtils.getInstance().getProjectClassPath() + "temp/";

            if(systemName.toUpperCase().contains("WINDOW")) {
                String webRoot  = "C:\\home\\phis";  //TODO
                runBat(projectPath, no, fileName, webRoot, tempDir) ;
            } else{
                String webRoot  = "/home/phis/";  //TODO
                runShell(projectPath, no, fileName, webRoot, tempDir) ;
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


    private void release(HttpServletRequest req, HttpServletResponse resp) {

        if(!"center".equals(CachePool.self.getNodetype())){
            this.returnError(resp, "节点类型为中心时，才可发布文件！");
            return;
        }

        PackDao dao = new PackDao();

        try {
            Pack pack = (Pack) this.parseForm(req, Pack.class);
            dao.release(pack);
            CachePool.getInstance().addRelease(pack);
            this.returnSuccess(resp, "发布成功!");
        } catch (Exception e) {
            e.printStackTrace();
            this.returnError(resp, e.getMessage());
        }finally {
            dao.close();
        }
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp) {
        PackDao dao = new PackDao();
        try {
            Pack pack = (Pack) this.parseForm(req, Pack.class);
            dao.delete(pack);
            CachePool.getInstance().deleteRelease(pack.getNo());
            this.returnSuccess(resp, "删除成功!");
        } catch (Exception e) {
            e.printStackTrace();
            this.returnError(resp, e.getMessage());
        }finally {
            dao.close();
        }
    }

    private void create(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PackDao dao = new PackDao();
        try {
            Pack pack = parseBeanAndTransferFile(req);
            dao.insert(pack);
            this.returnSuccess(resp);
        } catch (Exception e) {
            e.printStackTrace();
            this.returnError(resp, e.getMessage());
        }finally {
            dao.close();
        }
    }

    /**
     * 将表单对象转换为 bean.Pack 的对象，并保存文件
     * @param req
     * @throws Exception
     */
    private Pack parseBeanAndTransferFile(HttpServletRequest req) throws Exception {
        System.out.println("开始上传");
        DiskFileItemFactory f = new DiskFileItemFactory();      // 磁盘对象
        String tempDir = FilePathUtils.getInstance().getProjectClassPath() + "temp/";
        File tempDirFile = new File(tempDir);
        if(!tempDirFile.exists()){
            tempDirFile.mkdirs();
        }
        f.setRepository(tempDirFile);                           // 设置临时目录
        f.setSizeThreshold(8192);                               // 8k的缓冲区,文件大于8K则保存到临时目录
        ServletFileUpload upload = new ServletFileUpload(f);    // 声明解析request的对象
        upload.setHeaderEncoding("UTF-8");                      // 处理文件名中文
        upload.setFileSizeMax(UPLOAD_SIZE_MAX);                 // 设置每个文件最大为5M
        upload.setSizeMax(UPLOAD_SIZE_MAX);                     // 一共最多能上传10M
        String path = PATH_PACKLIST_CREATE;                     // 获取文件要保存的目录

        int no = -1;                                            // 序号,由系统生成每个增量包唯一
        String comment= null;                                   // 注释
//        String version = null;                                  // 版本号，和序号的区别是由人工输入
        String assesstime = null;                               // 预计更新时间
        String area = null;                                     // 发布到哪个区域
        String fileName = null;                                 // 文件名

        long size = 0;                                          // 上传的文件大小

        boolean uploadSuccess = false;
        List<FileItem> list = upload.parseRequest(req);         // 解析
        for (FileItem ff : list) {
            if (ff.isFormField()) {
                String key = ff.getFieldName();
                String value = ff.getString("UTF-8");           //处理中文
                if("comment".equals(key)){
                    comment = value;
                }
//                if("version".equals(key)){
//                    version = value;
//                }
                if("assesstime".equals(key)){
                    assesstime = value;
                }
                if("area".equals(key)){
                    area = value;
                }
            } else {
                no = Sequence.getInstance().getSeqNo("packno");
                size = ff.getSize();
                fileName = ff.getName();
                String dirPath = path + File.separator + no + File.separator;
                File dirPathFile = new File(dirPath);
                if(!dirPathFile.exists()){
                    dirPathFile.mkdirs();
                }
                fileName = fileName.substring(fileName.lastIndexOf(File.separator) + 1);    //解析文件名
                FileUtils.copyInputStreamToFile(                                            //直接使用commons.io.FileUtils
                        ff.getInputStream(),
                        new File(dirPath + fileName));
                uploadSuccess = true;
            }
        }

        if(!uploadSuccess){
            return null;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Pack pack = new Pack();
        pack.setNo(no);
        pack.setFilename(fileName);
//        pack.setVersion(version);
        pack.setSize(size + "");
        pack.setComment(comment);
        pack.setCreatetime(dateFormat.format(new Date()));
        pack.setAssesstime(assesstime);
        pack.setReleasetime("");
        pack.setUpdatenum(0);
        pack.setReleasenum(0);
        pack.setArea(area);
        pack.setStatus(Pack.STATUS_ADD);
        return pack;
    }


    private void queryPage(HttpServletRequest req, HttpServletResponse resp) {
        String type = req.getParameter("type");
        String page = req.getParameter("page");
        String rows = req.getParameter("rows");

        page = page == null ? "1" : page;
        rows = rows == null ? "10" : rows;

        PackDao dao = new PackDao();
        List<Pack> list = null;
        HashMap<String, Object> result = new HashMap<String, Object>();
        try {
            list = dao.queryPage(type, page, rows, null, " no desc");
            int total = dao.queryTotal();
            result.put("rows", list);
            result.put("total", total);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        this.writeJson(resp, result);
    }


    private void queryDownloadProgress(HttpServletRequest req, HttpServletResponse resp) {
        // 不考虑分页
//        String type = req.getParameter("type");
//        String page = req.getParameter("page");
//        String rows = req.getParameter("rows");
//
//        page = page == null ? "1" : page;
//        rows = rows == null ? "10" : rows;
//
        HashMap<String, Object> result = new HashMap<String, Object>();
        Vector<Pack> list = CachePool.getInstance().getUpdateList();
        result.put("rows", list);
        result.put("total", list.size());
        this.writeJson(resp, result);
    }


    private void refreshDownload(HttpServletRequest req, HttpServletResponse resp) {

        try {
            task.executeProxy();
            this.returnSuccess(resp, "刷新列表成功!");
        } catch (Exception e) {
            e.printStackTrace();
            this.returnError(resp, e.getMessage());
        }
    }


    private void sysemOverview(HttpServletRequest req, HttpServletResponse resp) {
        HashMap<String,String> hashMap = new HashMap<String, String>();

        hashMap.put("releaseNum", "" + CachePool.getInstance().getReleaseList().size() + "个");
        hashMap.put("releaseNo", "" + CachePool.getInstance().getReleaseMaxNo());

        hashMap.put("updateNum", "" + CachePool.getInstance().getUpdateList().size()+ "个");
        hashMap.put("updateNo", "" + CachePool.getInstance().getLastUpdateFileNo() );

        boolean isBusy = DownloadService.getInstance().isBusy();
        String result = "比较空闲";
        if(isBusy){
            result = "正在下载，请不要打扰它工作！";
        }

        hashMap.put("busy", result);

        this.writeJson(resp, hashMap);
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
