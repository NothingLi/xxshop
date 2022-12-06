package top.bielai.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.bielai.shop.api.admin.param.UpdateAdminNameParam;
import top.bielai.shop.api.admin.param.UpdateAdminPasswordParam;
import top.bielai.shop.domain.XxShopAdminUser;

/**
 * @author bielai
 * @description 针对表【tb_xx_shop_admin_user】的数据库操作Service
 * @createDate 2022-11-30 13:58:39
 */
public interface XxShopAdminUserService extends IService<XxShopAdminUser> {

    /**
     * 管理员登陆
     *
     * @param username 用户名
     * @param password 密码
     * @return token
     */
    String login(String username, String password);

    /**
     * 修改后台用户密码
     *
     * @param adminUserId        用户id
     * @param adminPasswordParam 修改密码
     * @return 结果
     */
    boolean updatePassword(Long adminUserId, UpdateAdminPasswordParam adminPasswordParam);

    /**
     * 修改用户名
     *
     * @param adminUserId    用户id
     * @param adminNameParam 用户名信息
     * @return 结果
     */
    boolean updateName(Long adminUserId, UpdateAdminNameParam adminNameParam);

    /**
     * 退出登录
     *
     * @param adminUserId 用户id
     * @return 结果
     */
    boolean logout(Long adminUserId);
}
