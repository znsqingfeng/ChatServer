package chat.server;

import chat.model.Message;
import org.apache.mina.core.session.IoSession;

/**
 * Created by zhanggd on 2016/4/28.
 */
public class WaitClientResponse {
	public Message message;
	public IoSession ioSession;
	public WaitClientResponseCallBack waitClientResponseCallBack;
	
	public WaitClientResponse(IoSession ioSession, Message message) {
		this.ioSession = ioSession;
		this.message = message;
		this.waitClientResponseCallBack = null;
	}
	
	public WaitClientResponse(IoSession ioSession, Message message, WaitClientResponseCallBack waitClientResponseCallBack) {
		this.ioSession = ioSession;
		this.message = message;
		this.waitClientResponseCallBack = waitClientResponseCallBack;
	}
}
