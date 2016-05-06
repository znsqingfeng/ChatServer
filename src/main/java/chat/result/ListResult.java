package chat.result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanggd on 2016/5/5.
 */
public class ListResult {
    int status;
    int code = 200;
    List<Object> result = new ArrayList<Object>();

    public ListResult(boolean status,List<Object> result){
        if (status)
            this.status = 1;
        else
            this.status = 0;
        this.result = result;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Object> getResult() {
        return result;
    }

    public void setResult(List<Object> result) {
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
