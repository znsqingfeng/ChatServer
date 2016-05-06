package chat.enums;

/**
 * Created by zhanggd on 2016/4/29.
 */
public enum MessageTypes {

    TextMessage("文本消息", 0),
    ImageMessage("图片消息", 1),
    AudioMessage("语音消息",2),
    HeartBeat("心跳检测",3);

    public String Name;
    public int Value;

    private MessageTypes(String name, int value) {
        this.Name = name;
        this.Value = value;
    }

    public static MessageTypes valueOf(int index) {
        for (MessageTypes c : MessageTypes.values()) {
            if (c.getValue() == index) {
                return c;
            }
        }
        return TextMessage;
    }

    public int getValue() {
        return Value;
    }
}
