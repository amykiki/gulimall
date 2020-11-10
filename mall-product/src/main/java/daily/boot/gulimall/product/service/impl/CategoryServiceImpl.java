package daily.boot.gulimall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.product.dao.CategoryDao;
import daily.boot.gulimall.product.entity.CategoryEntity;
import daily.boot.gulimall.product.service.CategoryBrandRelationService;
import daily.boot.gulimall.product.service.CategoryService;
import daily.boot.gulimall.product.vo.Catelog2Vo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;
    @Autowired
    private StringRedisTemplate redisTemplate;
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
    
    @Override
    @Cacheable(value = {"category"}, key = "#root.method.name")
    public List<CategoryEntity> getLevel1Categorys() {
        System.out.println("从数据库中获取1级菜单!!!");
        long l = System.currentTimeMillis();
        List<CategoryEntity> list = this.lambdaQuery().eq(CategoryEntity::getParentCid, 0).list();
        //System.out.println("消耗时间："+ (System.currentTimeMillis() - l));
        return list;
    }
    
    //@Override
    //public Map<String, List<Catelog2Vo>> getCatalogJson() {
    //    String key = "catalogJson";
    //    String data = redisTemplate.opsForValue().get(key);
    //    if (StringUtils.isBlank(data)) {
    //        Map<String, List<Catelog2Vo>> catalogJsonFromDb = getCatalogJsonFromDb();
    //        String jsonStr = JSON.toJSONString(catalogJsonFromDb);
    //        redisTemplate.opsForValue().set(key, jsonStr);
    //        return catalogJsonFromDb;
    //    }else {
    //        Map<String, List<Catelog2Vo>> result = JSON.parseObject(data, new TypeReference<Map<String, List<Catelog2Vo>>>() {
    //        });
    //        return result;
    //    }
    //}
    
    @Override
    @Cacheable(value = {"category"}, key = "#root.method.name")
    public Map<String, List<Catelog2Vo>> getCatalogJson() {
        System.out.println("菜单信息缓存为空，从数据库中获取");
        return getCatalogJsonFromDb();
    }
    
    private Map<String, List<Catelog2Vo>> getCatalogJsonFromDb() {
        Map<Long, List<CategoryEntity>> listMap = this.list()
                                                      .stream()
                                                      .collect(Collectors.toMap(CategoryEntity::getParentCid,
                                                                                c -> new ArrayList<>(Collections.singletonList(c)),
                                                                                (List<CategoryEntity> oldList, List<CategoryEntity> newList) -> {
                                                                                    oldList.addAll(newList);
                                                                                    return oldList;
                                                                                }));
        List<CategoryEntity> level1 = listMap.get(0L);
        Map<String, List<Catelog2Vo>> rtnMap = level1.stream().collect(Collectors.toMap(k -> String.valueOf(k.getCatId()), c1 -> {
            List<CategoryEntity> l2list = listMap.get(c1.getCatId());
            List<Catelog2Vo> vo2list = l2list.stream().map(l2c -> {
                Catelog2Vo vo = new Catelog2Vo();
                vo.setCatalog1Id(String.valueOf(c1.getCatId()));
                vo.setId(String.valueOf(l2c.getCatId()));
                vo.setName(l2c.getName());
                List<CategoryEntity> l3list = listMap.get(l2c.getCatId());
                if (CollectionUtils.isNotEmpty(l3list)) {
                    List<Catelog2Vo.Category3Vo> vo3list = l3list.stream().map(l3c -> {
                        Catelog2Vo.Category3Vo vo3 = new Catelog2Vo.Category3Vo();
                        vo3.setId(String.valueOf(l3c.getCatId()));
                        vo3.setName(l3c.getName());
                        vo3.setCatalog2Id(String.valueOf(l2c.getCatId()));
                        return vo3;
                    }).collect(Collectors.toList());
                    vo.setCatalog3List(vo3list);
                }
                return vo;
            }).collect(Collectors.toList());
            return vo2list;
        
        }));
    
        return rtnMap;
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