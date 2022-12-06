package top.bielai.shop.api.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.bielai.shop.api.admin.param.BatchIdParam;
import top.bielai.shop.api.admin.param.IndexConfigAddParam;
import top.bielai.shop.api.admin.param.IndexConfigEditParam;
import top.bielai.shop.common.ErrorEnum;
import top.bielai.shop.common.XxShopException;
import top.bielai.shop.domain.XxShopIndexConfig;
import top.bielai.shop.service.XxShopGoodsInfoService;
import top.bielai.shop.service.XxShopIndexConfigService;
import top.bielai.shop.util.BeanUtil;
import top.bielai.shop.util.Result;
import top.bielai.shop.util.ResultGenerator;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Arrays;

/**
 * 后台管理系统首页配置模块接口
 *
 * @author bielai
 */
@Valid
@Validated
@RestController
@RequestMapping("/manage-api/v2/indexConfigs")
public class XxShopAdminIndexConfigApi {


    @Resource
    private XxShopIndexConfigService indexConfigService;

    @Resource
    private XxShopGoodsInfoService goodsInfoService;

    /**
     * 分页查询首页配置列表
     *
     * @param pageNumber 页码
     * @param pageSize   页数
     * @param configType 1-搜索框热搜 2-搜索下拉框热搜 3-(首页)热销商品 4-(首页)新品上线 5-(首页)为你推荐
     * @return 分页结果
     */
    @GetMapping
    public Result<Page<XxShopIndexConfig>> page(@RequestParam @Min(value = 1, message = "第几页的数据呀") Integer pageNumber,
                                                @RequestParam @Min(value = 10, message = "每页几条啊") Integer pageSize,
                                                @RequestParam(required = false) @Range(min = 1, max = 5, message = "类型不对噢") Integer configType) {

        return ResultGenerator.genSuccessResult(indexConfigService.page(new Page<>(pageNumber, pageSize),
                new LambdaQueryWrapper<XxShopIndexConfig>().eq(configType != null, XxShopIndexConfig::getConfigType, configType)
                        .orderByDesc(XxShopIndexConfig::getConfigRank)));
    }

    /**
     * 新增首页配置项
     *
     * @param indexConfigAddParam 配置项信息
     * @return 结果
     */
    @PostMapping
    public Result<String> save(@Validated @RequestBody IndexConfigAddParam indexConfigAddParam) {
        if (ObjectUtils.isEmpty(goodsInfoService.getById(indexConfigAddParam.getGoodsId()))) {
            XxShopException.fail(ErrorEnum.GOODS_NOT_EXIST_ERROR);
        }
        XxShopIndexConfig exist = indexConfigService.getOne(new LambdaQueryWrapper<XxShopIndexConfig>()
                .eq(XxShopIndexConfig::getConfigType, indexConfigAddParam.getConfigType())
                .eq(XxShopIndexConfig::getGoodsId, indexConfigAddParam.getGoodsId()));
        if (ObjectUtils.isNotEmpty(exist)) {
            XxShopException.fail(ErrorEnum.DATA_EXIST_ERROR);
        }
        XxShopIndexConfig indexConfig = new XxShopIndexConfig();
        BeanUtil.copyProperties(indexConfigAddParam, indexConfig);
        if (indexConfigService.save(indexConfig)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult();
        }
    }


    /**
     * 修改首页配置项
     *
     * @param indexConfigEditParam 配置项信息
     * @return 结果
     */
    @PutMapping
    public Result<String> update(@Validated @RequestBody IndexConfigEditParam indexConfigEditParam) {
        if (ObjectUtils.isEmpty(indexConfigService.getById(indexConfigEditParam.getConfigId()))) {
            XxShopException.fail(ErrorEnum.DATA_NOT_EXIST);
        }
        if (ObjectUtils.isEmpty(goodsInfoService.getById(indexConfigEditParam.getGoodsId()))) {
            XxShopException.fail(ErrorEnum.GOODS_NOT_EXIST_ERROR);
        }
        XxShopIndexConfig exist = indexConfigService.getOne(new LambdaQueryWrapper<XxShopIndexConfig>()
                .eq(XxShopIndexConfig::getConfigType, indexConfigEditParam.getConfigType())
                .eq(XxShopIndexConfig::getGoodsId, indexConfigEditParam.getGoodsId())
                .ne(XxShopIndexConfig::getConfigId, indexConfigEditParam.getConfigId()));
        if (ObjectUtils.isNotEmpty(exist)) {
            XxShopException.fail(ErrorEnum.DATA_EXIST_ERROR);
        }
        XxShopIndexConfig indexConfig = new XxShopIndexConfig();
        BeanUtil.copyProperties(indexConfigEditParam, indexConfig);
        if (indexConfigService.updateById(indexConfig)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult();
        }
    }

    /**
     * 获取单条首页配置项信息
     *
     * @param id 配置id
     * @return 详情
     */
    @GetMapping("/{id}")
    public Result<XxShopIndexConfig> detail(@PathVariable("id") Long id) {
        XxShopIndexConfig exist = indexConfigService.getById(id);
        if (ObjectUtils.isEmpty(exist)) {
            XxShopException.fail(ErrorEnum.DATA_NOT_EXIST);
        }
        return ResultGenerator.genSuccessResult(exist);
    }

    /**
     * 批量删除首页配置项信息
     *
     * @param batchIdParam ids
     * @return 结果
     */
    @DeleteMapping
    public Result<String> delete(@Validated @RequestBody BatchIdParam batchIdParam) {
        if (indexConfigService.removeBatchByIds(Arrays.asList(batchIdParam.getIds()))) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }

}