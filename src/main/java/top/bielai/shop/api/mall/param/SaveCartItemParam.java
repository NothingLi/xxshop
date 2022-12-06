package top.bielai.shop.api.mall.param;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 添加购物项param
 *
 * @author bielai
 */
@Data
public class SaveCartItemParam implements Serializable {


    /**
     * 商品数量
     */
    @NotNull(message = "商品数量不能为空噢！")
    @Max(value = 100, message = "添加太多了，买不动呀，少点吧！")
    private Integer goodsCount;


    /**
     * 商品id
     */
    @NotNull(message = "到底要买哪个商品呢？")
    private Long goodsId;
}
