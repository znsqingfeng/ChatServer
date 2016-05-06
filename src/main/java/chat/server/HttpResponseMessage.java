package chat.server;

/**
 * Created by zhanggd on 2016/5/4.
 */
import java.util.*;

public class HttpResponseMessage {
    public static final int HTTP_STATUS_SUCCESS = 200;
    public static final int HTTP_STATUS_NOT_FOUND = 404;

    private Map<String, String> headers = null;

    private String body = null;

    private int status = HTTP_STATUS_SUCCESS;

    public HttpResponseMessage() {
        headers = new HashMap<String,String>();
        headers.put("Server", "ChatServer");
        headers.put("Date", new Date().toString());
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setContentType(String contentType) {
        headers.put("Content-Type", contentType);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        headers.put("Content-Length",String.valueOf(body.getBytes().length));
        this.body = body;
    }
    public String buildResponse(){
        StringBuilder response = new StringBuilder("HTTP/1.1 " + status + " OK\r\n");
        response.append("Content-Type: application/json; charset=UTF-8\r\n");
        //构造header
        Set<String> keys = headers.keySet();
        for (String key:keys)
            response.append(key + ": " + headers.get(key) + "\r\n");
        response.append("\r\n");
        response.append(body);
        return response.toString();
    }
}

