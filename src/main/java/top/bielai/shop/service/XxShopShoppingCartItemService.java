package top.bielai.shop.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import top.bielai.shop.api.mall.param.SaveCartItemParam;
import top.bielai.shop.api.mall.param.UpdateCartItemParam;
import top.bielai.shop.api.mall.vo.XxShopShoppingCartItemVO;
import top.bielai.shop.domain.XxShopShoppingCartItem;

import java.util.List;

/**
 * @author bielai
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

    /**
     * 分页查询购物车项
     *
     * @param pageParam    分页参数
     * @param queryWrapper 查询条件
     * @return 结果
     */
    Page<XxShopShoppingCartItemVO> pageVo(Page<XxShopShoppingCartItem> pageParam, LambdaQueryWrapper<XxShopShoppingCartItem> queryWrapper);

    /**
     * 添加商品到购物车
     *
     * @param saveCartItemParam 购物车信息
     * @param userId            用户id
     * @return 结果
     */
    boolean saveXxShopCartItem(SaveCartItemParam saveCartItemParam, Long userId);

    /**
     * 修改购物车
     *
     * @param updateCartItemParam 购物车信息
     * @param userId              用户id
     * @return 结果
     */
    boolean updateXxShopCartItem(UpdateCartItemParam updateCartItemParam, Long userId);

    /**
     * 立即购买商品
     *
     * @param saveCartItemParam 购买商品信息
     * @param userId            用户id
     * @return 结算项
     */
    List<XxShopShoppingCartItemVO> immediatelySettle(SaveCartItemParam saveCartItemParam, Long userId);
}
