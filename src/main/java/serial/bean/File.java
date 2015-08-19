package serial.bean;

/**
 * Created by QPing on 2015/8/12.
 */
public class File {

    private String id            ;  // 文件号
    private String version       ;  // 版本号
    private String filename      ;  // 文件名
    private String size          ;  // 大小(M)
    private String comment       ;  // 备注
    private String createtime    ;  // 创建时间
    private String assesstime    ;  // 预估更新时间
    private int status        ;     // 状态 0 新增状态，未发布 1 正在更新  2 发布（更新完成）

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
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

    public String getAssesstime() {
        return assesstime;
    }

    public void setAssesstime(String assesstime) {
        this.assesstime = assesstime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
