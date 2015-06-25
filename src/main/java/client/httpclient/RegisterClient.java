package client.httpclient;

import bean.Node;
import bean.RegisterInfo;
import client.bean.TestConnResult;
import client.dao.RegisterDao;
import client.httpclient.common.FormPost;
import client.httpclient.common.Result;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by QPing on 2015/3/23.
 */
public class RegisterClient {
    protected final Logger log = Logger.getLogger(RegisterClient.class);

    private ObjectMapper mapper = new ObjectMapper() ;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public boolean registerToServer(Node self, String ip,String port, String inviteCode){
        System.out.println("开启注册... ");
        if(null == self || null == ip || null == port || null == inviteCode)
            return false;
        RegisterDao dao = new RegisterDao();
        try {
            // 不能向自己发起注册请求
            if(ip.equals("127.0.0.1") || ip.equals("localhost")){
//                return false;
            }

            // 判断是否已注册
            RegisterInfo currentRegister = dao.querySelf();
            if(ip.equals(currentRegister.getParentip()) && port.equals(currentRegister.getParentport())){
                return false;
            }
            // 向父节点发出注册请求
            log.info("向父节点发出注册请求");

            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("nodeId", self.getNodeid()));
            nvps.add(new BasicNameValuePair("nodeType", self.getNodetype()));
            nvps.add(new BasicNameValuePair("ip", self.getIp()));
            nvps.add(new BasicNameValuePair("port", self.getPort() + ""));
            nvps.add(new BasicNameValuePair("mac", self.getMac()));
            nvps.add(new BasicNameValuePair("javaversion", self.getJavaversion()));
            nvps.add(new BasicNameValuePair("osname", self.getOsname()));
            nvps.add(new BasicNameValuePair("osversion", self.getOsversion()));
            nvps.add(new BasicNameValuePair("hostname", self.getHostname()));
            nvps.add(new BasicNameValuePair("invitecode", inviteCode));

            Result registerResult = (Result) FormPost.send(ip, port , "/transfer/client/servlet/DownloadServlet?method=register", nvps, Result.class);

            // 注册成功或者已经注册
            if (registerResult != null && (registerResult.code.equals("1") || registerResult.code.equals("-1"))){
                log.info("注册成功,回写状态");
                String current = dateFormat.format(new Date());
                RegisterInfo registerInfo = new RegisterInfo();
                registerInfo.setStatus(RegisterInfo.STATUS_OK);
                registerInfo.setLasttime(current);
                registerInfo.setRegistertime(current);
                registerInfo.setParentport(port);
                registerInfo.setInvitecode(inviteCode);
                registerInfo.setLinkstatus(RegisterInfo.LINKSTATUS_OK);
                registerInfo.setParentid(registerResult.content);
                registerInfo.setParentip(ip);
                registerInfo.setSelfid(self.getNodeid());
                registerInfo.setSelfhostname(self.getHostname());
                registerInfo.setSelfip(self.getIp());
                registerInfo.setSelfmac(self.getMac());
                registerInfo.setSelfnodetype(self.getNodetype());
                dao.save(registerInfo);
                return true;
            }else{
                log.error(registerResult.msg);
                return false;
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            dao.close();
        }
        return false;
    }

    public boolean testConnectServer(Node self, String ip,String port){
        log.info("开启测试... ");
        if(null == self || StringUtils.isBlank(ip) || StringUtils.isBlank(port))
            return false;
        try {
            // 向父节点发出测试连接请求
            log.info("向父节点发出测试连接请求:" + ip + ":" + port);
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("http://" + ip + ":" + port + "/transfer/client/servlet/DownloadServlet?method=test");
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("nodeId", self.getNodeid()));
            nvps.add(new BasicNameValuePair("ip", self.getIp()));
            nvps.add(new BasicNameValuePair("port", self.getPort() + ""));
            nvps.add(new BasicNameValuePair("mac", self.getMac()));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));

            CloseableHttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            EntityUtils.consume(entity);

            if(StringUtils.isBlank(result)) {
                log.error("父节点没有回答");
                return false;
            }

            TestConnResult testConnResult = mapper.readValue(result, TestConnResult.class);

            // 连接成功
            if (testConnResult.code.equals("1")){
                log.info("测试连接成功!");
                return true;
            }else{
                log.error(testConnResult.msg);
                return false;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
