package bean;

/**
 * Created by QPing on 2015/4/6.
 */
public class Deploy {
    public static int TYPE_UPDATE;  // 子节点获取更新列表，并自动下载
    public static int TYPE_DEPLOY;  // 子节点获取更新，如果自身是可发布的节点，就更新程序

    String toId;        // 发布到什么节点
    boolean isDirect;   // 是否是直接可到达的
    String nextGate;    // 如果不是直接可到达的，需要经过的下一个节点ID
    int type;           // 发布部署的形式
}
