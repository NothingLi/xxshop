package top.bielai.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.bielai.shop.api.mall.vo.XxShopIndexCategoryVO;
import top.bielai.shop.domain.XxShopGoodsCategory;

import java.util.List;

/**
 * @author Administrator
 * @description 针对表【tb_xx_shop_goods_category】的数据库操作Service
 * @createDate 2022-11-30 13:58:39
 */
public interface XxShopGoodsCategoryService extends IService<XxShopGoodsCategory> {

    /**
     * 获取商品分类信息
     *
     * @return 商品分类列表
     */
    List<XxShopIndexCategoryVO> getCategoriesForIndex();
}
