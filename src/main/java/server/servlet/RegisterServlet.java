package server.servlet;

import bean.Node;
import client.dao.InviteCodeDao;
import client.dao.NodeDao;
import client.dao.NodeSelfDao;
import db.DB;
import server.exception.NodeDuplicateRegisterException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by QPing on 2015/3/23.
 */
public class RegisterServlet extends BaseServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String method = req.getParameter("method");

        if(method.equals("update")){
            update(req, resp);
        }
    }

    private void update(HttpServletRequest req, HttpServletResponse resp) {
        String parentid = req.getParameter("parentid");
        String parentip = req.getParameter("parentip");
        String parentport = req.getParameter("parentport");
        String invitecode = "from_register_servlet";

        NodeSelfDao dao = new NodeSelfDao();
        try {
            int port = Integer.parseInt(parentport);
            dao.updateParent(parentid, parentip, port, invitecode);
            returnSuccess(resp, "注册成功!");
        } catch (Exception e) {
            e.printStackTrace();
            returnError(resp, e.getMessage());
        }finally {
            dao.close();
        }
    }

}
