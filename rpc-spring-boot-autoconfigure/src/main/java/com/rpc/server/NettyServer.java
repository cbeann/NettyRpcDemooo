package com.rpc.server;

import com.rpc.properties.RpcProperties;
import com.rpc.properties.RpcServerProperties;
import com.rpc.zk.ZKServer;
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
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;
import java.net.InetAddress;

/**
 * @author chaird
 * @create 2021-02-07 2:32
 */
public class NettyServer implements ApplicationContextAware {

  private Integer port;

  private ApplicationContext applicationContext;

  EventLoopGroup bossGroup = new NioEventLoopGroup();
  EventLoopGroup workerGroup = new NioEventLoopGroup();

  ServerBootstrap b = new ServerBootstrap();

  public NettyServer(int port) {
    this.port = port;
  }

  /** * 开启NettyServer的方法 */
  public void bind() {
    try {
      b.group(bossGroup, workerGroup)
          .channel(NioServerSocketChannel.class)
          .option(ChannelOption.SO_BACKLOG, 1024)
          .childHandler(
              new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                  // TimeClientHandler是自己定义的方法
                  socketChannel.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));
                  socketChannel.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
                  socketChannel.pipeline().addLast(new RcpServerHandler(applicationContext));
                }
              });
      System.out.println("服务端    Netty服务器启动成功:" + port);
      // 绑定端口
      ChannelFuture f = b.bind(port).sync();
      // 等待服务端监听端口关闭
      f.channel().closeFuture().sync();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      // 优雅关闭，释放线程池资源
      bossGroup.shutdownGracefully();
      workerGroup.shutdownGracefully();
    }
  }

  /** 启动Netty服务器 */
  @PostConstruct
  public void postConstruct() {
    // 向外暴露端口
    doExport();
    // 异步开启netty服务器
    new Thread(
            () -> {
              this.bind();
            })
        .start();
  }

  /** 服务暴露(其实就是把服务信息保存到Zookeeper上) */
  private void doExport() {
    ZKServer zkServer = applicationContext.getBean(ZKServer.class);
    RpcServerProperties rpcServerProperties = applicationContext.getBean(RpcServerProperties.class);
    RpcProperties rpcProperties = applicationContext.getBean(RpcProperties.class);

    //   providerGroupDir = /rpc/provider/myProviderName
    String providerGroupDir = rpcProperties.getPath() + rpcProperties.getProviderPath();
    providerGroupDir = providerGroupDir + "/" + rpcServerProperties.getProviderName();

    try {
      // 创建服务名目录（用于集群）
      zkServer.createPathPermanent(providerGroupDir, "");
    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      String providerAddress = InetAddress.getLocalHost().getHostAddress();
      String providerInstance = providerAddress + ":" + rpcServerProperties.getProviderPort();
      // key(path) = /rpc/provider/myProviderName/127.0.0.1:8080   value:127.0.0.1:8080
      zkServer.createPathTemp(providerGroupDir + "/" + providerInstance, providerInstance);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
