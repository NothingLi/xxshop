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
import top.bielai.shop.config.annotation.TokenToAdminUser;
import top.bielai.shop.domain.XxShopAdminUserToken;
import top.bielai.shop.service.XxShopAdminUserTokenService;

/**
 * @author Administrator
 */
@Component
public class TokenToAdminUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private XxShopAdminUserTokenService adminUserTokenService;

    public TokenToAdminUserMethodArgumentResolver() {
    }

    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(TokenToAdminUser.class);
    }

    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        if (parameter.getParameterAnnotation(TokenToAdminUser.class) instanceof TokenToAdminUser) {
            String token = webRequest.getHeader("token");
            if (null != token && !"".equals(token) && token.length() == Constants.TOKEN_LENGTH) {
                XxShopAdminUserToken adminUserToken = adminUserTokenService.getOne(new LambdaQueryWrapper<XxShopAdminUserToken>()
                        .eq(XxShopAdminUserToken::getToken, token));
                if (adminUserToken == null) {
                    XxShopException.fail(ErrorEnum.ADMIN_NULL_ERROR);
                } else if (adminUserToken.getExpireTime().getTime() <= System.currentTimeMillis()) {
                    XxShopException.fail(ErrorEnum.TOKEN_EXPIRE_ERROR);
                }
                return adminUserToken;
            } else {
                XxShopException.fail(ErrorEnum.NOT_LOGIN_ERROR);
            }
        }
        return null;
    }

}
