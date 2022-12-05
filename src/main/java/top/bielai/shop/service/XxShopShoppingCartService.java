
package top.bielai.shop.service;

import top.bielai.shop.api.mall.param.SaveCartItemParam;
import top.bielai.shop.api.mall.param.UpdateCartItemParam;
import top.bielai.shop.api.mall.vo.XxShopShoppingCartItemVO;
import top.bielai.shop.config.annotation.TokenToShopUser;
import top.bielai.shop.domain.XxShopShoppingCartItem;
import top.bielai.shop.util.PageResult;

import java.util.List;

public interface XxShopShoppingCartService {

    /**
     * 保存商品至购物车中
     *
     * @param saveCartItemParam
     * @param userId
     * @return
     */
    String saveXxShopCartItem(SaveCartItemParam saveCartItemParam, @TokenToShopUser Long userId);

    /**
     * 修改购物车中的属性
     *
     * @param updateCartItemParam
     * @param userId
     * @return
     */
    String updateXxShopCartItem(UpdateCartItemParam updateCartItemParam, @TokenToShopUser Long userId);

    /**
     * 获取购物项详情
     *
     * @param xxShopShoppingCartItemId
     * @return
     */
    XxShopShoppingCartItem getXxShopCartItemById(Long xxShopShoppingCartItemId);

    /**
     * 删除购物车中的商品
     *
     *
     * @param shoppingCartItemId
     * @param userId
     * @return
     */
    Boolean deleteById(Long shoppingCartItemId, @TokenToShopUser Long userId);

    /**
     * 删除购物车中的商品
     *
     *
     * @param shoppingCartItemId
     * @param userId
     * @return
     */
    Boolean deleteBatchById(Long[] shoppingCartItemId, @TokenToShopUser Long userId);

    /**
     * 获取我的购物车中的列表数据
     *
     * @param xxShopUserId
     * @return
     */
    List<XxShopShoppingCartItemVO> getMyShoppingCartItems(Long xxShopUserId);

    /**
     * 根据userId和cartItemIds获取对应的购物项记录
     *
     * @param cartItemIds
     * @param xxShopUserId
     * @return
     */
    List<XxShopShoppingCartItemVO> getCartItemsForSettle(List<Long> cartItemIds, Long xxShopUserId);

    /**
     * 我的购物车(分页数据)
     *
     * @param pageUtil
     * @return
     */
    PageResult getMyShoppingCartItems(PageQueryUtil pageUtil);
}
