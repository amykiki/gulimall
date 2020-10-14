package daily.boot.gulimall.order.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 退货原因
 * 
 * @author amy
 * @date 2020-10-14 16:57:10
 */
@Data
@TableName("oms_order_return_reason")
@ApiModel(value = "退货原因类")
public class OrderReturnReasonEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
    @ApiModelProperty(value = "id")
    private Long id;
    /**
     * 退货原因名
     */
    @ApiModelProperty(value = "退货原因名")
    private String name;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;
    /**
     * 启用状态
     */
    @ApiModelProperty(value = "启用状态")
    private Integer status;
    /**
     * create_time
     */
    @ApiModelProperty(value = "create_time")
    private Date createTime;

}
