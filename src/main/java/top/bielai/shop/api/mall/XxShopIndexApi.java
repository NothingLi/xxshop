
package top.bielai.shop.api.mall;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.bielai.shop.api.mall.vo.IndexInfoVO;
import top.bielai.shop.api.mall.vo.XxShopIndexCarouselVO;
import top.bielai.shop.api.mall.vo.XxShopIndexConfigGoodsVO;
import top.bielai.shop.common.Constants;
import top.bielai.shop.common.IndexConfigTypeEnum;
import top.bielai.shop.domain.XxShopCarousel;
import top.bielai.shop.service.XxShopCarouselService;
import top.bielai.shop.service.XxShopIndexConfigService;
import top.bielai.shop.util.BeanUtil;
import top.bielai.shop.util.Result;
import top.bielai.shop.util.ResultGenerator;

import javax.annotation.Resource;
import java.util.List;

/**
 * 小新商城首页接口
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/api/v1")
public class XxShopIndexApi {

    @Resource
    private XxShopCarouselService xxShopCarouselService;

    @Resource
    private XxShopIndexConfigService xxShopIndexConfigService;

    /**
     * 获取首页数据轮播图、新品、推荐等
     *
     * @return 首页数据
     */
    @GetMapping("/index-infos")
    public Result<IndexInfoVO> indexInfo() {
        IndexInfoVO indexInfoVO = new IndexInfoVO();
        List<XxShopCarousel> list = xxShopCarouselService.list(new LambdaQueryWrapper<XxShopCarousel>()
                .orderByDesc(XxShopCarousel::getCarouselRank)
                .last("limit " + Constants.INDEX_CAROUSEL_NUMBER));
        List<XxShopIndexCarouselVO> carousels = BeanUtil.copyList(list, XxShopIndexCarouselVO.class);
        List<XxShopIndexConfigGoodsVO> hotGoods = xxShopIndexConfigService.getConfigGoodsForIndex(IndexConfigTypeEnum.INDEX_GOODS_HOT.getType(),
                Constants.INDEX_GOODS_HOT_NUMBER);
        List<XxShopIndexConfigGoodsVO> newGoods = xxShopIndexConfigService.getConfigGoodsForIndex(IndexConfigTypeEnum.INDEX_GOODS_NEW.getType(),
                Constants.INDEX_GOODS_NEW_NUMBER);
        List<XxShopIndexConfigGoodsVO> recommendGoods = xxShopIndexConfigService.getConfigGoodsForIndex(IndexConfigTypeEnum.INDEX_GOODS_RECOMMOND.getType(),
                Constants.INDEX_GOODS_RECOMMEND_NUMBER);
        indexInfoVO.setCarousels(carousels);
        indexInfoVO.setHotGoods(hotGoods);
        indexInfoVO.setNewGoods(newGoods);
        indexInfoVO.setRecommendGoods(recommendGoods);
        return ResultGenerator.genSuccessResult(indexInfoVO);
    }
}
