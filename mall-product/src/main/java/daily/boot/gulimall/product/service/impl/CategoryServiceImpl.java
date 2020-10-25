package daily.boot.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.product.dao.CategoryDao;
import daily.boot.gulimall.product.entity.CategoryEntity;
import daily.boot.gulimall.product.service.CategoryBrandRelationService;
import daily.boot.gulimall.product.service.CategoryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;
    @Override
    public PageInfo<CategoryEntity> queryPage(PageQueryVo queryVo) {
        IPage<CategoryEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }
    
    @Override
    public List<CategoryEntity> listWithTree() {
        //1. 找出所有分类
        List<CategoryEntity> list = this.list();
        
        //2. 组装成父子树形结构
    
        return treeList(list, 0L);
    }
    
    @Override
    public void removeMenuByIds(List<Long> delIds) {
        // TODO: 2020/10/18 检查是否有业务引用ID
        this.removeByIds(delIds);
    }
    
    @Override
    public Long[] findCatelogPath(Long catelogId) {
        LinkedList<Long> path = new LinkedList<>();
        addCatelogPath(catelogId, path);
        return path.toArray(new Long[0]);
    }
    
    @Override
    public CategoryEntity getSimpleCategoryEntityById(Long catId) {
        return this.getOne(
                Wrappers.lambdaQuery(CategoryEntity.class)
                        .select(CategoryEntity::getCatId,
                                CategoryEntity::getName,
                                CategoryEntity::getParentCid,
                                CategoryEntity::getCatLevel)
                        .eq(CategoryEntity::getCatId, catId));
    }
    
    @Override
    @Transactional
    public void updateCascaded(CategoryEntity category) {
        //保证冗余字段的数据一致--冗余name
        this.updateById(category);
        if (StringUtils.isNotBlank(category.getName())) {
            //同步更新其他关联表中的数据
            categoryBrandRelationService.updateRelationCategory(
                    category.getCatId(), category.getName());
        }
    }
    
    private void addCatelogPath(Long catelogId, LinkedList<Long> path) {
        if (catelogId == 0) {
            return;
        }
        path.push(catelogId);
        LambdaQueryWrapper<CategoryEntity> query = Wrappers
                .<CategoryEntity>lambdaQuery()
                .select(CategoryEntity::getCatId, CategoryEntity::getParentCid)
                .eq(CategoryEntity::getCatId, catelogId);
        CategoryEntity entity = this.getOne(query);
        addCatelogPath(entity.getParentCid(), path);
    }
    
    private List<CategoryEntity> treeList(List<CategoryEntity> dataList, Long parentId) {
        return dataList.stream()
                .filter(entity -> parentId.equals(entity.getParentCid()))
                .peek(entity -> entity.setChildren(treeList(dataList, entity.getCatId())))
                .sorted(Comparator.comparingInt(entity -> (entity.getSort() == null ? 0 : entity.getSort())))
                .collect(Collectors.toList());
        
    }
}