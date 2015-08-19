package client.httpclient.common;

/**
 * Created by QPing on 2015/8/12.
 */
public class TickResult extends Result{
    public static String NOT_NEED_REQ = "0";
    public static String NEED_REQ = "1";

    public String reqNeed;
    public String reqClass; // 请求类名
    public String reqID;    // 请求的ID
    public String reqParams;
}
