package client.httpclient.common;

/**
 * Created by QPing on 2015/4/8.
 */
public class Result {
    public static final String SUCCESS = "1";
    public static final String ERROR = "0";

    public String code;     // 状态  SUCCESS or ERROR
    public String msg;      // 成功或失败提示
    public String content;  // 需要传回的json数据
}
