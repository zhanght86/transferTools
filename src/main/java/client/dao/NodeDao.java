package client.dao;

import bean.Node;
import client.exception.NodeDuplicateRegisterException;
import org.apache.log4j.Logger;
import db.DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Vector;

public class NodeDao extends BaseDao{
    protected final Logger log = Logger.getLogger(InviteCodeDao.class);
    private static final String TABLE_NAME = "node_child";
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public NodeDao() {
        super();
    }

    public NodeDao(DB db) {
        super(db);
    }

    public void insert(Node node) throws Exception {
        String insertSQL = "insert into " + TABLE_NAME + "(" +
                " nodeid,name,ip,parentid,nodetype,area,port,mac,javaversion,osname,osversion,hostname,registertime,lastconnecttime) " +
                " values(?,?,?,?,?, ?,?,?,?,?, ?,?,?)";

        Object[] params = new Object[]{
                node.getNodeid(),
                node.getName(),
                node.getIp(),
                node.getParentid(),
                node.getNodetype(),
                node.getArea(),
                node.getPort(),
                node.getMac(),
                node.getJavaversion(),
                node.getOsname(),
                node.getOsversion(),
                node.getHostname(),
                node.getRegistertime(),
                node.getLastconnecttime()
        };

        try {
            this.executeUpdate(insertSQL, params);
        } catch (SQLException e) {
            throw new Exception("注册子节点信息失败：" + e.getMessage());
        }
    }

    public void validDuplicateNode(Node child, String nodeId) throws Exception {
        String querySQL = "select count(1) as cnt from " + TABLE_NAME + " where mac = ? ";

        Object[] params = new Object[]{
                child.getMac()
        };
        ResultSet rs = this.executeQuery(querySQL, params);
        int cnt = 0;
        if(rs.next()){
            cnt = rs.getInt("cnt");
        }

        if(cnt > 0){
            throw new NodeDuplicateRegisterException(nodeId, "该节点已注册！");
        }
    }

    public Vector<Node> queryAll(String filter, String orderBy) throws SQLException {

        Vector<Node> list = new Vector<Node>();
        String querySQL = "select * from " + TABLE_NAME;

        if (null != filter) {
            querySQL += " where " + filter;
        }
        if(null != orderBy){
            querySQL += " order by " + orderBy;
        }

        ResultSet rs = this.executeQuery(querySQL, null);
        while(rs.next()){
            Node node = new Node();

            node.setNodeid(rs.getString("nodeid"));
            node.setName(rs.getString("name"));
            node.setIp(rs.getString("ip"));
            node.setParentid(rs.getString("parentid"));
            node.setNodetype(rs.getString("nodetype"));
            node.setArea(rs.getString("area"));
            node.setPort(rs.getInt("port"));
            node.setMac(rs.getString("mac"));
            node.setJavaversion(rs.getString("javaversion"));
            node.setOsname(rs.getString("osname"));
            node.setOsversion(rs.getString("osversion"));
            node.setHostname(rs.getString("hostname"));
            node.setRegistertime(rs.getString("registertime"));
            node.setLastconnecttime(rs.getString("lastconnecttime"));

            list.add(node);
        }

        return list;
    }
}
