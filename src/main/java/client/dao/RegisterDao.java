package client.dao;

import bean.RegisterInfo;
import org.apache.log4j.Logger;
import db.DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

/**
 * Created by QPing on 2015/3/23.
 */
public class RegisterDao extends BaseDao{


    protected final Logger log = Logger.getLogger(InviteCodeDao.class);
    private static final String TABLE_NAME = "node_register";
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public RegisterDao() {

    }

    public RegisterDao(DB db){
        super(db);
    }

    public RegisterInfo querySelf() {
        String querySQL = "select parentid,parentip,parentport,invitecode,registertime,lasttime,linkstatus,status,selfid,selfmac,selfip,selfhostname,selfnodetype" +
                " from " + TABLE_NAME + " where status = '1' " ;
        RegisterInfo registerInfo = new RegisterInfo();
        try {
            ResultSet rs = this.executeQuery(querySQL, null);

            if (rs.next()) {
                String parentid =  rs.getString("parentid");
                String parentip = rs.getString("parentip");
                String parentport = rs.getString("parentport");
                String invitecode = rs.getString("invitecode");
                String registertime = rs.getString("registertime");
                String lasttime = rs.getString("lasttime");
                String linkstatus = rs.getString("linkstatus");

                String selfid = rs.getString("selfid");
                String selfmac = rs.getString("selfmac");
                String selfip = rs.getString("selfip");
                String selfhostname = rs.getString("selfhostname");
                String selfnodetype = rs.getString("selfnodetype");

                registerInfo.setParentid(parentid);
                registerInfo.setParentip(parentip);
                registerInfo.setParentport(parentport);
                registerInfo.setInvitecode(invitecode);
                registerInfo.setRegistertime(registertime);
                registerInfo.setLasttime(lasttime);
                registerInfo.setLinkstatus(linkstatus);
                registerInfo.setSelfid(selfid);
                registerInfo.setSelfmac(selfmac);
                registerInfo.setSelfip(selfip);
                registerInfo.setSelfhostname(selfhostname);
                registerInfo.setSelfnodetype(selfnodetype);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return registerInfo;
    }

    public void save(RegisterInfo registerInfo) throws SQLException {
        String insertSQL = "insert into " + TABLE_NAME + "(parentid,parentip,parentport,invitecode,registertime,lasttime,linkstatus,status," +
                "selfid,selfmac,selfip,selfhostname,selfnodetype) " +
                "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] params = new Object[]{
                registerInfo.getParentid(),
                registerInfo.getParentip(),
                registerInfo.getParentport(),
                registerInfo.getInvitecode(),
                registerInfo.getRegistertime(),
                registerInfo.getLasttime(),
                registerInfo.getLinkstatus(),
                registerInfo.getStatus(),
                registerInfo.getSelfid(),
                registerInfo.getSelfmac(),
                registerInfo.getSelfip(),
                registerInfo.getSelfhostname(),
                registerInfo.getSelfnodetype()
        };
        this.executeUpdate(insertSQL, params);
    }

    public void update(RegisterInfo registerInfo) throws SQLException {
        String updateSQL = "update " + TABLE_NAME + " set lasttime =  ? where status = '1' ";
        this.executeUpdate(updateSQL, new Object[]{
                registerInfo.getLasttime()
        });
    }
}
