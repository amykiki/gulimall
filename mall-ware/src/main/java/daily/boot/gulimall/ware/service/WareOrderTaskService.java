package daily.boot.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.ware.entity.WareOrderTaskEntity;


/**
 * 库存工作单
 *
 * @author amy
 * @date 2020-10-14 17:07:54
 */
public interface WareOrderTaskService extends IService<WareOrderTaskEntity> {

    /**
     * 无条件翻页查询
     * @param queryVo 分页查询参数
     * @return
     */
    PageInfo<WareOrderTaskEntity> queryPage(PageQueryVo queryVo);
}

