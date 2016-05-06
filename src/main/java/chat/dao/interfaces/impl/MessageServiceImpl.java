package chat.dao.interfaces.impl;

import chat.dao.interfaces.MessageService;
import chat.model.Message;
import chat.utils.Sql2oUtil;
import com.alibaba.fastjson.JSON;
import org.sql2o.Connection;

/**
 * Created by zhanggd on 2016/4/28.
 */
public class MessageServiceImpl implements MessageService{
    public int insert(Message message) {
        String args = "{}";
        if (message.Args != null){
            args = JSON.toJSONString(message.Args);
        }
        Connection connection = Sql2oUtil.getConnection();
        String sql = "insert into im_message(ID,MessageType,Content,IsGroup,UserId,TargetId,Args,Created) " +
                "values(:ID,:MessageType,:Content,:IsGroup,:UserId,:TargetId,:Args,:Created)";
        try {
            connection.createQuery(sql)
                    .addParameter("ID", message.ID)
                    .addParameter("MessageType",message.MessageType)
                    .addParameter("Content",message.Content)
                    .addParameter("IsGroup",message.IsGroup)
                    .addParameter("UserId",message.UserId)
                    .addParameter("TargetId",message.TargetId)
                    .addParameter("Args",args)
                    .addParameter("Created",message.Created)
                    .executeUpdate();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }
}
