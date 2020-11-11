package daily.boot.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.common.Result;
import daily.boot.common.exception.BusinessException;
import daily.boot.common.exception.error.CommonErrorCode;
import daily.boot.gulimall.common.constant.ProductConstant;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.common.utils.R;
import daily.boot.gulimall.product.dao.SpuInfoDao;
import daily.boot.gulimall.product.entity.*;
import daily.boot.gulimall.product.service.*;
import daily.boot.gulimall.product.vo.SpuSaveVo;
import daily.boot.gulimall.service.api.feign.CouponFeignService;
import daily.boot.gulimall.service.api.feign.SearchFeignService;
import daily.boot.gulimall.service.api.feign.WareFeignService;
import daily.boot.gulimall.service.api.to.SkuEsTo;
import daily.boot.gulimall.service.api.to.SkuHasStockTo;
import daily.boot.gulimall.service.api.to.SpuBoundsTo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {
    @Autowired
    private SpuInfoDescService spuInfoDescService;
    @Autowired
    private SpuImagesService spuImagesService;
    @Autowired
    private ProductAttrValueService productAttrValueService;
    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;
    @Autowired
    private SkuInfoService skuInfoService;
    @Autowired
    private CouponFeignService couponFeignService;
    @Autowired
    private AttrService attrService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private WareFeignService wareFeignService;
    @Autowired
    private SearchFeignService searchFeignService;
    
    
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
    
    @Override
    public void updateStatusById(Long id, int status) {
        SpuInfoEntity entity = new SpuInfoEntity();
        entity.setPublishStatus(status);
        entity.setId(id);
        this.updateById(entity);
    
        /**
           注意：如果只通过updateWrapper更新，没有entity实体，那么更新的时候是不会自动填充属性的
           通过下列方式更新，不会自动填充updateTime字段
         */
        //LambdaUpdateWrapper<SpuInfoEntity> wrapper =
        //        Wrappers.lambdaUpdate(SpuInfoEntity.class)
        //                .set(SpuInfoEntity::getPublishStatus, status)
        //                .eq(SpuInfoEntity::getId, id);
        //this.update(wrapper);
        
    }
    
    @Override
    @Transactional
    public void upStatus(Long spuId) {
        // 1. 查出当前spuId对应的sku信息，品牌的名字
        List<SkuInfoEntity> skuInfoEntities = skuInfoService.listBySpuId(spuId);
        
        // 2. 查出当前spuId的所有可以被用来检索的规格属性
        List<ProductAttrValueEntity> spuAllAttrs = productAttrValueService.listBySpuId(spuId);
        // 2.1 过滤出可以被检索的规格属性
        List<Long> attrIds = spuAllAttrs.stream().map(ProductAttrValueEntity::getAttrId).collect(Collectors.toList());
        List<Long> selectableIds = attrService.selectableIds(attrIds);
        Set<Long> idSet = new HashSet<>(selectableIds);
        //2.2 生成SKUESTO的ATTR属性
        List<SkuEsTo.Attr> skuEsAttr = spuAllAttrs.stream().filter(attr -> idSet.contains(attr.getAttrId()))
                                                  .map(attr -> {
                                                      SkuEsTo.Attr skuAttr = new SkuEsTo.Attr();
                                                      BeanUtils.copyProperties(attr, skuAttr);
                                                      return skuAttr;
                                                  }).collect(Collectors.toList());
        //把4G:5G类似这样的一个属性包含多个值的情况扁平化，使得es搜索时更方便
        List<SkuEsTo.Attr> skuEssAttrs = new ArrayList<>();
        for (SkuEsTo.Attr attr : skuEsAttr) {
            if (attr.getAttrValue().contains(";")) {
                String[] attrValues = attr.getAttrValue().split(";");
                for (String attrValue : attrValues) {
                    SkuEsTo.Attr newAttr = new SkuEsTo.Attr();
                    newAttr.setAttrName(attr.getAttrName());
                    newAttr.setAttrId(attr.getAttrId());
                    newAttr.setAttrValue(attrValue);
                    skuEssAttrs.add(newAttr);
                }
            }else {
                skuEssAttrs.add(attr);
            }
        }
                 
        List<Long> skuIds = new ArrayList<>();
        Set<Long> categorySet = new HashSet<>();
        Set<Long> brandSet = new HashSet<>();
        //批量获取skuId，categoryId，brandId来为批量查询信息做准备
        for (SkuInfoEntity sku : skuInfoEntities) {
            skuIds.add(sku.getSkuId());
            categorySet.add(sku.getCatelogId());
            brandSet.add(sku.getBrandId());
        }
        //3. 远程调用查询每个sku是否有库存
        //映射每个stock的库存
        Map<Long, Boolean> skuStockMap = null;
        try {
            Result<List<SkuHasStockTo>> wareRtn = wareFeignService.getSkuHasStock(skuIds);
            if (Objects.nonNull(wareRtn) && wareRtn.isOk()) {
                List<SkuHasStockTo> stocks = wareRtn.getData();
                skuStockMap = stocks.stream().collect(Collectors.toMap(SkuHasStockTo::getSkuId, SkuHasStockTo::getHasStock));
            }
        } catch (Exception e) {
            log.error("upStatus -- 远程调用库存服务异常，原因{}", e);
        }
        
        //4. 批量获取品牌分类的信息
        List<CategoryEntity> categoryEntities = categoryService.listByIds(new ArrayList<>(categorySet));
        Map<Long, CategoryEntity> categoryEntityMap = categoryEntities.stream().collect(Collectors.toMap(CategoryEntity::getCatId, Function.identity()));
        
        //5. 批量获取分类的信息
        List<BrandEntity> brandEntities = brandService.listByIds(new ArrayList<>(brandSet));
        Map<Long, BrandEntity> brandEntityMap = brandEntities.stream().collect(Collectors.toMap(BrandEntity::getBrandId, Function.identity()));
        
        //6. 批量获取每个SKU的销售属性
        List<SkuSaleAttrValueEntity> skuSaleAttrs = skuSaleAttrValueService.listBySkuIds(skuIds);
        Map<Long, List<SkuEsTo.Attr>> skuSaleAttrMap = skuSaleAttrs.stream()
                                                            .collect(Collectors.toMap(SkuSaleAttrValueEntity::getSkuId,
                                                                                      a -> new ArrayList<>(Collections.singletonList(new SkuEsTo.Attr(a.getAttrId(), a.getAttrName(), a.getAttrValue()))),
                                                                                      (List<SkuEsTo.Attr> oldList, List<SkuEsTo.Attr> newList) -> {
                                                                                          oldList.addAll(newList);
                                                                                          return oldList;
                                                                                      }));
        //7. 封装每个SKU的ES消息
        Map<Long, Boolean> finalSkuStockMap = skuStockMap;
        List<SkuEsTo> skuEsToList = skuInfoEntities.stream().map(sku -> {
            //组装需要的数据
            SkuEsTo esTo = new SkuEsTo();
            esTo.setSkuPrice(sku.getPrice());
            esTo.setSkuImg(sku.getSkuDefaultImg());

            //设置库存信息,远程服务调用失败，则默认有库存
            if (finalSkuStockMap == null) {
                esTo.setHasStock(true);
            } else {
                esTo.setHasStock(finalSkuStockMap.get(sku.getSkuId()));
            }

            //设置默认热度评分
            esTo.setHotScore(0L);

            //设置品牌信息
            BrandEntity brandEntity = brandEntityMap.get(sku.getBrandId());
            esTo.setBrandId(brandEntity.getBrandId());
            esTo.setBrandName(brandEntity.getName());
            esTo.setBrandImg(brandEntity.getLogo());

            //设置分类信息
            CategoryEntity categoryEntity = categoryEntityMap.get(sku.getCatelogId());
            esTo.setCatalogId(categoryEntity.getCatId());
            esTo.setCatalogName(categoryEntity.getName());

            //设置每个sku单独销售属性
            esTo.setAttrs(skuSaleAttrMap.get(sku.getSkuId()));
            //设置可检索的规格属性
            esTo.getAttrs().addAll(skuEssAttrs);

            //复制相同其他属性
            BeanUtils.copyProperties(sku, esTo);
            return esTo;
        }).collect(Collectors.toList());

        Result<Boolean> esRtn = null;
        try {
            //8. 将数据发送给es保存
            esRtn = searchFeignService.productStatusUp(skuEsToList);
            //远程调用成功
            //9修改当前spu的状态
            if (Objects.nonNull(esRtn) && esRtn.getData()) {
                this.updateStatusById(spuId, ProductConstant.ProductStatusEnum.SPU_UP.getCode());
            }
        } catch (Exception e) {
            //远程调用失败
            //TODO 重复调用？接口幂等性:重试机制
        }
        
    }
}