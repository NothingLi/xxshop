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
    ERROR_PARAM(400, "参数异常"),

    TOKEN_EXPIRE_ERROR(401, "你登陆过期了啊！请重新登录！"),

    USER_NULL_ERROR(401, "你是这的人么？请重新登录！"),
    ADMIN_NULL_ERROR(401, "你是这的人么？请重新登录！"),
    USER_NOT_NULL_ERROR(409, "注册过了啊，直接登陆吧！"),

    LOGIN_USER_LOCKED_ERROR(401, "你被禁止登陆咯！"),

    LOGIN_PHONE_ERROR(400, "要输入正确的手机号噢！"),

    NOT_LOGIN_ERROR(401, "你没登陆啊蒙面人！"),

    GOODS_PUT_DOWN(410, "商品已下架噢！"),

    IMG_TOO_MORE(410, "最多上传五张图片！"),

    GOODS_EXIST_ERROR(409, "这个分类下面有这个商品了！"),

    GOODS_NOT_EXIST_ERROR(404, "没有这个商品啊！"),

    USER_ADDRESS_DOWN(410, "这地址不对劲噢，你再看看！"),

    CART_ITEM_ERROR(410, "购物车商品有点问题噢！"),
    CART_ITEM_LIMIT_ERROR(410, "购物车放不下了噢！"),

    CART_ITEM_GOODS_NUM_ERROR(410, "库存不够辣！"),

    ORDER_STATUS_ERROR(410, "订单状态不允许这样操作噢"),

    CATEGORY_LEVEL_ERROR(410, "分类选到底了么"),

    DATA_NOT_EXIST(404, "没有这条数据！"),

    DATA_EXIST_ERROR(409, "已经有相同的数据了！"),

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
