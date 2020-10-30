package daily.boot.gulimall.ware.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 采购信息
 * 
 * @author amy
 * @date 2020-10-14 17:07:54
 */
@Data
@TableName("wms_purchase")
@ApiModel(value = "采购信息类")
public class PurchaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId
    @ApiModelProperty(value = "")
    private Long id;
    /**
     * 
     */
    @ApiModelProperty(value = "")
    private Long assigneeId;
    /**
     * 
     */
    @ApiModelProperty(value = "")
    private String assigneeName;
    /**
     * 
     */
    @ApiModelProperty(value = "")
    private String phone;
    /**
     * 
     */
    @ApiModelProperty(value = "")
    private Integer priority;
    /**
     * 
     */
    @ApiModelProperty(value = "")
    private Integer status;
    /**
     * 
     */
    @ApiModelProperty(value = "")
    private Long wareId;
    /**
     * 
     */
    @ApiModelProperty(value = "")
    private BigDecimal amount;
    /**
     * 
     */
    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 
     */
    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

}
