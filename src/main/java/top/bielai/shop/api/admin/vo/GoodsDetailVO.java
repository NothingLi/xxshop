package top.bielai.shop.api.admin.vo;

import lombok.Data;
import top.bielai.shop.domain.XxShopGoodsCategory;
import top.bielai.shop.domain.XxShopGoodsInfo;

/**
 * 后台管理商品详情VO
 *
 * @author Administrator
 */
@Data
public class GoodsDetailVO {
    /**
     * 商品信息
     */
    private XxShopGoodsInfo goodsInfo;

    /**
     * 三级分类信息
     */
    private XxShopGoodsCategory thirdCategory;

    /**
     * 二级分类信息
     */
    private XxShopGoodsCategory secondCategory;

    /**
     * 一级分类信息
     */
    private XxShopGoodsCategory firstCategory;
}
