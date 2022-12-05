/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本系统已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2021 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package top.bielai.shop.api.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.bielai.shop.api.admin.param.BatchIdParam;
import top.bielai.shop.api.admin.param.CarouselAddParam;
import top.bielai.shop.api.admin.param.CarouselEditParam;
import top.bielai.shop.common.ErrorEnum;
import top.bielai.shop.common.XxShopException;
import top.bielai.shop.domain.XxShopCarousel;
import top.bielai.shop.service.XxShopCarouselService;
import top.bielai.shop.util.BeanUtil;
import top.bielai.shop.util.Result;
import top.bielai.shop.util.ResultGenerator;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Arrays;

/**
 * 后台管理系统轮播图模块接口
 *
 * @author bielai
 */
@Valid
@Validated
@RestController
@RequestMapping("/manage-api/v1/carousels")
public class XxShopAdminCarouselApi {


    @Resource
    private XxShopCarouselService xxShopCarouselService;

    /**
     * 轮播图列表
     *
     * @param pageNumber 页码
     * @param pageSize   页数
     * @return 分页结果
     */
    @GetMapping
    public Result<Page<XxShopCarousel>> list(@RequestParam @Min(value = 1, message = "第几页的数据呀") Integer pageNumber,
                                             @RequestParam @Min(value = 1, message = "每页几条啊") Integer pageSize) {
        return ResultGenerator.genSuccessResult(xxShopCarouselService.page(new Page<>(pageNumber, pageSize)));
    }

    /**
     * 添加轮播图
     *
     * @param carouselAddParam 添加参数
     * @return 结果
     */
    @PostMapping
    public Result<String> save(@Validated @RequestBody CarouselAddParam carouselAddParam) {
        XxShopCarousel carousel = new XxShopCarousel();
        BeanUtil.copyProperties(carouselAddParam, carousel);
        if (xxShopCarouselService.save(carousel)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult();
        }
    }


    /**
     * 修改轮播图
     *
     * @param carouselEditParam 修改参数
     * @return 结果
     */
    @PutMapping
    public Result<String> update(@Validated @RequestBody CarouselEditParam carouselEditParam) {
        XxShopCarousel byId = xxShopCarouselService.getById(carouselEditParam.getCarouselId());
        if (ObjectUtils.isEmpty(byId)) {
            XxShopException.fail(ErrorEnum.DATA_NOT_EXIST);
        }
        BeanUtil.copyProperties(carouselEditParam, byId);
        if (xxShopCarouselService.updateById(byId)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult();
        }
    }


    /**
     * 根据id查询轮播图详情
     *
     * @param id 轮播图id
     * @return 详情
     */
    @GetMapping("/{id}")
    public Result<XxShopCarousel> info(@PathVariable("id") Integer id) {
        XxShopCarousel byId = xxShopCarouselService.getById(id);
        if (ObjectUtils.isEmpty(byId)) {
            XxShopException.fail(ErrorEnum.DATA_NOT_EXIST);
        }
        return ResultGenerator.genSuccessResult(byId);
    }


    /**
     * 批量删除轮播图
     *
     * @param batchIdParam id数组
     * @return 结果
     */
    @DeleteMapping
    public Result<String> delete(@Validated @RequestBody BatchIdParam batchIdParam) {

        if (xxShopCarouselService.removeBatchByIds(Arrays.asList(batchIdParam.getIds()))) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }

}