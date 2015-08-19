package tick;


import client.httpclient.common.Result;
import client.httpclient.common.TickResult;
import org.apache.http.message.BasicNameValuePair;
import server.servlet.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * 接收子节点心跳请求
 *
 * 1 更新子节点状态
 * 2 如果有命令则返回命令
 *
 * Created by QPing on 2015/8/11.
 */
public class TickServerService  extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String nodeid = req.getParameter("nodeid");
        String name = req.getParameter("name");
        String typeid = req.getParameter("typeid");
        String mac = req.getParameter("mac");

        updateClientState(nodeid,name,typeid,mac);
        returnNeedReq(resp);
    }

    private void updateClientState(String nodeid, String name, String typeid, String mac) {
        // TODO 更新子节点状态
    }


    private void returnNeedReq(HttpServletResponse resp) {
        // TODO 获取是否要子节点发起请求

        String reqNeed = TickResult.NOT_NEED_REQ;
        String reqClass = "";
        String reqID = "";

        HashMap<String,String> result = new HashMap<String, String>();
        result.put("code", TickResult.SUCCESS);
        result.put("msg", "");
        result.put("content", "");

        result.put("reqNeed", reqNeed);
        result.put("reqClass", reqClass);
        result.put("reqID", reqID);

        writeJson(resp, result);
    }
}
