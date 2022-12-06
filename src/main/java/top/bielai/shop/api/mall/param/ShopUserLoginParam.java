package top.bielai.shop.api.mall.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 用户登录param
 *
 * @author bielai
 */
@Data
public class ShopUserLoginParam implements Serializable {


    /**
     * 登录名
     */
    @NotBlank(message = "登录名不能为空")
    private String loginName;


    /**
     * 用户密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;
}
