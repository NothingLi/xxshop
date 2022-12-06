package top.bielai.shop.api.mall;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.bielai.shop.api.mall.param.ShopUserLoginParam;
import top.bielai.shop.api.mall.param.ShopUserRegisterParam;
import top.bielai.shop.api.mall.param.ShopUserUpdateParam;
import top.bielai.shop.api.mall.vo.XxShopUserVO;
import top.bielai.shop.common.ErrorEnum;
import top.bielai.shop.common.XxShopException;
import top.bielai.shop.config.annotation.TokenToShopUser;
import top.bielai.shop.service.XxShopUserService;
import top.bielai.shop.util.BeanUtil;
import top.bielai.shop.util.NumberUtil;
import top.bielai.shop.util.Result;
import top.bielai.shop.util.ResultGenerator;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 小新商城用户操作相关接口
 *
 * @author bielai
 */
@Valid
@RestController
@RequestMapping("/api/v2/user")
public class XxShopPersonalApi {

    @Resource
    private XxShopUserService xxShopUserService;


    /**
     * 用户登录
     *
     * @param userLoginParam 登录参数
     * @return token
     */
    @PostMapping("/login")
    public Result<String> login(@Validated @RequestBody ShopUserLoginParam userLoginParam) {
        if (NumberUtil.isNotPhone(userLoginParam.getLoginName())) {
            XxShopException.fail(ErrorEnum.LOGIN_PHONE_ERROR);
        }
        String token = xxShopUserService.login(userLoginParam.getLoginName(), userLoginParam.getPassword());


        //登录成功
        if (StringUtils.isNotBlank(token)) {
            return ResultGenerator.genSuccessResult("登陆成功", token);
        }
        //登录失败
        return ResultGenerator.genFailResult();
    }


    /**
     * 退出登录
     *
     * @param userId 用户id
     * @return 结果
     */
    @PostMapping("/logout")
    public Result<String> logout(Long userId) {
        Boolean logoutResult = xxShopUserService.logout(userId);
        //登出成功
        if (logoutResult) {
            return ResultGenerator.genSuccessResult();
        }
        //登出失败
        return ResultGenerator.genFailResult("logout error");
    }


    /**
     * 注册用户
     *
     * @param userRegisterParam 注册信息
     * @return 注册结果
     */
    @PostMapping("/register")
    public Result<String> register(@Validated @RequestBody ShopUserRegisterParam userRegisterParam) {
        if (NumberUtil.isNotPhone(userRegisterParam.getLoginName())) {
            XxShopException.fail(ErrorEnum.LOGIN_PHONE_ERROR);
        }
        boolean registerResult = xxShopUserService.register(userRegisterParam.getLoginName(), userRegisterParam.getPassword());
        //注册成功
        if (registerResult) {
            return ResultGenerator.genSuccessResult("注册成功！", null);
        }
        //注册失败
        return ResultGenerator.genFailResult();
    }

    /**
     * 修改用户信息
     *
     * @param userUpdateParam 用户信息
     * @return 结果
     */
    @PutMapping("/info")
    public Result<String> updateInfo(@Validated @RequestBody ShopUserUpdateParam userUpdateParam, @TokenToShopUser Long userId) {
        boolean flag = xxShopUserService.updateUserInfo(userUpdateParam, userId);
        if (flag) {
            return ResultGenerator.genSuccessResult();
        }
        //返回失败
        return ResultGenerator.genFailResult("修改失败");

    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/info")
    public Result<XxShopUserVO> getUserDetail(@TokenToShopUser Long userId) {
        XxShopUserVO mallUserVO = new XxShopUserVO();
        BeanUtil.copyProperties(xxShopUserService.getById(userId), mallUserVO);
        return ResultGenerator.genSuccessResult(mallUserVO);
    }
}
