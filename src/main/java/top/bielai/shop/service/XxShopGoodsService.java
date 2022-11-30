
package top.bielai.shop.service;

import top.bielai.shop.domain.XxShopGoodsInfo;
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
    String saveXxShopGoods(XxShopGoodsInfo goods);

    /**
     * 批量新增商品数据
     *
     * @param xxShopGoodsList
     * @return
     */
    void batchSaveXxShopGoods(List<XxShopGoodsInfo> xxShopGoodsList);

    /**
     * 修改商品信息
     *
     * @param goods
     * @return
     */
    String updateXxShopGoods(XxShopGoodsInfo goods);

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
    XxShopGoodsInfo getXxShopGoodsById(Long id);

    /**
     * 商品搜索
     *
     * @param pageUtil
     * @return
     */
    PageResult searchXxShopGoods(PageQueryUtil pageUtil);
}
