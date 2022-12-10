package top.bielai.shop.api.mall.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品详情页VO
 *
 * @author bielai
 */
@Data
public class XxShopGoodsDetailVO implements Serializable {


    /**
     * 商品id
     */
    private Long goodsId;


    /**
     * 商品名称
     */
    private String goodsName;


    /**
     * 商品简介
     */
    private String goodsIntro;


    /**
     * 商品图片地址
     */
    private String goodsCoverImg;


    /**
     * 商品价格
     */
    private BigDecimal sellingPrice;


    /**
     * 商品标签
     */
    private String tag;


    /**
     * 商品图片
     */
    private String[] goodsCarouselList;


    /**
     * 商品原价
     */
    private BigDecimal originalPrice;


    /**
     * 商品详情字段
     */
    private String goodsDetailContent;

    /**
     * 商品库存
     */
    private Integer stockNum;
}
