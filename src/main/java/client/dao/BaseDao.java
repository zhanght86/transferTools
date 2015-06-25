package client.dao;

import db.DBFactory;
import org.apache.log4j.Logger;
import db.DB;
import db.impl.SQLiteWritableDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseDao {
    protected final Logger log = Logger.getLogger(BaseDao.class);
    private Connection conn = null;
    private PreparedStatement prep;
    private DB db;
    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";
    private SimpleDateFormat df = new SimpleDateFormat(PATTERN);

    public String getCurrentTime(){
        return  df.format(new Date());
    }

    public BaseDao(){
        this.db = DBFactory.create();
        this.conn = db.getConn();
    }

    public DB getDB(){
        return this.db;
    }

    public BaseDao(DB db){
        this.db = db;
        this.conn = db.getConn();
    }

    public Connection getConn() {
        return conn;
    }

    public void close(){
        this.conn = null;
        db.close();
    }

    public boolean execute(String sql) throws SQLException {
        prep = conn.prepareStatement(sql);
        return prep.execute();
    }

    public ResultSet executeQuery(String sql, Object[] params) throws SQLException{
        prep = conn.prepareStatement(sql);
        if(params != null){
            for(int i = 0; i < params.length; i++){
                prep.setObject((i + 1), params[i]);
            }
        }
        ResultSet rs = prep.executeQuery();
        return rs;
    }

    public int executeUpdate(String sql, Object[] params) throws SQLException {
        prep = conn.prepareStatement(sql);
        if(params != null){
            for(int i = 0; i < params.length; i++){
                prep.setObject((i + 1), params[i]);
            }
        }
        return prep.executeUpdate();
    }

    public void executeBatchUpdate(String sql, Object[][] paramsArr) throws SQLException{
        prep = conn.prepareStatement(sql);
        for(Object[] params : paramsArr){
            for(int i = 0; i < params.length; i++){
                prep.setObject((i + 1), params[i]);
            }
            prep.addBatch();
        }
        prep.executeBatch();
        prep.close();
    }

    public void rollback(){
        db.rollback();
    }

    public void commit(){
        db.commit();
    }

    public void beginTransaction(){
        db.beginTransaction();
    }
}
