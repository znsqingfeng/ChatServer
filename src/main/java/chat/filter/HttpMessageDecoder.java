package chat.filter;

import chat.exception.ServerException;
import chat.server.HttpRequestMessage;
import chat.utils.HttpUtil;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhanggd on 2016/5/4.
 */
public class HttpMessageDecoder implements MessageDecoder {
    private static CharsetDecoder cd = Charset.forName("UTF-8").newDecoder();
    @Override
    public MessageDecoderResult decodable(IoSession session, IoBuffer in) {
        if(in.remaining() < 5){
            return MessageDecoderResult.NEED_DATA;
        }
        if(session.getLocalAddress().toString().contains(":9002")){
            return MessageDecoderResult.OK ;
        }else{
            return MessageDecoderResult.NOT_OK;
        }
    }

    @Override
    public MessageDecoderResult decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        String fullMessage = in.getString(cd);
        //根据请求字符串构造请求
        HttpRequestMessage request = getRequest(fullMessage);

        out.write(request);
        return MessageDecoderResult.OK;
    }

    /**
     * 通过message构造http请求
     * @param message
     * @return
     * @throws ServerException
     */
    public static HttpRequestMessage getRequest(String message) throws ServerException {

        if (!isComplete(message))
            throw new ServerException();

        HttpRequestMessage request = new HttpRequestMessage();

        if(message.startsWith("GET")){
            request.setMethod(HttpRequestMessage.GET);

            String url = null;
            if (message.contains("?")) {
                url = message.substring(message.indexOf("GET") + 5, message.indexOf("?"));
                String urlParam = message.substring(message.indexOf("?")+1, message.indexOf("HTTP/1.1")-1);
                if (!"".equals(urlParam)){
                    String[] params = urlParam.split("&");
                    Map<String,String> param = new HashMap<String,String>();
                    for (String str:params)
                        param.put(str.substring(0,str.indexOf('=')),str.substring(str.indexOf('=')+1,str.length()));
                    request.setParam(param);
                }
            }
            else
                url = message.substring(message.indexOf("GET") + 4, message.indexOf("HTTP/1.1")-1);

            request.setUrl(url);

            request.setHeaders(getHeaders(message));

        }else if(message.startsWith("POST")){
            request.setMethod(HttpRequestMessage.POST);

            request.setUrl(message.substring(message.indexOf("POST") + 5, message.indexOf("HTTP/1.1") - 1));

            request.setHeaders(getHeaders(message));

            request.setBody(message.split("\r\n\r\n")[1]);
        }

        return request;
    }
    public static Map<String,String> getHeaders(String message){
        String[] headers = message.substring(message.indexOf("\r\n")+2,message.indexOf("\r\n\r\n")).split("\r\n");
        Map<String,String> header = new HashMap<String,String>();
        for (String h:headers){
            String key = h.substring(0,h.indexOf(":"));
            String value = h.substring(h.indexOf(":")+2,h.length());
            header.put(key,value);
        }
        return header;
    }
    @Override
    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }

    private static boolean isComplete(String message){
        if(message.startsWith("GET")){
            return message.endsWith("\r\n\r\n");
        }
        if(message.startsWith("POST")){
            if(message.contains("Content-Length:")){
                String msgLenFlag = message.substring(message.indexOf("Content-Length:") + 15);
                if(msgLenFlag.contains("\r\n")){
                    int contentLength = Integer.parseInt(msgLenFlag.substring(0, msgLenFlag.indexOf("\r\n")).trim());
                    if(contentLength == 0){
                        return true;
                    }else if(contentLength > 0){
                        String messageBody = message.split("\r\n\r\n")[1];
                        if(contentLength == HttpUtil.getBytes(messageBody, "UTF-8").length){
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

}
