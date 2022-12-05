package top.bielai.shop.api.mall.param;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 保存订单param
 *
 * @author bielai
 */
@Data
public class SaveOrderParam implements Serializable {


    /**
     * 购物车项id数组
     */
    @NotNull(message = "你确定什么都不买下单么？")
    private Long[] cartItemIds;


    /**
     * 收货地址id
     */
    @NotNull(message = "收货地址必须选择噢，不然送到火星去~")
    private Long addressId;
}
