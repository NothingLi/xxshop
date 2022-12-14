package top.bielai.shop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import top.bielai.shop.common.Constants;
import top.bielai.shop.config.handler.AuthHandlerInterceptor;
import top.bielai.shop.config.handler.TokenToAdminUserMethodArgumentResolver;
import top.bielai.shop.config.handler.TokenToUserIdMethodArgumentResolver;

import java.util.List;

/**
 * @author bielai
 */
@Configuration
public class XxShopWebMvcConfigurer extends WebMvcConfigurationSupport {

    @Autowired
    private TokenToUserIdMethodArgumentResolver tokenToUserIdMethodArgumentResolver;

    @Autowired
    private TokenToAdminUserMethodArgumentResolver tokenToAdminUserMethodArgumentResolver;

    @Autowired
    private AuthHandlerInterceptor authHandlerInterceptor;


    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(tokenToUserIdMethodArgumentResolver);
        argumentResolvers.add(tokenToAdminUserMethodArgumentResolver);
    }

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/files/**").addResourceLocations("file:" + Constants.FILE_UPLOAD_DIC);
        registry.addResourceHandler("/goods-img/**").addResourceLocations("file:" + Constants.FILE_UPLOAD_DIC);

        registry.
                addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
                .resourceChain(false);
    }


    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authHandlerInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/files/**");
    }

    /**
     * ????????????
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOriginPatterns("*")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true).maxAge(3600);
    }
}
