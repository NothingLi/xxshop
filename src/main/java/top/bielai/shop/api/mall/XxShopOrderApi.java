
package top.bielai.shop.api.mall;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.bielai.shop.api.mall.param.SaveOrderParam;
import top.bielai.shop.api.mall.vo.*;
import top.bielai.shop.common.*;
import top.bielai.shop.config.annotation.TokenToShopUser;
import top.bielai.shop.domain.XxShopOrder;
import top.bielai.shop.domain.XxShopOrderAddress;
import top.bielai.shop.domain.XxShopOrderItem;
import top.bielai.shop.domain.XxShopUserAddress;
import top.bielai.shop.service.*;
import top.bielai.shop.util.BeanUtil;
import top.bielai.shop.util.Result;
import top.bielai.shop.util.ResultGenerator;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.List;

/**
 * 小新商城订单操作相关接口
 *
 * @author Administrator
 */
@Valid
@Validated
@RestController
@RequestMapping("/api/v1")
public class XxShopOrderApi {

    @Resource
    private XxShopShoppingCartItemService cartItemService;

    @Resource
    private XxShopOrderService xxShopOrderService;

    @Resource
    private XxShopUserAddressService xxShopUserAddressService;

    @Resource
    private XxShopOrderItemService orderItemService;

    @Resource
    private XxShopOrderAddressService orderAddressService;

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

    /**
     * 根据订单号查询订单详情
     *
     * @param orderNo 订单编号
     * @return 订单详情
     */
    @GetMapping("/order/{orderNo}")
    public Result<XxShopOrderDetailVO> getOrderDetail(@PathVariable("orderNo") @NotBlank(message = "订单编号呢？") String orderNo, @TokenToShopUser Long userId) {
        // 根据订单编号和用户id查询相关订单
        XxShopOrder one = xxShopOrderService.getOne(new LambdaQueryWrapper<XxShopOrder>().eq(XxShopOrder::getOrderNo, orderNo).eq(XxShopOrder::getUserId, userId));
        if (ObjectUtils.isEmpty(one)) {
            XxShopException.fail(ErrorEnum.DATA_NOT_EXIST);
        }
        XxShopOrderDetailVO xxShopOrderDetailVO = new XxShopOrderDetailVO();
        BeanUtil.copyProperties(one, xxShopOrderDetailVO);

        // 查询订单项
        List<XxShopOrderItem> orderItems = orderItemService.list(new LambdaQueryWrapper<XxShopOrderItem>().eq(XxShopOrderItem::getOrderId, one.getOrderId()));
        List<XxShopOrderItemVO> xxShopOrderItemVOList = BeanUtil.copyList(orderItems, XxShopOrderItemVO.class);
        // 查询订单地址
        XxShopOrderAddress orderAddress = orderAddressService.getOne(new LambdaQueryWrapper<XxShopOrderAddress>().eq(XxShopOrderAddress::getOrderId, one.getOrderId()));
        XxShopUserAddressVO xxShopUserAddressVO = new XxShopUserAddressVO();
        BeanUtil.copyProperties(orderAddress, xxShopUserAddressVO);

        // 设置订单状态
        xxShopOrderDetailVO.setOrderStatusString(OrderStatusEnum.getXxShopOrderStatusEnumByStatus(xxShopOrderDetailVO.getOrderStatus()).getName());
        // 设置支付类型
        xxShopOrderDetailVO.setPayTypeString(PayTypeEnum.getPayTypeEnumByType(xxShopOrderDetailVO.getPayType()).getName());
        // 设置支付状态
        xxShopOrderDetailVO.setPayStatusString(PayStatusEnum.getPayStatusEnumByStatus(xxShopOrderDetailVO.getPayStatus()).getName());
        xxShopOrderDetailVO.setXxShopOrderItemVOList(xxShopOrderItemVOList);
        xxShopOrderDetailVO.setXxShopUserAddressVO(xxShopUserAddressVO);
        return ResultGenerator.genSuccessResult(xxShopOrderDetailVO);
    }

    /**
     * 分页查询订单列表数据
     *
     * @param pageNumber 页码
     * @param status     订单状态:0.待支付 1.待确认 2.待发货 3:已发货 4.交易成功
     * @return 订单分页
     */
    @GetMapping("/order")
    @ApiOperation(value = "订单列表接口", notes = "传参为页码")
    public Result<Page<XxShopOrderListVO>> orderList(@RequestParam @Min(value = 1,message = "你要看哪样啊？") Integer pageNumber,
                                                     @RequestParam(required = false) Integer status,
                                                     @TokenToShopUser Long userId) {
        Page<XxShopOrder> pageParam = new Page<XxShopOrder>().setCurrent(pageNumber).setSize(Constants.ORDER_SEARCH_PAGE_LIMIT);
        LambdaQueryWrapper<XxShopOrder> queryWrapper = new LambdaQueryWrapper<XxShopOrder>().eq(null != status, XxShopOrder::getOrderStatus, status)
                .eq(XxShopOrder::getUserId, userId);
        //封装分页请求参数
        return ResultGenerator.genSuccessResult(xxShopOrderService.orderList(pageParam, queryWrapper));
    }

    @PutMapping("/order/{orderNo}/cancel")
    @ApiOperation(value = "订单取消接口", notes = "传参为订单号")
    public Result cancelOrder(@ApiParam(value = "订单号") @PathVariable("orderNo") String orderNo, @TokenToShopUser Long userId) {
        String cancelOrderResult = xxShopOrderService.cancelOrder(orderNo, loginShopUser.getUserId());
        if (ServiceResultEnum.SUCCESS.getResult().equals(cancelOrderResult)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(cancelOrderResult);
        }
    }

    @PutMapping("/order/{orderNo}/finish")
    @ApiOperation(value = "确认收货接口", notes = "传参为订单号")
    public Result finishOrder(@ApiParam(value = "订单号") @PathVariable("orderNo") String orderNo, @TokenToShopUser Long userId) {
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
