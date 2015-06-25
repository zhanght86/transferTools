package bean;


public class InviteCode {
    public static final int STATUS_OK = 0;         // 有效 未使用
    public static final int STATUS_USED = 1;       // 已使用
    public static final int STATUS_TERMINAL = 2;   // 终止
    public static final int STATUS_CANCEL = 3;     // 作废

    private String invitecode;
    private String createtime;
    private String terminaltime;
    private int status;
    private String statusAttr;

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getTerminaltime() {
        return terminaltime;
    }

    public void setTerminaltime(String terminaltime) {
        this.terminaltime = terminaltime;
    }

    public String getInvitecode() {
        return invitecode;
    }

    public void setInvitecode(String invitecode) {
        this.invitecode = invitecode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
    public String getStatusAttr() {
        return statusAttr;
    }

    public void setStatusAttr(String statusAttr) {
        this.statusAttr = statusAttr;
    }

    public void setStatusAttr(int status) {
        String statusAttr = "";
        switch(status){
            case STATUS_OK:
                statusAttr = "有效";
                break;
            case STATUS_USED:
                statusAttr = "已使用";
                break;
            case STATUS_TERMINAL:
                statusAttr = "终止";
                break;
            case STATUS_CANCEL:
                statusAttr = "作废";
                break;
        }
        this.statusAttr = statusAttr;
    }
}
