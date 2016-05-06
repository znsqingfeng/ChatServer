package chat.server;

import chat.utils.RedisClient;
import chat.utils.Sql2oUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.Jedis;

import java.io.IOException;

/**
 * Created by zhanggd on 2016/4/28.
 */
public class Server {
    public static boolean keepAliveSwitch = true;
    //	public static boolean keepAliveSwitch = false;
    static Logger logger = LoggerFactory.getLogger(Server.class);

    private ChatServer chatServer;
    private ServerModel serverModel;

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


    public void onDestroy() {
        chatServer.onDestroy();
    }

    public static void main(String[] args) throws IOException {
        //实例化bean
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        ((ChatServer) ctx.getBean("chatServer")).init();
        ((ServerModel)ctx.getBean("serverModel")).init();
        Sql2oUtil.getConnection();
        Jedis jedis = RedisClient.getJedis();
        if (jedis != null)
            RedisClient.returnResource(jedis);
    }
}