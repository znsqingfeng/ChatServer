package chat.model;

/**
 * Created by zhanggd on 2016/4/21.
 */
public class Relation {
    long ID = 0;
    //0 朋友 1 群
    int RelationType = 0;
    long UserID;
    long TargetId;
    long Created;
    long Updated;
}
