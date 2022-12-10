package top.bielai.shop.api.mall.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单列表页面VO
 *
 * @author bielai
 */
@Data
public class XxShopOrderListVO implements Serializable {

    /**
     * 订单id
     */
    private Long orderId;


    /**
     * 订单号
     */
    private String orderNo;


    /**
     * 订单价格
     */
    private BigDecimal totalPrice;


    /**
     * 订单支付方式
     */
    private Byte payType;


    /**
     * 订单状态码
     */
    private Byte orderStatus;


    /**
     * 订单状态
     */
    private String orderStatusString;


    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;


    /**
     * 订单项列表
     */
    private List<XxShopOrderItemVO> xxShopOrderItemVOList;
}
