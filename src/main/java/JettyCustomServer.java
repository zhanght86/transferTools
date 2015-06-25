import org.apache.commons.lang.StringUtils;
import org.apache.log4j.PropertyConfigurator;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.xml.XmlConfiguration;
import org.xml.sax.SAXException;
import schedule.ScheduleCenter;
import schedule.TaskGroup;
import service.CachePool;
import service.DatabaseService;
import util.FilePathUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class JettyCustomServer extends Server {

    private String xmlConfigPath;
    private String contextPath;
    private String warPath;
    private String resourceBase;
    private String webXmlPath;

    public static void main(String[] args) {
        String rootPath = FilePathUtils.getInstance().getProjectClassPath();
        String log4jPath = rootPath + "jetty/log4j.properties";
        String xmlConfigPath = rootPath + "jetty/jetty.xml";
        String contextPath = "/transfer";
        String resourceBase = rootPath +  "webapp";
        String webXmlPath = rootPath + "webapp/WEB-INF/web.xml";
        String taskXmlPath = rootPath + "schedule/tasks.xml";


        System.out.println("log4j config path : " + log4jPath);
        System.out.println("jetty config path : " + xmlConfigPath);
        System.out.println("task config path : " + taskXmlPath);

        PropertyConfigurator.configure(log4jPath);

        JettyCustomServer server = new JettyCustomServer(xmlConfigPath, contextPath, resourceBase, webXmlPath);

        // 启动数据库
        DatabaseService.getInstance().start();

        // 升级本地数据库
        DatabaseService.getInstance().update();

        // 服务器启动时，加载缓存池内容
        CachePool.getInstance();

        // 加载定时任务，定时去父节点下载增量包
        ScheduleCenter sc = new ScheduleCenter();
        TaskGroup taskGroup = sc.initTaskGroup(taskXmlPath);
        sc.start(taskGroup);

        try {
            server.start();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JettyCustomServer(String xmlConfigPath, String contextPath, String resourceBase, String webXmlPath) {
        this(xmlConfigPath, contextPath, resourceBase, webXmlPath, null);
    }

    public JettyCustomServer(String xmlConfigPath, String contextPath) {
        this(xmlConfigPath, contextPath, null, null, null);
    }

    public JettyCustomServer(String xmlConfigPath, String contextPath, String warPath) {
        this(xmlConfigPath, contextPath, null, null, warPath);
    }

    public JettyCustomServer(String xmlConfigPath, String contextPath, String resourceBase, String webXmlPath, String warPath) {
        super();
        if (StringUtils.isNotBlank(xmlConfigPath)) {
            this.xmlConfigPath = xmlConfigPath;
            readXmlConfig();
        }
        if (StringUtils.isNotBlank(warPath)) {
            this.warPath = warPath;
            if (StringUtils.isNotBlank(contextPath)) {
                this.contextPath = contextPath;
                applyHandle(true);
            }
        } else {
            if (StringUtils.isNotBlank(resourceBase))
                this.resourceBase = resourceBase;
            if (StringUtils.isNotBlank(webXmlPath))
                this.webXmlPath = webXmlPath;
            if (StringUtils.isNotBlank(contextPath)) {
                this.contextPath = contextPath;
                applyHandle(false);
            }
        }
    }

    private void readXmlConfig() {
        try {
            XmlConfiguration configuration = new XmlConfiguration(new FileInputStream(this.xmlConfigPath));
            configuration.configure(this);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (SAXException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void applyHandle(Boolean warDeployFlag) {
        ContextHandlerCollection handler = new ContextHandlerCollection();
        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath(contextPath);
//        webapp.setDefaultsDescriptor("jetty/webdefault.xml");
        if (!warDeployFlag) {
            webapp.setResourceBase(resourceBase);
            webapp.setDescriptor(webXmlPath);
        } else {
            webapp.setWar(warPath);
        }
        handler.addHandler(webapp);
        super.setHandler(handler);
    }

    public void startServer() {
        try {
            super.start();
            System.out.println("current thread:" + super.getThreadPool().getThreads() + "| idle thread:" + super.getThreadPool().getIdleThreads());
            super.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getXmlConfigPath() {
        return xmlConfigPath;
    }

    public void setXmlConfigPath(String xmlConfigPath) {
        this.xmlConfigPath = xmlConfigPath;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getResourceBase() {
        return resourceBase;
    }

    public void setResourceBase(String resourceBase) {
        this.resourceBase = resourceBase;
    }

    public String getWebXmlPath() {
        return webXmlPath;
    }

    public void setWebXmlPath(String webXmlPath) {
        this.webXmlPath = webXmlPath;
    }

    public String getWarPath() {
        return warPath;
    }

    public void setWarPath(String warPath) {
        this.warPath = warPath;
    }


}