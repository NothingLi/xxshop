package top.bielai.shop.api.mall.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 用户修改param
 *
 * @author bielai
 */
@Data
public class ShopUserUpdateParam implements Serializable {


    /**
     * 用户昵称
     */
    @NotBlank(message = "您怎么称呼？")
    private String nickName;


    /**
     * 用户密码
     */
    private String password;


    /**
     * 个性签名
     */
    private String introduceSign;

}
