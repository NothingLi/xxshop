package top.bielai.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.bielai.shop.api.mall.param.ShopUserUpdateParam;
import top.bielai.shop.domain.XxShopUser;

/**
 * @author bielai
 * @description 针对表【tb_xx_shop_user】的数据库操作Service
 * @createDate 2022-11-30 13:58:39
 */
public interface XxShopUserService extends IService<XxShopUser> {

    /**
     * 登录
     *
     * @param loginName   登录名
     * @param passwordMd5 密码md5
     * @return token
     */
    String login(String loginName, String passwordMd5);

    /**
     * 登出
     *
     * @param userId 用户id
     * @return 结果
     */
    Boolean logout(Long userId);

    /**
     * 注册
     *
     * @param loginName 用户名
     * @param password  密码
     * @return 结果
     */
    boolean register(String loginName, String password);

    /**
     * 更新用户信息
     *
     * @param userUpdateParam 用户信息
     * @param userId          用户id
     * @return 结果
     */
    boolean updateUserInfo(ShopUserUpdateParam userUpdateParam, Long userId);
}
