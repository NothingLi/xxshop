package top.bielai.shop.api.mall.param;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 修改购物项param
 *
 * @author Administrator
 */
@Data
public class UpdateCartItemParam implements Serializable {


    /**
     * 购物项id
     */
    @NotNull(message = "要修改哪个商品呢？")
    private Long cartItemId;


    /**
     * 商品数量
     */
    @NotNull(message = "要改成多少个呢？")
    private Integer goodsCount;
}
