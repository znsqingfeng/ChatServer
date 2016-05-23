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

    public int  pageIndex = 0;
    public int pageSize = 10;
    public int pageNo;
    public int totalCount;
    public Long timestamp = null;//最后一条数据时间戳

    public ListResult addMeta(String key,Object data){
        if(this.meta==null){
            meta=new HashMap<String,Object>();
        }
        meta.put(key,data);
        return this;
    }

    public void setPageInfo(PageInfoList list){
        this.pageIndex = list.getPageIndex();
        this.pageSize = list.getPageSize();
        this.pageNo = list.getPageNo();
        this.totalCount = list.getTotalCount();
    }

    public void setPageInfo(int pageIndex,int pageSize){
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.pageNo = pageIndex/pageSize + 1;
    }

    public ListResult(){
        success=true;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
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


