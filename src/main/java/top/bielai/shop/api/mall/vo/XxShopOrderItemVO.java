
package top.bielai.shop.api.mall.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 订单详情页页面订单项VO
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
    private Integer sellingPrice;
}
