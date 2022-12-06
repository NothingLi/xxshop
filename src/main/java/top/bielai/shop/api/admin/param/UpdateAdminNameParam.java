package top.bielai.shop.api.admin.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author bielai
 */
@Data
public class UpdateAdminNameParam {

    @NotEmpty(message = "loginUserName不能为空")
    private String loginUserName;

    @NotEmpty(message = "nickName不能为空")
    private String nickName;
}
