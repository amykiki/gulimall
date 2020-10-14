package daily.boot.gulimall.order.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 退款信息
 * 
 * @author amy
 * @date 2020-10-14 16:57:10
 */
@Data
@TableName("oms_refund_info")
@ApiModel(value = "退款信息类")
public class RefundInfoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
    @ApiModelProperty(value = "id")
    private Long id;
    /**
     * 退款的订单
     */
    @ApiModelProperty(value = "退款的订单")
    private Long orderReturnId;
    /**
     * 退款金额
     */
    @ApiModelProperty(value = "退款金额")
    private BigDecimal refund;
    /**
     * 退款交易流水号
     */
    @ApiModelProperty(value = "退款交易流水号")
    private String refundSn;
    /**
     * 退款状态
     */
    @ApiModelProperty(value = "退款状态")
    private Integer refundStatus;
    /**
     * 退款渠道[1-支付宝，2-微信，3-银联，4-汇款]
     */
    @ApiModelProperty(value = "退款渠道[1-支付宝，2-微信，3-银联，4-汇款]")
    private Integer refundChannel;
    /**
     * 
     */
    @ApiModelProperty(value = "")
    private String refundContent;

}
