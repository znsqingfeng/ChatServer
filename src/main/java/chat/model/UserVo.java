package chat.model;

/**
 * Created by zhanggd on 2016/5/5.
 */
public class UserVo {
    public long id = 0;
    public String nick_name;
    public String avatar;
    public String introduction;
    public long group_id = 0;
    public long updated;
    public long created = 0;

    public static UserVo fromClientUser(ClientUser user){
        UserVo vo = new UserVo();
        vo.id = user.ID;
        vo.nick_name = user.NickName;
        vo.avatar = user.Avatar;
        vo.introduction = user.Introduction;
        vo.group_id = user.GroupID;
        vo.updated = user.Updated;
        vo.created = user.Created;
        return vo;
    }
}
