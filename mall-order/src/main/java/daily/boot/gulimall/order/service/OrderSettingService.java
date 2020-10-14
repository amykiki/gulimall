package daily.boot.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.order.entity.OrderSettingEntity;


/**
 * 订单配置信息
 *
 * @author amy
 * @date 2020-10-14 16:57:10
 */
public interface OrderSettingService extends IService<OrderSettingEntity> {

    /**
     * 无条件翻页查询
     * @param queryVo 分页查询参数
     * @return
     */
    PageInfo<OrderSettingEntity> queryPage(PageQueryVo queryVo);
}

