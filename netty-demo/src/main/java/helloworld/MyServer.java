package helloworld;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

/**
 * @author CBeann
 * @create 2019-10-16 15:51
 */
public class MyServer {

  public static void main(String[] args) {

    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();

    try {

      ServerBootstrap b = new ServerBootstrap();
      b.group(bossGroup, workerGroup)
          .channel(NioServerSocketChannel.class)
          .option(ChannelOption.SO_BACKLOG, 1024)
          // 保持连接
          .childOption(ChannelOption.SO_KEEPALIVE, true)
          .childHandler(
              new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                  // TimeClientHandler是自己定义的方法
                  socketChannel.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));
                  socketChannel.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
                  socketChannel.pipeline().addLast(new MyServerHandler());
                }
              });
      // 绑定端口
      ChannelFuture f = b.bind(8888).sync();
      // 等待服务端监听端口关闭
      f.channel().closeFuture().sync();

    } catch (Exception e) {

    } finally {
      // 优雅关闭，释放线程池资源
      bossGroup.shutdownGracefully();
      workerGroup.shutdownGracefully();
    }
  }
}
