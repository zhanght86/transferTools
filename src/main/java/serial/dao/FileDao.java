package serial.dao;

import bean.Pack;
import client.dao.BaseDao;
import db.DB;
import db.Page;
import serial.bean.File;
import serial.bean.Release;
import serial.bean.Type;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by QPing on 2015/3/27.
 */
public class FileDao extends BaseDao {

    public FileDao(DB db){
        super(db);
    }

    public FileDao(){
        super();
    }

    public Vector<File> queryFileList(String filter, String orderBy) throws SQLException {
        Vector<File> list = new Vector<File>();
        String querySQL = "select * from filelist" ;
        if (null != filter) {
            querySQL += " where " + filter;
        }
        if(null != orderBy){
            querySQL += " order by " + orderBy;
        }
        ResultSet rs = this.executeQuery(querySQL, null);
        while (rs.next()) {
            File file = new File();
            file.setId(rs.getString("id"));
            file.setFilename(rs.getString("filename"));
            file.setVersion(rs.getString("version"));
            file.setSize(rs.getString("size"));
            file.setComment(rs.getString("comment"));
            file.setAssesstime(rs.getString("assesstime"));
            file.setCreatetime(rs.getString("createtime"));
            file.setStatus(rs.getInt("status"));
            list.add(file);
        }
        return list;
    }

    public Vector<Type> queryTypeList() throws SQLException {
        Vector<Type> list = new Vector<Type>();
        String querySQL = "select * from typelist";
        ResultSet rs = this.executeQuery(querySQL, null);
        while (rs.next()) {
            Type type = new Type();
            type.setTypeid(rs.getString("typeid"));
            type.setTypename(rs.getString("typename"));
            list.add(type);
        }
        return list;
    }

    public Vector<Release> queryReleaseList() throws SQLException {
        Vector<Release> list = new Vector<Release>();
        String querySQL = "select * from releaselist order by index asc";
        ResultSet rs = this.executeQuery(querySQL, null);
        while (rs.next()) {
            Release release = new Release();
            release.setTypeid(rs.getString("typeid"));
            release.setFileid(rs.getString("fileid"));
            release.setDownloadnum(rs.getString("downloadnum"));
            release.setReleasetime(rs.getString("releasetime"));
            release.setIndex(rs.getInt("index"));
            list.add(release);
        }
        return list;
    }

}
