package schedule;

/**
 * Created by QPing on 2015/1/19.
 */
public class Formula {

    public long calculate(String expression){
        String[] numbers = expression.split("\\*");
        long result = 1L;
        for(String number : numbers){
            result *= Long.parseLong(number.trim());
        }
        return result;
    }
}
