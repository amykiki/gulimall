package daily.boot.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.common.utils.R;
import daily.boot.gulimall.product.dao.SpuInfoDao;
import daily.boot.gulimall.product.entity.SpuInfoEntity;
import daily.boot.gulimall.product.service.*;
import daily.boot.gulimall.product.vo.SpuSaveVo;
import daily.boot.gulimall.service.api.feign.CouponFeignService;
import daily.boot.gulimall.service.api.to.SpuBoundsTo;
import daily.boot.unified.dispose.exception.BusinessException;
import daily.boot.unified.dispose.exception.error.CommonErrorCode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {
    @Autowired
    private SpuInfoDescService spuInfoDescService;
    @Autowired
    private SpuImagesService spuImagesService;
    @Autowired
    private ProductAttrValueService productAttrValueService;
    @Autowired
    private SkuInfoService skuInfoService;
    @Autowired
    private CouponFeignService couponFeignService;
    
    @Override
    public PageInfo<SpuInfoEntity> queryPage(PageQueryVo queryVo, SpuInfoEntity spuInfoEntity) {
        SpuInfoEntity queryEntity = new SpuInfoEntity();
        if (spuInfoEntity.getBrandId() != null && spuInfoEntity.getBrandId() > 0) {
            queryEntity.setBrandId(spuInfoEntity.getBrandId());
        }
        if (spuInfoEntity.getCatelogId() != null && spuInfoEntity.getCatelogId() > 0) {
            queryEntity.setCatelogId(spuInfoEntity.getCatelogId());
        }
        queryEntity.setPublishStatus(spuInfoEntity.getPublishStatus());
        LambdaQueryWrapper<SpuInfoEntity> queryWrapper = Wrappers.lambdaQuery(queryEntity);
        
        String key = queryVo.getKey();
        if (StringUtils.isNotBlank(key)) {
            queryWrapper.and(q -> {
                q.like(SpuInfoEntity::getSpuName, key)
                 .or()
                 .like(SpuInfoEntity::getSpuDescription, key);
            });
        }
        IPage<SpuInfoEntity> page = this.page(Query.getPage(queryVo), queryWrapper);
        return PageInfo.of(page);
    }
    
    @Override
    @Transactional
    public void saveSpuInfo(SpuSaveVo spuSaveVo) {
        //1 保存spu信息
        //1.1 保存spu基本信息 --> gulimall_pms.pms_spu_info
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuSaveVo, spuInfoEntity);
        this.save(spuInfoEntity);
        Long spuId = spuInfoEntity.getId();
        
        //1.2 保存spu描述图片 --> gulimall_pms.pms_spu_info_desc
        spuInfoDescService.save(spuId, spuSaveVo.getDecript());
        
        //1.3 保存spu图集 --> gulimall_pms.pms_spu_images
        spuImagesService.save(spuId, spuSaveVo.getImages());
        
        //1.4 保存spu规格参数 --> gulimall_pms.pms_product_attr_value
        productAttrValueService.save(spuId, spuSaveVo.getBaseAttrs());
        
        //1.5 远程调用保存spu积分信息 --> gulimall_sms.sms_spu_bounds
        SpuBoundsTo spuBoundsTo = new SpuBoundsTo();
        BeanUtils.copyProperties(spuSaveVo.getBounds(), spuBoundsTo);
        spuBoundsTo.setSpuId(spuId);
        R rtn = couponFeignService.saveSpuBounds(spuBoundsTo);
        System.out.println(rtn);
        if (!rtn.isSuccess()) {
            throw new BusinessException(CommonErrorCode.API_GATEWAY_ERROR,
                                        "SpuInfoServiceImpl#saveSpuInfo-->远程调用couponFeignService.saveSpuBounds失败--->"
                                        + spuBoundsTo);
        }
        
        //2 保存sku信息
        skuInfoService.save(spuInfoEntity, spuSaveVo.getSkus());
        
        
    }
}