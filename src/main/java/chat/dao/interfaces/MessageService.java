package chat.dao.interfaces;

import chat.model.Message;

/**
 * Created by zhanggd on 2016/4/28.
 */
public interface MessageService {
    int insert(Message message);
}
