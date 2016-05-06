package chat.model;

import chat.enums.MessageTypes;

import java.util.HashMap;

/**
 * Created by zhanggd on 2016/4/21.
 */
public class Message {
    public long ID = 0;
    public int MessageType = MessageTypes.TextMessage.getValue();
    public String Content;
    public int IsGroup = 0;
    public long UserId = 0;
    public long TargetId = 0;
    public HashMap<String,Object> Args;
    public long Created;

    @Override
    public String toString() {
        return ID + ":" + MessageType + ":" + Content + ":" + IsGroup
                + ":" + UserId + ":" + TargetId + ":" + Args + ":" + Created;
    }
}
