
package top.bielai.shop.api.mall;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import top.bielai.shop.api.mall.param.SaveCartItemParam;
import top.bielai.shop.api.mall.param.UpdateCartItemParam;
import top.bielai.shop.api.mall.vo.XxShopShoppingCartItemVO;
import top.bielai.shop.common.Constants;
import top.bielai.shop.common.ServiceResultEnum;
import top.bielai.shop.common.XxShopException;
import top.bielai.shop.config.annotation.TokenToShopUser;
import top.bielai.shop.domain.XxShopUser;
import top.bielai.shop.domain.XxShopShoppingCartItem;
import top.bielai.shop.service.XxShopShoppingCartService;
import top.bielai.shop.util.PageQueryUtil;
import top.bielai.shop.util.PageResult;
import top.bielai.shop.util.Result;
import top.bielai.shop.util.ResultGenerator;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(value = "v1", tags = "5.小新商城购物车相关接口")
@RequestMapping("/api/v1")
public class XxShopShoppingCartAPI {

    @Resource
    private XxShopShoppingCartService xxShopShoppingCartService;

    @GetMapping("/shop-cart/page")
    @ApiOperation(value = "购物车列表(每页默认5条)", notes = "传参为页码")
    public Result<PageResult<List<XxShopShoppingCartItemVO>>> cartItemPageList(Integer pageNumber, @TokenToShopUser Long userId) {
        Map params = new HashMap(8);
        if (pageNumber == null || pageNumber < 1) {
            pageNumber = 1;
        }
        params.put("userId", loginShopUser.getUserId());
        params.put("page", pageNumber);
        params.put("limit", Constants.SHOPPING_CART_PAGE_LIMIT);
        //封装分页请求参数
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(xxShopShoppingCartService.getMyShoppingCartItems(pageUtil));
    }

    @GetMapping("/shop-cart")
    @ApiOperation(value = "购物车列表(网页移动端不分页)", notes = "")
    public Result<List<XxShopShoppingCartItemVO>> cartItemList(Long userId) {
        return ResultGenerator.genSuccessResult(xxShopShoppingCartService.getMyShoppingCartItems(loginShopUser.getUserId()));
    }

    @PostMapping("/shop-cart")
    @ApiOperation(value = "添加商品到购物车接口", notes = "传参为商品id、数量")
    public Result saveXxShopShoppingCartItem(@RequestBody SaveCartItemParam saveCartItemParam,
                                                 Long userId) {
        String saveResult = xxShopShoppingCartService.saveXxShopCartItem(saveCartItemParam, loginShopUser.getUserId());
        //添加成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(saveResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //添加失败
        return ResultGenerator.genFailResult(saveResult);
    }

    @PutMapping("/shop-cart")
    @ApiOperation(value = "修改购物项数据", notes = "传参为购物项id、数量")
    public Result updateXxShopShoppingCartItem(@RequestBody UpdateCartItemParam updateCartItemParam,
                                                   Long userId) {
        String updateResult = xxShopShoppingCartService.updateXxShopCartItem(updateCartItemParam, loginShopUser.getUserId());
        //修改成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(updateResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //修改失败
        return ResultGenerator.genFailResult(updateResult);
    }

    @DeleteMapping("/shop-cart/{xxShopShoppingCartItemId}")
    @ApiOperation(value = "删除购物项", notes = "传参为购物项id")
    public Result updateXxShopShoppingCartItem(@PathVariable("xxShopShoppingCartItemId") Long xxShopShoppingCartItemId,
                                                   Long userId) {
        XxShopShoppingCartItem xxShopCartItemById = xxShopShoppingCartService.getXxShopCartItemById(xxShopShoppingCartItemId);
        if (!loginShopUser.getUserId().equals(xxShopCartItemById.getUserId())) {
            return ResultGenerator.genFailResult(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }
        Boolean deleteResult = xxShopShoppingCartService.deleteById(xxShopShoppingCartItemId,loginShopUser.getUserId());
        //删除成功
        if (deleteResult) {
            return ResultGenerator.genSuccessResult();
        }
        //删除失败
        return ResultGenerator.genFailResult(ServiceResultEnum.OPERATE_ERROR.getResult());
    }


    @DeleteMapping("/shop-cart")
    @ApiOperation(value = "删除购物项", notes = "传参为购物项id")
    public Result updateXxShopShoppingCartItem(@RequestBody Long[] xxShopShoppingCartItemId,
                                               Long userId) {

        Boolean deleteResult = xxShopShoppingCartService.deleteBatchById(xxShopShoppingCartItemId,loginShopUser.getUserId());
        //删除成功
        if (deleteResult) {
            return ResultGenerator.genSuccessResult();
        }
        //删除失败
        return ResultGenerator.genFailResult(ServiceResultEnum.OPERATE_ERROR.getResult());
    }

    @GetMapping("/shop-cart/settle")
    @ApiOperation(value = "根据购物项id数组查询购物项明细", notes = "确认订单页面使用")
    public Result<List<XxShopShoppingCartItemVO>> toSettle(Long[] cartItemIds, @TokenToShopUser Long userId) {
        if (cartItemIds.length < 1) {
            XxShopException.fail("参数异常");
        }
        int priceTotal = 0;
        List<XxShopShoppingCartItemVO> itemsForSettle = xxShopShoppingCartService.getCartItemsForSettle(Arrays.asList(cartItemIds), loginShopUser.getUserId());
        if (CollectionUtils.isEmpty(itemsForSettle)) {
            //无数据则抛出异常
            XxShopException.fail("参数异常");
        } else {
            //总价
            for (XxShopShoppingCartItemVO xxShopShoppingCartItemVO : itemsForSettle) {
                priceTotal += xxShopShoppingCartItemVO.getGoodsCount() * xxShopShoppingCartItemVO.getSellingPrice();
            }
            if (priceTotal < 1) {
                XxShopException.fail("价格异常");
            }
        }
        return ResultGenerator.genSuccessResult(itemsForSettle);
    }
}
