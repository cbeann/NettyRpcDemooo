package com.rpc.client;

import cn.hutool.json.JSONUtil;
import com.rpc.constant.FuturePool;
import com.rpc.model.RpcRequest;
import com.rpc.model.RpcResponse;
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
import lombok.Data;
import org.springframework.context.ApplicationContext;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author chaird
 * @create 2021-02-07 9:46
 */
@Data
public class NettyClient {

  public NettyClient(String ip, Integer port) {
    this.ip = ip;
    this.port = port;
  }

  private ApplicationContext applicationContext;

  private String ip;
  private Integer port;

  EventLoopGroup group = new NioEventLoopGroup();
  Bootstrap b = new Bootstrap();

  ChannelFuture f = null;

  public ChannelFuture getChannelFuture() {
    return f;
  }

  public void start() {
    new Thread(
            new Runnable() {
              @Override
              public void run() {
                bind(ip, port);
              }
            },
            "name")
        .start();
  }

  public void bind(String url, Integer port) {
    // url = "127.0.0.1";
    // port = 8888;
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
                  socketChannel.pipeline().addLast(new RpcClientHandler());
                }
              });
      // 发起异步连接操作
      System.out.println("客户端    Netty客户端启动成功：" + ip + ":" + port);
      f = b.connect(ip, port).sync();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public RpcResponse sendMessage(RpcRequest msg) {
    // 存起来
    RpcFuture<RpcResponse> future = new RpcFuture<>();
    FuturePool.put(msg.getRequestId(), future);

    RpcResponse rpcResponse = null;
    try {
      String s = JSONUtil.toJsonStr(msg);
      f.channel().writeAndFlush(s);

      rpcResponse = future.get(2000, TimeUnit.MILLISECONDS);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      FuturePool.remove(msg.getRequestId());
    }

    return rpcResponse;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NettyClient that = (NettyClient) o;
    return Objects.equals(ip, that.ip) && Objects.equals(port, that.port);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ip, port);
  }
}
