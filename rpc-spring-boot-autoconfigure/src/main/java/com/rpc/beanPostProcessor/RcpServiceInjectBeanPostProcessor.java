package com.rpc.beanPostProcessor;

import com.rpc.annotation.RpcService;
import com.rpc.client.NettyClientBootStarp;
import com.rpc.proxy.RpcFactoryProxy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * @author chaird
 * @create 2021-02-07 17:03
 * 该BeanPostProcessor用于注入远程服务接口代理类
 */
public class RcpServiceInjectBeanPostProcessor
    implements InstantiationAwareBeanPostProcessor, ApplicationContextAware {

  private ApplicationContext context;

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName)
      throws BeansException {

    // 判断类里是否有@RpcService注解

    Class<?> clazz = context.getType(beanName);
    if (Objects.isNull(clazz)) {
      return bean;
    }
    Field[] declaredFields = clazz.getDeclaredFields();
    for (Field field : declaredFields) {
      // 找出标记了InjectService注解的属性
      RpcService injectService = field.getAnnotation(RpcService.class);
      if (injectService == null) {
        continue;
      }

      // 获取服务名称
      String providerName = injectService.value();
      // 获取接口Class
      Class<?> fieldClass = field.getType();
      // 获取nettyClient
      NettyClientBootStarp nettyClientBootStarp = context.getBean(NettyClientBootStarp.class);

      RpcFactoryProxy rpcFactoryProxy =
          new RpcFactoryProxy(fieldClass, providerName, nettyClientBootStarp);
      Object proxy = rpcFactoryProxy.getProxy();

      Object object = bean;
      field.setAccessible(true);
      try {
        // 请开始你的表演
        field.set(object, proxy);
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }

    return bean;
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    return bean;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.context = applicationContext;
  }

  public RcpServiceInjectBeanPostProcessor() {
    System.out.println("-----RcpServiceInjectBeanPostProcessor-----------");
  }
}
