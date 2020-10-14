package daily.boot.gulimall.ware.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 采购详情
 * 
 * @author amy
 * @date 2020-10-14 17:07:54
 */
@Data
@TableName("wms_purchase_detail")
@ApiModel(value = "采购详情类")
public class PurchaseDetailEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId
    @ApiModelProperty(value = "")
    private Long id;
    /**
     * 采购单id
     */
    @ApiModelProperty(value = "采购单id")
    private Long purchaseId;
    /**
     * 采购商品id
     */
    @ApiModelProperty(value = "采购商品id")
    private Long skuId;
    /**
     * 采购数量
     */
    @ApiModelProperty(value = "采购数量")
    private Integer skuNum;
    /**
     * 采购金额
     */
    @ApiModelProperty(value = "采购金额")
    private BigDecimal skuPrice;
    /**
     * 仓库id
     */
    @ApiModelProperty(value = "仓库id")
    private Long wareId;
    /**
     * 状态[0新建，1已分配，2正在采购，3已完成，4采购失败]
     */
    @ApiModelProperty(value = "状态[0新建，1已分配，2正在采购，3已完成，4采购失败]")
    private Integer status;

}
