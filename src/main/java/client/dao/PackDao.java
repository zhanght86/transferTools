package client.dao;

import bean.Pack;
import db.Page;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * Created by QPing on 2015/3/27.
 */
public class PackDao extends BaseDao {
    private static final String TABLE_NAME = "node_packlist";

    public List<Pack> queryPage(String type, String page, String rows, String filter, String orderBy) throws SQLException {
        List<Pack> list = new ArrayList<Pack>();
        String querySQL = "select * from " + TABLE_NAME;

        if("upload".equals(type)){
            querySQL += " where status in ( " + Pack.STATUS_ADD + ", " + Pack.STATUS_RELEASED  + ") ";
        }else{
            querySQL += " where status in (" + Pack.STATUS_UPDATING + ") ";
        }

        if (null != filter) {
            querySQL += filter;
        }

        if(null != orderBy){
            querySQL += " order by " + orderBy;
        }

        querySQL += new Page(page, rows).getPageSQL();
        ResultSet rs = this.executeQuery(querySQL, null);
        while (rs.next()) {
            Pack pack = new Pack();
            pack.setNo(rs.getInt("no"));
            pack.setFilename(rs.getString("filename"));
            pack.setVersion(rs.getString("version"));
            pack.setSize(rs.getString("size"));
            pack.setComment(rs.getString("comment"));
            pack.setAssesstime(rs.getString("assesstime"));
            pack.setCreatetime(rs.getString("createtime"));
            pack.setReleasetime(rs.getString("releasetime"));
            pack.setReleasenum(rs.getInt("releasenum"));
            pack.setUpdatenum(rs.getInt("updatenum"));
            pack.setArea(rs.getString("area"));
            pack.setDeploycount(rs.getInt("deploycount"));
            pack.setDeploytime(rs.getString("deploytime"));
            pack.setStatus(rs.getInt("status"));
            list.add(pack);
        }
        return list;
    }

    public Vector<Pack> queryAll(String filter, String orderBy) throws SQLException {
        Vector<Pack> list = new Vector<Pack>();
        String querySQL = "select * from " + TABLE_NAME;
        if (null != filter) {
            querySQL += " where " + filter;
        }
        if(null != orderBy){
            querySQL += " order by " + orderBy;
        }
        ResultSet rs = this.executeQuery(querySQL, null);
        while (rs.next()) {
            Pack pack = new Pack();
            pack.setNo(rs.getInt("no"));
            pack.setFilename(rs.getString("filename"));
            pack.setVersion(rs.getString("version"));
            pack.setSize(rs.getString("size"));
            pack.setComment(rs.getString("comment"));
            pack.setAssesstime(rs.getString("assesstime"));
            pack.setCreatetime(rs.getString("createtime"));
            pack.setReleasetime(rs.getString("releasetime"));
            pack.setReleasenum(rs.getInt("releasenum"));
            pack.setUpdatenum(rs.getInt("updatenum"));
            pack.setArea(rs.getString("area"));
            pack.setDeploycount(rs.getInt("deploycount"));
            pack.setDeploytime(rs.getString("deploytime"));
            pack.setStatus(rs.getInt("status"));
            list.add(pack);
        }
        return list;
    }

    public void insert(Pack pack) throws SQLException {
        String insertSQL = "insert into " + TABLE_NAME + " (no,filename,version,size,comment,createtime,releasetime,assesstime,releasenum,updatenum,area,status,deploycount,deploytime) " +
                " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)";

        Object[] params = new Object[]{
                pack.getNo(),
                pack.getFilename(),
                pack.getVersion(),
                pack.getSize(),
                pack.getComment(),
                pack.getCreatetime(),
                pack.getReleasetime(),
                pack.getAssesstime(),
                pack.getReleasenum(),
                pack.getUpdatenum(),
                pack.getArea(),
                pack.getStatus(),
                pack.getDeploycount(),
                pack.getDeploytime()
        };

        this.executeUpdate(insertSQL, params);
    }

    public void delete(Pack pack) throws SQLException {
        String deleteSQL = "delete from " + TABLE_NAME + " where no = ?" ;
        this.executeUpdate(deleteSQL, new Object[]{pack.getNo()});
    }

    public int queryTotal() throws SQLException {
        String totalSQL = "select count(1) as cnt from "+ TABLE_NAME;
        ResultSet rs = this.executeQuery(totalSQL, null);
        int total = -1;
        if (rs.next()){
            total = rs.getInt("cnt");
        }
        return total;
    }

    public void release(Pack pack) throws SQLException {
        String updateSQL = "update " + TABLE_NAME + " set status = ?, releasetime = ? where no = ? and status in ( " + Pack.STATUS_ADD  + ")";
        this.executeUpdate(updateSQL, new Object[]{Pack.STATUS_RELEASED, this.getCurrentTime(), pack.getNo()});
    }

    public void insertList(List<Pack> list) throws  Exception{
        if(null == list || 0 == list.size()){
            return;
        }
        for(Pack p : list){
            insert(p);
        }
    }


    public void deploy(Pack pack) throws SQLException {
        pack.setDeploycount(pack.getDeploycount() + 1);
        String updateSQL = "update " + TABLE_NAME + " set deploycount = ?,deploytime = ? where no = ?";
        this.executeUpdate(updateSQL, new Object[]{pack.getDeploycount(),  this.getCurrentTime(),  pack.getNo()});
    }
}
