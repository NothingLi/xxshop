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
import top.bielai.shop.entity.StockNumDTO;
import top.bielai.shop.entity.XxShopGoods;

import java.util.List;

public interface XxShopGoodsMapper {
    int deleteByPrimaryKey(Long goodsId);

    int insert(XxShopGoods record);

    int insertSelective(XxShopGoods record);

    XxShopGoods selectByPrimaryKey(Long goodsId);

    XxShopGoods selectByCategoryIdAndName(@Param("goodsName") String goodsName, @Param("goodsCategoryId") Long goodsCategoryId);

    int updateByPrimaryKeySelective(XxShopGoods record);

    int updateByPrimaryKeyWithBLOBs(XxShopGoods record);

    int updateByPrimaryKey(XxShopGoods record);

    List<XxShopGoods> findXxShopGoodsList(PageQueryUtil pageUtil);

    int getTotalXxShopGoods(PageQueryUtil pageUtil);

    List<XxShopGoods> selectByPrimaryKeys(List<Long> goodsIds);

    List<XxShopGoods> findXxShopGoodsListBySearch(PageQueryUtil pageUtil);

    int getTotalXxShopGoodsBySearch(PageQueryUtil pageUtil);

    int batchInsert(@Param("xxShopGoodsList") List<XxShopGoods> xxShopGoodsList);

    int updateStockNum(@Param("stockNumDTOS") List<StockNumDTO> stockNumDTOS);

    int recoverStockNum(@Param("stockNumDTOS") List<StockNumDTO> stockNumDTOS);

    int batchUpdateSellStatus(@Param("orderIds")Long[] orderIds,@Param("sellStatus") int sellStatus);

}