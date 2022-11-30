
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
import top.bielai.shop.service.XxShopGoodsCategoryService;
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

    @Resource
    private XxShopGoodsCategoryService categoryService;

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
