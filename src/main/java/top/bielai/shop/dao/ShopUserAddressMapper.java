/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本软件已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2021 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package top.bielai.shop.dao;

import top.bielai.shop.entity.ShopUserAddress;

import java.util.List;

public interface ShopUserAddressMapper {
    int deleteByPrimaryKey(Long addressId);

    int insert(ShopUserAddress record);

    int insertSelective(ShopUserAddress record);

    ShopUserAddress selectByPrimaryKey(Long addressId);

    ShopUserAddress getMyDefaultAddress(Long userId);

    List<ShopUserAddress> findMyAddressList(Long userId);

    int updateByPrimaryKeySelective(ShopUserAddress record);

    int updateByPrimaryKey(ShopUserAddress record);
}