package com.rpc.server;

import cn.hutool.json.JSONUtil;
import com.rpc.model.RpcRequest;
import com.rpc.model.RpcResponse;
import com.rpc.utils.String2Class;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;

/**
 * @author chaird
 * @create 2021-02-07 2:59
 */
public class RcpServerHandler extends SimpleChannelInboundHandler<String> {

  private ApplicationContext applicationContext;

  public RcpServerHandler(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

    System.out.println(msg);

    //msg =
        //"{\"methodName\":\"getId\",\"serviceName\":\"provider01\",\"requestId\":\"d53815a0\",\"parameters\":[1],\"clazzName\":\"com.service.StudentService\",\"parameterTypeStrings\":[\"java.lang.Integer\"]}";

    RpcRequest requestBean = JSONUtil.toBean(msg, RpcRequest.class);
    String[] parameterTypeStrings = requestBean.getParameterTypeStrings();
    Class<?>[] parameterTypes = String2Class.string2Class(parameterTypeStrings);
    requestBean.setParameterTypes(parameterTypes);

    String clazzName = requestBean.getClazzName();
    Class<?> aClass = Class.forName(clazzName);
    Object bean = applicationContext.getBean(aClass);
    Method method = aClass.getMethod(requestBean.getMethodName(), requestBean.getParameterTypes());

    Object invoke = method.invoke(bean, requestBean.getParameters());

    RpcResponse rpcResponse = new RpcResponse();
    rpcResponse.setRequestId(requestBean.getRequestId());
    rpcResponse.setReturnValue(invoke);

    String s = JSONUtil.toJsonStr(rpcResponse);


    ctx.channel().writeAndFlush(s);





    // 获取请求的接口类名称
    //    String clazzName = requestBean.getClazzName();
    //    Class<?> aClass = Class.forName(clazzName);
    //
    //    Object bean = applicationContext.getBean(aClass);
    //
    //    // 获取方法
    //    Method method = aClass.getMethod(requestBean.getMethodName(),
    // requestBean.getParameterTypes());
    //
    //    // 执行并且返回结果
    //    Object result = method.invoke(bean, requestBean.getParameters());
    //
    //    RpcResponse rpcResponse = new RpcResponse();
    //
    //    rpcResponse.setRequestId(requestBean.getRequestId());
    //    rpcResponse.setReturnValue(result);
    //
    //    String response = JSONUtil.toJsonStr(rpcResponse);
    //
    //    ctx.writeAndFlush(response);
  }

  @Override
  public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
    System.out.println("active");
    super.channelRegistered(ctx);
  }
}
