package chat.dao.interfaces;

import chat.model.ClientUser;

/**
 * Created by zhanggd on 2016/4/28.
 */
public interface UserService {
    ClientUser updateProfile(ClientUser clientUser);
    ClientUser getUserById(long userId);
}
