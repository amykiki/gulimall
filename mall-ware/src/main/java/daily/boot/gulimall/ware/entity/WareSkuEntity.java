package daily.boot.gulimall.ware.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 商品库存
 * 
 * @author amy
 * @date 2020-10-14 17:07:54
 */
@Data
@TableName("wms_ware_sku")
@ApiModel(value = "商品库存类")
public class WareSkuEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
    @ApiModelProperty(value = "id")
    private Long id;
    /**
     * sku_id
     */
    @ApiModelProperty(value = "sku_id")
    private Long skuId;
    /**
     * 仓库id
     */
    @ApiModelProperty(value = "仓库id")
    private Long wareId;
    /**
     * 库存数
     */
    @ApiModelProperty(value = "库存数")
    private Integer stock;
    /**
     * sku_name
     */
    @ApiModelProperty(value = "sku_name")
    private String skuName;
    /**
     * 锁定库存
     */
    @ApiModelProperty(value = "锁定库存")
    private Integer stockLocked;

}
