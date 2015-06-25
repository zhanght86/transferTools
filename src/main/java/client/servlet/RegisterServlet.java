package client.servlet;

import bean.Node;
import bean.RegisterInfo;
import client.dao.RegisterDao;
import client.httpclient.RegisterClient;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by QPing on 2015/3/23.
 */
public class RegisterServlet  extends BaseServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String method = req.getParameter("method");

        if(method.equals("query")){
            query(req, resp);
        }else if(method.equals("register")){
            register(req, resp);
        }else if(method.equals("test")){
            test(req, resp);
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
        result.put("linkstatus", flag ? "1" : "0");
        this.writeJson(resp, result);
    }

    private void query(HttpServletRequest req, HttpServletResponse resp) {
//        DatabaseService service = new DatabaseService();
//        service.initDB();
        RegisterDao dao = new RegisterDao();
        RegisterInfo registerInfo = dao.querySelf();
        dao.close();
        this.writeJson(resp, registerInfo);
    }

    private void register(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String parentipNew = req.getParameter("parentipNew");
        String parentportNew = req.getParameter("parentportNew");
        String inviteCode = req.getParameter("inviteCode");

        RegisterClient client = new RegisterClient();
        Node node = new Node().current();
        node.setNodetype("node");

        boolean flag = client.registerToServer(node, parentipNew, parentportNew, inviteCode);
        String msg = flag ? "success" : "error";
        resp.sendRedirect("/jsp/register.jsp?msg=" + msg);
    }
}
