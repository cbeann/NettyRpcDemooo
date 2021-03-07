package com.rpc.model;

import com.rpc.constant.ConstantPool;
import lombok.Data;

import java.io.Serializable;

/**
 * @author chaird
 * @create 2021-02-06 23:47
 */
@Data
public class RpcRequest implements Serializable {

  private String requestId;
  /** 请求的服务名 */
  private String serviceName;
  /** 请求的class类 */
  private String clazzName;
  /** 请求调用的方法 */
  private String methodName;

  private Class<?>[] parameterTypes;
  private String[] parameterTypeStrings;

  private Object[] parameters;

  /**
   * 心跳
   *
   * @return
   */
  public static RpcRequest heartBeatRequest() {
    RpcRequest rpcRequest = new RpcRequest();
    rpcRequest.setRequestId(ConstantPool.HEART_BEAT);
    return rpcRequest;
  }
}
