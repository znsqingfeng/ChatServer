package chat.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;

/**
 * Created by zhanggd on 15/9/18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultMessage<T> extends AckMessage {
    private T result;

    public ResultMessage(){
        success=true;
    }
    public ResultMessage(boolean success,T result){
        this.success = success;
        this.result = result;
    }
    public T getResult() {
        return result;
    }

    public ResultMessage setResult(T result) {
        if(result!=null){
            this.result = result;
        }
        return this;
    }
    public HashMap<String,Object> meta;

    public void addMeta(String key,Object data){
        if(this.meta==null){
            meta=new HashMap<String,Object>();
        }
        meta.put(key,data);
    }

    public HashMap<String, Object> getMeta() {
        return meta;
    }

    public void setMeta(HashMap<String, Object> meta) {
        this.meta = meta;
    }
}
