package chat.filter;

import chat.exception.ServerException;
import chat.model.ClientUser;
import chat.model.Message;
import chat.server.*;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhanggd on 2016/4/22.
 */
public class ChatServerHandler extends IoHandlerAdapter {
    private static Logger logger = LoggerFactory.getLogger(ChatServerHandler.class);
    private RequestDispatcher dispatcher;
    private HttpRequestDispatcher httpRequestDispatcher;
    private ChatServer chatServer;
    private ServerModel serverModel;

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        super.sessionCreated(session);
        logger.debug("session created not login");
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        super.sessionOpened(session);
        logger.debug("session opened");
        addClientUser(session);
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        serverModel.clientUserLogout(session);
        super.sessionClosed(session);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        super.sessionIdle(session, status);
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
         if (cause.toString().equals("java.io.IOException: Connection reset by peer"))
            return;
        logger.error("throws exception");
        logger.error("session.toString() : " + session.toString());
        logger.error("cause.toString() : " + cause.toString());

        if (cause.toString().equals("java.io.IOException: 远程主机强迫关闭了一个现有的连接。")) {
            try {
                session.close(true);
            } catch (Exception e) {}
            return;
        }
        String exceptionStack = "";
        for (StackTraceElement element : cause.getStackTrace())
            exceptionStack += element.toString() + "\n";
        logger.error("stack : " + exceptionStack);
        logger.error("Report Error Over!!");
    }

    @Override
    public void messageReceived(IoSession session, Object object) throws Exception {
        //收到tcp消息
        if (object instanceof Message){
            Message message = (Message)object;
            logger.debug("received message " + message.toString());
            //登录验证
            ClientUser clientUser = (ClientUser)serverModel.getUserFromIPTable(session);

            //未添加到在线用户
            if (clientUser != null){
                //未登录
                if (clientUser.isLogin){
                    dispatcher.dispatcher(session, message);
                }
                else {
                    Message sessionKey = new Message();
                    sessionKey.Content = "not login:" + serverModel.getIoSessionKey(session);
                    session.write(sessionKey);
                }
            }
            else {
                Message sessionKey = new Message();
                sessionKey.Content = "not join ip table,please reconnect:" + serverModel.getIoSessionKey(session);
                session.write(sessionKey);
                session.close(true);
            }
        }
        //收到http请求
        else if (object instanceof HttpRequestMessage){
            HttpRequestMessage request = (HttpRequestMessage)object;
            String response = httpRequestDispatcher.dispatcher(session,request);
            session.write(response);
            session.close(true);
        }
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        super.messageSent(session, message);
    }
    public void addClientUser(IoSession session){
        //已经存在
        try {
            if (serverModel.getUserFromIPTable(session) != null){
                logger.error("user is exist");
                return;
            }

        } catch (ServerException e) {
            e.printStackTrace();
        }
        logger.debug("user " + session.getRemoteAddress() + "login");
        try {
            ClientUser user = new ClientUser(session);
            user.ioSession = session;
            serverModel.addUserToIpTable(session, user);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public RequestDispatcher getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(RequestDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public ChatServer getChatServer() {
        return chatServer;
    }

    public void setChatServer(ChatServer chatServer) {
        this.chatServer = chatServer;
    }

    public ServerModel getServerModel() {
        return serverModel;
    }

    public void setServerModel(ServerModel serverModel) {
        this.serverModel = serverModel;
    }

    public HttpRequestDispatcher getHttpRequestDispatcher() {
        return httpRequestDispatcher;
    }

    public void setHttpRequestDispatcher(HttpRequestDispatcher httpRequestDispatcher) {
        this.httpRequestDispatcher = httpRequestDispatcher;
    }
}
