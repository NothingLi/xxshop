
package top.bielai.shop.common;


/**
 * @author JiangYouYuan
 */

public enum PayStatusEnum {

    /**
     * 支付状态
     */
    DEFAULT(-1, "支付失败"),
    WAIT_PAY(0, "未支付"),
    PAY_SUCCESS(1, "支付成功");

    private final int payStatus;

    private final String name;

    PayStatusEnum(int payStatus, String name) {
        this.payStatus = payStatus;
        this.name = name;
    }

    public static PayStatusEnum getPayStatusEnumByStatus(int payStatus) {
        for (PayStatusEnum payStatusEnum : PayStatusEnum.values()) {
            if (payStatusEnum.getPayStatus() == payStatus) {
                return payStatusEnum;
            }
        }
        return DEFAULT;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public String getName() {
        return name;
    }
}
