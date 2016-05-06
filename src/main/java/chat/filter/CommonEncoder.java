package chat.filter;

import chat.model.Message;
import com.alibaba.fastjson.JSON;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

/**
 * Created by zhanggd on 2016/4/22.
 */
public class CommonEncoder implements MessageEncoder {

    private final CharsetEncoder ce = Charset.forName("UTF-8").newEncoder();

    @Override
    public void encode(IoSession ioSession, Object message, ProtocolEncoderOutput out) throws Exception {
        IoBuffer buf = IoBuffer.allocate(1024).setAutoExpand(true);

        String msg = null;
        //tcp Message类型 转成String
        if (message instanceof Message)
            msg = JSON.toJSONString(message);
        //http 不做处理
        else if (message instanceof String)
            msg = (String)message;

        buf.putString(msg, ce);

        buf.flip();

        out.write(buf);
    }
}
