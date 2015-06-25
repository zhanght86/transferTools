package bean;

/**
 * Created by QPing on 2015/2/28.
 * 增量包
 *
 */
public class Pack {

    public static int STATUS_ADD = 0;           // 新增状态，未发布
    public static int STATUS_UPDATING = 1;      // 正在更新
    public static int STATUS_RELEASED = 2;      // 发布（更新完成）

    private int no;                 // 增量包序号
    private String version ;        // 版本号
    private String size ;           // 大小(M)
    private String comment ;        // 备注
    private String createtime ;     // 创建时间
    private String releasetime ;    // 发布时间
    private String assesstime ;     // 预估更新时间
    private String downloadtime;    // 下载时间
    private int releasenum ;        // 发布数
    private int updatenum ;         // 已更新数
    private String area;            // 发布区域
    private int status;             // 状态 0 新增状态，未发布 1 正在更新  2 更新完成或发布
    private int deploycount;        // 部署次数  0 未部署 >0 部署次数
    private String deploytime;      // 上次部署时间
    private String filename;        // 文件名

    // =========================== 伪列 =========================================
    private String progress = "0%";        // 下载进度

    // ==========================================================================
    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getReleasetime() {
        return releasetime;
    }

    public void setReleasetime(String releasetime) {
        this.releasetime = releasetime;
    }

    public String getAssesstime() {
        return assesstime;
    }

    public void setAssesstime(String assesstime) {
        this.assesstime = assesstime;
    }

    public int getReleasenum() {
        return releasenum;
    }

    public void setReleasenum(int releasenum) {
        this.releasenum = releasenum;
    }

    public int getUpdatenum() {
        return updatenum;
    }

    public void setUpdatenum(int updatenum) {
        this.updatenum = updatenum;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDownloadtime() {
        return downloadtime;
    }

    public void setDownloadtime(String downloadtime) {
        this.downloadtime = downloadtime;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public int getDeploycount() {
        return deploycount;
    }

    public void setDeploycount(int deploycount) {
        this.deploycount = deploycount;
    }

    public String getDeploytime() {
        return deploytime;
    }

    public void setDeploytime(String deploytime) {
        this.deploytime = deploytime;
    }

}
