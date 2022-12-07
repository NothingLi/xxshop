package top.bielai.shop.api.admin;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.bielai.shop.api.admin.param.AdminLoginParam;
import top.bielai.shop.api.admin.param.UpdateAdminNameParam;
import top.bielai.shop.api.admin.param.UpdateAdminPasswordParam;
import top.bielai.shop.common.Constants;
import top.bielai.shop.common.ErrorEnum;
import top.bielai.shop.common.XxShopException;
import top.bielai.shop.config.annotation.TokenToAdminUser;
import top.bielai.shop.domain.XxShopAdminUser;
import top.bielai.shop.service.XxShopAdminUserService;
import top.bielai.shop.util.Result;
import top.bielai.shop.util.ResultGenerator;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 后台管理系统管理员模块接口
 *
 * @author bielai
 */
@Valid
@Validated
@RestController
@RequestMapping("/manage-api/v2/adminUser")
public class XxShopAdminManageUserApi {

    @Resource
    private XxShopAdminUserService adminUserService;


    /**
     * 后台用户登录
     *
     * @param adminLoginParam 登陆参数
     * @return token
     */
    @PostMapping("/login")
    public Result<String> login(@Validated @RequestBody AdminLoginParam adminLoginParam) {
        String token = adminUserService.login(adminLoginParam.getUserName(), adminLoginParam.getPassword());

        //登录成功
        if (StringUtils.isNotBlank(token) && token.length() == Constants.TOKEN_LENGTH) {
            return ResultGenerator.genSuccessResult(token);
        }
        //登录失败
        return ResultGenerator.genFailResult();
    }

    /**
     * 获取当前登录用户的信息
     *
     * @return 用户信息
     */
    @GetMapping
    public Result<XxShopAdminUser> detail(@TokenToAdminUser XxShopAdminUser adminUser) {
        XxShopAdminUser byId = adminUserService.getById(adminUser.getAdminUserId());
        if (ObjectUtils.isNotEmpty(byId)) {
            byId.setLoginPassword("******");
            return ResultGenerator.genSuccessResult(byId);
        }
        throw new XxShopException(ErrorEnum.DATA_NOT_EXIST);
    }

    /**
     * 修改后台用户密码
     *
     * @param adminPasswordParam 用户信息
     * @return 结果
     */
    @PutMapping("/password")
    public Result<String> updatePassword(@Validated @RequestBody UpdateAdminPasswordParam adminPasswordParam, @TokenToAdminUser XxShopAdminUser adminUser) {
        if (adminUserService.updatePassword(adminUser.getAdminUserId(), adminPasswordParam)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult();
        }
    }

    /**
     * 修改登录名、昵称
     *
     * @param adminNameParam 用户名信息
     * @return 结果
     */
    @PutMapping("/name")
    public Result<String> updateName(@Validated @RequestBody UpdateAdminNameParam adminNameParam, @TokenToAdminUser XxShopAdminUser adminUser) {
        if (adminUserService.updateName(adminUser.getAdminUserId(), adminNameParam)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult();
        }
    }

    /**
     * 当前用户退出登录
     *
     * @return 结果
     */
    @DeleteMapping("/logout")
    public Result<String> logout(@TokenToAdminUser XxShopAdminUser adminUser) {
        if (adminUserService.logout(adminUser.getAdminUserId())) {
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult();
    }

}