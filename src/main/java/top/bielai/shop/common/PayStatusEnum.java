package top.bielai.shop.common;


/**
 * @author bielai
 */

public enum PayStatusEnum {

    /**
     * 支付状态
     */
    DEFAULT((byte) -1, "支付失败"),
    WAIT_PAY((byte) 0, "未支付"),
    PAY_SUCCESS((byte) 1, "支付成功");

    private final byte payStatus;

    private final String name;

    PayStatusEnum(byte payStatus, String name) {
        this.payStatus = payStatus;
        this.name = name;
    }

    public static PayStatusEnum getPayStatusEnumByStatus(byte payStatus) {
        for (PayStatusEnum payStatusEnum : PayStatusEnum.values()) {
            if (payStatusEnum.getPayStatus() == payStatus) {
                return payStatusEnum;
            }
        }
        return DEFAULT;
    }

    public byte getPayStatus() {
        return payStatus;
    }

    public String getName() {
        return name;
    }
}
