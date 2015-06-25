package schedule;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;


/**
 * 任务组：一组任务拥有相同的开始时间及重复时间
 */
public class TaskGroup extends TimerTask {

    protected Logger logger = Logger.getLogger(TaskGroup.class);

    List<TaskBase> taskLists = new ArrayList<TaskBase>();

    private static final boolean NEVER = false;
    private static final boolean SET = true;
    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";
    private SimpleDateFormat df = new SimpleDateFormat(PATTERN);

    private Date firstTime;
    private long period;
    private boolean endCondition = NEVER;
    private int endCount = -1;

    @Override
    public void run() {
        // 如果设定了结束条件
        if(endCondition == SET){
            if(endCount > 0){
                endCount --;
            }else{
                logger.info("任务组寿命到头了。");
                cancel();
                return;
            }
        }

        for(TaskBase task : taskLists){
            try{
                task.executeProxy();
            }catch(Exception e){
                logger.error("执行过程中遇到错误：" + e.getMessage());
            }
        }
    }

    public Date getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(Date firstTime) {
        this.firstTime = firstTime;
    }

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public void addTask(TaskBase task) {
        taskLists.add(task);
    }

    public void setEndCount(int endCount){
        this.endCount = endCount;
    }

    public void setEndCondition(String endCondition) {
        // 结束条件
        if(StringUtils.isBlank(endCondition) || endCondition.toUpperCase().equals("NEVER")){
            this.endCondition = NEVER;
            this.endCount = -1;
        }else{
            int temp = -1;
            try{
                temp = Integer.parseInt(endCondition);
            }catch (Exception e){}
            this.endCondition = SET;
            this.endCount = temp;
        }
    }

    public void setFirstTime(String firstTimeStr) {
        if(StringUtils.isBlank(firstTimeStr) || firstTimeStr.toUpperCase().equals("NOW")){
            setFirstTime(new Date());
        }else{
            try {
                setFirstTime(df.parse(firstTimeStr))  ;
            } catch (ParseException e) {
                setFirstTime(new Date());
            }
        }
    }

    public void setPeriod(String periodStr) {
        if(StringUtils.isBlank(periodStr)){
            setPeriod(60 * 60 * 1000);
        }else{
            setPeriod(new Formula().calculate(periodStr));
        }
    }
}
