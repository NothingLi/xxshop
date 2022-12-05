/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本软件已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2021 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package top.bielai.shop.dao;

import org.apache.ibatis.annotations.Param;
import top.bielai.shop.entity.XxShopShoppingCartItem;

import java.util.List;

public interface XxShopShoppingCartItemMapper {
    int deleteByPrimaryKey(Long cartItemId);

    int insert(XxShopShoppingCartItem record);

    int insertSelective(XxShopShoppingCartItem record);

    XxShopShoppingCartItem selectByPrimaryKey(Long cartItemId);

    XxShopShoppingCartItem selectByUserIdAndGoodsId(@Param("xxShopUserId") Long xxShopUserId, @Param("goodsId") Long goodsId);

    List<XxShopShoppingCartItem> selectByUserId(@Param("xxShopUserId") Long xxShopUserId, @Param("number") int number);

    List<XxShopShoppingCartItem> selectByUserIdAndCartItemIds(@Param("xxShopUserId") Long xxShopUserId, @Param("cartItemIds") List<Long> cartItemIds);

    int selectCountByUserId(Long xxShopUserId);

    int updateByPrimaryKeySelective(XxShopShoppingCartItem record);

    int updateByPrimaryKey(XxShopShoppingCartItem record);

    int deleteBatch(@Param("ids") List<Long> ids, @Param("userId") Long userId);

    List<XxShopShoppingCartItem> findMyXxShopCartItems(PageQueryUtil pageUtil);

    int getTotalMyXxShopCartItems(PageQueryUtil pageUtil);
}