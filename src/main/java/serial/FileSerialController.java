package serial;

import db.DB;
import db.DBFactory;
import serial.bean.File;
import serial.bean.Release;
import serial.bean.Type;
import serial.dao.FileDao;

import java.sql.SQLException;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 文件更新序列控制器
 * Created by QPing on 2015/8/12.
 */
public class FileSerialController {

    private Vector<Type> typeList = new Vector<Type>();
    private Vector<File> fileList = new Vector<File>();
    private ConcurrentHashMap<String, File> fileMap = new ConcurrentHashMap<String, File>();
    private Vector<Release> releases = new Vector<Release>();
    private ConcurrentHashMap<String, Vector<Release>> releaseMap = new ConcurrentHashMap<String, Vector<Release>>();

    public void init() throws Exception {
        DB db = DBFactory.create();
        FileDao fileDao = new FileDao(db);

        // 加载所有的发布文件到List
        fileList = fileDao.queryFileList(null, " createtime desc");
        // 将list中的文件映射到map中
        for(File file : fileList){
            fileMap.put(file.getId(), file);
        }
        // 加载所有文件类别
        typeList = fileDao.queryTypeList();
        // 根据文件类别，形成发布分支
        releases = fileDao.queryReleaseList();

        for (Type type : typeList){
            Vector<Release> releaseVector = new Vector<Release>();
            releaseMap.put(type.getTypeid(), releaseVector);
        }

        for(Release release : releases){
            Vector<Release> releaseVector = releaseMap.get(release.getTypeid());
            if(releaseVector != null){
                int index = release.getIndex();
                if(index != releaseVector.size()){
                    throw new Exception("检测到增量包序号错误");
                }
                releaseVector.add(release);
            }
        }
    }


}
