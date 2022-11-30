
package top.bielai.shop.service;

import top.bielai.shop.api.mall.vo.XxShopIndexCategoryVO;
import top.bielai.shop.domain.XxShopGoodsCategory;
import top.bielai.shop.util.PageQueryUtil;
import top.bielai.shop.util.PageResult;

import java.util.List;

public interface XxShopCategoryService {

    String saveCategory(XxShopGoodsCategory goodsCategory);

    String updateGoodsCategory(XxShopGoodsCategory goodsCategory);

    XxShopGoodsCategory getGoodsCategoryById(Long id);

    Boolean deleteBatch(Long[] ids);

    /**
     * 返回分类数据(首页调用)
     *
     * @return
     */
    List<XxShopIndexCategoryVO> getCategoriesForIndex();

    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getCategorisPage(PageQueryUtil pageUtil);

    /**
     * 根据parentId和level获取分类列表
     *
     * @param parentIds
     * @param categoryLevel
     * @return
     */
    List<XxShopGoodsCategory> selectByLevelAndParentIdsAndNumber(List<Long> parentIds, int categoryLevel);
}
