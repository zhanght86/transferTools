package util;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;

/**
 * 获取文件路径的一些方法
 * Created by QPing on 2015/3/27.
 */
public class FilePathUtils {
//    public static void main(String[] args) {
//        FilePathUtils utils = FilePathUtils.getInstance();
//        System.out.println(utils.getProjectClassPath());
//        System.out.println(utils.getCurrentClassPath());
//        System.out.println(utils.getUserDir());
//        System.out.println(utils.getResourcePath());
//    }

    // 如果工程打成jar包后，获取项目的路径方法就不同了
    // 在打jar包时，把这个值设为true
    private boolean isJar = true;

    private FilePathUtils(){

    }

    private static class FilePathUtilsHolder{
        public static FilePathUtils instance = new FilePathUtils();
    }
    public static FilePathUtils getInstance(){
        return FilePathUtilsHolder.instance;
    }
    /**
     * 获取src目录下文件的方法
     * @return
     */
    public static String getResourcePath() {
        String className = FilePathUtils.class.getName();
        String classNamePath = className.replace(".", "/") + ".class";
        URL is = FilePathUtils.class.getClassLoader().getResource(classNamePath);
        String path = is.getFile();
        path = StringUtils.replace(path, "%20", " ");

        return StringUtils.removeStart(path, "/");
    }

    /**
     * 获取当前运行的目录
     * @return
     */
    public String getUserDir(){
        return System.getProperty("user.dir");
    }

    /**
     * 获取当前项目的根目录绝对路径
     * @return
     */
    public String getProjectClassPath(){
        if(isJar){
            String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
            try {
                path = java.net.URLDecoder.decode(path, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


            // 截取jar路径
            int lastIndex = path.lastIndexOf("/") + 1;

            path = path.substring(0, lastIndex);

//            System.out.println(path);   // /E:/code/java/transferTools/target/classes/

            return path;
        }else{
            return this.getClass().getResource("/").getPath();
        }
    }

    /**
     * 获取当前类的绝对路径
     * @return
     */
    public String getCurrentClassPath(){
        return this.getClass().getResource("").getPath();
    }

}
