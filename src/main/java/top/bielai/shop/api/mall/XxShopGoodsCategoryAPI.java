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
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.bielai.shop.api.mall.vo.XxShopIndexCategoryVO;
import top.bielai.shop.common.ServiceResultEnum;
import top.bielai.shop.common.XxShopException;
import top.bielai.shop.service.XxShopCategoryService;
import top.bielai.shop.util.Result;
import top.bielai.shop.util.ResultGenerator;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(value = "v1", tags = "3.小新商城分类页面接口")
@RequestMapping("/api/v1")
public class XxShopGoodsCategoryAPI {

    @Resource
    private XxShopCategoryService xxShopCategoryService;

    @GetMapping("/categories")
    @ApiOperation(value = "获取分类数据", notes = "分类页面使用")
    public Result<List<XxShopIndexCategoryVO>> getCategories() {
        List<XxShopIndexCategoryVO> categories = xxShopCategoryService.getCategoriesForIndex();
        if (CollectionUtils.isEmpty(categories)) {
            XxShopException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        return ResultGenerator.genSuccessResult(categories);
    }
}
