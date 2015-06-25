package client.servlet;

import bean.Node;
import bean.RegisterInfo;
import client.dao.NodeSelfDao;
import client.dao.RegisterDao;
import client.httpclient.RegisterClient;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.util.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by QPing on 2015/3/23.
 */
public class SelfServlet extends BaseServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String method = req.getParameter("method");

        if(method.equals("query")){
            query(req, resp);
        }else if(method.equals("save")){
            save(req, resp);
        }else if(method.equals("test")){
            test(req, resp);
        }else if(method.equals("updateParent")){
            updateParent(req, resp);
        }else if(method.equals("updateSelf")){
            updateSelf(req, resp);
        }
    }

    private void updateSelf(HttpServletRequest req, HttpServletResponse resp) {
        String name = req.getParameter("name");
        String port = req.getParameter("port");
        String nodetype = req.getParameter("nodetype");
        String deploytype = req.getParameter("deploytype");
        String area = req.getParameter("area");

        NodeSelfDao dao = new NodeSelfDao();
        try {
            int portInt = Integer.parseInt(port);
            dao.updateSelf(name, portInt, nodetype, deploytype, area);
            returnSuccess(resp, "保存成功!");
        } catch (Exception e) {
            e.printStackTrace();
            returnError(resp, e.getMessage());
        }finally {
            dao.close();
        }

    }

    private void updateParent(HttpServletRequest req, HttpServletResponse resp) {
        String parentid = req.getParameter("parentid");
        String parentip = req.getParameter("parentip");
        String parentport = req.getParameter("parentport");
        String invitecode = req.getParameter("invitecode");


        if (StringUtils.isBlank(invitecode)){
            invitecode = "from_web";
        }
        NodeSelfDao dao = new NodeSelfDao();
        try {
            int port = Integer.parseInt(parentport);
            dao.updateParent(parentid, parentip, port, invitecode);
            returnSuccess(resp, "保存成功!");
        } catch (Exception e) {
            e.printStackTrace();
            returnError(resp, e.getMessage());
        }finally {
            dao.close();
        }

    }

    private void test(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String parentip = req.getParameter("parentip");
        String parentport = req.getParameter("parentport");

        RegisterClient client = new RegisterClient();
        Node node = new Node().current();
        node.setNodetype("node");

        boolean flag = client.testConnectServer(node, parentip, parentport);

        Map<String,String> result = new HashMap<String,String>();

        String msg = flag ? "连接成功" : "连接失败";
        this.returnSuccess(resp, msg);
    }

    private void query(HttpServletRequest req, HttpServletResponse resp) {
        NodeSelfDao dao = new NodeSelfDao();
        Node self = null;
        try {
            self = dao.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            dao.close();
        }

        this.writeJson(resp, self);
    }

    private void save(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Node self = (Node) parseForm(req, Node.class);
        self.current();

        HashMap<String,String> result = new HashMap<String, String>();
        NodeSelfDao dao = new NodeSelfDao();

        try {
            dao.validDuplicate(self);
            dao.insert(self);
            returnSuccess(resp, "上传成功!");
        } catch (Exception e) {
            returnError(resp, e.getMessage());
        }finally {
            dao.close();
        }
    }

}
