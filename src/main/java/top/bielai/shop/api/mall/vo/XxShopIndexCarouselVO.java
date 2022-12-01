
package top.bielai.shop.api.mall.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 首页轮播图VO
 * @author Administrator
 */
@Data
public class XxShopIndexCarouselVO implements Serializable {

    /**
     * 轮播图图片地址
     */
    private String carouselUrl;

    /**
     * 轮播图点击后的跳转路径
     */
    private String redirectUrl;
}
