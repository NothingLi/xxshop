package top.bielai.shop.common;


/**
 * @author bielai
 */

public enum OrderStatusEnum {

    /**
     * 订单状态
     */
    DEFAULT((byte) -9, "ERROR"),
    ORDER_WAIT_PAY((byte) 0, "待支付"),
    ORDER_PAID((byte) 1, "已支付"),
    ORDER_PACKAGED((byte) 2, "配货完成"),
    ORDER_EXPRESS((byte) 3, "出库成功"),
    ORDER_SUCCESS((byte) 4, "交易成功"),
    ORDER_CLOSED_BY_USER((byte) -1, "手动关闭"),
    ORDER_CLOSED_BY_EXPIRED((byte) -2, "超时关闭"),
    ORDER_CLOSED_BY_JUDGE((byte) -3, "商家关闭");

    private final byte orderStatus;

    private final String name;

    OrderStatusEnum(byte orderStatus, String name) {
        this.orderStatus = orderStatus;
        this.name = name;
    }

    public static OrderStatusEnum getXxShopOrderStatusEnumByStatus(byte orderStatus) {
        for (OrderStatusEnum orderStatusEnum : OrderStatusEnum.values()) {
            if (orderStatusEnum.getOrderStatus() == orderStatus) {
                return orderStatusEnum;
            }
        }
        return DEFAULT;
    }

    public byte getOrderStatus() {
        return orderStatus;
    }

    public String getName() {
        return name;
    }
}
