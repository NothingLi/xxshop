
package top.bielai.shop.api.mall.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author bielai
 */
@Data
public class IndexInfoVO implements Serializable {

    /**
     * 轮播图(列表)
     */
    private List<XxShopIndexCarouselVO> carousels;

    /**
     * 首页热销商品(列表)
     */
    private List<XxShopIndexConfigGoodsVO> hotGoods;

    /**
     * 首页新品推荐(列表)
     */
    private List<XxShopIndexConfigGoodsVO> newGoods;

    /**
     * 首页推荐商品(列表)
     */
    private List<XxShopIndexConfigGoodsVO> recommendGoods;
}
