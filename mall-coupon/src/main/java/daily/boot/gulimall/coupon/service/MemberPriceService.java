package daily.boot.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.coupon.entity.MemberPriceEntity;

import java.util.List;
import java.util.function.Predicate;


/**
 * 商品会员价格
 *
 * @author amy
 * @date 2020-10-14 16:05:21
 */
public interface MemberPriceService extends IService<MemberPriceEntity> {

    /**
     * 无条件翻页查询
     * @param queryVo 分页查询参数
     * @return
     */
    PageInfo<MemberPriceEntity> queryPage(PageQueryVo queryVo);
    
    /**
     * 批量保存前先过滤list
     * @param memberPriceEntities
     * @param predicate
     */
    void saveBatch(List<MemberPriceEntity> memberPriceEntities, Predicate<? super MemberPriceEntity> predicate);
}

