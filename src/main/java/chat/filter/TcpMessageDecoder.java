package chat.filter;

import chat.model.Message;
import com.alibaba.fastjson.JSON;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * Created by zhanggd on 2016/4/22.
 */
public class TcpMessageDecoder implements MessageDecoder {
    private static Logger logger = LoggerFactory.getLogger(TcpMessageDecoder.class);
    private final CharsetDecoder cd = Charset.forName("UTF-8").newDecoder();

    @Override
    public MessageDecoderResult decodable(IoSession session, IoBuffer in) {
        //服务端启动时已绑定9901端口,专门用来处理TCP请求的
        if(session.getLocalAddress().toString().contains(":9001")){
                return MessageDecoderResult.OK;
        }else{
            return MessageDecoderResult.NOT_OK;
        }
    }

    @Override
    public MessageDecoderResult decode(IoSession ioSession, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        Message message = null;
        String msg = in.getString(cd);
        try {
            message =  JSON.parseObject(msg,Message.class);
        }
        catch (Exception e){
            logger.error("erro message format",in.getString(cd));
            throw e;
        }

        out.write(message);
        return MessageDecoderResult.OK;
    }

    @Override
    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {
        //no operate
    }
}
