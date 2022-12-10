package top.bielai.shop.api.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.bielai.shop.api.admin.param.BatchIdParam;
import top.bielai.shop.api.admin.param.GoodsCategoryAddParam;
import top.bielai.shop.api.admin.param.GoodsCategoryEditParam;
import top.bielai.shop.common.ErrorEnum;
import top.bielai.shop.common.XxShopException;
import top.bielai.shop.domain.XxShopGoodsCategory;
import top.bielai.shop.service.XxShopGoodsCategoryService;
import top.bielai.shop.util.BeanUtil;
import top.bielai.shop.util.Result;
import top.bielai.shop.util.ResultGenerator;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Arrays;

/**
 * 后台管理系统分类模块接口
 *
 * @author bielai
 */
@Valid
@Validated
@RestController
@RequestMapping("/manage-api/v2/categories")
public class XxShopAdminGoodsCategoryApi {

    @Resource
    private XxShopGoodsCategoryService goodsCategoryService;


    /**
     * 分页查询分类数据
     *
     * @param pageNumber    页码
     * @param pageSize      页数
     * @param categoryLevel 分类等级
     * @param parentId      父级分类id
     * @return 分页结果
     */
    @GetMapping
    public Result<Page<XxShopGoodsCategory>> page(@RequestParam @Min(value = 1, message = "页码输入不对！") Integer pageNumber,
                                                  @RequestParam @Min(value = 10, message = "每页几条啊") Integer pageSize,
                                                  @RequestParam(required = false) @Min(value = 1, message = "分类级别不对噢") @Max(value = 3, message = "分类级别不对噢") Integer categoryLevel,
                                                  @RequestParam(required = false) @Min(value = 0, message = "上级分类id不对噢") Long parentId) {

        return ResultGenerator.genSuccessResult(goodsCategoryService.page(new Page<>(pageNumber, pageSize),
                new LambdaQueryWrapper<XxShopGoodsCategory>().eq(categoryLevel != null, XxShopGoodsCategory::getCategoryLevel, categoryLevel)
                        .eq(parentId != null, XxShopGoodsCategory::getParentId, parentId)
                        .orderByDesc(XxShopGoodsCategory::getCategoryRank)));
    }

    /**
     * 新增商品分类
     *
     * @param goodsCategoryAddParam 分类参数
     * @return 结果
     */
    @PostMapping
    @ApiOperation(value = "新增分类", notes = "新增分类")
    public Result<String> save(@Validated @RequestBody GoodsCategoryAddParam goodsCategoryAddParam) {
        XxShopGoodsCategory goodsCategory = new XxShopGoodsCategory();
        BeanUtil.copyProperties(goodsCategoryAddParam, goodsCategory);
        if (goodsCategoryService.save(goodsCategory)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult();
        }
    }


    /**
     * 修改商品分类
     *
     * @param goodsCategoryEditParam 更新分类参数
     * @return 结果
     */
    @PutMapping
    public Result<String> update(@Validated @RequestBody GoodsCategoryEditParam goodsCategoryEditParam) {
        XxShopGoodsCategory byId = goodsCategoryService.getById(goodsCategoryEditParam.getCategoryId());
        if (ObjectUtils.isEmpty(byId)) {
            throw new XxShopException(ErrorEnum.DATA_NOT_EXIST);
        }
        BeanUtil.copyProperties(goodsCategoryEditParam, byId);
        if (goodsCategoryService.updateById(byId)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult();
        }
    }

    /**
     * 根据id查询分类详情
     *
     * @param id 分类id
     * @return 详情
     */
    @GetMapping("/{id}")
    public Result<XxShopGoodsCategory> info(@PathVariable("id") Long id) {
        XxShopGoodsCategory goodsCategory = goodsCategoryService.getById(id);
        if (goodsCategory == null) {
            throw new XxShopException(ErrorEnum.DATA_NOT_EXIST);
        }
        return ResultGenerator.genSuccessResult(goodsCategory);
    }

    /**
     * 根据id批量删除分类
     *
     * @param batchIdParam id数组
     * @return 结果
     */
    @DeleteMapping
    public Result<String> delete(@Validated @RequestBody BatchIdParam batchIdParam) {
        if (goodsCategoryService.removeBatchByIds(Arrays.asList(batchIdParam.getIds()))) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }
}