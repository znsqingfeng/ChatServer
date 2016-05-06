package chat.filter;

import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

/**
 * Created by zhanggd on 2016/5/4.
 */
public class ServerProtocolCodecFactory extends DemuxingProtocolCodecFactory {
    public ServerProtocolCodecFactory() {
        super.addMessageEncoder(Object.class, CommonEncoder.class);
        super.addMessageDecoder(TcpMessageDecoder.class);
        super.addMessageDecoder(HttpMessageDecoder.class);
    }
}
