package chat.server;

import chat.exception.ServerException;
import chat.filter.ChatServerHandler;
import chat.filter.ServerProtocolCodecFactory;
import chat.filter.TcpMessageDecoder;
import chat.filter.CommonEncoder;
import chat.model.Message;
import com.alibaba.fastjson.JSON;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanggd on 2016/4/22.
 */
public class ChatServer {
    private static Logger logger = LoggerFactory.getLogger(ChatServer.class);
    private String host = "127.0.0.1";
    public static int tcpPort = 9001;
    public static int httpPort = 9002;
    private InetAddress address;
    private IoAcceptor acceptor;
    private List<SocketAddress>  socketAddress;
    private ChatServerHandler chatServerHandler;
    private RequestDispatcher dispatcher;
    private ServerModel serverModel;

    public void init(){
        if (address != null || chatServerHandler == null)
            return;
        logger.debug("start init server");
        initHost();
        logger.debug("server host is " + host);
        acceptor = new NioSocketAcceptor();
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ServerProtocolCodecFactory()));
        logger.debug("add protocol filter and encoder decoder");
//        acceptor.getSessionConfig().setMaxReadBufferSize(1024 * 8);
        acceptor.setHandler(chatServerHandler);
        try {
            socketAddress = new ArrayList<SocketAddress>();
            socketAddress.add(new InetSocketAddress(tcpPort));
            socketAddress.add(new InetSocketAddress(httpPort));
            acceptor.bind(socketAddress);
            logger.debug("server is started bind port http:" + httpPort + ",tcp:" + tcpPort);
        } catch (IOException e) {
            logger.error("server bind error port http:" + httpPort + ",tcp:" + tcpPort,e);
        }

    }
    public void initHost(){
        try {
            address = InetAddress.getByName(host);
        } catch (UnknownHostException e) {
            logger.error("unknown host address:" + address,e);
        }
    }

    public void onDestroy(){
        acceptor.unbind(socketAddress);
    }
    public void sendToClient(IoSession session,Object msg){
        sendToClient(new WaitClientResponse(session, (Message) msg));
    }
    public void sendToClient(WaitClientResponse response){
        sendToClient(response,0);
    }
    private void sendToClient(final WaitClientResponse waitClientResponse,final int times){
        try {
            //用户离线
            if (serverModel.getUserFromIPTable(waitClientResponse.ioSession) == null){
                if (waitClientResponse.waitClientResponseCallBack != null)
                    waitClientResponse.waitClientResponseCallBack.beforeDelete();
                //todo 离线消息处理 可以放在缓存或者设置消息状态
                return;
            }
            //获得消息
            Message message = waitClientResponse.message;
            //获取信息接收者的session
            IoSession session = serverModel.getClientUserByUserId(message.TargetId).ioSession;
            //发送消息
            WriteFuture writeFuture = session.write(message);

            writeFuture.addListener(new IoFutureListener<IoFuture>() {
                public void operationComplete(IoFuture ioFuture) {
                    if (((WriteFuture)ioFuture).isWritten())
                        logger.debug("message is sent:" + JSON.toJSONString(waitClientResponse.message));
                    else{
                        if (times < 3){
                            //发送超时
                            sendToClient(waitClientResponse,times + 1);
                        }
                        else {
                            //重发超过限制 不再发送
                            return;
                        }
                    }
                }
            });
        } catch (ServerException e) {
            e.printStackTrace();
        }
    }
    //发送群消息
    public void sendToGroup(Message message){
        //todo 循环groupid为message中targetid的群中的用户发送消息
        //todo 先通过serverModel.getClientUserByUserId获取用户session
        //todo 在通过chatServer.sendToClient发送消息 走同一个离线消息处理逻辑
    }

    public ChatServerHandler getChatServerHandler() {
        return chatServerHandler;
    }

    public void setChatServerHandler(ChatServerHandler chatServerHandler) {
        this.chatServerHandler = chatServerHandler;
    }

    public RequestDispatcher getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(RequestDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public ServerModel getServerModel() {
        return serverModel;
    }

    public void setServerModel(ServerModel serverModel) {
        this.serverModel = serverModel;
    }
}