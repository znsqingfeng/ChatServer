package chat.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhanggd on 15/9/18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListResult<T> extends AckMessage {
    public HashMap<String,Object> meta;

    public List<T> result=new ArrayList<T>();

    public ListResult addMeta(String key,Object data){
        if(this.meta==null){
            meta=new HashMap<String,Object>();
        }
        meta.put(key,data);
        return this;
    }

    public ListResult(){
        success=true;
    }

    public HashMap<String, Object> getMeta() {
        return meta;
    }

    public void setMeta(HashMap<String, Object> meta) {
        this.meta = meta;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }
}


