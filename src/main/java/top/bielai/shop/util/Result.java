package top.bielai.shop.util;

import lombok.Data;

import java.io.Serializable;

/**
 * @author bielai
 */
@Data
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 返回码
     */
    private int resultCode;
    /**
     * 返回信息
     */
    private String message;
    /**
     * 返回数据
     */
    private T data;

    public Result() {
    }

    public Result(int resultCode, String message) {
        this.resultCode = resultCode;
        this.message = message;
    }
}
