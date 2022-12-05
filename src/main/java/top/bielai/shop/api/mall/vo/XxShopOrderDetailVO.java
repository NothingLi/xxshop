package top.bielai.shop.api.mall.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单详情页页面VO
 *
 * @author Administrator
 */
@Data
public class XxShopOrderDetailVO implements Serializable {


    /**
     * 订单号
     */
    private String orderNo;


    /**
     * 订单价格
     */
    private BigDecimal totalPrice;


    /**
     * 订单支付状态码
     */
    private Byte payStatus;

    /**
     * 订单支付状态
     */
    private String payStatusString;


    /**
     * 订单支付方式
     */
    private Byte payType;


    /**
     * 订单支付方式
     */
    private String payTypeString;


    /**
     * 订单支付时间
     */
    private Date payTime;


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


    /**
     * 收货地址
     */
    private XxShopUserAddressVO xxShopUserAddressVO;
}
