package LengthFieldBasedFrameDecoderDemo.server.codec;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author chaird
 * @create 2022-04-06 17:46
 */
public class OrderFrameDecoder extends LengthFieldBasedFrameDecoder {
    public OrderFrameDecoder() {
        super(10240, 0, 2, 0, 2);
    }
}