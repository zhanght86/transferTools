package bean;

import util.MD5Helper;
import util.MacUtil;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Formatter;
import java.util.Locale;
import java.util.Properties;

public class Node implements Cloneable{
    private String nodeid = "";     // 唯一标识，由mac地址生成
    private String name;            // 名称
    private String ip = "127.0.0.1";// IP地址
    private int port = 80;          // 端口
    private String nodetype = "";   // 节点类型，枚举型， center/node/deploy
    private String deploytype = ""; //部署类型 self 全人工决定，semi-auto 半自动，auto 全自动
    private String area;            // 区域
    private String osname;          // 系统名
    private String osversion;       // 系统版本
    private String javaversion;     // java版本
    private String hostname;        // 计算机名
    private String mac;             // MAC地址
    private String parentid = "";   // 父节点标识
    private String parentip = "";   // 父节点ip
    private int parentport = 80; // 父节点端口
    private String invitecode;      // 邀请码，首次注册需要此字段
    private String registertime;    // 注册时间
    private String lastconnecttime; // 上次访问时间
    private String status;          // 节点状态
    public static final String STATUS_OK = "1";
    public static final String STATUS_DELETE = "0";

    public String getRegistertime() {
        return registertime;
    }

    public void setRegistertime(String registertime) {
        this.registertime = registertime;
    }

    public void setLastconnecttime(String lastconnecttime) {
        this.lastconnecttime = lastconnecttime;
    }
    public String getLastconnecttime() {
        return lastconnecttime;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getJavaversion() {
        return javaversion;
    }

    public void setJavaversion(String javaversion) {
        this.javaversion = javaversion;
    }

    public String getOsname() {
        return osname;
    }

    public void setOsname(String osname) {
        this.osname = osname;
    }

    public String getOsversion() {
        return osversion;
    }

    public void setOsversion(String osversion) {
        this.osversion = osversion;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getNodeid() {
        return nodeid;
    }

    public void setNodeid(String nodeid) {
        this.nodeid = nodeid;
    }

    public String getNodetype() {
        return nodetype;
    }

    public void setNodetype(String nodetype) {
        this.nodetype = nodetype;
    }

    public String getInvitecode() {
        return invitecode;
    }

    public void setInvitecode(String invitecode) {
        this.invitecode = invitecode;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeploytype() {
        return deploytype;
    }

    public void setDeploytype(String deploytype) {
        this.deploytype = deploytype;
    }

    public String getParentip() {
        return parentip;
    }

    public void setParentip(String parentip) {
        this.parentip = parentip;
    }

    public int getParentport() {
        return parentport;
    }

    public void setParentport(int parentport) {
        this.parentport = parentport;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    /**
     * 获取本机的信息（只能支持单网卡）
     */
    public Node current(){
        try {

            status = STATUS_OK;

            Properties props = System.getProperties();
            javaversion = props.getProperty("java.version");
            osname = props.getProperty("os.name");
            osversion = props.getProperty("os.version");

            InetAddress address = InetAddress.getLocalHost();
            if(address != null){
                ip = address.getHostAddress();
                hostname = address.getHostName(); //获取本机计算机名称
            }
            mac = MacUtil.getMac();
            if(mac == null){
                throw new Exception("无法获取Mac地址！");
            }
            nodeid = MD5Helper.MD5(mac);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

}
