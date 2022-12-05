package top.bielai.shop.api.mall.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单列表页面VO
 *
 * @author Administrator
 */
@Data
public class XxShopOrderListVO implements Serializable {

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
    private Date createTime;


    /**
     * 订单项列表
     */
    private List<XxShopOrderItemVO> xxShopOrderItemVOList;
}
