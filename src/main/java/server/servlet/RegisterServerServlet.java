package server.servlet;

import bean.Node;
import client.dao.InviteCodeDao;
import client.dao.NodeDao;
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
public class RegisterServerServlet extends BaseServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String method = req.getParameter("method");

        if(method.equals("register")){
            register(req, resp);
        }else if(method.equals("test")){
            test(req, resp);
        }
    }

    private void register(HttpServletRequest req, HttpServletResponse resp) {
        Map<String,String> result = new HashMap<String,String>();

        NodeDao nodeDao = new NodeDao();
        DB db = nodeDao.getDB();
        InviteCodeDao inviteCodeDao = new InviteCodeDao();
        try {
            db.beginTransaction();

            Node current = new Node().current();
            Node child = (Node) parseForm(req, Node.class);
            inviteCodeDao.validRegisterNode(child);
            nodeDao.validDuplicateNode(child, current.getNodeid());
            nodeDao.insert(child);
            inviteCodeDao.update(child.getInvitecode());

            result.put("code","1");
            result.put("id", current.getNodeid());
            System.out.println("接受子节点注册,mac:" + child.getNodeid() + " ip:" + child.getIp() + " hostname:" + child.getHostname());
            db.commit();
        }catch (NodeDuplicateRegisterException e){
            // 已经注册
            db.rollback();
            result.put("code", "-1");
            result.put("id", e.getId());
            result.put("msg",e.getMessage());
        }catch (Exception e) {
            db.rollback();
            result.put("code","0");
            result.put("msg",e.getMessage());
        }finally {
            db.endTransaction();
            db.close();
        }

        this.writeJson(resp, result);
    }

    private void test(HttpServletRequest req, HttpServletResponse resp) {
//        Node child = (Node) parseForm(req, Node.class);
        Map<String,String> result = new HashMap<String,String>();
        result.put("code","1");
        result.put("msg","");
        this.writeJson(resp, result);
    }

}
