package top.bielai.shop.common;

import lombok.Getter;

/**
 * @author bielai
 */

@Getter
public enum ErrorEnum {

    /**
     * 异常响应的各种情况
     */
    ERROR_PARAM(400, "非法搜索参数"),

    TOKEN_EXPIRE_ERROR(401, "你钥匙拿错了吧！请重新登录！"),

    USER_NULL_ERROR(401, "你是这的人么？请重新登录！"),

    LOGIN_USER_LOCKED_ERROR(401, "你被禁止登陆咯！"),

    NOT_LOGIN_ERROR(401, "你没登陆啊蒙面人！"),

    GOODS_PUT_DOWN(410, "商品已下架噢！"),

    USER_ADDRESS_DOWN(410, "这地址不对劲噢，你再看看！"),

    CART_ITEM_ERROR(410, "购物车商品有点问题噢！"),

    CART_ITEM_GOODS_NUM_ERROR(410, "没这么多货呀！"),

    ORDER_STATUS_ERROR(410, "订单不能取消了哦"),

    DATA_NOT_EXIST(404, "没有你想要的东西噢！"),

    ORDER_NOT_EXIST(404, "订单号不对哦！"),

    PRICE_ERROR(500, "这价不对啊，我重新算算！"),
    ERROR(500, "我这网有点问题啊!");

    private final int code;

    private final String msg;

    ErrorEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
