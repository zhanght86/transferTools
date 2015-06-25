package client.dao;

import bean.Node;
import org.apache.log4j.Logger;
import service.CachePool;
import db.DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

/**
 * Created by QPing on 2015/4/17.
 */
public class NodeSelfDao extends BaseDao{

    protected final Logger log = Logger.getLogger(NodeSelfDao.class);
    private static final String TABLE_NAME = "node_self";
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public NodeSelfDao() {
        super();
    }

    public NodeSelfDao(DB db) {
        super(db);
    }

    public void insert(Node node) throws  SQLException{

        String insertSQL = "insert into " + TABLE_NAME
                + "(nodeid, name, ip, port, nodetype, deploytype, area,"
                + " osname, osversion, javaversion, hostname, mac,"
                + " parentid, parentip, parentport, invitecode, registertime, lastconnecttime, status) "
                + " values(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,? )";

        Object[] params = new Object[]{
                node.getNodeid(),
                node.getName(),
                node.getIp(),
                node.getPort(),
                node.getNodetype(),
                node.getDeploytype(),
                node.getArea(),
                node.getOsname(),
                node.getOsversion(),
                node.getJavaversion(),
                node.getHostname(),
                node.getMac(),
                node.getParentid(),
                node.getParentip(),
                node.getParentport(),
                node.getInvitecode(),
                node.getRegistertime(),
                node.getLastconnecttime(),
                node.getStatus()
        };

        this.executeUpdate(insertSQL, params);
    }

    public Node query() throws SQLException {
        String querySQL = "select * from " + TABLE_NAME + " where status = " + Node.STATUS_OK;

        Node node = null;
        ResultSet rs = this.executeQuery(querySQL, null);
        if(rs.next()){
            node = new Node();
            node.setNodeid(rs.getString("nodeid"));
            node.setName(rs.getString("name"));
            node.setIp(rs.getString("ip"));
            node.setPort(rs.getInt("port"));
            node.setNodetype(rs.getString("nodetype"));
            node.setDeploytype(rs.getString("deploytype"));
            node.setArea(rs.getString("area"));
            node.setOsname(rs.getString("osname"));
            node.setOsversion(rs.getString("osversion"));
            node.setJavaversion(rs.getString("javaversion"));
            node.setHostname(rs.getString("hostname"));
            node.setMac(rs.getString("mac"));
            node.setParentid(rs.getString("parentid"));
            node.setParentip(rs.getString("parentip"));
            node.setParentport(rs.getInt("parentport"));
            node.setInvitecode(rs.getString("invitecode"));
            node.setRegistertime(rs.getString("registertime"));
            node.setLastconnecttime(rs.getString("lastconnecttime"));
            node.setStatus(rs.getString("status"));
        }
        return node;
    }

    public void validDuplicate(Node self) throws Exception {
        int cnt = 0;
        String querySQL = "select mac from " + TABLE_NAME + " where status = " + Node.STATUS_OK;

        String macNew = self.getMac();
        String macOld = null;

        ResultSet rs = this.executeQuery(querySQL, null);
        if(rs.next()) {
            macOld = rs.getString("mac");
        }
        if(cnt > 0){
            throw new Exception("已经保存过注册信息!");
        }
    }

    public void updateParent(String parentid, String parentip, int parentport, String invitecode) throws Exception{
        String updateSQL = "update " + TABLE_NAME + " set parentid = ?, parentip = ? , parentport = ? , invitecode = ? where status = " + Node.STATUS_OK;

        this.executeUpdate(updateSQL, new Object[]{parentid, parentip, parentport, invitecode});

        // 更新缓存
        CachePool.self.setParentid(parentid);
        CachePool.self.setParentip(parentip);
        CachePool.self.setParentport(parentport);
        CachePool.self.setInvitecode(invitecode);
    }

    public void updateSelf(String name, int port, String nodetype, String deploytype, String area) throws SQLException {
        String updateSQL = "update " + TABLE_NAME + " set name = ?, port = ? , nodetype = ? , deploytype = ?, area = ? where status = " + Node.STATUS_OK;

        this.executeUpdate(updateSQL, new Object[]{name, port, nodetype, deploytype, area});

        // 更新缓存
        CachePool.self.setName(name);
        CachePool.self.setPort(port);
        CachePool.self.setNodetype(nodetype);
        CachePool.self.setDeploytype(deploytype);
        CachePool.self.setArea(area);
    }

    public void deleteAll() throws SQLException {
        String updateSQL = "update " + TABLE_NAME + " set status = " + Node.STATUS_DELETE;
        this.executeUpdate(updateSQL, null);

        // 更新缓存
        CachePool.self = null;
    }
}
