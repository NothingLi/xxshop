package top.bielai.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.bielai.shop.api.admin.param.UpdateAdminNameParam;
import top.bielai.shop.api.admin.param.UpdateAdminPasswordParam;
import top.bielai.shop.common.Constants;
import top.bielai.shop.common.ErrorEnum;
import top.bielai.shop.common.XxShopException;
import top.bielai.shop.domain.XxShopAdminUser;
import top.bielai.shop.domain.XxShopAdminUserToken;
import top.bielai.shop.mapper.XxShopAdminUserMapper;
import top.bielai.shop.mapper.XxShopAdminUserTokenMapper;
import top.bielai.shop.service.XxShopAdminUserService;
import top.bielai.shop.util.NumberUtil;
import top.bielai.shop.util.SystemUtil;

import java.time.LocalDateTime;

/**
 * @author bielai
 * @description 针对表【tb_xx_shop_admin_user】的数据库操作Service实现
 * @createDate 2022-11-30 13:58:39
 */
@Service
public class XxShopAdminUserServiceImpl extends ServiceImpl<XxShopAdminUserMapper, XxShopAdminUser>
        implements XxShopAdminUserService {

    private final XxShopAdminUserTokenMapper adminUserTokenMapper;

    public XxShopAdminUserServiceImpl(XxShopAdminUserTokenMapper adminUserTokenMapper) {
        this.adminUserTokenMapper = adminUserTokenMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String login(String username, String password) {
        XxShopAdminUser xxShopAdminUser = baseMapper.selectOne(new LambdaQueryWrapper<XxShopAdminUser>()
                .eq(XxShopAdminUser::getLoginUserName, username)
                .eq(XxShopAdminUser::getLoginPassword, DigestUtils.md5Hex(password + Constants.SALT)));
        if (ObjectUtils.isNotEmpty(xxShopAdminUser)) {
            //登录后即执行修改token的操作
            String token = getNewToken(System.currentTimeMillis() + "", xxShopAdminUser.getAdminUserId());
            XxShopAdminUserToken adminUserToken = adminUserTokenMapper.selectById(xxShopAdminUser.getAdminUserId());
            //过期时间 48 小时
            LocalDateTime expireTime = LocalDateTime.now().plusDays(2);
            if (adminUserToken == null) {
                adminUserToken = new XxShopAdminUserToken();
                adminUserToken.setAdminUserId(xxShopAdminUser.getAdminUserId());
                adminUserToken.setToken(token);
                adminUserToken.setExpireTime(expireTime);
                //新增一条token数据
                if (adminUserTokenMapper.insert(adminUserToken) > 0) {
                    //新增成功后返回
                    return token;
                }
            } else {
                adminUserToken.setToken(token);
                adminUserToken.setExpireTime(expireTime);
                adminUserToken.setUpdateTime(LocalDateTime.now());
                //更新
                if (adminUserTokenMapper.updateById(adminUserToken) > 0) {
                    //修改成功后返回
                    return token;
                }
            }
        }
        throw new XxShopException(ErrorEnum.USER_NULL_ERROR);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePassword(Long adminUserId, UpdateAdminPasswordParam adminPasswordParam) {
        XxShopAdminUser xxShopAdminUser = baseMapper.selectById(adminUserId);
        if (ObjectUtils.isEmpty(xxShopAdminUser)) {
            throw new XxShopException(ErrorEnum.DATA_NOT_EXIST);
        }
        if (xxShopAdminUser.getLoginPassword().equals(DigestUtils.md5Hex(adminPasswordParam.getOriginalPassword() + Constants.SALT))) {
            xxShopAdminUser.setLoginPassword(DigestUtils.md5Hex(adminPasswordParam.getNewPassword() + Constants.SALT));
            return baseMapper.updateById(xxShopAdminUser) > 0 && adminUserTokenMapper.deleteById(xxShopAdminUser.getAdminUserId()) > 0;
        }
        return false;
    }

    @Override
    public boolean updateName(Long adminUserId, UpdateAdminNameParam adminNameParam) {
        XxShopAdminUser xxShopAdminUser = baseMapper.selectById(adminUserId);
        if (ObjectUtils.isEmpty(xxShopAdminUser)) {
            throw new XxShopException(ErrorEnum.DATA_NOT_EXIST);
        }
        xxShopAdminUser.setLoginUserName(adminNameParam.getLoginUserName());
        xxShopAdminUser.setNickName(adminNameParam.getNickName());
        return baseMapper.updateById(xxShopAdminUser) > 0;
    }

    @Override
    public boolean logout(Long adminUserId) {
        return adminUserTokenMapper.deleteById(adminUserId) > 0;
    }

    private String getNewToken(String timeStr, Long userId) {
        String src = timeStr + userId + NumberUtil.genRandomNum(4);
        return SystemUtil.genToken(src);
    }
}




