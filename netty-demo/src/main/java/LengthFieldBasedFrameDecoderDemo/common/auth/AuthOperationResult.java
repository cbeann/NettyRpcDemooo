package LengthFieldBasedFrameDecoderDemo.common.auth;

import LengthFieldBasedFrameDecoderDemo.common.OperationResult;
import lombok.Data;

@Data
public class AuthOperationResult extends OperationResult {

    private final boolean passAuth;

}
