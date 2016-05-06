package chat.test;

import chat.model.Message;
import com.alibaba.fastjson.JSON;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;

/**
 * Created by zhanggd on 2016/4/28.
 */
public class MinaClient {
//    static String address = "192.168.1.180";
    static String address = "127.0.0.1";
    static int port = 9001;
    public static void main(String[] args) {
        ConnectFuture cf = getconnect(address,port);

        Message message = new Message();
        message.ID = System.currentTimeMillis() / 1000;
        message.MessageType = 0;
        message.Content = "send to 10000";
        message.UserId = 20000;
        message.TargetId = 10000;
        message.Created = System.currentTimeMillis() / 1000;
        try {
            sendMessage(cf, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 创建连接
     * @param address
     * @param port
     */
    static ConnectFuture getconnect(String address,int port){
        NioSocketConnector connector = new NioSocketConnector();
        connector.getFilterChain().addLast(
                "codec", new ProtocolCodecFilter(new ClientEncoder(), new ClientDecoder())); // 设置编码过滤器
        connector.setConnectTimeout(300);
        connector.setHandler(new ClientHandler());// 设置事件处理器

        return connector.connect(new InetSocketAddress(address,port));// 建立连接
    }

    public static void sendMessage(ConnectFuture cf,Message message) throws Exception{
        cf.awaitUninterruptibly();// 等待连接创建完成
        cf.getSession().write(message);// 发送消息 这里是发送字节数组的重点
        cf.getSession().getCloseFuture().awaitUninterruptibly();// 等待连接断开
    }
}