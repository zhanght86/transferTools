package tick;

import bean.Node;
import bean.Pack;
import client.bean.FileListResult;
import client.httpclient.common.FormPost;
import client.httpclient.common.Result;
import client.httpclient.common.TickResult;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import schedule.TaskBase;
import service.CachePool;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 子节点心跳服务
 *
 * 1 子节点每个n秒时间（n=10）向父节点发送心跳
 * 2 如果父节点没有响应，则心跳失败，则开启心跳加速（2秒一次）
 *
 * 心跳线程是单独的
 *
 * Created by QPing on 2015/8/11.
 */
public class TickClientService {

    // 快速心跳间隔 2 秒
    public static final int tickQuick = 1;
    // 正常心跳间隔 10 秒
    public static final int tickNormal = 10;
    // 当前心跳间隔 10 秒
    public static int tickInterval = tickNormal;

    public void start(){
        while(true){
            sendTick();
            sleep();
        }
    }

    private void sendTick() {
        Node parent = CachePool.parent;
        Node self = CachePool.self;

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("nodeid", self.getNodeid()));
        nvps.add(new BasicNameValuePair("name", self.getName()));
        nvps.add(new BasicNameValuePair("typeid", self.getArea()));
        nvps.add(new BasicNameValuePair("mac", self.getMac()));

        TickResult result = (TickResult) FormPost.send(parent.getIp(), "" + parent.getPort(),
                "/transfer/server/TickServlet", nvps, TickResult.class);

        // 心跳成功返回
        if (result != null && result.code.equals(Result.SUCCESS)){

            if(result.reqNeed.equals(TickResult.NEED_REQ)){
                tickInterval = tickQuick;
                // 父节点需要直接点发起请求
                String reqClassName = result.reqClass;
                Class<?> reqClass = null;
                try {
                    reqClass = Class.forName(reqClassName);
                    CRequest request = (CRequest) reqClass.newInstance();
                    request.send(result.reqID, result.reqParams);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                // 父节点没有命令，则心跳逐渐恢复正常
                if(tickInterval < tickNormal){
                    tickInterval++;
                }
            }

        } else {
            // 心跳失败，加速心跳
            tickInterval = tickQuick;
            System.out.println(result.msg);
        }
    }

    public void sleep(){
        try {
            Thread.sleep(tickInterval);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
