
package top.bielai.shop.api.mall.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 购物车页面购物项VO
 */
@Data
public class XxShopShoppingCartItemVO implements Serializable {

    
    /**
     * 购物项id
     */
    private Long cartItemId;

    
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
