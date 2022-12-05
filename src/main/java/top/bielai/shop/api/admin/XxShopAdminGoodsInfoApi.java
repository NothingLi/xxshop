/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本系统已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2021 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package top.bielai.shop.api.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Range;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.bielai.shop.api.admin.param.BatchIdParam;
import top.bielai.shop.api.admin.param.GoodsAddParam;
import top.bielai.shop.api.admin.param.GoodsEditParam;
import top.bielai.shop.api.admin.vo.GoodsDetailVO;
import top.bielai.shop.common.CategoryLevelEnum;
import top.bielai.shop.common.ErrorEnum;
import top.bielai.shop.common.XxShopException;
import top.bielai.shop.domain.XxShopGoodsCategory;
import top.bielai.shop.domain.XxShopGoodsInfo;
import top.bielai.shop.service.XxShopGoodsCategoryService;
import top.bielai.shop.service.XxShopGoodsInfoService;
import top.bielai.shop.util.BeanUtil;
import top.bielai.shop.util.Result;
import top.bielai.shop.util.ResultGenerator;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Arrays;

/**
 * 后台管理系统商品模块接口
 *
 * @author bielai
 */
@Valid
@Validated
@RestController
@RequestMapping("/manage-api/v1/goods")
public class XxShopAdminGoodsInfoApi {

    @Resource
    private XxShopGoodsInfoService goodsInfoService;

    @Resource
    private XxShopGoodsCategoryService goodsCategoryService;

    /**
     * 分页查询商品列表
     *
     * @param pageNumber      页码
     * @param pageSize        页数
     * @param goodsName       商品名
     * @param goodsSellStatus 上架状态
     * @return 分页
     */
    @GetMapping("/list")
    @ApiOperation(value = "商品列表", notes = "可根据名称和上架状态筛选")
    public Result<Page<XxShopGoodsInfo>> list(@RequestParam @Min(value = 1, message = "第几页的数据呀") Integer pageNumber,
                                              @RequestParam @Min(value = 1, message = "每页几条啊") Integer pageSize,
                                              @RequestParam(required = false) String goodsName,
                                              @RequestParam(required = false) Integer goodsSellStatus) {

        return ResultGenerator.genSuccessResult(goodsInfoService.page(new Page<>(pageNumber, pageSize), new LambdaQueryWrapper<XxShopGoodsInfo>()
                .like(StringUtils.isNotBlank(goodsName), XxShopGoodsInfo::getGoodsName, goodsName)
                .eq(goodsSellStatus != null, XxShopGoodsInfo::getGoodsSellStatus, goodsSellStatus)));
    }

    /**
     * 新增商品
     *
     * @param goodsAddParam 新增商品参数
     * @return 结果
     */
    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public Result<String> save(@Validated @RequestBody GoodsAddParam goodsAddParam) {
        goodsCheck(goodsAddParam.getGoodsCategoryId(), goodsAddParam.getGoodsName());
        XxShopGoodsInfo xxShopGoods = new XxShopGoodsInfo();
        BeanUtil.copyProperties(goodsAddParam, xxShopGoods);
        if (goodsInfoService.save(xxShopGoods)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult();
        }
    }


    /**
     * 根据id更新商品
     *
     * @param goodsEditParam 更新商品信息参数
     * @return 结果
     */
    @PutMapping
    @Transactional(rollbackFor = Exception.class)
    public Result<String> update(@Validated @RequestBody GoodsEditParam goodsEditParam) {
        XxShopGoodsInfo byId = goodsInfoService.getById(goodsEditParam.getGoodsId());
        if (ObjectUtils.isEmpty(byId)) {
            XxShopException.fail(ErrorEnum.DATA_NOT_EXIST);
        }
        goodsCheck(goodsEditParam.getGoodsCategoryId(), goodsEditParam.getGoodsName());
        BeanUtil.copyProperties(goodsEditParam, byId);
        if (goodsInfoService.updateById(byId)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult();
        }
    }

    private void goodsCheck(Long categoryId, String goodsName) {
        XxShopGoodsCategory category = goodsCategoryService.getById(categoryId);
        if (category.getCategoryLevel() != CategoryLevelEnum.LEVEL_THREE.getLevel()) {
            XxShopException.fail(ErrorEnum.CATEGORY_LEVEL_ERROR);
        }
        XxShopGoodsInfo exist = goodsInfoService.getOne(new LambdaQueryWrapper<XxShopGoodsInfo>()
                .eq(XxShopGoodsInfo::getGoodsName, goodsName)
                .eq(XxShopGoodsInfo::getGoodsCategoryId, categoryId));
        if (ObjectUtils.isNotEmpty(exist)) {
            XxShopException.fail(ErrorEnum.GOODS_EXIST_ERROR);
        }
    }

    /**
     * 根据商品id查询详情
     *
     * @param id 商品id
     * @return 详情
     */
    @GetMapping("/{id}")
    public Result<GoodsDetailVO> info(@PathVariable("id") Long id) {

        XxShopGoodsInfo goods = goodsInfoService.getById(id);
        if (ObjectUtils.isEmpty(goods)) {
            XxShopException.fail(ErrorEnum.DATA_NOT_EXIST);
        }
        GoodsDetailVO goodsDetailVO = new GoodsDetailVO();
        goodsDetailVO.setGoodsInfo(goods);
        XxShopGoodsCategory thirdLevel = goodsCategoryService.getById(goods.getGoodsCategoryId());
        if (thirdLevel != null) {
            goodsDetailVO.setThirdCategory(thirdLevel);
            XxShopGoodsCategory secondCategory = goodsCategoryService.getOne(new LambdaQueryWrapper<XxShopGoodsCategory>()
                    .eq(XxShopGoodsCategory::getCategoryId, thirdLevel.getParentId()));
            if (secondCategory != null) {
                goodsDetailVO.setSecondCategory(secondCategory);
                XxShopGoodsCategory firstCategory = goodsCategoryService.getOne(new LambdaQueryWrapper<XxShopGoodsCategory>()
                        .eq(XxShopGoodsCategory::getCategoryId, secondCategory.getParentId()));
                if (firstCategory != null) {
                    goodsDetailVO.setFirstCategory(firstCategory);
                }
            }
        }
        return ResultGenerator.genSuccessResult(goodsDetailVO);
    }

    /**
     * 批量修改商品上架下架
     *
     * @param batchIdParam id数组
     * @param sellStatus   状态
     * @return 结果
     */
    @PutMapping("/status/{sellStatus}")
    @Transactional(rollbackFor = Exception.class)
    public Result<String> delete(@Validated @RequestBody BatchIdParam batchIdParam, @PathVariable("sellStatus") @Range(min = 0, max = 1, message = "状态不对噢") int sellStatus) {

        if (goodsInfoService.update(new LambdaUpdateWrapper<XxShopGoodsInfo>()
                .eq(XxShopGoodsInfo::getGoodsSellStatus, sellStatus)
                .in(XxShopGoodsInfo::getGoodsId, Arrays.toString(batchIdParam.getIds())))) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("修改失败");
        }
    }

}