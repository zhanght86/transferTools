package db;

import db.impl.HSQLWritableDB;
import db.impl.SQLiteWritableDB;
import service.DatabaseService;

/**
 * Created by QPing on 2015/6/10.
 *
 * DB 接口用来屏蔽底层数据库
 * DBFactory 用来生产DB实例(会根据DatabaseService的dbType决定)
 * impl
 *   HSQLWritableDB 为HSQLDB的获取连接，事务处理等方法
 *   SQLiteWritableDB 为SQLite获取连接，事务处理的方法
 * BaseDao 封装了通用查询，更新的方法（调用DB的getConn）
 *
 */
public class DBFactory {

    public static DB create(){
        if(DatabaseService.dbType.equals("HSQLDB")){
            return new HSQLWritableDB();
        }else if(DatabaseService.dbType.equals("SQLITE")){
            return new SQLiteWritableDB();
        }else{
            System.out.println("当前数据库类型无法获取DB对象，请在DBFactory中实现create()方法");
            return null;
        }
    }
}
