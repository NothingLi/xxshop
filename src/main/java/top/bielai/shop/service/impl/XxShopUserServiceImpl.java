package top.bielai.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
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
import top.bielai.shop.util.NumberUtil;
import top.bielai.shop.util.SystemUtil;

import java.time.LocalDateTime;

/**
 * @author bielai
 * @description 针对表【tb_xx_shop_user】的数据库操作Service实现
 * @createDate 2022-11-30 13:58:39
 */
@Service
public class XxShopUserServiceImpl extends ServiceImpl<XxShopUserMapper, XxShopUser>
        implements XxShopUserService {

    private final XxShopUserTokenService userTokenService;

    public XxShopUserServiceImpl(XxShopUserTokenService userTokenService) {
        this.userTokenService = userTokenService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String login(String loginName, String password) {
        XxShopUser xxShopUser = baseMapper.selectOne(new LambdaQueryWrapper<XxShopUser>().eq(XxShopUser::getLoginName, loginName)
                .eq(XxShopUser::getPasswordMd5, DigestUtils.md5Hex(password + Constants.SALT)));
        if (ObjectUtils.isEmpty(xxShopUser)) {
            throw new XxShopException(ErrorEnum.USER_NULL_ERROR);
        }
        if (xxShopUser.getLockedFlag() == 1) {
            throw new XxShopException(ErrorEnum.LOGIN_USER_LOCKED_ERROR);
        }
        String newToken = getNewToken(System.currentTimeMillis() + "", xxShopUser.getUserId());
        XxShopUserToken byId = userTokenService.getById(xxShopUser.getUserId());
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireTime = now.plusDays(2);
        if (ObjectUtils.isEmpty(byId)) {
            XxShopUserToken token = new XxShopUserToken();
            token.setUserId(xxShopUser.getUserId());
            token.setToken(newToken);
            token.setUpdateTime(now);
            token.setExpireTime(expireTime);
            if (userTokenService.save(token)) {
                return newToken;
            } else {
                throw new XxShopException(ErrorEnum.ERROR);
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
            throw new XxShopException(ErrorEnum.USER_NOT_NULL_ERROR);
        }
        XxShopUser newUser = new XxShopUser();
        newUser.setLoginName(loginName);
        newUser.setNickName(loginName);
        newUser.setIntroduceSign(Constants.USER_INTRO);
        newUser.setLockedFlag(0);
        newUser.setPasswordMd5(DigestUtils.md5Hex(password + Constants.SALT));
        return baseMapper.insert(newUser) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserInfo(ShopUserUpdateParam userUpdateParam, Long userId) {
        XxShopUser xxShopUser = baseMapper.selectById(userId);
        if (ObjectUtils.isEmpty(xxShopUser)) {
            throw new XxShopException(ErrorEnum.USER_NULL_ERROR);
        }
        xxShopUser.setNickName(userUpdateParam.getNickName());
        if (StringUtils.isNotBlank(userUpdateParam.getPassword()) || !DigestUtils.md5Hex("").equals(userUpdateParam.getPassword())) {

            xxShopUser.setPasswordMd5(DigestUtils.md5Hex(userUpdateParam.getPassword() + Constants.SALT));
        }
        xxShopUser.setIntroduceSign(userUpdateParam.getIntroduceSign());
        return baseMapper.updateById(xxShopUser) > 0;
    }

    private String getNewToken(String timeStr, Long userId) {
        String src = timeStr + userId + NumberUtil.genRandomNum(4);
        return SystemUtil.genToken(src);
    }
}




