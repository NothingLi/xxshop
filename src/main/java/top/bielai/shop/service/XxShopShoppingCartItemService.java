package top.bielai.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.bielai.shop.api.mall.vo.XxShopShoppingCartItemVO;
import top.bielai.shop.domain.XxShopShoppingCartItem;

import java.util.List;

/**
 * @author Administrator
 * @description 针对表【tb_xx_shop_shopping_cart_item】的数据库操作Service
 * @createDate 2022-11-30 13:58:39
 */
public interface XxShopShoppingCartItemService extends IService<XxShopShoppingCartItem> {
    /**
     * 查询购物车数据vo
     *
     * @param cartItemIds  购物车项id数组
     * @param xxShopUserId 用户id
     * @return 购物车数据vo
     */
    List<XxShopShoppingCartItemVO> getCartItemsForSettle(List<Long> cartItemIds, Long xxShopUserId);
}
