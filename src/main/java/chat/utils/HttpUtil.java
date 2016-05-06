package chat.utils;

import chat.server.HttpRequestMessage;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

/**
 * Created by zhanggd on 2016/5/4.
 */
public class HttpUtil {
    public static byte[] getBytes(String data, String charset){
        data = (data==null ? "" : data);
        if("".equals(charset)){
            return data.getBytes();
        }
        try {
            return data.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return data.getBytes();
        }
    }
}
