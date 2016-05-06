package chat.dao.interfaces.impl;

import chat.dao.interfaces.UserService;
import chat.model.ClientUser;
import chat.model.Message;
import chat.utils.Sql2oUtil;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;

/**
 * Created by zhanggd on 2016/4/28.
 */
public class UserServiceImpl implements UserService{
    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Override
    public ClientUser updateProfile(ClientUser clientUser) {
        ClientUser user = null;
        Connection connection = Sql2oUtil.getConnection();
        String sql = "update im_user set Avatar = :Avatar,NickName = :NickName where ID = :ID;";
        try {
            user = connection.createQuery(sql)
                    .addParameter("Avatar", clientUser.Avatar)
                    .addParameter("NickName", clientUser.NickName)
                    .addParameter("ID", clientUser.ID)
                    .executeAndFetchFirst(ClientUser.class);
            return user;
        }
        catch (Exception e){
            logger.error("update profile error,id:" + clientUser.ID + ",nickname:" + clientUser.NickName + ",avatar:" + clientUser.Avatar,e);
        }
        return null;
    }

    @Override
    public ClientUser getUserById(long userId) {
        ClientUser user = null;
        Connection connection = Sql2oUtil.getConnection();
        String sql = "select * from im_user where ID = :ID;";
        try {
            user = connection.createQuery(sql)
                    .addParameter("ID", userId)
                    .executeAndFetchFirst(ClientUser.class);
            return user;
        }
        catch (Exception e){
            logger.error("get user error,id:" + userId,e);
        }
        return null;
    }
}
