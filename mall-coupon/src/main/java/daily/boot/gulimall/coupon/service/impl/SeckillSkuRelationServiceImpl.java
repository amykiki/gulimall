package daily.boot.gulimall.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.coupon.dao.SeckillSkuRelationDao;
import daily.boot.gulimall.coupon.entity.SeckillSkuRelationEntity;
import daily.boot.gulimall.coupon.service.SeckillSkuRelationService;

import java.util.List;


@Service("seckillSkuRelationService")
public class SeckillSkuRelationServiceImpl extends ServiceImpl<SeckillSkuRelationDao, SeckillSkuRelationEntity> implements SeckillSkuRelationService {

    @Override
    public PageInfo<SeckillSkuRelationEntity> queryPage(PageQueryVo queryVo, String promotionSessionId) {
        LambdaQueryWrapper<SeckillSkuRelationEntity> queryWrapper = Wrappers.lambdaQuery(SeckillSkuRelationEntity.class);
        if (StringUtils.isNotBlank(promotionSessionId)) {
            queryWrapper.eq(SeckillSkuRelationEntity::getPromotionSessionId, promotionSessionId);
        }
        IPage<SeckillSkuRelationEntity> page = this.page(Query.getPage(queryVo), queryWrapper);
        return PageInfo.of(page);
    }
    
    @Override
    public List<SeckillSkuRelationEntity> listByPromotionSessionId(Long promotionSessionId) {
        return this.lambdaQuery().eq(SeckillSkuRelationEntity::getPromotionSessionId, promotionSessionId).list();
    }
}