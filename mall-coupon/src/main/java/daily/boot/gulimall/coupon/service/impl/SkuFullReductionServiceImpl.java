package daily.boot.gulimall.coupon.service.impl;

import daily.boot.gulimall.common.utils.BeanCollectionUtil;
import daily.boot.gulimall.coupon.entity.MemberPriceEntity;
import daily.boot.gulimall.coupon.entity.SkuLadderEntity;
import daily.boot.gulimall.coupon.service.MemberPriceService;
import daily.boot.gulimall.coupon.service.SkuLadderService;
import daily.boot.gulimall.service.api.to.MemberPriceTo;
import daily.boot.gulimall.service.api.to.SkuReductionTo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.coupon.dao.SkuFullReductionDao;
import daily.boot.gulimall.coupon.entity.SkuFullReductionEntity;
import daily.boot.gulimall.coupon.service.SkuFullReductionService;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {

    @Autowired
    private SkuLadderService skuLadderService;
    @Autowired
    private MemberPriceService memberPriceService;
    
    @Override
    public PageInfo<SkuFullReductionEntity> queryPage(PageQueryVo queryVo) {
        IPage<SkuFullReductionEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }
    
    @Override
    @Transactional
    public void saveSkuReduction(List<SkuReductionTo> skuReductionTos) {
    
        List<SkuFullReductionEntity> skuFullReductionEntities = new ArrayList<>();
        List<MemberPriceEntity> memberPriceEntities = new ArrayList<>();
        List<SkuLadderEntity> skuLadderEntities = new ArrayList<>();
        //1. 获取要保存的信息
        skuReductionTos.forEach(to -> {
            SkuFullReductionEntity fullReductionEntity = new SkuFullReductionEntity();
            BeanUtils.copyProperties(to, fullReductionEntity);
            fullReductionEntity.setAddOther(to.getPriceStatus());
            skuFullReductionEntities.add(fullReductionEntity);
    
            SkuLadderEntity ladderEntity = new SkuLadderEntity();
            BeanUtils.copyProperties(to, ladderEntity);
            ladderEntity.setAddOther(to.getCountStatus());
            skuLadderEntities.add(ladderEntity);
    
            MemberPriceEntity memberPriceEntity = new MemberPriceEntity();
            List<MemberPriceEntity> memberPrices = BeanCollectionUtil.copyProperties(to.getMemberPrice(), MemberPriceEntity::new, (s, t) -> {
                t.setSkuId(to.getSkuId());
            });
            memberPriceEntities.addAll(memberPrices);
            
    
        });
        
        //1. 保存满减信息
        this.saveBatch(skuFullReductionEntities);
        
        //2. 保存阶梯折扣信息
        skuLadderService.saveBatch(skuLadderEntities);
        
        //3. 保存会员价格
        memberPriceService.saveBatch(memberPriceEntities);
        
    }
}