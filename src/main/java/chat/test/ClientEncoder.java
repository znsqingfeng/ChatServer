package chat.test;

import com.alibaba.fastjson.JSON;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

/**
 * Created by zhanggd on 2016/5/4.
 */
public class ClientEncoder extends ProtocolEncoderAdapter {
    private final CharsetEncoder ce = Charset.forName("UTF-8").newEncoder();
    @Override
    public void encode(IoSession ioSession, Object message, ProtocolEncoderOutput out) throws Exception {
        IoBuffer buf = IoBuffer.allocate(1024).setAutoExpand(true);

        String msg = JSON.toJSONString(message);

        buf.putString(msg, ce);

        buf.flip();

        out.write(buf);
    }
}
