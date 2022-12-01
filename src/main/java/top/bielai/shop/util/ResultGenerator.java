package top.bielai.shop.util;


/**
 * @author Administrator
 */
public class ResultGenerator {
    private static final String DEFAULT_SUCCESS_MESSAGE = "成功";
    private static final String DEFAULT_FAIL_MESSAGE = "失败";
    private static final int RESULT_CODE_SUCCESS = 200;
    private static final int RESULT_CODE_SERVER_ERROR = 500;

    public static Result<String> genSuccessResult() {
        Result<String> result = new Result<>();
        result.setResultCode(RESULT_CODE_SUCCESS);
        result.setMessage(DEFAULT_SUCCESS_MESSAGE);
        return result;
    }


    public static <T> Result<T> genSuccessResult(T data) {
        Result<T> result = new Result<>();
        result.setResultCode(RESULT_CODE_SUCCESS);
        result.setMessage(DEFAULT_SUCCESS_MESSAGE);
        result.setData(data);
        return result;
    }

    public static <T> Result<T> genSuccessResult(String message, T data) {
        Result<T> result = new Result<>();
        result.setResultCode(RESULT_CODE_SUCCESS);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static Result<String> genFailResult() {
        Result<String> result = new Result<>();
        result.setResultCode(RESULT_CODE_SERVER_ERROR);
        result.setMessage(DEFAULT_FAIL_MESSAGE);
        return result;
    }

    public static Result<String> genFailResult(String message) {
        Result<String> result = new Result<>();
        result.setResultCode(RESULT_CODE_SERVER_ERROR);
        result.setMessage(message);
        return result;
    }

    public static Result<String> genErrorResult(int code, String message) {
        Result<String> result = new Result<>();
        result.setResultCode(code);
        result.setMessage(message);
        return result;
    }
}
