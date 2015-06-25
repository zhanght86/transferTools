package db;

import java.sql.Connection;

/**
 * Created by QPing on 2015/3/25.
 */
public interface DB {
    public Connection getConn();
    public void close();
    public void beginTransaction();
    public void endTransaction();
    public void commit();
    public void rollback();
}
