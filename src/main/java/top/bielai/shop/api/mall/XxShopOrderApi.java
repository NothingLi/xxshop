package top.bielai.shop.api.mall;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.bielai.shop.api.mall.param.SaveOrderParam;
import top.bielai.shop.api.mall.vo.XxShopOrderDetailVO;
import top.bielai.shop.api.mall.vo.XxShopOrderListVO;
import top.bielai.shop.api.mall.vo.XxShopShoppingCartItemVO;
import top.bielai.shop.common.Constants;
import top.bielai.shop.common.ErrorEnum;
import top.bielai.shop.common.XxShopException;
import top.bielai.shop.config.annotation.TokenToShopUser;
import top.bielai.shop.domain.XxShopOrder;
import top.bielai.shop.domain.XxShopUser;
import top.bielai.shop.domain.XxShopUserAddress;
import top.bielai.shop.service.XxShopOrderService;
import top.bielai.shop.service.XxShopShoppingCartItemService;
import top.bielai.shop.service.XxShopUserAddressService;
import top.bielai.shop.util.Result;
import top.bielai.shop.util.ResultGenerator;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 小新商城订单操作相关接口
 *
 * @author bielai
 */
@Valid
@Validated
@RestController
@RequestMapping("/api/v2/order")
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
     * @return 订单编号
     */
    @PostMapping
    public Result<String> saveOrder(@Validated @RequestBody SaveOrderParam saveOrderParam, @TokenToShopUser XxShopUser user) {
        XxShopUserAddress address = xxShopUserAddressService.getOne(new LambdaQueryWrapper<XxShopUserAddress>()
                .eq(XxShopUserAddress::getUserId, user.getUserId()).eq(XxShopUserAddress::getAddressId, saveOrderParam.getAddressId()));
        if (ObjectUtils.isEmpty(address)) {
            throw new XxShopException(ErrorEnum.USER_ADDRESS_DOWN);
        }
        List<XxShopShoppingCartItemVO> itemsForSave = cartItemService.getCartItemsForSettle(Arrays.asList(saveOrderParam.getCartItemIds()), user.getUserId());
        //生成订单并返回订单号
        String saveOrderResult = xxShopOrderService.saveOrder(user.getUserId(), address, itemsForSave);

        return ResultGenerator.genSuccessResult(saveOrderResult);
    }

    /**
     * 根据订单号查询订单详情
     *
     * @param orderNo 订单编号
     * @return 订单详情
     */
    @GetMapping("/{orderNo}")
    public Result<XxShopOrderDetailVO> getOrderDetail(@PathVariable("orderNo") @NotBlank(message = "订单编号呢？") String orderNo, @TokenToShopUser XxShopUser user) {
        // 根据订单编号和用户id查询相关订单

        return ResultGenerator.genSuccessResult(xxShopOrderService.getDetailVO(null, user.getUserId(), orderNo));
    }

    /**
     * 分页查询订单列表数据
     *
     * @param pageNumber 页码
     * @param status     订单状态:0.待支付 1.待确认 2.待发货 3:已发货 4.交易成功
     * @return 订单分页
     */
    @GetMapping
    public Result<Page<XxShopOrderListVO>> orderList(@RequestParam @Min(value = 1, message = "页码输入不对！") Integer pageNumber,
                                                     @RequestParam(required = false) Integer status,
                                                     @TokenToShopUser XxShopUser user) {
        Page<XxShopOrder> pageParam = new Page<XxShopOrder>().setCurrent(pageNumber).setSize(Constants.ORDER_SEARCH_PAGE_LIMIT);
        LambdaQueryWrapper<XxShopOrder> queryWrapper = new LambdaQueryWrapper<XxShopOrder>().eq(null != status, XxShopOrder::getOrderStatus, status)
                .eq(XxShopOrder::getUserId, user.getUserId()).orderByDesc(XxShopOrder::getCreateTime);
        //封装分页请求参数
        return ResultGenerator.genSuccessResult(xxShopOrderService.orderList(pageParam, queryWrapper));
    }

    /**
     * 根据订单号取消订单
     *
     * @param orderNo 订单号
     * @return 结果
     */
    @PutMapping("/{orderNo}/cancel")
    public Result<String> cancelOrder(@PathVariable("orderNo") String orderNo, @TokenToShopUser XxShopUser user) {
        if (xxShopOrderService.cancelOrder(orderNo, user.getUserId())) {
            return ResultGenerator.genSuccessResult("订单取消成功", null);
        } else {
            return ResultGenerator.genFailResult();
        }
    }

    /**
     * 根据订单号将订单确认收货
     *
     * @param orderNo 订单编号
     * @return 结果
     */
    @PutMapping("/{orderNo}/finish")
    public Result<String> finishOrder(@PathVariable("orderNo") String orderNo, @TokenToShopUser XxShopUser user) {
        if (xxShopOrderService.finishOrder(orderNo, user.getUserId())) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult();
        }
    }

    /**
     * 根据订单号模拟支付成功回调
     *
     * @param orderNo 订单号
     * @param payType 支付类型
     * @return 结果
     */
    @GetMapping("/paySuccess")
    public Result<String> paySuccess(@RequestParam("orderNo") @NotBlank(message = "不给订单号怎么知道哪个确认收货呀") String orderNo, @RequestParam("payType") @NotNull(message = "到底怎么付的款啊") int payType, @TokenToShopUser XxShopUser user) {
        if (xxShopOrderService.paySuccess(orderNo, payType, user.getUserId())) {
            return ResultGenerator.genSuccessResult("支付成功！", null);
        } else {
            return ResultGenerator.genFailResult();
        }
    }

}
