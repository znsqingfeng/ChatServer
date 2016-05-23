package chat.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;


/**
 * Created by zhanggd on 15/9/18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AckMessage {
    public boolean success =true;
    public String code;
    public String message;
    public AckMessage(boolean success){
      this.success = success;
    }
    public AckMessage(boolean success, Object errorCode, String message){
        this.success = success;
        this.message=message;
        this.code= errorCode.toString();
    }
    public AckMessage(boolean success, int errorCode, String message){
        this.success = success;
        this.message=message;
        this.code= String.valueOf(errorCode);
    }
    public AckMessage(boolean success, String message){
      this.success = success;
      this.message=message;
    }

    public AckMessage(){}

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
