package db;

/**
 * Created by QPing on 2015/3/27.
 */
public class Page {
    int page = 1;
    int rows = 10;
    public Page(){

    }

    public Page(int page,int rows){
        set(page, rows);
    }

    public void set(int page, int rows){
        this.page = page;
        this.rows = rows;
        if(rows < 1 ){
            this.rows = 10;
        }
    }
    public Page(String page, String rows) {
        int pageInt = Integer.parseInt(page);
        int rowsInt = Integer.parseInt(rows);
        set(pageInt, rowsInt);
    }

    public String getPageSQL(){
        if(page < 1){
            return "";
        }
        int start = (page - 1) * rows;
        return " limit " + start + "," + rows;
    }

}
