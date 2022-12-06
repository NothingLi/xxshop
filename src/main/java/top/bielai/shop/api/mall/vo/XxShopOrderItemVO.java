package top.bielai.shop.api.mall.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单详情页页面订单项VO
 *
 * @author bielai
 */
@Data
public class XxShopOrderItemVO implements Serializable {


    /**
     * 商品id
     */
    private Long goodsId;


    /**
     * 商品数量
     */
    private Integer goodsCount;


    /**
     * 商品名称
     */
    private String goodsName;


    /**
     * 商品图片
     */
    private String goodsCoverImg;


    /**
     * 商品价格
     */
    private BigDecimal sellingPrice;
}
