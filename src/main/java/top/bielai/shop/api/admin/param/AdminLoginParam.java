package top.bielai.shop.api.admin.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author bielai
 */
@Data
public class AdminLoginParam implements Serializable {


    /**
     * 登录名
     */
    @NotEmpty(message = "登录名不能为空")
    private String username;


    /**
     * 用户密码
     */
    @NotEmpty(message = "密码不能为空")
    private String password;
}
