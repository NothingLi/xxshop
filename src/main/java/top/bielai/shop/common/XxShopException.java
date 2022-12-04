
package top.bielai.shop.common;

/**
 * @author bielai
 */
public class XxShopException extends RuntimeException {

    private int code;

    public XxShopException() {
    }

    public XxShopException(int code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 丢出一个异常
     *
     * @param error 异常
     */
    public static void fail(ErrorEnum error) {
        throw new XxShopException(error.getCode(), error.getMsg());
    }


    /**
     * 丢出一个异常
     *
     * @param message 信息
     */
    public static void fail(String message) {
        throw new XxShopException(500, message);
    }

    public int getCode() {
        return code;
    }
}
