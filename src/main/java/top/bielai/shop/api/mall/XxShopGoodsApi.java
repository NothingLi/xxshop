package top.bielai.shop.api.mall;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.bielai.shop.api.mall.vo.XxShopGoodsDetailVO;
import top.bielai.shop.api.mall.vo.XxShopSearchGoodsVO;
import top.bielai.shop.common.Constants;
import top.bielai.shop.common.ErrorEnum;
import top.bielai.shop.common.XxShopException;
import top.bielai.shop.domain.XxShopGoodsInfo;
import top.bielai.shop.service.XxShopGoodsInfoService;
import top.bielai.shop.util.BeanUtil;
import top.bielai.shop.util.Result;
import top.bielai.shop.util.ResultGenerator;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Min;

/**
 * 小新商城商品相关接口
 *
 * @author bielai
 */
@Valid
@Validated
@RestController
@RequestMapping("/api/v2/goods")
public class XxShopGoodsApi {

    @Resource
    private XxShopGoodsInfoService goodsInfoService;

    /**
     * 根据关键字和分类id进行商品搜索
     *
     * @param keyword         搜索关键字
     * @param goodsCategoryId 分类id
     * @param orderBy         orderBy
     * @param pageNumber      页码
     * @return 商品分页数据
     */
    @GetMapping("/search")
    public Result<Page<XxShopSearchGoodsVO>> search(@RequestParam(required = false) String keyword,
                                                    @RequestParam(required = false) Long goodsCategoryId,
                                                    @RequestParam(required = false) String orderBy,
                                                    @RequestParam @Min(value = 1, message = "页码输入不对！") Integer pageNumber) {
        //两个搜索参数都为空，直接返回异常
        if (goodsCategoryId == null && StringUtils.isBlank(keyword)) {
            XxShopException.fail(ErrorEnum.ERROR_PARAM);
        }
        LambdaQueryWrapper<XxShopGoodsInfo> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(StringUtils.isNotBlank("keyword"), XxShopGoodsInfo::getGoodsName, keyword)
                .or()
                .like(StringUtils.isNotBlank("keyword"), XxShopGoodsInfo::getGoodsIntro, keyword);
        queryWrapper.eq(goodsCategoryId != null, XxShopGoodsInfo::getGoodsCategoryId, goodsCategoryId);
        queryWrapper.eq(XxShopGoodsInfo::getGoodsSellStatus, Constants.SELL_STATUS_UP);
        if (Constants.ORDER_BY_NEW.equals(orderBy)) {
            queryWrapper.orderByDesc(XxShopGoodsInfo::getCreateTime);
        } else if (Constants.ORDER_BY_PRICE.equals(orderBy)) {
            queryWrapper.orderByDesc(XxShopGoodsInfo::getSellingPrice);
        } else {
            queryWrapper.orderByDesc(XxShopGoodsInfo::getStockNum);
        }
        Page<XxShopSearchGoodsVO> page = goodsInfoService.searchXxShopGoods(new Page<XxShopGoodsInfo>()
                .setCurrent(pageNumber).setSize(Constants.GOODS_SEARCH_PAGE_LIMIT), queryWrapper);

        return ResultGenerator.genSuccessResult(page);
    }

    /**
     * 根据商品id查询商品详情接口
     *
     * @param goodsId 商品id
     * @return 商品详情
     */
    @GetMapping("/detail/{goodsId}")
    public Result<XxShopGoodsDetailVO> goodsDetail(@ApiParam(value = "商品id") @PathVariable("goodsId") Long goodsId) {
        if (goodsId < 1) {
            XxShopException.fail(ErrorEnum.ERROR_PARAM);
        }
        XxShopGoodsInfo byId = goodsInfoService.getById(goodsId);
        if (Constants.SELL_STATUS_UP != byId.getGoodsSellStatus()) {
            XxShopException.fail(ErrorEnum.GOODS_PUT_DOWN);
        }
        XxShopGoodsDetailVO goodsDetailVO = new XxShopGoodsDetailVO();
        BeanUtil.copyProperties(byId, goodsDetailVO);
        goodsDetailVO.setGoodsCarouselList(byId.getGoodsCarousel().split(","));
        return ResultGenerator.genSuccessResult(goodsDetailVO);
    }

}
