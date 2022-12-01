package top.bielai.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.bielai.shop.api.mall.vo.XxShopIndexCategoryVO;
import top.bielai.shop.common.Constants;
import top.bielai.shop.domain.XxShopGoodsCategory;
import top.bielai.shop.mapper.XxShopGoodsCategoryMapper;
import top.bielai.shop.service.XxShopGoodsCategoryService;
import top.bielai.shop.util.BeanUtil;

import java.util.List;

/**
 * @author Administrator
 * @description 针对表【tb_xx_shop_goods_category】的数据库操作Service实现
 * @createDate 2022-11-30 13:58:39
 */
@Service
public class XxShopGoodsCategoryServiceImpl extends ServiceImpl<XxShopGoodsCategoryMapper, XxShopGoodsCategory>
        implements XxShopGoodsCategoryService {

    @Override
    public List<XxShopIndexCategoryVO> getCategoriesForIndex() {
        List<XxShopGoodsCategory> list = list(generateQueryWrapper(0L, 1));
        return getChildren(list);
    }

    private List<XxShopIndexCategoryVO> getChildren(List<XxShopGoodsCategory> list) {
        if (!list.isEmpty()) {
            List<XxShopIndexCategoryVO> voList = BeanUtil.copyList(list, XxShopIndexCategoryVO.class);
            for (XxShopIndexCategoryVO vo : voList) {
                List<XxShopGoodsCategory> children = list(generateQueryWrapper(vo.getCategoryId(),
                        vo.getCategoryLevel() + 1));
                if (!children.isEmpty()) {
                    getChildren(list);
                    List<XxShopIndexCategoryVO> childVoList = BeanUtil.copyList(children, XxShopIndexCategoryVO.class);
                    vo.setChildrenCategoryVO(childVoList);
                }
            }
            return voList;
        }
        return null;
    }

    private LambdaQueryWrapper<XxShopGoodsCategory> generateQueryWrapper(Long parent, int level) {
        return new LambdaQueryWrapper<XxShopGoodsCategory>()
                .eq(XxShopGoodsCategory::getParentId, parent)
                .eq(XxShopGoodsCategory::getCategoryLevel, level)
                .orderByDesc(XxShopGoodsCategory::getCategoryRank)
                .last("limit "+ Constants.INDEX_CATEGORY_NUMBER);
    }
}




