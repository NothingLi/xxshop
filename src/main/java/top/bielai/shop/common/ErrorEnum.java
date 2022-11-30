package top.bielai.shop.common;

import lombok.Getter;

/**
 * @author Administrator
 */

@Getter
public enum ErrorEnum {

    /**
     * 参数有误
     */
    ERROR_PARAM(400,"非法搜索参数"),

    GOODS_PUT_DOWN(410,"商品已下架！");

    private final int code;

    private final String msg;

    ErrorEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
