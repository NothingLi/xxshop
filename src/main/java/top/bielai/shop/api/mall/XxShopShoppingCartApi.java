package top.bielai.shop.api.mall;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.bielai.shop.api.mall.param.SaveCartItemParam;
import top.bielai.shop.api.mall.param.UpdateCartItemParam;
import top.bielai.shop.api.mall.vo.XxShopShoppingCartItemVO;
import top.bielai.shop.common.Constants;
import top.bielai.shop.common.ErrorEnum;
import top.bielai.shop.common.XxShopException;
import top.bielai.shop.config.annotation.TokenToShopUser;
import top.bielai.shop.domain.XxShopShoppingCartItem;
import top.bielai.shop.service.XxShopShoppingCartItemService;
import top.bielai.shop.util.Result;
import top.bielai.shop.util.ResultGenerator;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * 小新商城购物车相关接口
 *
 * @author Administrator
 */
@Valid
@Validated
@RestController
@RequestMapping("/api/v1/shop-cart")
public class XxShopShoppingCartApi {

    @Resource
    private XxShopShoppingCartItemService shoppingCartItemService;

    /**
     * 分页查询购物车项列表
     *
     * @param pageNumber 页数
     * @return 分页结果
     */
    @GetMapping("/page")
    public Result<Page<XxShopShoppingCartItemVO>> cartItemPageList(@RequestParam @Min(value = 1, message = "你要看啥啊？") Integer pageNumber, @TokenToShopUser Long userId) {


        Page<XxShopShoppingCartItem> pageParam = new Page<>(pageNumber, Constants.SHOPPING_CART_PAGE_LIMIT);
        LambdaQueryWrapper<XxShopShoppingCartItem> queryWrapper = new LambdaQueryWrapper<XxShopShoppingCartItem>().eq(XxShopShoppingCartItem::getUserId, userId);
        Page<XxShopShoppingCartItemVO> result = shoppingCartItemService.pageVo(pageParam, queryWrapper);

        return ResultGenerator.genSuccessResult(result);
    }

    /**
     * 把商品添加到购物车
     *
     * @param saveCartItemParam 商品信息
     * @return 结果
     */
    @PostMapping
    public Result<String> saveXxShopShoppingCartItem(@Validated @RequestBody SaveCartItemParam saveCartItemParam,
                                                     @TokenToShopUser Long userId) {
        boolean saveResult = shoppingCartItemService.saveXxShopCartItem(saveCartItemParam, userId);
        //添加成功
        if (saveResult) {
            return ResultGenerator.genSuccessResult();
        }
        //添加失败
        return ResultGenerator.genFailResult();
    }

    /**
     * 修改购物车
     *
     * @param updateCartItemParam 数据
     * @return 结果
     */
    @PutMapping
    public Result<String> updateXxShopShoppingCartItem(@Validated @RequestBody UpdateCartItemParam updateCartItemParam,
                                                       @TokenToShopUser Long userId) {
        boolean updateResult = shoppingCartItemService.updateXxShopCartItem(updateCartItemParam, userId);
        //修改成功
        if (updateResult) {
            return ResultGenerator.genSuccessResult();
        }
        //修改失败
        return ResultGenerator.genFailResult();
    }

    /**
     * 根据id删除购物车项
     *
     * @param xxShopShoppingCartItemId 购物车项id
     * @return 结果
     */
    @DeleteMapping("/{xxShopShoppingCartItemId}")
    @Transactional(rollbackFor = Exception.class)
    public Result<String> updateXxShopShoppingCartItem(@PathVariable("xxShopShoppingCartItemId") Long xxShopShoppingCartItemId,
                                                       @TokenToShopUser Long userId) {
        XxShopShoppingCartItem one = shoppingCartItemService.getOne(new LambdaQueryWrapper<XxShopShoppingCartItem>()
                .eq(XxShopShoppingCartItem::getCartItemId, xxShopShoppingCartItemId).eq(XxShopShoppingCartItem::getUserId, userId));
        if (ObjectUtils.isEmpty(one)) {
            XxShopException.fail(ErrorEnum.DATA_NOT_EXIST);
        }
        //删除成功
        if (shoppingCartItemService.removeById(one)) {
            return ResultGenerator.genSuccessResult();
        }
        //删除失败
        return ResultGenerator.genFailResult();
    }


    /**
     * 批量删除购物车
     *
     * @param xxShopShoppingCartItemIds 购物车项id数组
     * @return 结果
     */
    @DeleteMapping
    @Transactional(rollbackFor = Exception.class)
    public Result<String> deleteXxShopShoppingCartItemBatch(@RequestBody Long[] xxShopShoppingCartItemIds,
                                                            @TokenToShopUser Long userId) {
        if (shoppingCartItemService.remove(new LambdaQueryWrapper<XxShopShoppingCartItem>().eq(XxShopShoppingCartItem::getUserId, userId)
                .in(XxShopShoppingCartItem::getCartItemId, Arrays.asList(xxShopShoppingCartItemIds)))) {
            return ResultGenerator.genSuccessResult();
        }
        //删除失败
        return ResultGenerator.genFailResult();
    }

    /**
     * 结算页面展示购物车项
     *
     * @param cartItemIds 购物车项
     * @return 购物车项列表
     */
    @GetMapping("/settle")
    public Result<List<XxShopShoppingCartItemVO>> toSettle(@Length(min = 1) Long[] cartItemIds, @TokenToShopUser Long userId) {
        if (cartItemIds.length < 1) {
            XxShopException.fail("参数异常");
        }
        BigDecimal priceTotal = BigDecimal.ZERO;
        List<XxShopShoppingCartItemVO> itemsForSettle = shoppingCartItemService.getCartItemsForSettle(Arrays.asList(cartItemIds), userId);
        if (CollectionUtils.isEmpty(itemsForSettle)) {
            //无数据则抛出异常
            XxShopException.fail("参数异常");
        } else {
            //总价
            for (XxShopShoppingCartItemVO xxShopShoppingCartItemVO : itemsForSettle) {
                priceTotal = priceTotal.add(xxShopShoppingCartItemVO.getSellingPrice().multiply(BigDecimal.valueOf(xxShopShoppingCartItemVO.getGoodsCount())));
            }
            if (BigDecimal.ZERO.compareTo(priceTotal) <= 0) {
                XxShopException.fail(ErrorEnum.PRICE_ERROR);
            }
        }
        return ResultGenerator.genSuccessResult(itemsForSettle);
    }
}
