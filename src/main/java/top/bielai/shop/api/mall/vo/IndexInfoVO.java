
package top.bielai.shop.api.mall.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class IndexInfoVO implements Serializable {

    @ApiModelProperty("轮播图(列表)")
    private List<XxShopIndexCarouselVO> carousels;

    @ApiModelProperty("首页热销商品(列表)")
    private List<XxShopIndexConfigGoodsVO> hotGoodses;

    @ApiModelProperty("首页新品推荐(列表)")
    private List<XxShopIndexConfigGoodsVO> newGoodses;

    @ApiModelProperty("首页推荐商品(列表)")
    private List<XxShopIndexConfigGoodsVO> recommendGoodses;
}
