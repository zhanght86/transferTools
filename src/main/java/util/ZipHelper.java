package util;

import org.apache.log4j.Logger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;

/**
 * Created by QPing on 2015/2/28.
 */
public class ZipHelper {

    private final int BUFFER = 1024;
    protected final Logger log = Logger.getLogger(ZipHelper.class);

    /**
     * inputFile 压缩到 zipFileName
     *
     * @param srcPathName 需要压缩的文件夹路径，如 C://svn//pack
     * @param zipFilePath 压缩文件路径及文件名，如 C://1.zip
     * @return 压缩后的文件路径
     */
    public String compress(String srcPathName, String zipFilePath) throws Exception {
        log.info("压缩中...");

        File srcdir = new File(srcPathName);
        File zipFile = new File(zipFilePath);

        if (!srcdir.exists())
            throw new RuntimeException(srcPathName + "不存在！");

        Project prj = new Project();
        Zip zip = new Zip();
        zip.setProject(prj);
        zip.setDestFile(zipFile);
        FileSet fileSet = new FileSet();
        fileSet.setProject(prj);
        fileSet.setDir(srcdir);
        //fileSet.setIncludes("**/*.java"); 包括哪些文件或文件夹 eg:zip.setIncludes("*.java");
        //fileSet.setExcludes(...); 排除哪些文件或文件夹
        zip.addFileset(fileSet);

        zip.execute();
        log.info("压缩完成");

        return zipFilePath;
    }

    /**
     * 解压压缩包
     *
     * @param zipFilePath 压缩包路径
     * @param destFileDir 解压到哪个路径
     * @return
     */
    public String unCompress(String zipFilePath, String destFileDir) throws Exception {
        log.info("解压中...");
        ZipFile zipFile = new ZipFile(zipFilePath);
        Enumeration e = zipFile.getEntries();
        ZipEntry zipEntry = null;
        while (e.hasMoreElements()) {
            zipEntry = (ZipEntry) e.nextElement();
            log.info("解压文件： " + zipEntry.getName());
            if (zipEntry.isDirectory()) {
                String name = zipEntry.getName().trim();
                name = name.substring(0, name.length() - 1);
                File f = new File(destFileDir + File.separator + name);
                if (!f.exists()) {
                    f.mkdir();
                }

            } else {
                String fileName = zipEntry.getName();
                fileName = fileName.replace('\\', '/');
                if (fileName.indexOf("/") != -1) {
                    createDirectory(destFileDir, fileName.substring(0,
                            fileName.lastIndexOf("/")));
                    fileName = fileName
                            .substring(fileName.lastIndexOf("/") + 1);
                }
                File f = new File(destFileDir + File.separator
                        + zipEntry.getName());
                f.createNewFile();
                InputStream in = zipFile.getInputStream(zipEntry);
                FileOutputStream out = new FileOutputStream(f);
                byte[] buf = new byte[BUFFER];
                int c;
                while ((c = in.read(buf)) != -1) {
                    out.write(buf, 0, c);
                }
                in.close();
                out.close();
            }
        }
        log.info("解压完成");
        return destFileDir;
    }

    private void createDirectory(String directory, String subDirectory) throws Exception{
        String dir[];
        File fl = new File(directory);
        if (subDirectory == "" && fl.exists() != true) {
            fl.mkdir();
        } else if (subDirectory != "") {
            dir = subDirectory.replace('\\', '/').split("/");
            for (int i = 0; i < dir.length; i++) {
                File subFile = new File(directory + File.separator + dir[i]);
                if (subFile.exists() == false) {
                    subFile.mkdir();
                }
                directory += File.separator + dir[i];
            }
        }
    }
}
