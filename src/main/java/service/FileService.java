package service;

import org.apache.log4j.Logger;
import util.Prop;
import util.ZipHelper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by QPing on 2015/3/5.
 */
public class FileService{
    protected final Logger log = Logger.getLogger(FileService.class);

    private final static String COMPRESS_DIR_NAME = "compress"; //默认压缩根路径
    private final static String COMPRESS_FILE_SUFFIX = ".zip";   //默认压缩文件后缀名
    private final static String EXTRACT_DIR_NAME = "extract";   //默认解压根路径

    private String workDir = Prop.getInstance().get("workdir");
    private ZipHelper zipHelper = new ZipHelper();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * 将文件打包成一个压缩文件的增量包
     * @param srcFilePath
     * @param version 版本号
     */
    public String doPack(String srcFilePath, String version) {
        log.info("begin pack");

        String dateStr = dateFormat.format(new Date());
        String zipFilePath = workDir + File.separator + COMPRESS_DIR_NAME + File.separator + dateStr + "_" + version + COMPRESS_FILE_SUFFIX;

        File outDir = new File(workDir + File.separator +COMPRESS_DIR_NAME);
        if(!outDir.exists()){
            outDir.mkdirs();
        }
        try {
            return zipHelper.compress(srcFilePath, zipFilePath);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 解压缩增量包
     * @param zipFilePath
     * @return 解压后的文件夹路径
     */
    public String unPack(String zipFilePath, String destFileDir) {
        if(null == destFileDir || "".equals(destFileDir)){
            destFileDir = workDir + File.separator + EXTRACT_DIR_NAME;
        }
        //以压缩包的名字创建文件夹，并解压到该文件夹类
        String zipDeal = zipFilePath.replace('\\', '/');
        int beginIdx = zipDeal.lastIndexOf('/');
        int length = zipDeal.length();
        String zipFileName = zipDeal.substring(beginIdx, length).replace(".zip", "");
        destFileDir = destFileDir + File.separator + zipFileName;
        File destDir = new File(destFileDir);
        if(!destDir.exists()){
            destDir.mkdirs();
        }
        try {
            return zipHelper.unCompress(zipFilePath, destFileDir);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
