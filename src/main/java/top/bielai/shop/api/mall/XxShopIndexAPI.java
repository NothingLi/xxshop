/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本软件已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2021 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package top.bielai.shop.api.mall;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.bielai.shop.api.mall.vo.IndexInfoVO;
import top.bielai.shop.api.mall.vo.XxShopIndexCarouselVO;
import top.bielai.shop.api.mall.vo.XxShopIndexConfigGoodsVO;
import top.bielai.shop.common.Constants;
import top.bielai.shop.common.IndexConfigTypeEnum;
import top.bielai.shop.service.XxShopCarouselService;
import top.bielai.shop.service.XxShopIndexConfigService;
import top.bielai.shop.util.Result;
import top.bielai.shop.util.ResultGenerator;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(value = "v1", tags = "1.小新商城首页接口")
@RequestMapping("/api/v1")
public class XxShopIndexAPI {

    @Resource
    private XxShopCarouselService xxShopCarouselService;

    @Resource
    private XxShopIndexConfigService xxShopIndexConfigService;

    @GetMapping("/index-infos")
    @ApiOperation(value = "获取首页数据", notes = "轮播图、新品、推荐等")
    public Result<IndexInfoVO> indexInfo() {
        IndexInfoVO indexInfoVO = new IndexInfoVO();
        List<XxShopIndexCarouselVO> carousels = xxShopCarouselService.getCarouselsForIndex(Constants.INDEX_CAROUSEL_NUMBER);
        List<XxShopIndexConfigGoodsVO> hotGoodses = xxShopIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_HOT.getType(), Constants.INDEX_GOODS_HOT_NUMBER);
        List<XxShopIndexConfigGoodsVO> newGoodses = xxShopIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_NEW.getType(), Constants.INDEX_GOODS_NEW_NUMBER);
        List<XxShopIndexConfigGoodsVO> recommendGoodses = xxShopIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_RECOMMOND.getType(), Constants.INDEX_GOODS_RECOMMOND_NUMBER);
        indexInfoVO.setCarousels(carousels);
        indexInfoVO.setHotGoodses(hotGoodses);
        indexInfoVO.setNewGoodses(newGoodses);
        indexInfoVO.setRecommendGoodses(recommendGoodses);
        return ResultGenerator.genSuccessResult(indexInfoVO);
    }
}
