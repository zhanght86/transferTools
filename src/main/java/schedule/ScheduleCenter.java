package schedule;


import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;

/**
 * 定时任务中心
 * 用于解析任务xml，生成任务进程组，并定时执行
 * Created by QPing on 2015/1/14.
 */
public class ScheduleCenter {

    protected Logger logger = Logger.getLogger(ScheduleCenter.class);

    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";
    private SimpleDateFormat df = new SimpleDateFormat(PATTERN);

    Timer timer = new Timer();;

    public TaskGroup initTaskGroup(String path){

        TaskGroup taskGroup = new TaskGroup();
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(path);
            Element rootElm = document.getRootElement();

            Element taskGroupElm = rootElm.element("TaskGroup") ;

            // 开始时间
            String firstTimeStr = taskGroupElm.attributeValue("firstTime");
            taskGroup.setFirstTime(firstTimeStr);

            // 执行间隔
            String periodStr = taskGroupElm.attributeValue("period");
            taskGroup.setPeriod(periodStr);

            // 结束条件
            String endCondition = taskGroupElm.attributeValue("endCondition");
            taskGroup.setEndCondition(endCondition);

            // 添加各个任务到任务组
            List<Element> nodes = taskGroupElm.elements();
            for (Iterator it = nodes.iterator(); it.hasNext();) {
                Element taskElm = (Element) it.next();
                String className = taskElm.elementText("class");
                Class<?> taskClass = Class.forName(className);
                TaskBase  task = (TaskBase) taskClass.newInstance();

                String taskName = taskElm.elementText("name");
                task.setName(taskName);

                task.parseConf(taskElm.element("conf"));

                taskGroup.addTask(task);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.info("创建任务组,触发时间：" + df.format(taskGroup.getFirstTime()) + " , 间隔时间：" + taskGroup.getPeriod() / 1000 + "s");
        return taskGroup;
    }

    public void start(TaskGroup taskGroup){
        timer.schedule(taskGroup, taskGroup.getFirstTime(), taskGroup.getPeriod());
    }

}
