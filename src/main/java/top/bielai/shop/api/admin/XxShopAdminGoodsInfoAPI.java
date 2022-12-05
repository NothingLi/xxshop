/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本系统已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2021 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package top.bielai.shop.api.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import top.bielai.shop.api.admin.param.BatchIdParam;
import top.bielai.shop.api.admin.param.GoodsAddParam;
import top.bielai.shop.api.admin.param.GoodsEditParam;
import top.bielai.shop.common.Constants;
import top.bielai.shop.common.ServiceResultEnum;
import top.bielai.shop.config.annotation.TokenToAdminUser;
import top.bielai.shop.domain.XxShopGoodsInfo;
import top.bielai.shop.service.XxShopCategoryService;
import top.bielai.shop.service.XxShopGoodsService;
import top.bielai.shop.util.BeanUtil;
import top.bielai.shop.util.Result;
import top.bielai.shop.util.ResultGenerator;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * @author bielai
 */
@RestController
@Api(value = "v1", tags = "8-3.后台管理系统商品模块接口")
@RequestMapping("/manage-api/v1")
public class XxShopAdminGoodsInfoAPI {

    private static final Logger logger = LoggerFactory.getLogger(XxShopAdminGoodsInfoAPI.class);

    @Resource
    private XxShopGoodsService xxShopGoodsService;
    @Resource
    private XxShopCategoryService xxShopCategoryService;

    /**
     * 列表
     */
    @RequestMapping(value = "/goods/list", method = RequestMethod.GET)
    @ApiOperation(value = "商品列表", notes = "可根据名称和上架状态筛选")
    public Result list(@RequestParam(required = false) @ApiParam(value = "页码") Integer pageNumber,
                       @RequestParam(required = false) @ApiParam(value = "每页条数") Integer pageSize,
                       @RequestParam(required = false) @ApiParam(value = "商品名称") String goodsName,
                       @RequestParam(required = false) @ApiParam(value = "上架状态 0-上架 1-下架") Integer goodsSellStatus, @TokenToAdminUser AdminUserToken adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        if (pageNumber == null || pageNumber < 1 || pageSize == null || pageSize < 10) {
            return ResultGenerator.genFailResult("分页参数异常！");
        }
        Map params = new HashMap(8);
        params.put("page", pageNumber);
        params.put("limit", pageSize);
        if (!StringUtils.isEmpty(goodsName)) {
            params.put("goodsName", goodsName);
        }
        if (goodsSellStatus != null) {
            params.put("goodsSellStatus", goodsSellStatus);
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(xxShopGoodsService.getXxShopGoodsPage(pageUtil));
    }

    /**
     * 添加
     */
    @RequestMapping(value = "/goods", method = RequestMethod.POST)
    @ApiOperation(value = "新增商品信息", notes = "新增商品信息")
    public Result save(@RequestBody @Valid GoodsAddParam goodsAddParam, @TokenToAdminUser AdminUserToken adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        XxShopGoodsInfo xxShopGoods = new XxShopGoodsInfo();
        BeanUtil.copyProperties(goodsAddParam, xxShopGoods);
        String result = xxShopGoodsService.saveXxShopGoods(xxShopGoods);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }


    /**
     * 修改
     */
    @RequestMapping(value = "/goods", method = RequestMethod.PUT)
    @ApiOperation(value = "修改商品信息", notes = "修改商品信息")
    public Result update(@RequestBody @Valid GoodsEditParam goodsEditParam, @TokenToAdminUser AdminUserToken adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        XxShopGoodsInfo xxShopGoods = new XxShopGoodsInfo();
        BeanUtil.copyProperties(goodsEditParam, xxShopGoods);
        String result = xxShopGoodsService.updateXxShopGoods(xxShopGoods);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 详情
     */
    @GetMapping("/goods/{id}")
    @ApiOperation(value = "获取单条商品信息", notes = "根据id查询")
    public Result info(@PathVariable("id") Long id, @TokenToAdminUser AdminUserToken adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        Map goodsInfo = new HashMap(8);
        XxShopGoodsInfo goods = xxShopGoodsService.getXxShopGoodsById(id);
        if (goods == null) {
            return ResultGenerator.genFailResult(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        goodsInfo.put("goods", goods);
        GoodsCategory thirdCategory;
        GoodsCategory secondCategory;
        GoodsCategory firstCategory;
        thirdCategory = xxShopCategoryService.getGoodsCategoryById(goods.getGoodsCategoryId());
        if (thirdCategory != null) {
            goodsInfo.put("thirdCategory", thirdCategory);
            secondCategory = xxShopCategoryService.getGoodsCategoryById(thirdCategory.getParentId());
            if (secondCategory != null) {
                goodsInfo.put("secondCategory", secondCategory);
                firstCategory = xxShopCategoryService.getGoodsCategoryById(secondCategory.getParentId());
                if (firstCategory != null) {
                    goodsInfo.put("firstCategory", firstCategory);
                }
            }
        }
        return ResultGenerator.genSuccessResult(goodsInfo);
    }

    /**
     * 批量修改销售状态
     */
    @RequestMapping(value = "/goods/status/{sellStatus}", method = RequestMethod.PUT)
    @ApiOperation(value = "批量修改销售状态", notes = "批量修改销售状态")
    public Result delete(@RequestBody BatchIdParam batchIdParam, @PathVariable("sellStatus") int sellStatus, @TokenToAdminUser AdminUserToken adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        if (batchIdParam == null || batchIdParam.getIds().length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (sellStatus != Constants.SELL_STATUS_UP && sellStatus != Constants.SELL_STATUS_DOWN) {
            return ResultGenerator.genFailResult("状态异常！");
        }
        if (xxShopGoodsService.batchUpdateSellStatus(batchIdParam.getIds(), sellStatus)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("修改失败");
        }
    }

}