package top.bielai.shop.config.annotation;

import java.lang.annotation.*;

/**
 * @author bielai
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TokenToShopUser {

    String value() default "user";

}
