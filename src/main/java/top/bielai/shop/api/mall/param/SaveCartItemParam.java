
package top.bielai.shop.api.mall.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 添加购物项param
 */
@Data
public class SaveCartItemParam implements Serializable {

    
    /**
     * 商品数量
     */
    private Integer goodsCount;

    
    /**
     * 商品id
     */
    private Long goodsId;
}
