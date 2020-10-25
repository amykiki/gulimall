/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package daily.boot.gulimall.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.xss.SQLFilter;
import org.apache.commons.lang.StringUtils;

/**
 * 查询参数
 *
 * @author Mark sunlightcs@gmail.com
 */
public class Query {

    public static <T> IPage<T> getPage(PageQueryVo pageVo) {
        return getPage(pageVo, null, true);
    }

    public static <T> IPage<T> getPage(PageQueryVo pageVo, String defaultOrderField, boolean isAsc) {
        //分页参数
        long curPage = 1;
        long limit = 10;

        if(pageVo.getPage() != null){
            curPage = pageVo.getPage();
        }
        if(pageVo.getLimit() != null){
            limit = pageVo.getLimit();
        }

        //分页对象
        Page<T> page = new Page<>(curPage, limit);

        //分页参数
        //params.put(Constant.PAGE, page);

        //排序字段
        //防止SQL注入（因为sidx、order是通过拼接SQL实现排序的，会有SQL注入风险）
        String orderField = SQLFilter.sqlInject(pageVo.getOrderFiled());
        String order = pageVo.getOrder();


        //前端字段排序
        if(StringUtils.isNotEmpty(orderField) && StringUtils.isNotEmpty(order)){
            if(Constant.ASC.equalsIgnoreCase(order)) {
                return  page.addOrder(OrderItem.asc(orderField));
            }else {
                return page.addOrder(OrderItem.desc(orderField));
            }
        }

        //没有排序字段，则不排序
        if(StringUtils.isBlank(defaultOrderField)){
            return page;
        }

        //默认排序
        if(isAsc) {
            page.addOrder(OrderItem.asc(defaultOrderField));
        }else {
            page.addOrder(OrderItem.desc(defaultOrderField));
        }

        return page;
    }
}
