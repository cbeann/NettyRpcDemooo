package mychat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import java.util.Scanner;

/**
 * @author CBeann
 * @create 2019-10-16 21:23
 */
public class MyChatClient {

  public static void main(String[] args) throws Exception {
    int port = 8888;
    String host = "127.0.0.1";
    // 配置客户端NIO线程组
    EventLoopGroup group = new NioEventLoopGroup();
    try {
      Bootstrap b = new Bootstrap();
      b.group(group)
          .channel(NioSocketChannel.class)
          .handler(
              new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {

                  socketChannel.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));
                  socketChannel.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
                  // TimeClientHandler是自己定义的方法
                  socketChannel.pipeline().addLast(new MyChatClientHandler());
                }
              });
      // 发起异步连接操作
      ChannelFuture f = b.connect(host, port).sync();

      //            //发送数据
      Scanner reader = new Scanner(System.in);
      String body = reader.nextLine();
      while (!"exit".equals(body)) {
        f.channel().writeAndFlush(body);
        body = reader.nextLine();
      }

      // 等待客户端链路关闭
      f.channel().closeFuture().sync();

    } catch (Exception e) {

    } finally {
      // 优雅关闭
      group.shutdownGracefully();
    }
  }
}
