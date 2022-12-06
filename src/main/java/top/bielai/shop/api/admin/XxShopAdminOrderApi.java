package top.bielai.shop.api.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.bielai.shop.api.admin.param.BatchIdParam;
import top.bielai.shop.api.mall.vo.XxShopOrderDetailVO;
import top.bielai.shop.domain.XxShopOrder;
import top.bielai.shop.service.XxShopOrderService;
import top.bielai.shop.util.Result;
import top.bielai.shop.util.ResultGenerator;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Arrays;

/**
 * 后台管理系统订单模块接口
 *
 * @author bielai
 */
@Valid
@Validated
@RestController
@RequestMapping("/manage-api/v2/orders")
public class XxShopAdminOrderApi {

    @Resource
    private XxShopOrderService xxShopOrderService;

    /**
     * 分页查询订单列表
     *
     * @param pageNumber  页码
     * @param pageSize    页数
     * @param orderNo     订单编号
     * @param orderStatus 订单状态
     * @return 分页数据
     */
    @GetMapping
    public Result<Page<XxShopOrder>> page(@RequestParam @Min(value = 1, message = "第几页的数据呀") Integer pageNumber,
                                          @RequestParam @Min(value = 10, message = "每页几条啊") Integer pageSize,
                                          @RequestParam(required = false) String orderNo,
                                          @RequestParam(required = false) Integer orderStatus) {

        return ResultGenerator.genSuccessResult(xxShopOrderService.page(new Page<>(pageNumber, pageSize),
                new LambdaQueryWrapper<XxShopOrder>().eq(StringUtils.isNotBlank(orderNo), XxShopOrder::getOrderNo, orderNo)
                        .eq(ObjectUtils.isNotEmpty(orderStatus), XxShopOrder::getOrderStatus, orderStatus)
                        .orderByDesc(XxShopOrder::getCreateTime)));
    }

    /**
     * 根据id查询订单详情
     *
     * @param orderId 订单id
     * @return 订单详情
     */
    @GetMapping("/{orderId}")
    public Result<XxShopOrderDetailVO> detail(@PathVariable("orderId") Long orderId) {
        return ResultGenerator.genSuccessResult(xxShopOrderService.getDetailVO(orderId, null, null));
    }

    /**
     * 修改订单状态为配货成功
     *
     * @param batchIdParam id数组
     * @return 结果
     */
    @PutMapping("/checkDone")
    public Result<String> checkDone(@Validated @RequestBody BatchIdParam batchIdParam) {
        if (xxShopOrderService.checkDone(Arrays.asList(batchIdParam.getIds()))) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult();
        }
    }

    /**
     * 修改订单状态为已出库
     *
     * @param batchIdParam id数组
     * @return 结果
     */
    @PutMapping("/checkOut")
    public Result<String> checkOut(@Validated @RequestBody BatchIdParam batchIdParam) {
        if (xxShopOrderService.checkOut(Arrays.asList(batchIdParam.getIds()))) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult();
        }
    }

    /**
     * 修改订单状态为商家关闭
     *
     * @param batchIdParam id数组
     * @return 结果
     */
    @PutMapping("/close")
    public Result<String> closeOrder(@Validated @RequestBody BatchIdParam batchIdParam) {
        if (xxShopOrderService.closeOrder(Arrays.asList(batchIdParam.getIds()))) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult();
        }
    }
}