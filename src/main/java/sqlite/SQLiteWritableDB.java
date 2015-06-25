package sqlite;

import org.apache.log4j.Logger;

import java.sql.*;
import java.util.Properties;

/**
 * Created by QPing on 2015/3/24.
 */
public class SQLiteWritableDB implements SQLiteDB{
    protected final Logger log = Logger.getLogger(SQLiteWritableDB.class);

    Connection conn = null;

    public SQLiteWritableDB() {
        try {
            Class.forName(className);
            conn = DriverManager.getConnection(NODE_URL);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection getConn(){
        return conn;
    }

    public void close() {
        if (conn != null) {
            try {
                conn.close();
                conn = null;
            } catch (SQLException e) {
                log.error(e.getMessage());
            }
        }
    }


    public void beginTransaction() {
        try {
            conn.setAutoCommit(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void endTransaction() {
        try {
            conn.setAutoCommit(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void commit() {
        try {
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void rollback() {
        try {
            conn.rollback();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
