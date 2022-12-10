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

    public XxShopException(ErrorEnum error) {
        super(error.getMsg());
        this.code = error.getCode();
    }

    public XxShopException(String message) {
        super(message);
        this.code = 500;
    }

    public int getCode() {
        return code;
    }
}
