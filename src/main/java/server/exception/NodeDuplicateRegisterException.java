package server.exception;

/**
 * Created by QPing on 2015/3/27.
 */
public class NodeDuplicateRegisterException extends Exception{

    public  NodeDuplicateRegisterException(String id,String msg){
        super(msg);
        this.id = id;
    }


    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
