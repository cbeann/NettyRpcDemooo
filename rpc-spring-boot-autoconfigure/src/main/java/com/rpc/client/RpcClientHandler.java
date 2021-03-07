package com.rpc.client;

import cn.hutool.json.JSONUtil;
import com.rpc.constant.ConstantPool;
import com.rpc.constant.FuturePool;
import com.rpc.model.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author chaird
 * @create 2021-02-07 10:20
 */
public class RpcClientHandler extends SimpleChannelInboundHandler<String> {
  @Override
  protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

    String resp = msg;
    System.out.println("resp:" + resp);

    RpcResponse rpcResponse = JSONUtil.toBean(resp, RpcResponse.class);

    if (ConstantPool.HEART_BEAT.equals(rpcResponse.getRequestId())) {
      System.out.println("客户端接收到心跳");
      return;
    }

    RpcFuture future = FuturePool.get(rpcResponse.getRequestId());

    future.setResponse(rpcResponse);
  }
}
