package client.dao;

import bean.Node;
import org.apache.log4j.Logger;
import bean.InviteCode;
import db.DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class InviteCodeDao extends BaseDao{
    protected final Logger log = Logger.getLogger(InviteCodeDao.class);

    private static final String TABLE_NAME = "node_invitecode";

    private static int timeout = 24 * 3600 * 1000;    //邀请码作废时间，默认1天
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public InviteCodeDao(DB db) {
        super(db);
    }

    public InviteCodeDao() {
        super();
    }

    public String random() {

        String random = UUID.randomUUID().toString();
        Date now = new Date();

        Date terminalDate = new Date(now.getTime() + timeout);

        InviteCode inviteCode = new InviteCode();
        inviteCode.setInvitecode(random);
        inviteCode.setCreatetime(dateFormat.format(now));
        inviteCode.setTerminaltime(dateFormat.format(terminalDate));
        inviteCode.setStatus(InviteCode.STATUS_OK);

        return insert(inviteCode);
    }

    public boolean query(String inviteCode) throws Exception {
        String querySQL = "select terminaltime from " + TABLE_NAME + " where status = " + InviteCode.STATUS_OK + " and invitecode = ? ";
        String updateSQL = "update " + TABLE_NAME + " set status = " + InviteCode.STATUS_TERMINAL + " where status = " + InviteCode.STATUS_OK + " and invitecode = ? ";
        try {
            ResultSet rs = this.executeQuery(querySQL, new Object[]{inviteCode});
            if (rs.next()) {
                Date terminalDate = dateFormat.parse( rs.getString("terminaltime"));

                if (terminalDate.compareTo(new Date()) > 0) {
                    // 终止时间 大于当前时间，还未终止该码可用
                    return true;
                } else {
                    // 该码过期了，把状态置为 过期
                    this.executeUpdate(updateSQL, new Object[]{inviteCode});
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return false;
    }

    public String insert(InviteCode inviteCode) {
        String insertSQL = "insert into " + TABLE_NAME + "(invitecode,createtime,terminaltime,status) values(?,?,?,?)";
        Object[] params = new Object[]{
                inviteCode.getInvitecode(),
                inviteCode.getCreatetime(),
                inviteCode.getTerminaltime(),
                inviteCode.getStatus()
        };

        try {
            this.executeUpdate(insertSQL, params);
            return inviteCode.getInvitecode();
        } catch (SQLException e) {
            log.error("生成邀请码失败：" + e.getMessage());
        }

        return null;
    }

    public boolean update(String inviteCode) throws Exception {
        String updateSQL = "update " + TABLE_NAME + " set status = " + InviteCode.STATUS_USED + " where status = " + InviteCode.STATUS_OK + " and invitecode = ? ";
        int row = this.executeUpdate(updateSQL, new Object[]{inviteCode});
        if (row == 1) {
            return true;
        }
        return false;
    }

    public List<InviteCode> queryAll() {
        List<InviteCode> list = new ArrayList<InviteCode>();
        String querySQL = "select invitecode,createtime,terminaltime,status from " + TABLE_NAME + " order by createtime desc ";
        try {
            ResultSet rs = this.executeQuery(querySQL, null);
            while (rs.next()) {
                String invitecode =  rs.getString("invitecode");
                Date terminalDate = dateFormat.parse(rs.getString("terminaltime"));
                Date createtime = dateFormat.parse( rs.getString("createtime"));
                int status = rs.getInt("status");

                InviteCode inviteCode = new InviteCode();
                inviteCode.setInvitecode(invitecode);
                inviteCode.setCreatetime(dateFormat.format(createtime));
                inviteCode.setTerminaltime(dateFormat.format(terminalDate));
                inviteCode.setStatus(status);
                inviteCode.setStatusAttr(status);

                list.add(inviteCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void validRegisterNode(Node child) throws Exception {
        if(null == child.getMac() || "".equals(child.getMac())){
            throw new Exception("mac地址不能为空");
        }
        if(null == child.getHostname() || "".equals(child.getHostname())){
            throw new Exception("主机名不能为空");
        }
        if(null == child.getIp() || "".equals(child.getIp())){
            throw new Exception("ip地址不能为空");
        }
        if(null == child.getInvitecode()|| "".equals(child.getInvitecode())){
            throw new Exception("邀请码不能为空");
        }else{
            boolean codeValid = this.query(child.getInvitecode());
            if(!codeValid){
                throw new Exception("邀请码非法");
            }
        }

    }

}
