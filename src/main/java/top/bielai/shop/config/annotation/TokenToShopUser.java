
package top.bielai.shop.config.annotation;

import java.lang.annotation.*;

/**
 * @author bielai
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TokenToShopUser {

    /**
     * 当前用户在request中的id
     *
     * @return id
     */
    String value() default "user";

}
