package top.bielai.shop.api.mall.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 搜索列表页商品VO
 *
 * @author Administrator
 */
@Data
public class XxShopSearchGoodsVO implements Serializable {


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
    private Integer sellingPrice;

}
