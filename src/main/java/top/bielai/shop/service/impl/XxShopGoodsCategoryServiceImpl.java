package top.bielai.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.bielai.shop.api.mall.vo.XxShopIndexCategoryVO;
import top.bielai.shop.domain.XxShopGoodsCategory;
import top.bielai.shop.mapper.XxShopGoodsCategoryMapper;
import top.bielai.shop.service.XxShopGoodsCategoryService;
import top.bielai.shop.util.BeanUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * @author bielai
 * @description 针对表【tb_xx_shop_goods_category】的数据库操作Service实现
 * @createDate 2022-11-30 13:58:39
 */
@Service
public class XxShopGoodsCategoryServiceImpl extends ServiceImpl<XxShopGoodsCategoryMapper, XxShopGoodsCategory>
        implements XxShopGoodsCategoryService {

    @Override
    public List<XxShopIndexCategoryVO> getCategoriesForIndex() {
        List<XxShopGoodsCategory> list = list(generateQueryWrapper(Collections.singleton(0L), 1));
        return getVoList(list);
    }

    private List<XxShopIndexCategoryVO> getVoList(List<XxShopGoodsCategory> list){
        if (!list.isEmpty()) {
            List<XxShopIndexCategoryVO> voList = BeanUtil.copyList(list, XxShopIndexCategoryVO.class);
            getChildren(voList);
            return voList;
        }
        return null;
    }

    private List<XxShopIndexCategoryVO> getChildren(List<XxShopIndexCategoryVO> voList) {
        Set<Long> parentIds = voList.stream().map(XxShopIndexCategoryVO::getCategoryId).collect(Collectors.toSet());
        List<XxShopGoodsCategory> children = list(generateQueryWrapper(parentIds, voList.get(0).getCategoryLevel() + 1));
        if (!CollectionUtils.isEmpty(children)) {
            Map<Long, List<XxShopGoodsCategory>> groupByParent = children.stream().collect(groupingBy(XxShopGoodsCategory::getParentId));
            for (XxShopIndexCategoryVO vo : voList) {
                List<XxShopGoodsCategory> childCateGory = groupByParent.get(vo.getCategoryId());
                if (!CollectionUtils.isEmpty(childCateGory)) {
                    List<XxShopIndexCategoryVO> childVoList = getChildren(BeanUtil.copyList(childCateGory, XxShopIndexCategoryVO.class));
                    vo.setChildrenCategoryVO(childVoList);
                }
            }
        }
        return voList;
    }

    private LambdaQueryWrapper<XxShopGoodsCategory> generateQueryWrapper(Set<Long> parentIds, int level) {
        return new LambdaQueryWrapper<XxShopGoodsCategory>()
                .in(XxShopGoodsCategory::getParentId, parentIds)
                .eq(XxShopGoodsCategory::getCategoryLevel, level)
                .orderByDesc(XxShopGoodsCategory::getCategoryRank);
    }
}




