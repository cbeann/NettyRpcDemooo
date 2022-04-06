package LengthFieldBasedFrameDecoderDemo.server.handler;

import LengthFieldBasedFrameDecoderDemo.common.Operation;
import LengthFieldBasedFrameDecoderDemo.common.OperationResult;
import LengthFieldBasedFrameDecoderDemo.common.RequestMessage;
import LengthFieldBasedFrameDecoderDemo.common.ResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderServerProcessHandler extends SimpleChannelInboundHandler<RequestMessage> {
  @Override
  protected void channelRead0(ChannelHandlerContext ctx, RequestMessage requestMessage)
      throws Exception {
    Operation operation = requestMessage.getMessageBody();
    OperationResult operationResult = operation.execute();

    ResponseMessage responseMessage = new ResponseMessage();
    responseMessage.setMessageHeader(requestMessage.getMessageHeader());
    responseMessage.setMessageBody(operationResult);

    ctx.writeAndFlush(responseMessage);
  }
}
