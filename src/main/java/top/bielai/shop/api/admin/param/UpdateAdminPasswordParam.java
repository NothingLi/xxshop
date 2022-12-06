package top.bielai.shop.api.admin.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author bielai
 */
@Data
public class UpdateAdminPasswordParam {

    @NotEmpty(message = "originalPassword不能为空")
    private String originalPassword;

    @NotEmpty(message = "newPassword不能为空")
    private String newPassword;
}
