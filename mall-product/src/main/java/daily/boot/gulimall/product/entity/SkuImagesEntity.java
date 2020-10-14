package daily.boot.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * sku图片
 * 
 * @author amy
 * @date 2020-10-14 15:18:58
 */
@Data
@TableName("pms_sku_images")
@ApiModel(value = "sku图片类")
public class SkuImagesEntity implements Serializable {
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
     * 图片地址
     */
    @ApiModelProperty(value = "图片地址")
    private String imgUrl;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer imgSort;
    /**
     * 默认图[0 - 不是默认图，1 - 是默认图]
     */
    @ApiModelProperty(value = "默认图[0 - 不是默认图，1 - 是默认图]")
    private Integer defaultImg;

}
