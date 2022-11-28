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
import top.bielai.shop.entity.XxShopOrder;
import top.bielai.shop.util.PageQueryUtil;

import java.util.List;

public interface XxShopOrderMapper {
    int deleteByPrimaryKey(Long orderId);

    int insert(XxShopOrder record);

    int insertSelective(XxShopOrder record);

    XxShopOrder selectByPrimaryKey(Long orderId);

    XxShopOrder selectByOrderNo(String orderNo);

    int updateByPrimaryKeySelective(XxShopOrder record);

    int updateByPrimaryKey(XxShopOrder record);

    List<XxShopOrder> findXxShopOrderList(PageQueryUtil pageUtil);

    int getTotalXxShopOrders(PageQueryUtil pageUtil);

    List<XxShopOrder> selectByPrimaryKeys(@Param("orderIds") List<Long> orderIds);

    int checkOut(@Param("orderIds") List<Long> orderIds);

    int closeOrder(@Param("orderIds") List<Long> orderIds, @Param("orderStatus") int orderStatus);

    int checkDone(@Param("orderIds") List<Long> asList);
}