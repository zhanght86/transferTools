package bean;

import java.util.Hashtable;
import java.util.Vector;

/**
 * 节点的树形展示
 * Created by QPing on 2015/4/14.
 */
public class TreeNodeJson {
    // 节点的ID
    private String id;
    // 父节点ID
    private String pid;
    // 节点图标
    private String iconCls;
    // 节点显示的文字
    private String text;
    // 节点状态，有两个值  'open' or 'closed', 默认为'open'. 当为‘closed’时说明此节点下有子节点否则此节点为叶子节点
    private String state = "open";
    // 标识该节点是否被选中
    private boolean checked = false;
    // 子节点集合
    private Vector<TreeNodeJson> children = new Vector<TreeNodeJson>();
    // 节点中其他属性的集合
    private Hashtable<String,String> attributes = new Hashtable<String,String>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Vector<TreeNodeJson> getChildren() {
        return children;
    }

    public void setChildren(Vector<TreeNodeJson> children) {
        this.children = children;
    }

    public Hashtable<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Hashtable<String, String> attributes) {
        this.attributes = attributes;
    }

    public static TreeNodeJson getJsonObjByNode(Node node){
        TreeNodeJson json = new TreeNodeJson();
        json.setId(node.getNodeid());
        json.setText(node.getName());
        return json;
    }

    public void TraversalChildrenAndSet(Vector<Node> all){
        for(int i = 0; i < all.size(); i++){
            Node node = all.get(i);
            if(node.getParentid().equals(id)){
                TreeNodeJson child = getJsonObjByNode(node);
                child.setIconCls("icon-db");
                // 递归设置所有的儿子节点
                child.TraversalChildrenAndSet(all);
                getChildren().add(child);
            }
        }
    }
}
