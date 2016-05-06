package chat.server;

import chat.enums.Constant;
import chat.enums.MessageTypes;
import chat.exception.ServerException;
import chat.model.ClientUser;
import chat.model.Message;
import chat.result.ObjectResult;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.InetSocketAddress;
import java.util.*;

/**
 * Created by zhanggd on 2016/4/28.
 */
public class ServerModel extends Observable {
    private ChatServer chatServer;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    // 心跳包间隔(5秒)
    public static final int KEEP_ALIVE_PACKET_TIME = 5000;

    private Hashtable<String, ClientUser> clientUserIpTable = new Hashtable<String, ClientUser>();
    private Hashtable<Long, ClientUser> clientUserIdTable = new Hashtable<Long, ClientUser>();


    public void init() {
        // 开始新线程
        // new Thread(new DealClientRequest()).start();
        new Thread(new KeepAlivePacketSenser()).start();
        // new Thread(new CheckWaitClientResponseThread()).start();
    }


    /**
     * 用户登录
     * @param session
     * @param clientUser
     */
    public void clientUserLogin(IoSession session,ClientUser clientUser) {
        try {
            clientUser.onLine = true;
            clientUser.isLogin = true;
            clientUser.ioSession = session;
            clientUserIdTable.put(clientUser.ID, clientUser);
            clientUserIpTable.put(getIoSessionKey(session), clientUser);
        }catch (Exception e){
            logger.error("login error",e);
            throw e;
        }
    }

    /**
     * 用户登出
     * @param session
     */
    public void clientUserLogout(IoSession session) {
        try {
            if (session != null){
                ClientUser user = getUserFromIPTable(session);
                if (user != null){
                    clientUserIdTable.remove(user.ID);
                }
                String key = getIoSessionKey(session);
                if (clientUserIpTable.contains(key)){
                    clientUserIpTable.remove(key);
                }
            }
        } catch (Exception e) {
            logger.error("Logout Error!");
        }
    }


    /**
     * 根据UserID获取用户
     * @param userId
     * @return
     */
    public ClientUser getClientUserByUserId(long userId) {
        ClientUser user = clientUserIdTable.get(userId);
        try {
            if (user == null || user.onLine == false || !user.ioSession.isConnected())
                clientUserIdTable.remove(userId);
        } catch (Exception e) {
            return null;
        }

        return user;
    }

    /**
     * 从iosession生成Key
     *
     * @param ioSession
     * @return
     * @throws ServerException
     */
    public static String getIoSessionKey(IoSession ioSession){
        if (ioSession.getRemoteAddress() == null)
            return "";

        return ((InetSocketAddress) ioSession.getRemoteAddress()).getAddress().toString() + ":"
                + ((InetSocketAddress) ioSession.getRemoteAddress()).getPort();
    }

    /**
     * 添加连接用户
     * @param ioSession
     * @param clientUser
     * @throws ServerException
     */
    public void addUserToIpTable(IoSession ioSession, ClientUser clientUser) throws ServerException {
        synchronized (clientUserIpTable) {
            clientUser.onLine = true;
            clientUser.isLogin = false;
            clientUserIpTable.put(getIoSessionKey(ioSession), clientUser);
        }
    }

    /**
     * 获取用户
     * @param key
     * @return
     */
    public ClientUser getUserFromIpTable(String key) {
        synchronized (clientUserIpTable) {
            return clientUserIpTable.get(key);
        }
    }

    public ClientUser getUserFromIPTable(IoSession ioSession) throws ServerException {
        synchronized (clientUserIpTable) {
            return getUserFromIpTable(getIoSessionKey(ioSession));
        }
    }

    /**
     * 从在线列表删除用户
     * @param session
     */
    public void removeUserFromIpTable(IoSession session) {
        synchronized (clientUserIpTable) {
            removeUserFromIpTable(getIoSessionKey(session));
        }
    }

    public void removeUserFromIpTable(String key) {
        synchronized (clientUserIpTable) {
            clientUserIpTable.remove(key);
        }
    }

    /**
     * 广播前的设置变更
     */
    public void setChange() {
        super.setChanged();
    }

    public void notify(Object obj) {
        setChange();
        notifyObservers(this);
    }


    /**
     * 发送心跳包
     */
    private class KeepAlivePacketSenser implements Runnable, IoFutureListener<IoFuture> {
        private Iterator iterator;
        private String key;
        private Set<String> keySet;

        public void run() {
            // 创建心跳包
            Message message = new Message();

            while (true) {
                try {
                    Thread.sleep(KEEP_ALIVE_PACKET_TIME);
                    // 是否开启心跳检验
                    if (!Server.keepAliveSwitch)
                        continue;

                    ClientUser user;

                    synchronized (clientUserIpTable) {
                        keySet = clientUserIpTable.keySet();

                        iterator = keySet.iterator();
                        while (iterator.hasNext()) {
                            key = iterator.next().toString();

                            if (!clientUserIpTable.containsKey(key))
                                continue;
                            user = clientUserIpTable.get(key);

                            //todo generate messageid
                            message.ID = System.currentTimeMillis() / 1000;
                            message.MessageType = MessageTypes.HeartBeat.getValue();
                            message.Content = "heart beat from server";
                            message.UserId = Constant.SystemUser;
                            message.TargetId = user.ID;
                            message.Created = System.currentTimeMillis() / 1000;

                            IoSession session = user.ioSession;
                            if (session != null && user.isLogin) {
                                WriteFuture writeFuture = session.write(message);
                                writeFuture.addListener(this);
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    logger.info("'Send KeepAlivePacket Thread' fail at sleep module!\n" + e.toString());
                    System.err.println("发行心跳包线程异常! -----睡眠模块");
                }
            }
        }

        public void operationComplete(IoFuture future) {
            // 掉线处理
            if (((WriteFuture) future).isWritten()) {
                //未掉线
            } else {
                // 发送失败，判定掉线
                logger.debug("ServerModel", "Client User(" + key + ") was offline,now delete it!");
                try {
                    String key = iterator.next().toString();
                    clientUserIpTable.get(key).ioSession.close(true);
                } catch (Exception e) {
                }
                iterator.remove();
            }
        }
    }

    public ChatServer getChatServer() {
        return chatServer;
    }

    public void setChatServer(ChatServer chatServer) {
        this.chatServer = chatServer;
    }
}
