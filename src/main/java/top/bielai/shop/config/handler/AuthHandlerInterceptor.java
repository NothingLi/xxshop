package top.bielai.shop.config.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import top.bielai.shop.common.Constants;
import top.bielai.shop.common.ErrorEnum;
import top.bielai.shop.common.XxShopException;
import top.bielai.shop.domain.XxShopUserToken;
import top.bielai.shop.mapper.XxShopUserTokenMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author bielai
 */
@Slf4j
@Component
public class AuthHandlerInterceptor implements HandlerInterceptor {

    @Autowired
    private XxShopUserTokenMapper userTokenMapper;
    private static final String TOKEN = "token";
    private static final String METHOD = "OPTIONS";


    private final String[] ignorePath = new String[]{};

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
        if (StringUtils.isBlank(token) || token.length() == Constants.TOKEN_LENGTH) {
            log.error("没有传递token");
            XxShopException.fail(ErrorEnum.NOT_LOGIN_ERROR);
        }
        XxShopUserToken userToken = userTokenMapper.selectOne(new LambdaQueryWrapper<XxShopUserToken>().eq(XxShopUserToken::getToken, token));
        if (ObjectUtils.isEmpty(userToken) || userToken.getExpireTime().getTime() <= System.currentTimeMillis()) {
            XxShopException.fail(ErrorEnum.TOKEN_EXPIRE_ERROR);
        }
        return true;
    }


}
