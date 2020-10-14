package daily.boot.gulimall.ware.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 库存工作单
 * 
 * @author amy
 * @date 2020-10-14 17:07:54
 */
@Data
@TableName("wms_ware_order_task")
@ApiModel(value = "库存工作单类")
public class WareOrderTaskEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
    @ApiModelProperty(value = "id")
    private Long id;
    /**
     * order_id
     */
    @ApiModelProperty(value = "order_id")
    private Long orderId;
    /**
     * order_sn
     */
    @ApiModelProperty(value = "order_sn")
    private String orderSn;
    /**
     * 收货人
     */
    @ApiModelProperty(value = "收货人")
    private String consignee;
    /**
     * 收货人电话
     */
    @ApiModelProperty(value = "收货人电话")
    private String consigneeTel;
    /**
     * 配送地址
     */
    @ApiModelProperty(value = "配送地址")
    private String deliveryAddress;
    /**
     * 订单备注
     */
    @ApiModelProperty(value = "订单备注")
    private String orderComment;
    /**
     * 付款方式【 1:在线付款 2:货到付款】
     */
    @ApiModelProperty(value = "付款方式【 1:在线付款 2:货到付款】")
    private Integer paymentWay;
    /**
     * 任务状态
     */
    @ApiModelProperty(value = "任务状态")
    private Integer taskStatus;
    /**
     * 订单描述
     */
    @ApiModelProperty(value = "订单描述")
    private String orderBody;
    /**
     * 物流单号
     */
    @ApiModelProperty(value = "物流单号")
    private String trackingNo;
    /**
     * create_time
     */
    @ApiModelProperty(value = "create_time")
    private Date createTime;
    /**
     * 仓库id
     */
    @ApiModelProperty(value = "仓库id")
    private Long wareId;
    /**
     * 工作单备注
     */
    @ApiModelProperty(value = "工作单备注")
    private String taskComment;

}
