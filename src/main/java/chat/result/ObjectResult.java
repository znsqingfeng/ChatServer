package chat.result;

/**
 * Created by zhanggd on 2016/5/5.
 */
public class ObjectResult {
    int status = 0;
    int code = 500;
    Object result = "server error";

    public ObjectResult(){}

    public ObjectResult(boolean status,Object result){
        if (status)
            this.status = 1;
        else
            this.status = 0;
        this.code = 200;
        this.result = result;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
