
package top.bielai.shop.api.mall.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 修改购物项param
 */
@Data
public class UpdateCartItemParam implements Serializable {

    
    /**
     * 购物项id
     */
    private Long cartItemId;

    
    /**
     * 商品数量
     */
    private Integer goodsCount;
}
