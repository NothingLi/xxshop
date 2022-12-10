package top.bielai.shop.config.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import top.bielai.shop.common.Constants;
import top.bielai.shop.common.ErrorEnum;
import top.bielai.shop.common.XxShopException;
import top.bielai.shop.config.annotation.TokenToShopUser;
import top.bielai.shop.domain.XxShopUser;
import top.bielai.shop.domain.XxShopUserToken;
import top.bielai.shop.mapper.XxShopUserMapper;
import top.bielai.shop.mapper.XxShopUserTokenMapper;

import java.time.LocalDateTime;

/**
 * @author bielai
 */
@Component
public class TokenToUserIdMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private XxShopUserMapper userMapper;
    @Autowired
    private XxShopUserTokenMapper userTokenMapper;

    public TokenToUserIdMethodArgumentResolver() {
    }

    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(TokenToShopUser.class);
    }

    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        if (parameter.getParameterAnnotation(TokenToShopUser.class) instanceof TokenToShopUser) {
            XxShopUser mallUser = null;
            String token = webRequest.getHeader("token");
            if (null != token && !"".equals(token) && token.length() == Constants.TOKEN_LENGTH) {
                XxShopUserToken userToken = userTokenMapper.selectOne(new LambdaQueryWrapper<XxShopUserToken>().eq(XxShopUserToken::getToken, token));
                if (userToken == null || userToken.getExpireTime().isBefore(LocalDateTime.now())) {
                    throw new XxShopException(ErrorEnum.TOKEN_EXPIRE_ERROR);
                }
                mallUser = userMapper.selectById(userToken.getUserId());
                if (mallUser == null) {
                    throw new XxShopException(ErrorEnum.USER_NULL_ERROR);
                }
                if (mallUser.getLockedFlag() == 1) {
                    throw new XxShopException(ErrorEnum.LOGIN_USER_LOCKED_ERROR);
                }
                return mallUser;
            } else {
                throw new XxShopException(ErrorEnum.NOT_LOGIN_ERROR);
            }
        }
        return null;
    }

}
