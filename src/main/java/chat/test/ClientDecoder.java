package chat.test;

import chat.model.Message;
import com.alibaba.fastjson.JSON;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * Created by zhanggd on 2016/5/4.
 */
public class ClientDecoder extends CumulativeProtocolDecoder {
    private final CharsetDecoder cd = Charset.forName("UTF-8").newDecoder();
    @Override
    protected boolean doDecode(IoSession ioSession, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        Message message = null;

        try {
            message =  JSON.parseObject(in.getString(cd), Message.class);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        out.write(message);
        return false;
    }
}
