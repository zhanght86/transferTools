package bean;

/**
 * Created by QPing on 2015/3/23.
 */
public class RegisterInfo {
    public static final  String STATUS_OK = "1";        //1 正用
    public static final  String STATUS_DEL = "0";       //0 作废

    public static final String LINKSTATUS_OK = "1";     //1 正常连接
    public static final String LINKSTATUS_FAIL = "0";   //0 无法连接

    String selfid;
    String selfmac;
    String selfip;
    String selfhostname;
    String selfnodetype;

    String parentid;
    String parentip;
    String parentport;
    String invitecode;
    String registertime;
    String lasttime;
    String linkstatus;
    String status;

    public String getSelfid() {
        return selfid;
    }

    public void setSelfid(String selfid) {
        this.selfid = selfid;
    }

    public String getSelfmac() {
        return selfmac;
    }

    public void setSelfmac(String selfmac) {
        this.selfmac = selfmac;
    }

    public String getSelfip() {
        return selfip;
    }

    public void setSelfip(String selfip) {
        this.selfip = selfip;
    }

    public String getSelfhostname() {
        return selfhostname;
    }

    public void setSelfhostname(String selfhostname) {
        this.selfhostname = selfhostname;
    }

    public String getSelfnodetype() {
        return selfnodetype;
    }

    public void setSelfnodetype(String selfnodetype) {
        this.selfnodetype = selfnodetype;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public String getParentip() {
        return parentip;
    }

    public void setParentip(String parentip) {
        this.parentip = parentip;
    }

    public String getParentport() {
        return parentport;
    }

    public void setParentport(String parentport) {
        this.parentport = parentport;
    }

    public String getInvitecode() {
        return invitecode;
    }

    public void setInvitecode(String invitecode) {
        this.invitecode = invitecode;
    }

    public String getRegistertime() {
        return registertime;
    }

    public void setRegistertime(String registertime) {
        this.registertime = registertime;
    }

    public String getLasttime() {
        return lasttime;
    }

    public void setLasttime(String lasttime) {
        this.lasttime = lasttime;
    }

    public String getLinkstatus() {
        return linkstatus;
    }

    public void setLinkstatus(String linkstatus) {
        this.linkstatus = linkstatus;
    }
}
