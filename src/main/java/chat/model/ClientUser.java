package chat.model;

import org.apache.mina.core.session.IoSession;

/**
 * Created by zhanggd on 2016/4/28.
 */
public class ClientUser {
	public IoSession ioSession;
	public boolean onLine = true;
    public boolean isLogin = false;
    public long ID = 0;
    public String NickName;
    public String Avatar;
    public String Introduction;
    public long GroupID = 0;
    public String TerminalInfo;
    public long Updated;
    public long Created = 0;
	
	public ClientUser(IoSession ioSession){
		this.ioSession = ioSession;
	}
}
