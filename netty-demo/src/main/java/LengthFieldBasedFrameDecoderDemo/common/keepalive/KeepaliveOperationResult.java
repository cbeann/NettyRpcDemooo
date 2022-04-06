package LengthFieldBasedFrameDecoderDemo.common.keepalive;

import LengthFieldBasedFrameDecoderDemo.common.OperationResult;
import lombok.Data;

@Data
public class KeepaliveOperationResult extends OperationResult {

    private final long time;

}
