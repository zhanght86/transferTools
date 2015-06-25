package db.impl;

import db.DB;
import org.apache.log4j.Logger;
import org.hsqldb.server.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by QPing on 2015/6/10.
 */
public class HSQLWritableDB implements DB {

    public static final String DB_NAME = "svnpack.db";
    public static final String DB_PATH = "./";
    public static final String USER_NAME = "sa";
    public static final String PASSWORD = "";
    public static final int PORT = 9002;
    public static final int SERVER_MODE = 0;
    public static final int STAND_ALONE_MODE = 1;   //In-Process
    public static int mode = SERVER_MODE;          //记录当前用什么模式，开发时用Server，发布时用standalone

    protected final Logger log = Logger.getLogger(HSQLWritableDB.class);

    Connection conn = null;

    /*
     * 启动数据库服务
     */
    public static boolean startHSQL() {
        if (mode == SERVER_MODE) {
            Server server = new Server();//它可是hsqldb.jar里面的类啊。
            server.setDatabaseName(0, DB_NAME);
            server.setDatabasePath(0, DB_PATH + DB_NAME);
            server.setPort(PORT);
            server.setSilent(true);
            server.start();         //自动多线程运行
            System.out.println("hsqldb started...");
        } else if (mode == STAND_ALONE_MODE) {
            //standalone模式，打开连接就同时启动数据库，所以这里可以什么都不做
        }

        try {
            Thread.sleep(1000);        // 等待Server启动
        } catch (InterruptedException e) {
        }
        return true;
    }

    /**
     * 关闭数据库服务
     */
    public boolean stopHSQL() {
        try {
            Statement statement = getConn().createStatement();
            statement.executeUpdate("SHUTDOWN;");
            return true;
        } catch (SQLException ex) {
            log.error(ex);
            return false;
        }
    }


    public HSQLWritableDB() {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            if (mode == SERVER_MODE) {
                conn = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost:" + PORT + "/" + DB_NAME, USER_NAME, PASSWORD);
            } else if (mode == STAND_ALONE_MODE) {
                conn = DriverManager.getConnection("jdbc:hsqldb:file:" + DB_PATH + DB_NAME, USER_NAME, PASSWORD);
            }
        } catch (ClassNotFoundException ex) {
            log.error(ex);
        } catch (SQLException ex) {
            log.error(ex);
        }
    }

    @Override
    public Connection getConn() {
        return conn;
    }

    @Override
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

    @Override
    public void beginTransaction() {
        try {
            conn.setAutoCommit(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void endTransaction() {
        try {
            conn.setAutoCommit(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void commit() {
        try {
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rollback() {
        try {
            conn.rollback();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
