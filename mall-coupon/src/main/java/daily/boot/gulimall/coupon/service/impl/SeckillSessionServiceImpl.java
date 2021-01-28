package daily.boot.gulimall.coupon.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.common.util.DateTimeFormatterUtil;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.coupon.dao.SeckillSessionDao;
import daily.boot.gulimall.coupon.entity.SeckillSessionEntity;
import daily.boot.gulimall.coupon.entity.SeckillSkuRelationEntity;
import daily.boot.gulimall.coupon.service.SeckillSessionService;
import daily.boot.gulimall.coupon.service.SeckillSkuRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


@Service("seckillSessionService")
public class SeckillSessionServiceImpl extends ServiceImpl<SeckillSessionDao, SeckillSessionEntity> implements SeckillSessionService {
    @Autowired
    private SeckillSkuRelationService seckillSkuRelationService;
    
    @Override
    public List<SeckillSessionEntity> getLatest3DaySession() {
        //计算最近三天参与秒杀活动的商品
        List<SeckillSessionEntity> list = this.lambdaQuery().between(SeckillSessionEntity::getStartTime, startTime(), endTime())
                                              .list();
        if (CollectionUtils.isEmpty(list))
            return null;
        list.forEach(session -> {
            Long id = session.getId();
            List<SeckillSkuRelationEntity> relationSkus = seckillSkuRelationService.listByPromotionSessionId(id);
            session.setRelationSkus(relationSkus);
        });
        return list;
    }
    
    private String startTime() {
        //获取起始时间
        LocalDate now = LocalDate.now();
        LocalTime min = LocalTime.MIN;
        LocalDateTime start = LocalDateTime.of(now, min);
        
        //格式化时间返回
        return DateTimeFormatterUtil.formatToDateTimeStr(start);
    }
    
    private String endTime() {
        //获取三天的结束时间
        LocalDate now = LocalDate.now();
        LocalDate endDay = now.plusDays(2);
        LocalTime max = LocalTime.MAX;
        LocalDateTime end = LocalDateTime.of(endDay, max);
    
        return DateTimeFormatterUtil.formatToDateTimeStr(end);
    }
    
    @Override
    public PageInfo<SeckillSessionEntity> queryPage(PageQueryVo queryVo) {
        IPage<SeckillSessionEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }

}