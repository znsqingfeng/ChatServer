package chat.server;

import chat.dao.interfaces.MessageService;
import chat.model.Message;
import chat.enums.MessageTypes;
import chat.utils.Util;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhanggd on 2016/4/27.
 */
public class RequestDispatcher {
    private static Logger logger = LoggerFactory.getLogger(RequestDispatcher.class);
    private MessageService messageService;
    private ChatServer chatServer;
    private ServerModel serverModel;

    public void dispatcher(IoSession session,Message message){
        //入库
        message.ID = Util.createMessageId;

        messageService.insert(message);
        //todo 异步入库
        if (message.MessageType == MessageTypes.TextMessage.getValue()){
            if (message.IsGroup == 1)
                chatServer.sendToGroup(message);
            else if (message.IsGroup == 0)
                chatServer.sendToClient(session,message);
        }
        else if (message.MessageType == MessageTypes.ImageMessage.getValue()){
            //如果是普通消息 直接发送
            chatServer.sendToClient(session,message);
        }
        else if (message.MessageType == MessageTypes.AudioMessage.getValue()){
            //如果是普通消息 直接发送
            chatServer.sendToClient(session,message);
        }
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
}
