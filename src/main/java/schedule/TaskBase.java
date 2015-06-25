package schedule;

import org.apache.log4j.Logger;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * 任务基础类
 */
public abstract class TaskBase {
    protected Logger log = Logger.getLogger(TaskBase.class);

    private String name;
    public long count = 0;  // 记录执行了几次
    public HashMap<String, String> propMap = new HashMap<String, String>();

    public void executeProxy(){
        count ++;
        log.info("执行任务" + getName() + ",当前第 " + count + "次");
        execute();
    }

    public abstract void execute();

    public void parseConf(Element conf){
        if(null == conf) return;
        List<Element> nodes = conf.elements();
        for (Iterator it = nodes.iterator(); it.hasNext();) {
            Element propElm = (Element) it.next();
            String propName = propElm.attributeValue("name");
            String propValue = propElm.getText();
            propMap.put(propName, propValue);
        }
    }

    public String getConf(String key){
        return propMap.get(key);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
