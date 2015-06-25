package service;


import client.dao.BaseDao;
import db.impl.HSQLWritableDB;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import util.FilePathUtils;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TODO 备份恢复，暂未实现，只实现了升级表结构，数据无法迁移
 */
public class DatabaseService {

    protected final Logger log = Logger.getLogger(DatabaseService.class);

    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";
    private SimpleDateFormat df = new SimpleDateFormat(PATTERN);

    private static final String BACKUP_FILE_NAME = "backup.db";
    public static final int LEVEL_INIT_ERROR = 0;
    public static final int LEVEL_INIT_SUCCESS = 1;
    public static String dbType = "HSQLDB"; // 选择数据种类 HSQLDB 或者 SQLITE

    private DatabaseService(){}

    public void start() {
        if(dbType.equals("HSQLDB")){
            HSQLWritableDB.startHSQL();
        }else if(dbType.equals("SQLITE")){
             // sqlite不需要启动
        }else {
            System.out.println("当前数据库类型无法启动，请在DatabaseService中实现start()方法");
        }
    }

    public static class DatabaseServiceHolder{
        public static DatabaseService instance = new DatabaseService();
    }

    public static DatabaseService getInstance(){
        return DatabaseServiceHolder.instance;
    }

    /**
     * 检查版本是否需要升级，版本变化则升级
     * 1 检查是否初始化sqlite数据库，如果没有则执行 initDB()，如果有则进行步骤2
     * 2 检查版本是否发生变化，如果发生则进行步骤3
     * 3 备份老数据，备份成功则前往步骤4
     * 4 执行initDB()，删除旧表，创建新表
     * 5 执行数据迁移，成功后删除备份文件
     * @return
     */
    public boolean update(){
        if(!isInit()){
            initDB();
            return true;
        }

        if(!needUpdate()){
            log.info("当前数据库已经更新到最新，不需要升级！");
            return true;
        }

        if(backup()){
            if(initDB()){
                // 当升级表结构成功
                recover(LEVEL_INIT_SUCCESS);
                return true;
            }else{
                // 当升级表结构失败，恢复状态至升级前
                recover(LEVEL_INIT_ERROR);
                return false;
            }
        }
        return false;
    }

    private boolean backup(){
        return true;
    }

    private void recover(int level){
        if(level == LEVEL_INIT_ERROR){
            // 当升级表结构失败，恢复状态至升级前
        }
        if(level == LEVEL_INIT_SUCCESS){
            // 当升级表结构成功，数据迁移
        }
        // 删除备份
    }

    /**
     * 判断是否版本需要升级
     * @return
     */
    private boolean needUpdate(){
        // 读取initDatabase.sql 文件中的版本号
        String versionFile = null;
        BufferedReader reader = null;
        try {
            String rootPath = FilePathUtils.getInstance().getProjectClassPath();

            String initSQLPath = rootPath + "initDatabase.sql";
            Reader f = new InputStreamReader(new FileInputStream(new File(initSQLPath)));
//            Reader f = new InputStreamReader(this.getClass().getResourceAsStream("/initDatabase.sql"));
            reader = new BufferedReader(f);
            reader.readLine();
            versionFile = formatVersionStr(reader.readLine());
            log.info("当前数据库版本：" + versionFile);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally{
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }

        // 读取数据库中的版本号
        String sql = "select * from sys_version ";
        String versionDB = null;
        BaseDao dao = new BaseDao();
        try {
            ResultSet rs = dao.executeQuery(sql, null);
            if(rs.next()){
                versionDB = rs.getString("version");
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        } finally {
            dao.close();
        }

        if(StringUtils.isBlank(versionFile)){
            // initDatabase.sql 中无法读到版本号，不更新
            return false;
        }

        if(!versionFile.equals(versionDB)){
            // 版本不一致则更新
            return true;
        }

        return false;
    }


    /**
     * 判断是否初始化sqlite数据库
     * @return
     */
    private boolean isInit(){
        String testSQL = null;

        if(dbType.equals("SQLITE")){
            testSQL = "select count(*) as cnt from sqlite_master where type='table' and name='sys_version' ";
        }else if(dbType.equals("HSQLDB")){
            testSQL = "SELECT count(*) as cnt FROM INFORMATION_SCHEMA.TABLES where table_name = 'SYS_VERSION' ";
        }else{
            System.out.println("当前数据库类型没有实现对应的检查数据库是否初始化的方法，请在DatabaseService中实现！");
        }
        log.info("检测数据库是否初始化...");
        int hasTable = 0;
        BaseDao dao = new BaseDao();
        try {
            ResultSet rs = dao.executeQuery(testSQL, null);
            while(rs.next()){
                hasTable = rs.getInt("cnt");
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        } finally {
            dao.close();
        }
        log.info("检测完成，数据库 " + (hasTable == 1 ? "已初始化！" : "未初始化成功！"));
        return hasTable == 1 ? true : false;
    }


    /**
     * 加载 initDatabase.sql 中的 sql 语句，初始化sqlite数据库
     * @return
     */
    private boolean initDB(){
        String tempString = null;
        String version = null;
        BufferedReader reader = null;
        log.info("首次打开初始数据库中...");
        BaseDao dao = new BaseDao();
        try {
            String sql = "";
//            Reader f = new InputStreamReader(this.getClass().getResourceAsStream("/initDatabase.sql"));
            String rootPath = FilePathUtils.getInstance().getProjectClassPath();
            Reader f = new InputStreamReader(new FileInputStream(new File(rootPath + "initDatabase.sql")));
            reader = new BufferedReader(f);

            // 从前两行中读取版本信息
            reader.readLine();
            version = formatVersionStr(reader.readLine());
            String autoInit = formatAutoInitStr(reader.readLine());

            if(!autoInit.equals("true")){
                throw new Exception("未开启自动初始化本地sqlite，无法初始化！");
            }

            // 执行创表语句
            while ((tempString = reader.readLine()) != null) {
                //去除注释
                int commentLocation = tempString.indexOf("--");
                if(commentLocation > -1)
                    tempString = tempString.substring(0, commentLocation);

                tempString = tempString.trim();
                //如果是;结尾，则执行该sql
                if(tempString.endsWith(";")){
                    sql += tempString.substring(0, tempString.length() - 1);
                    log.info(sql);
                    dao.executeUpdate(sql, null);
                    sql = "";
                }else{
                    sql += tempString;
                }
            }

            dao.executeUpdate("insert into sys_version values(?,?)", new Object[]{
                version, df.format(new Date())
            });
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally{
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
            dao.close();
        }
        return false;
    }

    private String formatVersionStr(String versionStr){
        return versionStr.replace("----", "").replace("version :","").trim();
    }

    // 是否开启自动初始化
    private String formatAutoInitStr(String initStr) {
        return initStr.replace("----", "").replace("auto-init :","").trim();
    }

}
