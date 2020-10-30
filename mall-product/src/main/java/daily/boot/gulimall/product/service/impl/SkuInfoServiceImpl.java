package daily.boot.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.common.utils.R;
import daily.boot.gulimall.product.dao.SkuInfoDao;
import daily.boot.gulimall.product.entity.SkuInfoEntity;
import daily.boot.gulimall.product.entity.SpuInfoEntity;
import daily.boot.gulimall.product.service.SkuImagesService;
import daily.boot.gulimall.product.service.SkuInfoService;
import daily.boot.gulimall.product.service.SkuSaleAttrValueService;
import daily.boot.gulimall.product.vo.SpuSaveVo;
import daily.boot.gulimall.service.api.feign.CouponFeignService;
import daily.boot.gulimall.service.api.to.MemberPriceTo;
import daily.boot.gulimall.service.api.to.SkuReductionTo;
import daily.boot.unified.dispose.exception.BusinessException;
import daily.boot.unified.dispose.exception.error.CommonErrorCode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Autowired
    private SkuImagesService skuImagesService;
    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;
    @Autowired
    private CouponFeignService couponFeignService;
    
    @Override
    public PageInfo<SkuInfoEntity> queryPage(PageQueryVo queryVo, SkuInfoEntity skuInfoEntity) {
        SkuInfoEntity queryEntity = new SkuInfoEntity();
        if (Objects.nonNull(skuInfoEntity.getCatelogId()) && skuInfoEntity.getCatelogId() > 0) {
            queryEntity.setCatelogId(skuInfoEntity.getCatelogId());
        }
        if (Objects.nonNull(skuInfoEntity.getBrandId()) && skuInfoEntity.getBrandId() > 0) {
            queryEntity.setBrandId(skuInfoEntity.getBrandId());
        }
        
        LambdaQueryWrapper<SkuInfoEntity> queryWrapper = Wrappers.lambdaQuery(queryEntity);
        String key = queryVo.getKey();
        if (StringUtils.isNotBlank(key)) {
            queryWrapper.and(q -> {
                q.eq(SkuInfoEntity::getSkuId, key)
                 .or()
                 .like(SkuInfoEntity::getSkuName, key);
            });
        }
    
    
        BigDecimal minPrice = null;
        if (StringUtils.isNotBlank(queryVo.getMin())) {
            minPrice = new BigDecimal(queryVo.getMin());
            queryWrapper.ge(SkuInfoEntity::getPrice, minPrice);
        }
        
        if (StringUtils.isNotBlank(queryVo.getMax())) {
            BigDecimal maxPrice = new BigDecimal(queryVo.getMax());
            if (Objects.isNull(minPrice)
                || (maxPrice.compareTo(minPrice) >= 0 && maxPrice.compareTo(new BigDecimal(0)) > 0)) {
                queryWrapper.le(SkuInfoEntity::getPrice, maxPrice);
            }
        }
        
        
        IPage<SkuInfoEntity> page = this.page(Query.getPage(queryVo), queryWrapper);
        return PageInfo.of(page);
    }
    
    @Override
    @Transactional
    public void save(SpuInfoEntity spuInfoEntity, List<SpuSaveVo.Skus> skus) {
        //2.1 保存sku基本信息 --> gulimall_pms.pms_sku_info
        //每个sku默认image
        List<SkuReductionTo> skuReductionTos = new ArrayList<>();
        skus.forEach(sku -> {
            SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
            BeanUtils.copyProperties(sku, skuInfoEntity);
            skuInfoEntity.setCatelogId(spuInfoEntity.getCatelogId());
            skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
            skuInfoEntity.setSpuId(spuInfoEntity.getId());
            skuInfoEntity.setSkuDesc(String.join(",", sku.getDescar()));
            skuInfoEntity.setSaleCount(0L);
            if (CollectionUtils.isNotEmpty(sku.getImages())) {
                for (SpuSaveVo.Skus.Image img : sku.getImages()) {
                    if (img.getDefaultImg() == 1) {
                        skuInfoEntity.setSkuDefaultImg(img.getImgUrl());
                        break;
                    }
                }
            }
            this.save(skuInfoEntity);
            Long skuId = skuInfoEntity.getSkuId();
    
            //2.2 保存sku图片信息 --> gulimall_pms.pms_sku_images
            skuImagesService.saveSku(skuId, sku.getImages());
            //2.3 保存sku销售参数 --> gulimall_pms.pms_sku_sale_attr_value
            skuSaleAttrValueService.save(skuId, sku.getAttr());
            //2.4 生成远程调用保存sku满减折扣信息 --> gulimall_sms.sms_sku_ladder/gulimall_sms.sms_sku_full_reduction/gulimall_sms.sms_member_price
            SkuReductionTo reductionTo = new SkuReductionTo();
            BeanUtils.copyProperties(sku, reductionTo);
            if (CollectionUtils.isNotEmpty(sku.getMemberPrice())) {
                List<MemberPriceTo> memberPriceTos = sku.getMemberPrice().stream().map(p -> {
                    MemberPriceTo to = new MemberPriceTo();
                    to.setMemberLevelId(p.getId());
                    to.setMemberPrice(p.getPrice());
                    to.setMemberLevelName(p.getName());
                    return to;
                }).collect(Collectors.toList());
                reductionTo.setMemberPrice(memberPriceTos);
            }
            reductionTo.setSkuId(skuId);
            skuReductionTos.add(reductionTo);
        });
        
        //远程调用coupon service
        if (CollectionUtils.isNotEmpty(skuReductionTos)) {
            R rtn = couponFeignService.saveSkuReduction(skuReductionTos);
            if (!rtn.isSuccess()) {
                throw new BusinessException(CommonErrorCode.API_GATEWAY_ERROR,
                                            "SkuInfoServiceImpl#save-->远程调用couponFeignService.saveSkuReduction失败--->"
                                            + skuReductionTos.stream().map(SkuReductionTo::toString).collect(Collectors.joining(",")));
            }
        }
    
    }
}