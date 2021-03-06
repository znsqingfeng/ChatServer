package chat.server;

import chat.dao.interfaces.MessageService;
import chat.dao.interfaces.UserService;
import chat.enums.Constant;
import chat.enums.RequestURL;
import chat.model.ClientUser;
import chat.model.Message;
import chat.model.UserVo;
import chat.result.AckMessage;
import chat.result.ResultMessage;
import chat.utils.RedisClient;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

/**
 * Created by zhanggd on 2016/4/27.
 */
public class HttpRequestDispatcher {
    private static Logger logger = LoggerFactory.getLogger(HttpRequestDispatcher.class);
    private MessageService messageService;
    private UserService userService;
    private ChatServer chatServer;
    private ServerModel serverModel;

    public String dispatcher(IoSession session,HttpRequestMessage request){

        AckMessage result = null;

        switch (request.getUrl()){
            case RequestURL.Login:
                result = login(session,request.getBody());
                break;
            case RequestURL.UpdateProfile:
                result = updateProfile(request.getBody());
                break;
            case RequestURL.AddFriend:
                result = addFriend(request.getBody());
                break;
            case RequestURL.FriendList:
                result = friendList(request.getBody());
                break;
            case RequestURL.DeleteFriend:
                result = deleteFriend(request.getBody());
                break;
            case RequestURL.JoinGroup:
                result = joinGroup(request.getBody());
                break;
            case RequestURL.LeaveGroup:
                result = joinGroup(request.getBody());
                break;
            default:
                result = notFound(request.getUrl());
        }


        HttpResponseMessage response = new HttpResponseMessage();

        response.setBody(JSON.toJSONString(result));

        return response.buildResponse();
    }

    public AckMessage notFound(String url){
        AckMessage result = new AckMessage(false,"url '" + url + "' not found");

        return result;
    }

    public AckMessage updateProfile(String body){
        JSONObject object = JSON.parseObject(body);
        long id = object.getLong("user_id");
        String avatar = object.getString("avatar");
        String nickName = object.getString("nick_name");

        ClientUser user = userService.getUserById(id);
        user.Avatar = avatar;
        user.NickName = nickName;
        user = userService.updateProfile(user);

        ResultMessage<UserVo> result = new ResultMessage<UserVo>(true,UserVo.fromClientUser(user));

        return result;
    }

    public AckMessage addFriend(String body){
        //todo add friend
        return new AckMessage();
    }
    public AckMessage deleteFriend(String body){
        //todo delete friend
        return new AckMessage();
    }
    public AckMessage login(IoSession session,String body){
        JSONObject object = JSON.parseObject(body);
        long userId = object.getLong("user_id");
        String value = object.getString("ticket");
        String sessionKey = object.getString("session_key");
        String key = "XXGJ.ChatKey." + userId;

        Jedis jedis = null;
        try {
            ClientUser clientUser = userService.getUserById(userId);
            if (clientUser != null){
                if (value != null && !"".equals(value)){
                    jedis = RedisClient.getJedis();
                    String auth = jedis.get(key);
                    //方便测试 设置固定值
                    auth = "dfa33d212fds343fa";
                    if (value.equals(auth)){
                        //通过seesionKey获取session
                        ClientUser tcp = serverModel.getUserFromIpTable(sessionKey);
                        if (tcp == null){
                            return new AckMessage(false,"user not connect");
                        }
                        IoSession tcpSession = tcp.ioSession;
                        serverModel.clientUserLogin(tcpSession,clientUser);
                        //todo 登录成功 处理离线消息
                        return new AckMessage(true,"login success");
                    }
                    else {
                        return new AckMessage(false,"error ticket");
                    }
                }
                else {
                    return new AckMessage(false,"error ticket");
                }
            }
            else {
                serverModel.removeUserFromIpTable(session);
                return new AckMessage(false,"user not found");
            }
        }catch (Exception e){
            logger.error("check value error",e);
        }finally {
            RedisClient.returnResource(jedis);
        }
        return new AckMessage();
    }
    public AckMessage joinGroup(String body){
        //todo join group
        return new AckMessage();
    }
    public AckMessage leaveGroup(String body){
        //todo leave group
        return new AckMessage();
    }

    public AckMessage friendList(String body){
        //todo 先获取用户的好友 再校验是否在线
        return new AckMessage();
    }


    public MessageService getMessageService() {
        return messageService;
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
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

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
