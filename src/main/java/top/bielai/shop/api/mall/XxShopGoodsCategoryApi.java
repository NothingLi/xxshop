package top.bielai.shop.api.mall;

import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.bielai.shop.api.mall.vo.XxShopIndexCategoryVO;
import top.bielai.shop.common.ErrorEnum;
import top.bielai.shop.common.XxShopException;
import top.bielai.shop.service.XxShopGoodsCategoryService;
import top.bielai.shop.util.Result;
import top.bielai.shop.util.ResultGenerator;

import javax.annotation.Resource;
import java.util.List;

/**
 * 小新商城分类页面接口
 *
 * @author bielai
 */
@RestController
@RequestMapping("/api/v2/categories")
public class XxShopGoodsCategoryApi {

    @Resource
    private XxShopGoodsCategoryService categoryService;

    /**
     * 获取分类列表数据，包含子集
     *
     * @return 分类列表
     */
    @GetMapping
    public Result<List<XxShopIndexCategoryVO>> getCategories() {
        List<XxShopIndexCategoryVO> categories = categoryService.getCategoriesForIndex();
        if (CollectionUtils.isEmpty(categories)) {
            XxShopException.fail(ErrorEnum.DATA_NOT_EXIST);
        }
        return ResultGenerator.genSuccessResult(categories);
    }
}
