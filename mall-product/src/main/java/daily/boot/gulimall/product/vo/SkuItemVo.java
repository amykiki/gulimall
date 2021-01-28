package daily.boot.gulimall.product.vo;

import daily.boot.gulimall.product.entity.SkuImagesEntity;
import daily.boot.gulimall.product.entity.SkuInfoEntity;
import daily.boot.gulimall.product.entity.SpuInfoDescEntity;
import daily.boot.gulimall.service.api.to.SeckillSkuItemVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel("商品详情页VO")
public class SkuItemVo implements Serializable {
    private static final long serialVersionUID = -3072380574911070101L;
    @ApiModelProperty("sku基本信息")
    private SkuInfoEntity info;
    
    @ApiModelProperty("是否有库存")
    private boolean hasStock;
    
    @ApiModelProperty("sku的图片信息")
    private List<SkuImagesEntity> images;
    
    @ApiModelProperty("spu的销售属性集合")
    private List<SkuItemSaleAttr> saleAttr;
    
    @ApiModelProperty("spu的介绍")
    private SpuInfoDescEntity desc;
    
    private List<SpuItemAttrGroup> groupAttrs;
    
    //秒杀商品优惠信息
    private SeckillSkuItemVo seckillSkuVo;
    
    @Data
    public static class SkuItemSaleAttr implements Serializable{
        private static final long serialVersionUID = -598217586058910707L;
        private Long attrId;
        /**
         * 用于页面展示属性名称
         */
        private String attrName;
        /**
         * 保存属性值和对应的哪些sku有这些属性
         */
        private List<AttrValueWithSkuId> attrValues;
    }
    
    @Data
    public static class AttrValueWithSkuId implements Serializable {
        private static final long serialVersionUID = 1282138144744497203L;
        /**
         * 属性值
         */
        private String attrValue;
        /**
         * 包含该属性值的skuId列表，skuId之间以","分割
         */
        private String skuIds;
    }
    
    @Data
    public static class SpuItemAttrGroup implements Serializable {
        private static final long serialVersionUID = -1776790849574589462L;
        /**
         * spu属性分组的分组名
         */
        private String groupName;
        private List<SpuAttr> attrs;
    }
    
    /**
     * spu属性的名称和值
     */
    @Data
    public static class SpuAttr {
        private String attrName;
        private String attrValue;
    }
}
