
package top.bielai.shop.api.mall;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import top.bielai.shop.api.mall.param.SaveOrderParam;
import top.bielai.shop.api.mall.vo.XxShopOrderDetailVO;
import top.bielai.shop.api.mall.vo.XxShopOrderListVO;
import top.bielai.shop.api.mall.vo.XxShopShoppingCartItemVO;
import top.bielai.shop.common.Constants;
import top.bielai.shop.common.ServiceResultEnum;
import top.bielai.shop.common.XxShopException;
import top.bielai.shop.config.annotation.TokenToShopUser;
import top.bielai.shop.domain.XxShopUser;
import top.bielai.shop.domain.XxShopUserAddress;
import top.bielai.shop.service.XxShopOrderService;
import top.bielai.shop.service.XxShopShoppingCartService;
import top.bielai.shop.service.XxShopUserAddressService;
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
@Api(value = "v1", tags = "7.小新商城订单操作相关接口")
@RequestMapping("/api/v1")
public class XxShopOrderAPI {

    @Resource
    private XxShopShoppingCartService xxShopShoppingCartService;
    @Resource
    private XxShopOrderService xxShopOrderService;
    @Resource
    private XxShopUserAddressService xxShopUserAddressService;

    @PostMapping("/saveOrder")
    @ApiOperation(value = "生成订单接口", notes = "传参为地址id和待结算的购物项id数组")
    public Result<String> saveOrder(@ApiParam(value = "订单参数") @RequestBody SaveOrderParam saveOrderParam, @TokenToShopUser XxShopUser loginShopUser) {
        int priceTotal = 0;
        if (saveOrderParam == null || saveOrderParam.getCartItemIds() == null || saveOrderParam.getAddressId() == null) {
            XxShopException.fail(ServiceResultEnum.PARAM_ERROR.getResult());
        }
        if (saveOrderParam.getCartItemIds().length < 1) {
            XxShopException.fail(ServiceResultEnum.PARAM_ERROR.getResult());
        }
        List<XxShopShoppingCartItemVO> itemsForSave = xxShopShoppingCartService.getCartItemsForSettle(Arrays.asList(saveOrderParam.getCartItemIds()), loginShopUser.getUserId());
        if (CollectionUtils.isEmpty(itemsForSave)) {
            //无数据
            XxShopException.fail("参数异常");
        } else {
            //总价
            for (XxShopShoppingCartItemVO xxShopShoppingCartItemVO : itemsForSave) {
                priceTotal += xxShopShoppingCartItemVO.getGoodsCount() * xxShopShoppingCartItemVO.getSellingPrice();
            }
            if (priceTotal < 1) {
                XxShopException.fail("价格异常");
            }
            ShopUserAddress address = xxShopUserAddressService.getShopUserAddressById(saveOrderParam.getAddressId());
            if (!loginShopUser.getUserId().equals(address.getUserId())) {
                return ResultGenerator.genFailResult(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
            }
            //保存订单并返回订单号
            String saveOrderResult = xxShopOrderService.saveOrder(loginShopUser, address, itemsForSave);
            Result result = ResultGenerator.genSuccessResult();
            result.setData(saveOrderResult);
            return result;
        }
        return ResultGenerator.genFailResult("生成订单失败");
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
