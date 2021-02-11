# NettyRpcDemooo

利用netty实现的rpc框架：Netty+自定义starter+zk+自定义注解+Bean的生命周期

#使用方法

1.下载代码到本地
```bash
git clone https://github.com/cbeann/NettyRpcDemooo.git
```
2. 打包到本地(建议导入IDEA在打包)
```bash
mvn  clean install -DskipTests=true
```
3. 添加maven依赖到你的项目中
 ```xml
 <dependency>
             <groupId>com.demo</groupId>
             <artifactId>rpc-spring-boot-starter</artifactId>
             <version>1.0-SNAPSHOT</version>
         </dependency>
```

#服务端

###添加依赖
 ```xml
 <dependency>
             <groupId>com.demo</groupId>
             <artifactId>rpc-spring-boot-starter</artifactId>
             <version>1.0-SNAPSHOT</version>
         </dependency>
```
###添加配置信息
 ```properties
###服务注册中心地址
#zk的IP
rpc.register-address=127.0.0.1
#zk的端口
rpc.server-port=2181

###服务提供者信息
#服务提供者名称
rpc.server.provider-name=provider01
#服务提供者Netty端口
rpc.server.provider-port=18083
```


#客户端

###添加依赖
 ```xml
 <dependency>
             <groupId>com.demo</groupId>
             <artifactId>rpc-spring-boot-starter</artifactId>
             <version>1.0-SNAPSHOT</version>
         </dependency>
```
###添加配置信息

 ```properties
###服务注册中心地址
#zk的IP
rpc.register-address=127.0.0.1
#zk的端口
rpc.server-port=2181

###服务消费者信息
#服务消费者名称
rpc.client.consumer-name=consumer01
```

###调用服务
```java
@RestController
public class HelloController {

  @RpcService("provider01") // 自定义注解，其中value为服务提供者名称，类似OpenFeign的使用
  private StudentService studentService;


  @GetMapping("/index/{id}")
  public Object hello(@PathVariable Integer id) {
      //服务器远程调用
    String res = studentService.getId(id);

    return res;
  }
}
```

#案例
url:https://github.com/cbeann/NettyRpcDemooo

| 模块                        | 模块内容      | 备注 |
| ----------------------------- | ----------------- | ---- |
| rpc-spring-boot-autoconfigure | 自定义rpc-starter | 源码 |
| rpc-spring-boot-starter       | 自定义rpc-starter | 源码 |
| rpc-example-api               | 案例api         | 案例 |
| rpc-example-server            | 案例服务者   | 案例 |
| rpc-example-client            | 案例消费者   | 案例 |



#参考

https://github.com/2YSP/rpc-spring-boot-starter#rpc-spring-boot-starter