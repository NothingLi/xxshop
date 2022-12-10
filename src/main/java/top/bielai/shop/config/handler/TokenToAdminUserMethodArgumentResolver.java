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
import top.bielai.shop.domain.XxShopAdminUser;
import top.bielai.shop.domain.XxShopAdminUserToken;
import top.bielai.shop.service.XxShopAdminUserService;
import top.bielai.shop.service.XxShopAdminUserTokenService;

import java.time.LocalDateTime;

/**
 * @author bielai
 */
@Component
public class TokenToAdminUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private XxShopAdminUserService adminUserService;


    private final XxShopAdminUserTokenService adminUserTokenService;

    public TokenToAdminUserMethodArgumentResolver(XxShopAdminUserTokenService adminUserTokenService) {
        this.adminUserTokenService = adminUserTokenService;
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
                    throw new XxShopException(ErrorEnum.ADMIN_NULL_ERROR);
                } else if (adminUserToken.getExpireTime().isBefore(LocalDateTime.now())) {
                    throw new XxShopException(ErrorEnum.TOKEN_EXPIRE_ERROR);
                }
                XxShopAdminUser byId = adminUserService.getById(adminUserToken.getAdminUserId());
                if (byId == null) {
                    throw new XxShopException(ErrorEnum.ADMIN_NULL_ERROR);
                }
                if (byId.getLocked() == 1) {
                    throw new XxShopException(ErrorEnum.LOGIN_USER_LOCKED_ERROR);
                }
                return byId;
            } else {
                throw new XxShopException(ErrorEnum.NOT_LOGIN_ERROR);
            }
        }
        return null;
    }

}
