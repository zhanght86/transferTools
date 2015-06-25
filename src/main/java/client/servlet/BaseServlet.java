package client.servlet;

import org.apache.commons.beanutils.BeanUtils;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class BaseServlet extends HttpServlet {

    private ObjectMapper mapper = new ObjectMapper();

    public Object parseForm(HttpServletRequest request, Class clazz){
        Map map = request.getParameterMap();
        try {
            Object obj = clazz.newInstance();
            BeanUtils.populate(obj, map);
            return obj;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void writeJson(HttpServletResponse resp, Object obj) {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        try {
            String jsonStr = mapper.writeValueAsString(obj);
            resp.getWriter().write(jsonStr);
            resp.getWriter().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeStr(HttpServletResponse resp, String str) {
        resp.setCharacterEncoding("UTF-8");
        try {
            resp.getWriter().write(str);
            resp.getWriter().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void returnSuccess(HttpServletResponse resp, String msg){
        HashMap<String,String> result = new HashMap<String, String>();
        result.put("code", "1");
        result.put("msg", msg);
        writeJson(resp, result);
    }
    public void returnSuccess(HttpServletResponse resp) {
        returnSuccess(resp, "操作成功!");
    }

    public void returnError(HttpServletResponse resp, String code, String msg){
        HashMap<String,String> result = new HashMap<String, String>();
        result.put("code", code);
        result.put("msg", msg);
        writeJson(resp, result);
    }

    public void returnError(HttpServletResponse resp, String message) {
        returnError(resp, "0", message);
    }
    public void returnError(HttpServletResponse resp) {
        returnError(resp, "0", "操作失败!");
    }

}
