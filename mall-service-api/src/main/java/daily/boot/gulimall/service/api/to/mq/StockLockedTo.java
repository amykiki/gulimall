package daily.boot.gulimall.service.api.to.mq;

import lombok.Data;

@Data
public class StockLockedTo {
    /**
     * 库存工作单id
     */
    private Long wareOrderTaskId;
    /**
     * 库存工作单详情id
     */
    private Long wareOrderTaskDetailId;
}
