package top.bielai.shop.common;


/**
 * @author bielai
 */

public enum PayTypeEnum {

    /**
     * 支付方式
     */
    DEFAULT((byte) -1, "ERROR"),
    NOT_PAY((byte) 0, "无"),
    ALI_PAY((byte) 1, "支付宝"),
    WECHAT_PAY((byte) 2, "微信支付");

    private final byte payType;

    private final String name;

    PayTypeEnum(byte payType, String name) {
        this.payType = payType;
        this.name = name;
    }

    public static PayTypeEnum getPayTypeEnumByType(byte payType) {
        for (PayTypeEnum payTypeEnum : PayTypeEnum.values()) {
            if (payTypeEnum.getPayType() == payType) {
                return payTypeEnum;
            }
        }
        return DEFAULT;
    }

    public byte getPayType() {
        return payType;
    }


    public String getName() {
        return name;
    }

}
