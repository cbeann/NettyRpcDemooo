package helloworld;



import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author CBeann
 * @create 2019-10-16 21:23
 */
public class MyClientHandler extends SimpleChannelInboundHandler<String> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {


        //收到服务端发送的消息
        String body = s;
        System.out.println(body);


    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        System.out.println("[客户端出现异常");
        ctx.close();
    }
}

