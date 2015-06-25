package client.servlet;

import bean.InviteCode;
import client.dao.InviteCodeDao;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by QPing on 2015/3/19.
 */
public class InviteServlet extends BaseServlet {

    protected final Logger log = Logger.getLogger(InviteServlet.class);


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
//        System.out.println("doGet");
//        resp.getWriter().write("hello world!");
//        resp.getWriter().close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String method = req.getParameter("method");

        if(method.equals("queryAll")){
            queryAll(req, resp);
        }else if(method.equals("createInviteCode")){
            createInviteCode(req, resp);
        }
    }

    private void createInviteCode(HttpServletRequest req, HttpServletResponse resp) {
        InviteCodeDao dao = new InviteCodeDao();
        dao.random();
        dao.close();
        this.writeJson(resp, 1);
    }

    private void queryAll(HttpServletRequest req, HttpServletResponse resp) {
        InviteCodeDao dao = new InviteCodeDao();
        List<InviteCode> list = dao.queryAll();
        dao.close();
        this.writeJson(resp, list);
    }

}
