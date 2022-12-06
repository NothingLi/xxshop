package top.bielai.shop.config.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import top.bielai.shop.common.Constants;
import top.bielai.shop.common.ErrorEnum;
import top.bielai.shop.common.XxShopException;
import top.bielai.shop.domain.XxShopAdminUserToken;
import top.bielai.shop.domain.XxShopUserToken;
import top.bielai.shop.service.XxShopAdminUserTokenService;
import top.bielai.shop.service.XxShopUserTokenService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author bielai
 */
@Slf4j
@Component
public class AuthHandlerInterceptor implements HandlerInterceptor {

    private final XxShopUserTokenService userTokenService;

    private final XxShopAdminUserTokenService adminUserTokenService;
    private static final String TOKEN = "token";
    private static final String METHOD = "OPTIONS";


    private final String[] ignorePath = new String[]{
            "/api/v2/index-infos", "/api/v2/categories",
            "/user/login", "/user/logout", "/user/register",
            "adminUser/login", "/adminUser/logout"};

    public AuthHandlerInterceptor(XxShopUserTokenService userTokenService, XxShopAdminUserTokenService adminUserTokenService) {
        this.userTokenService = userTokenService;
        this.adminUserTokenService = adminUserTokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (METHOD.equals(request.getMethod())) {
            return true;
        }
        for (String s : ignorePath) {
            if (request.getRequestURI().contains(s)) {
                return true;
            }
        }
        String token = request.getHeader(TOKEN);
        if (StringUtils.isBlank(token) || token.length() != Constants.TOKEN_LENGTH) {
            log.error("没有传递token");
            XxShopException.fail(ErrorEnum.NOT_LOGIN_ERROR);
        }
        XxShopUserToken userToken = userTokenService.getOne(new LambdaQueryWrapper<XxShopUserToken>().eq(XxShopUserToken::getToken, token));
        XxShopAdminUserToken adminUserToken = adminUserTokenService.getOne(new LambdaQueryWrapper<XxShopAdminUserToken>()
                .eq(XxShopAdminUserToken::getToken, token));
        if (tokenAccess(userToken, adminUserToken)) {
            XxShopException.fail(ErrorEnum.TOKEN_EXPIRE_ERROR);
        }
        return true;
    }

    private boolean tokenAccess(XxShopUserToken userToken, XxShopAdminUserToken adminUserToken) {
        return (ObjectUtils.isNotEmpty(userToken) && userToken.getExpireTime().getTime() <= System.currentTimeMillis()) ||
                (ObjectUtils.isNotEmpty(adminUserToken) && adminUserToken.getExpireTime().getTime() <= System.currentTimeMillis());
    }


}
