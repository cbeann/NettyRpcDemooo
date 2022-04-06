package LengthFieldBasedFrameDecoderDemo.common.order;

import LengthFieldBasedFrameDecoderDemo.common.Operation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class OrderOperation extends Operation {

  private int tableId;
  private String dish;

  public OrderOperation(int tableId, String dish) {
    this.tableId = tableId;
    this.dish = dish;
  }

  @Override
  public OrderOperationResult execute() {
    System.out.println("order's executing startup with orderRequest: " + toString());
    // execute order logic
    System.out.println("order's executing complete");
    OrderOperationResult orderResponse = new OrderOperationResult(tableId, dish, true);
    return orderResponse;
  }
}
