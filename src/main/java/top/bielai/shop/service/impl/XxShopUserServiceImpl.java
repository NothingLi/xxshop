package top.bielai.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.bielai.shop.api.mall.param.ShopUserUpdateParam;
import top.bielai.shop.common.Constants;
import top.bielai.shop.common.ErrorEnum;
import top.bielai.shop.common.XxShopException;
import top.bielai.shop.domain.XxShopUser;
import top.bielai.shop.domain.XxShopUserToken;
import top.bielai.shop.mapper.XxShopUserMapper;
import top.bielai.shop.service.XxShopUserService;
import top.bielai.shop.service.XxShopUserTokenService;
import top.bielai.shop.util.MD5Util;
import top.bielai.shop.util.NumberUtil;
import top.bielai.shop.util.SystemUtil;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @author bielai
 * @description 针对表【tb_xx_shop_user】的数据库操作Service实现
 * @createDate 2022-11-30 13:58:39
 */
@Service
public class XxShopUserServiceImpl extends ServiceImpl<XxShopUserMapper, XxShopUser>
        implements XxShopUserService {

    @Autowired
    private XxShopUserTokenService userTokenService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String login(String loginName, String password) {
        String loginNameMd5 = MD5Util.md5Encode(loginName, StandardCharsets.UTF_8.name());
        String passwordMd5 = MD5Util.md5Encode(loginNameMd5 + password, StandardCharsets.UTF_8.name());
        XxShopUser xxShopUser = baseMapper.selectOne(new LambdaQueryWrapper<XxShopUser>().eq(XxShopUser::getLoginName, loginName)
                .eq(XxShopUser::getPasswordMd5, passwordMd5));
        if (ObjectUtils.isEmpty(xxShopUser)) {
            XxShopException.fail(ErrorEnum.USER_NULL_ERROR);
        }
        if (xxShopUser.getLockedFlag() == 1) {
            XxShopException.fail(ErrorEnum.LOGIN_USER_LOCKED_ERROR);
        }
        String newToken = getNewToken(System.currentTimeMillis() + "", xxShopUser.getUserId());
        XxShopUserToken byId = userTokenService.getById(xxShopUser.getUserId());
        //当前时间
        Date now = new Date();
        //过期时间
        Date expireTime = new Date(now.getTime() + 2 * 24 * 3600 * 1000);
        if (ObjectUtils.isEmpty(byId)) {
            XxShopUserToken token = new XxShopUserToken();
            token.setUserId(xxShopUser.getUserId());
            token.setToken(newToken);
            token.setUpdateTime(now);
            token.setExpireTime(expireTime);
            if (userTokenService.save(token)) {
                return newToken;
            } else {
                XxShopException.fail(ErrorEnum.ERROR);
            }
        }
        byId.setToken(newToken);
        byId.setUpdateTime(now);
        byId.setExpireTime(expireTime);
        if (userTokenService.updateById(byId)) {
            return newToken;
        }
        throw new XxShopException(ErrorEnum.ERROR);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean logout(Long userId) {
        return baseMapper.deleteById(userId) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean register(String loginName, String password) {
        XxShopUser xxShopUser = baseMapper.selectOne(new LambdaQueryWrapper<XxShopUser>().eq(XxShopUser::getLoginName, loginName));
        if (ObjectUtils.isNotEmpty(xxShopUser)) {
            XxShopException.fail(ErrorEnum.USER_NOT_NULL_ERROR);
        }
        XxShopUser newUser = new XxShopUser();
        newUser.setLoginName(loginName);
        newUser.setNickName(loginName);
        newUser.setIntroduceSign(Constants.USER_INTRO);
        newUser.setLockedFlag(0);
        String loginNameMd5 = MD5Util.md5Encode(loginName, StandardCharsets.UTF_8.name());
        String passwordMd5 = MD5Util.md5Encode(password, StandardCharsets.UTF_8.name());
        newUser.setPasswordMd5(MD5Util.md5Encode(loginNameMd5 + passwordMd5, StandardCharsets.UTF_8.name()));
        return baseMapper.insert(newUser) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserInfo(ShopUserUpdateParam userUpdateParam, Long userId) {
        XxShopUser xxShopUser = baseMapper.selectById(userId);
        if (ObjectUtils.isEmpty(xxShopUser)) {
            XxShopException.fail(ErrorEnum.USER_NULL_ERROR);
        }
        xxShopUser.setNickName(userUpdateParam.getNickName());
        if (StringUtils.isNotBlank(userUpdateParam.getPassword()) || !MD5Util.md5Encode("", StandardCharsets.UTF_8.name()).equals(userUpdateParam.getPassword())) {
            xxShopUser.setPasswordMd5(MD5Util.md5Encode(MD5Util.md5Encode(xxShopUser.getLoginName(), StandardCharsets.UTF_8.name()), StandardCharsets.UTF_8.name()) + userUpdateParam.getPassword());
        }
        xxShopUser.setIntroduceSign(userUpdateParam.getIntroduceSign());
        return baseMapper.updateById(xxShopUser) > 0;
    }

    private String getNewToken(String timeStr, Long userId) {
        String src = timeStr + userId + NumberUtil.genRandomNum(4);
        return SystemUtil.genToken(src);
    }
}




