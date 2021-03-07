package helloworld;


import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @author CBeann
 * @create 2019-10-16 15:55
 */
public class MyServerHandler extends SimpleChannelInboundHandler<String> {

  // 用一个ChannelGroup保存所有连接到服务器的客户端通道
  private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s)
      throws Exception {

    Channel channel = channelHandlerContext.channel();

    // 服务器收到消息
    // "[服务端]   " + channel.remoteAddress() + "通道关闭";
    String body = s;

    // 群发
    channelGroup.forEach(
        (x) -> {
          if (x != channel) {
            x.writeAndFlush(channel.remoteAddress() + "说===>" + s);
          } else {
            x.writeAndFlush("自己说===>" + s);
          }
        });
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    Channel channel = ctx.channel();
    String notice = "[服务端]   " + channel.remoteAddress() + "通道激活";
    System.out.println(notice);
    channelGroup.writeAndFlush(notice);
    // 添加建立连接的channel
    channelGroup.add(channel);
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    Channel channel = ctx.channel();
    // 删除失效的channel
    channelGroup.remove(channel);
    String notice = "[服务端]   " + channel.remoteAddress() + "通道关闭";
    channelGroup.writeAndFlush(notice);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    Channel channel = ctx.channel();
    System.out.println("[服务端]   " + channel.remoteAddress() + "出现异常");
    ctx.close();
  }
}
