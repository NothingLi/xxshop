package top.bielai.shop.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author bielai
 */
public class NumberUtil {

    private NumberUtil() {
    }

    private static final Pattern PATTERN = Pattern.compile("^((13[0-9])|(14[5,7])|(15[^4,\\D])|(17[0-8])|(18[0-9]))\\d{8}$");


    /**
     * 判断是否为11位电话号码
     *
     * @param phone 手机号
     * @return 结果
     */
    public static boolean isPhone(String phone) {
        Matcher matcher = PATTERN.matcher(phone);
        return matcher.matches();
    }

    /**
     * 判断是否为11位电话号码
     *
     * @param phone 手机号
     * @return 结果
     */
    public static boolean isNotPhone(String phone) {
        return !isPhone(phone);
    }

    /**
     * 生成指定长度的随机数
     *
     * @param length 长度
     * @return 随机数
     */
    public static int genRandomNum(int length) {
        int num = 1;
        double random = Math.random();
        if (random < 0.1) {
            random = random + 0.1;
        }
        for (int i = 0; i < length; i++) {
            num = num * 10;
        }
        return (int) ((random * num));
    }

    /**
     * 生成订单流水号
     *
     * @return 流水号
     */
    public static String genOrderNo() {
        StringBuilder buffer = new StringBuilder(String.valueOf(System.currentTimeMillis()));
        int num = genRandomNum(4);
        buffer.append(num);
        return buffer.toString();
    }
}
