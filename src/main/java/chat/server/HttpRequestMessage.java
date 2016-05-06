package chat.server;

/**
 * Created by zhanggd on 2016/5/4.
 */
import java.util.Map;

public class HttpRequestMessage {
    public static int POST = 2;
    public static int GET = 1;

    private int method;
    private Map<String, String> param;
    private String url;
    private Map<String, String> headers = null;
    private String body;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public Map<String, String> getParam() {
        return param;
    }

    public void setParam(Map<String, String> param) {
        this.param = param;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}

