package chat.test;

/**
 * Created by zhanggd on 2016/4/29.
 */
import chat.enums.MessageTypes;
import chat.model.Message;
import com.alibaba.fastjson.JSON;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class ClientHandler extends IoHandlerAdapter{

    private int count = 0;

    /**
     * 当一个客户端连接进入时
     */
    @Override
    public void sessionOpened(IoSession session) throws Exception {

        System.out.println("incoming client: " + session.getRemoteAddress());

    }

    /**
     * 当一个客户端关闭时
     */
    @Override
    public void sessionClosed(IoSession session) throws Exception {

        System.out.println(session.getRemoteAddress() + "is Disconnection");

    }

    public void messageReceived(IoSession session, Object message)
            throws Exception {

        Message receive = (Message)message;

        System.out.println("收到服务端发来的消息为:" + message.toString());


        if (receive.MessageType == MessageTypes.HeartBeat.getValue()){
            Message send = new Message();
            send.ID = System.currentTimeMillis() / 1000;
            send.MessageType = 0;
            send.Content = "message from 10000";
            send.IsGroup = 0;
            send.UserId = 10000;
            send.TargetId = 10000;
            send.Created = System.currentTimeMillis() / 1000;
            session.write(send);
        }

        //将测试消息会送给客户端
        //session.write(str + count);
    }

}