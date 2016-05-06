package chat.test;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import sun.net.www.http.HttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by zhanggd on 2016/4/28.
 */
public class Test {
    public static void main(String[] args) {
        try {
            final OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://127.0.0.1:9002/test")
                    .build();
            Response response = client.newCall(request).execute();
            System.out.println(response.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
