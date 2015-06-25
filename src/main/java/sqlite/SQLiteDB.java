package sqlite;

import java.sql.Connection;

/**
 * Created by QPing on 2015/3/25.
 */
public interface SQLiteDB {
    public static final String className = "org.sqlite.JDBC";
    public static final String NODE_URL = "jdbc:sqlite:svnpack.db";

    public Connection getConn();
    public void close();
    public void beginTransaction();
    public void endTransaction();
    public void commit();
    public void rollback();
}
