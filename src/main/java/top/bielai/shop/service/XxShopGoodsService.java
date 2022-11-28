/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本软件已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2021 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package top.bielai.shop.service;

import top.bielai.shop.entity.XxShopGoods;
import top.bielai.shop.util.PageQueryUtil;
import top.bielai.shop.util.PageResult;

import java.util.List;

public interface XxShopGoodsService {
    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getXxShopGoodsPage(PageQueryUtil pageUtil);

    /**
     * 添加商品
     *
     * @param goods
     * @return
     */
    String saveXxShopGoods(XxShopGoods goods);

    /**
     * 批量新增商品数据
     *
     * @param xxShopGoodsList
     * @return
     */
    void batchSaveXxShopGoods(List<XxShopGoods> xxShopGoodsList);

    /**
     * 修改商品信息
     *
     * @param goods
     * @return
     */
    String updateXxShopGoods(XxShopGoods goods);

    /**
     * 批量修改销售状态(上架下架)
     *
     * @param ids
     * @return
     */
    Boolean batchUpdateSellStatus(Long[] ids, int sellStatus);

    /**
     * 获取商品详情
     *
     * @param id
     * @return
     */
    XxShopGoods getXxShopGoodsById(Long id);

    /**
     * 商品搜索
     *
     * @param pageUtil
     * @return
     */
    PageResult searchXxShopGoods(PageQueryUtil pageUtil);
}
