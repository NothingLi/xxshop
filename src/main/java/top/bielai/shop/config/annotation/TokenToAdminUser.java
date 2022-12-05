package top.bielai.shop.config.annotation;

import java.lang.annotation.*;

/**
 * @author Administrator
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TokenToAdminUser {


    String value() default "adminUser";

}
