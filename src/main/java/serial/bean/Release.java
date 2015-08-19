package serial.bean;

/**
 * Created by QPing on 2015/8/12.
 */
public class Release {

    private String fileid      ; // 文件ID
    private String typeid      ; // 发布类别
    private String downloadnum ; // 下载次数
    private String releasetime ; // 发布时间
    private int index;           // 序号

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getFileid() {
        return fileid;
    }

    public void setFileid(String fileid) {
        this.fileid = fileid;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getDownloadnum() {
        return downloadnum;
    }

    public void setDownloadnum(String downloadnum) {
        this.downloadnum = downloadnum;
    }

    public String getReleasetime() {
        return releasetime;
    }

    public void setReleasetime(String releasetime) {
        this.releasetime = releasetime;
    }
}
