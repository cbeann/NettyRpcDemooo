package com.rpc.proxy;

/**
 * @author CBeann
 * @create 2020-03-09 21:21
 */
import com.rpc.client.NettyClient;
import com.rpc.client.NettyClientBootStarp;
import com.rpc.client.NettyClientGroup;
import com.rpc.model.RpcRequest;
import com.rpc.model.RpcResponse;
import com.rpc.utils.Class2String;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.UUID;

/**
 * @author CBeann
 * @create 2020-03-09 17:37
 */
public class RpcFactoryProxy<T> implements InvocationHandler {

  private Class<T> proxyInterface;
  // 这里可以维护一个缓存，存这个接口的方法抽象的对象

  private NettyClientBootStarp nettyClientBootStarp;

  private String serviceName;

  public RpcFactoryProxy(
      Class<T> proxyInterface, String serviceName, NettyClientBootStarp nettyClient) {
    this.serviceName = serviceName;
    this.proxyInterface = proxyInterface;
    this.nettyClientBootStarp = nettyClient;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

    System.out.println("invoke");

    Map<String, NettyClientGroup> providers = nettyClientBootStarp.getProviders();

    NettyClientGroup nettyClientGroup = providers.get(serviceName);

    if (null == nettyClientGroup) {
      RpcResponse response = RpcResponse.NO_SERVICE();
      return response.getReturnValue();
    }

    NettyClient nettyClient = nettyClientGroup.next();

    if (null == nettyClient) {
      RpcResponse response = RpcResponse.NO_SERVICE();
      return response.getReturnValue();
    }

    RpcRequest rpcRequest = new RpcRequest();
    rpcRequest.setRequestId(UUID.randomUUID().toString().substring(0, 8));
    // 设置服务名称
    rpcRequest.setServiceName(serviceName);
    // 设置是哪个类
    rpcRequest.setClazzName(proxyInterface.getName());
    // 设置哪个方法
    rpcRequest.setMethodName(method.getName());
    // 设置参数类型
    Class<?>[] parameterTypes = method.getParameterTypes();
    String[] parameterTypeString = Class2String.class2String(parameterTypes);
    rpcRequest.setParameterTypeStrings(parameterTypeString);

    // 设置参数
    rpcRequest.setParameters(args);

    // 发送消息
    // Future future = nettyClient.sendMessage(rpcRequest);
    RpcResponse response = nettyClient.sendMessage(rpcRequest);

    if (response == null) {
      throw new Exception("the response is null");
    }

    return response.getReturnValue();
  }

  public T getProxy() {
    return (T)
        Proxy.newProxyInstance(proxyInterface.getClassLoader(), new Class[] {proxyInterface}, this);
  }
}
