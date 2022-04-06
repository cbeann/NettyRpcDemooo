package LengthFieldBasedFrameDecoderDemo.client;

import LengthFieldBasedFrameDecoderDemo.client.codec.OrderFrameDecoder;
import LengthFieldBasedFrameDecoderDemo.client.codec.OrderFrameEncoder;
import LengthFieldBasedFrameDecoderDemo.client.codec.OrderProtocolDecoder;
import LengthFieldBasedFrameDecoderDemo.client.codec.OrderProtocolEncoder;
import LengthFieldBasedFrameDecoderDemo.client.dispatcher.OperationResultFuture;
import LengthFieldBasedFrameDecoderDemo.client.dispatcher.RequestPendingCenter;
import LengthFieldBasedFrameDecoderDemo.client.dispatcher.ResponseDispatcherHandler;
import LengthFieldBasedFrameDecoderDemo.common.OperationResult;
import LengthFieldBasedFrameDecoderDemo.common.RequestMessage;
import LengthFieldBasedFrameDecoderDemo.common.order.OrderOperation;
import LengthFieldBasedFrameDecoderDemo.util.IdUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author CBeann
 * @create 2019-10-16 21:23
 */
public class MyChatClient {

  public static void main(String[] args) throws Exception {
    int port = 8888;
    String host = "127.0.0.1";

    RequestPendingCenter requestPendingCenter = new RequestPendingCenter();
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

                  ChannelPipeline pipeline = socketChannel.pipeline();
                  pipeline.addLast(new OrderFrameDecoder());
                  pipeline.addLast(new OrderFrameEncoder());
                  pipeline.addLast(new OrderProtocolEncoder());
                  pipeline.addLast(new OrderProtocolDecoder());
                  // TimeClientHandler是自己定义的方法
                  pipeline.addLast(new ResponseDispatcherHandler(requestPendingCenter));

                }
              });
      // 发起异步连接操作
      ChannelFuture channelFuture = b.connect(host, port).sync();

      long streamId = IdUtil.nextId();

      RequestMessage requestMessage = new RequestMessage(
              streamId, new OrderOperation(1001, "tudou"));

      OperationResultFuture operationResultFuture = new OperationResultFuture();


      requestPendingCenter.add(streamId, operationResultFuture);

      channelFuture.channel().writeAndFlush(requestMessage);

      OperationResult operationResult = operationResultFuture.get();

      System.out.println(operationResult);


      // 等待客户端链路关闭
      channelFuture.channel().closeFuture().sync();

    } catch (Exception e) {

    } finally {
      // 优雅关闭
      group.shutdownGracefully();
    }
  }
}
