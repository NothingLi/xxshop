
package top.bielai.shop.api.mall;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.bielai.shop.api.mall.param.SaveOrderParam;
import top.bielai.shop.api.mall.vo.XxShopOrderDetailVO;
import top.bielai.shop.api.mall.vo.XxShopOrderListVO;
import top.bielai.shop.api.mall.vo.XxShopShoppingCartItemVO;
import top.bielai.shop.common.Constants;
import top.bielai.shop.common.ErrorEnum;
import top.bielai.shop.common.ServiceResultEnum;
import top.bielai.shop.common.XxShopException;
import top.bielai.shop.config.annotation.TokenToShopUser;
import top.bielai.shop.domain.XxShopUser;
import top.bielai.shop.domain.XxShopUserAddress;
import top.bielai.shop.service.XxShopOrderService;
import top.bielai.shop.service.XxShopShoppingCartItemService;
import top.bielai.shop.service.XxShopUserAddressService;
import top.bielai.shop.util.PageQueryUtil;
import top.bielai.shop.util.PageResult;
import top.bielai.shop.util.Result;
import top.bielai.shop.util.ResultGenerator;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 小新商城订单操作相关接口
 *
 * @author Administrator
 */
@Valid
@RestController
@RequestMapping("/api/v1")
public class XxShopOrderApi {

    @Resource
    private XxShopShoppingCartItemService cartItemService;
    @Resource
    private XxShopOrderService xxShopOrderService;
    @Resource
    private XxShopUserAddressService xxShopUserAddressService;

    /**
     * 生成订单接口
     *
     * @param saveOrderParam 订单参数
     * @param userId         用户Id
     * @return 订单编号
     */
    @PostMapping("/saveOrder")
    public Result<String> saveOrder(@Validated @RequestBody SaveOrderParam saveOrderParam, @TokenToShopUser Long userId) {
        XxShopUserAddress address = xxShopUserAddressService.getOne(new LambdaQueryWrapper<XxShopUserAddress>()
                .eq(XxShopUserAddress::getUserId, userId).eq(XxShopUserAddress::getAddressId, saveOrderParam.getAddressId()));
        if (ObjectUtils.isEmpty(address)) {
            XxShopException.fail(ErrorEnum.USER_ADDRESS_DOWN);
        }
        List<XxShopShoppingCartItemVO> itemsForSave = cartItemService.getCartItemsForSettle(Arrays.asList(saveOrderParam.getCartItemIds()), userId);
        //生成订单并返回订单号
        String saveOrderResult = xxShopOrderService.saveOrder(userId, address, itemsForSave);

        return ResultGenerator.genSuccessResult(saveOrderResult);
    }

    @GetMapping("/order/{orderNo}")
    @ApiOperation(value = "订单详情接口", notes = "传参为订单号")
    public Result<XxShopOrderDetailVO> orderDetailPage(@ApiParam(value = "订单号") @PathVariable("orderNo") String orderNo, @TokenToShopUser XxShopUser loginShopUser) {
        return ResultGenerator.genSuccessResult(xxShopOrderService.getOrderDetailByOrderNo(orderNo, loginShopUser.getUserId()));
    }

    @GetMapping("/order")
    @ApiOperation(value = "订单列表接口", notes = "传参为页码")
    public Result<PageResult<List<XxShopOrderListVO>>> orderList(@ApiParam(value = "页码") @RequestParam(required = false) Integer pageNumber,
                                                                 @ApiParam(value = "订单状态:0.待支付 1.待确认 2.待发货 3:已发货 4.交易成功") @RequestParam(required = false) Integer status,
                                                                 @TokenToShopUser XxShopUser loginShopUser) {
        Map params = new HashMap(8);
        if (pageNumber == null || pageNumber < 1) {
            pageNumber = 1;
        }
        params.put("userId", loginShopUser.getUserId());
        params.put("orderStatus", status);
        params.put("page", pageNumber);
        params.put("limit", Constants.ORDER_SEARCH_PAGE_LIMIT);
        //封装分页请求参数
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(xxShopOrderService.getMyOrders(pageUtil));
    }

    @PutMapping("/order/{orderNo}/cancel")
    @ApiOperation(value = "订单取消接口", notes = "传参为订单号")
    public Result cancelOrder(@ApiParam(value = "订单号") @PathVariable("orderNo") String orderNo, @TokenToShopUser XxShopUser loginShopUser) {
        String cancelOrderResult = xxShopOrderService.cancelOrder(orderNo, loginShopUser.getUserId());
        if (ServiceResultEnum.SUCCESS.getResult().equals(cancelOrderResult)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(cancelOrderResult);
        }
    }

    @PutMapping("/order/{orderNo}/finish")
    @ApiOperation(value = "确认收货接口", notes = "传参为订单号")
    public Result finishOrder(@ApiParam(value = "订单号") @PathVariable("orderNo") String orderNo, @TokenToShopUser XxShopUser loginShopUser) {
        String finishOrderResult = xxShopOrderService.finishOrder(orderNo, loginShopUser.getUserId());
        if (ServiceResultEnum.SUCCESS.getResult().equals(finishOrderResult)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(finishOrderResult);
        }
    }

    @GetMapping("/paySuccess")
    @ApiOperation(value = "模拟支付成功回调的接口", notes = "传参为订单号和支付方式")
    public Result paySuccess(@ApiParam(value = "订单号") @RequestParam("orderNo") String orderNo, @ApiParam(value = "支付方式") @RequestParam("payType") int payType) {
        String payResult = xxShopOrderService.paySuccess(orderNo, payType);
        if (ServiceResultEnum.SUCCESS.getResult().equals(payResult)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(payResult);
        }
    }

}
