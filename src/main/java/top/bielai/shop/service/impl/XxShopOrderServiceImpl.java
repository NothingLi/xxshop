package top.bielai.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import top.bielai.shop.api.mall.vo.XxShopOrderDetailVO;
import top.bielai.shop.api.mall.vo.XxShopOrderItemVO;
import top.bielai.shop.api.mall.vo.XxShopOrderListVO;
import top.bielai.shop.api.mall.vo.XxShopShoppingCartItemVO;
import top.bielai.shop.common.*;
import top.bielai.shop.domain.*;
import top.bielai.shop.entity.*;
import top.bielai.shop.mapper.XxShopGoodsInfoMapper;
import top.bielai.shop.mapper.XxShopOrderMapper;
import top.bielai.shop.mapper.XxShopShoppingCartItemMapper;
import top.bielai.shop.service.XxShopOrderAddressService;
import top.bielai.shop.service.XxShopOrderItemService;
import top.bielai.shop.service.XxShopOrderService;
import top.bielai.shop.util.BeanUtil;
import top.bielai.shop.util.NumberUtil;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * @author bielai
 * @description 针对表【tb_xx_shop_order】的数据库操作Service实现
 * @createDate 2022-11-30 13:58:39
 */
@Service
public class XxShopOrderServiceImpl extends ServiceImpl<XxShopOrderMapper, XxShopOrder>
        implements XxShopOrderService {

    @Autowired
    private XxShopShoppingCartItemMapper cartItemMapper;

    @Autowired
    private XxShopGoodsInfoMapper goodsInfoMapper;

    @Autowired
    private XxShopOrderItemService orderItemService;

    @Autowired
    private XxShopOrderAddressService orderAddressService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveOrder(Long userId, XxShopUserAddress address, List<XxShopShoppingCartItemVO> shoppingCartItems) {
        List<Long> itemIdList = shoppingCartItems.stream().map(XxShopShoppingCartItemVO::getCartItemId).collect(Collectors.toList());
        Set<Long> goodIds = shoppingCartItems.stream().map(XxShopShoppingCartItemVO::getGoodsId).collect(Collectors.toSet());
        List<XxShopGoodsInfo> xxShopGoodsInfos = goodsInfoMapper.selectList(new LambdaQueryWrapper<XxShopGoodsInfo>().in(XxShopGoodsInfo::getGoodsId, goodIds));
        List<XxShopGoodsInfo> putDownGoods = xxShopGoodsInfos.stream().filter(good -> Constants.SELL_STATUS_DOWN == good.getGoodsSellStatus()).collect(Collectors.toList());
        if (!putDownGoods.isEmpty() || xxShopGoodsInfos.size() != goodIds.size()) {
            XxShopException.fail(ErrorEnum.CART_ITEM_ERROR);
        }
        //删除购物项
        if (cartItemMapper.delete(new LambdaQueryWrapper<XxShopShoppingCartItem>().in(XxShopShoppingCartItem::getCartItemId, itemIdList).eq(XxShopShoppingCartItem::getUserId, userId)) > 0) {
            shoppingCartItems.forEach(item -> goodsInfoMapper.reduceStockNum(item.getGoodsId(), item.getGoodsCount()));
            //生成订单号
            String orderNo = NumberUtil.genOrderNo();
            //保存订单
            XxShopOrder xxShopOrder = new XxShopOrder();
            xxShopOrder.setOrderNo(orderNo);
            xxShopOrder.setUserId(userId);
            BigDecimal priceTotal = BigDecimal.ZERO;
            //总价
            for (XxShopShoppingCartItemVO xxShopShoppingCartItemVO : shoppingCartItems) {
                priceTotal = priceTotal.add(xxShopShoppingCartItemVO.getSellingPrice().multiply(BigDecimal.valueOf(xxShopShoppingCartItemVO.getGoodsCount())));
            }
            if (BigDecimal.ZERO.compareTo(priceTotal) <= 0) {
                XxShopException.fail(ErrorEnum.PRICE_ERROR);
            }
            xxShopOrder.setTotalPrice(priceTotal);
            xxShopOrder.setPayStatus(PayStatusEnum.WAIT_PAY.getPayStatus());
            xxShopOrder.setOrderStatus(OrderStatusEnum.ORDER_WAIT_PAY.getOrderStatus());
            xxShopOrder.setExtraInfo("");
            //生成订单项并保存订单项纪录
            if (baseMapper.insert(xxShopOrder) > 0) {
                //生成订单收货地址快照，并保存至数据库
                XxShopOrderAddress xxShopOrderAddress = new XxShopOrderAddress();
                BeanUtil.copyProperties(address, xxShopOrderAddress);
                xxShopOrderAddress.setOrderId(xxShopOrder.getOrderId());
                //生成所有的订单项快照，并保存至数据库
                List<XxShopOrderItem> xxShopOrderItems = new ArrayList<>();
                for (XxShopShoppingCartItemVO xxShopShoppingCartItemVO : shoppingCartItems) {
                    XxShopOrderItem xxShopOrderItem = new XxShopOrderItem();
                    BeanUtil.copyProperties(xxShopShoppingCartItemVO, xxShopOrderItem);
                    xxShopOrderItem.setOrderId(xxShopOrder.getOrderId());
                    xxShopOrderItems.add(xxShopOrderItem);
                }
                //保存至数据库
                if (orderItemService.saveBatch(xxShopOrderItems) && orderAddressService.save(xxShopOrderAddress)) {
                    return orderNo;
                }
            }
            XxShopException.fail(ErrorEnum.ERROR);
        } else {
            XxShopException.fail(ErrorEnum.CART_ITEM_ERROR);
        }
        return null;
    }


    @Override
    public Page<XxShopOrderListVO> orderList(Page<XxShopOrder> pageParam, LambdaQueryWrapper<XxShopOrder> queryWrapper) {
        Page<XxShopOrder> page = page(pageParam, queryWrapper);
        Page<XxShopOrderListVO> result = new Page<>();
        BeanUtils.copyProperties(page, result, "records");
        if (!CollectionUtils.isEmpty(page.getRecords())) {
            List<XxShopOrderListVO> voList = BeanUtil.copyList(page.getRecords(), XxShopOrderListVO.class);
            //设置订单状态中文显示值
            for (XxShopOrderListVO xxShopOrderListVO : voList) {
                xxShopOrderListVO.setOrderStatusString(OrderStatusEnum.getXxShopOrderStatusEnumByStatus(xxShopOrderListVO.getOrderStatus()).getName());
            }
            List<Long> orderIds = page.getRecords().stream().map(XxShopOrder::getOrderId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(orderIds)) {
                List<XxShopOrderItem> orderItems = orderItemService.list(new LambdaQueryWrapper<XxShopOrderItem>().in(XxShopOrderItem::getOrderId, orderIds));
                Map<Long, List<XxShopOrderItem>> itemByOrderIdMap = orderItems.stream().collect(groupingBy(XxShopOrderItem::getOrderId));
                for (XxShopOrderListVO xxShopOrderListVO : voList) {
                    //封装每个订单列表对象的订单项数据
                    if (itemByOrderIdMap.containsKey(xxShopOrderListVO.getOrderId())) {
                        List<XxShopOrderItem> orderItemListTemp = itemByOrderIdMap.get(xxShopOrderListVO.getOrderId());
                        List<XxShopOrderItemVO> xxShopOrderItemVOList = BeanUtil.copyList(orderItemListTemp, XxShopOrderItemVO.class);
                        xxShopOrderListVO.setXxShopOrderItemVOList(xxShopOrderItemVOList);
                    }
                }
            }
            result.setRecords(voList);
        }
        return result;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelOrder(String orderNo, Long userId) {
        XxShopOrder order = getOrderByNo(orderNo, userId);
        if (ObjectUtils.isEmpty(order)) {
            XxShopException.fail(ErrorEnum.ORDER_NOT_EXIST);
        }
        if (order.getOrderStatus() == OrderStatusEnum.ORDER_SUCCESS.getOrderStatus()
                || order.getOrderStatus() == OrderStatusEnum.ORDER_CLOSED_BY_USER.getOrderStatus()
                || order.getOrderStatus() == OrderStatusEnum.ORDER_CLOSED_BY_EXPIRED.getOrderStatus()
                || order.getOrderStatus() == OrderStatusEnum.ORDER_CLOSED_BY_JUDGE.getOrderStatus()) {
            XxShopException.fail(ErrorEnum.ORDER_STATUS_ERROR);
        }
        order.setOrderStatus(OrderStatusEnum.ORDER_CLOSED_BY_USER.getOrderStatus());
        return baseMapper.updateById(order) > 0 && recoverStockNum(Collections.singletonList(order.getOrderId()));
    }

    private XxShopOrder getOrderByNo(String orderNo, Long userId) {
        return baseMapper.selectOne(new LambdaQueryWrapper<XxShopOrder>().eq(XxShopOrder::getOrderNo, orderNo).eq(XxShopOrder::getUserId, userId));
    }


    @Override
    public XxShopOrderDetailVO getOrderDetailByOrderId(Long orderId) {
        XxShopOrder xxShopOrder = xxShopOrderMapper.selectByPrimaryKey(orderId);
        if (xxShopOrder == null) {
            XxShopException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        List<XxShopOrderItem> orderItems = xxShopOrderItemMapper.selectByOrderId(xxShopOrder.getOrderId());
        //获取订单项数据
        if (!CollectionUtils.isEmpty(orderItems)) {
            List<XxShopOrderItemVO> xxShopOrderItemVOS = BeanUtil.copyList(orderItems, XxShopOrderItemVO.class);
            XxShopOrderDetailVO xxShopOrderDetailVO = new XxShopOrderDetailVO();
            BeanUtil.copyProperties(xxShopOrder, xxShopOrderDetailVO);
            xxShopOrderDetailVO.setOrderStatusString(OrderStatusEnum.getXxShopOrderStatusEnumByStatus(xxShopOrderDetailVO.getOrderStatus()).getName());
            xxShopOrderDetailVO.setPayTypeString(PayTypeEnum.getPayTypeEnumByType(xxShopOrderDetailVO.getPayType()).getName());
            xxShopOrderDetailVO.setXxShopOrderItemVOS(xxShopOrderItemVOS);
            return xxShopOrderDetailVO;
        } else {
            XxShopException.fail(ServiceResultEnum.ORDER_ITEM_NULL_ERROR.getResult());
            return null;
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean finishOrder(String orderNo, Long userId) {
        XxShopOrder xxShopOrder = getOrderByNo(orderNo, userId);
        if (ObjectUtils.isEmpty(xxShopOrder)) {
            XxShopException.fail(ErrorEnum.ORDER_NOT_EXIST);
        }
        if (xxShopOrder.getOrderStatus() != OrderStatusEnum.ORDER_EXPRESS.getOrderStatus()) {
            XxShopException.fail(ErrorEnum.ORDER_STATUS_ERROR);
        }
        xxShopOrder.setOrderStatus(OrderStatusEnum.ORDER_SUCCESS.getOrderStatus());
        return baseMapper.updateById(xxShopOrder) > 0;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean paySuccess(String orderNo, int payType, Long userId) {
        XxShopOrder xxShopOrder = getOrderByNo(orderNo, userId);
        if (ObjectUtils.isEmpty(xxShopOrder)) {
            XxShopException.fail(ErrorEnum.ORDER_NOT_EXIST);
        }
        if (xxShopOrder.getOrderStatus() != OrderStatusEnum.ORDER_WAIT_PAY.getOrderStatus()) {
            XxShopException.fail(ErrorEnum.ORDER_STATUS_ERROR);
        }
        xxShopOrder.setOrderStatus(OrderStatusEnum.ORDER_PAID.getOrderStatus());
        xxShopOrder.setPayType(payType);
        xxShopOrder.setPayStatus(PayStatusEnum.PAY_SUCCESS.getPayStatus());
        xxShopOrder.setPayTime(new Date());
        return baseMapper.updateById(xxShopOrder) > 0;
    }

    @Override
    public PageResult getXxShopOrdersPage(PageQueryUtil pageUtil) {
        List<XxShopOrder> xxShopOrders = xxShopOrderMapper.findXxShopOrderList(pageUtil);
        int total = xxShopOrderMapper.getTotalXxShopOrders(pageUtil);
        PageResult pageResult = new PageResult(xxShopOrders, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    @Transactional
    public String updateOrderInfo(XxShopOrder xxShopOrder) {
        XxShopOrder temp = xxShopOrderMapper.selectByPrimaryKey(xxShopOrder.getOrderId());
        //不为空且orderStatus>=0且状态为出库之前可以修改部分信息
        if (temp != null && temp.getOrderStatus() >= 0 && temp.getOrderStatus() < 3) {
            temp.setTotalPrice(xxShopOrder.getTotalPrice());
            temp.setUpdateTime(new Date());
            if (xxShopOrderMapper.updateByPrimaryKeySelective(temp) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            }
            return ServiceResultEnum.DB_ERROR.getResult();
        }
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String checkDone(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        List<XxShopOrder> orders = xxShopOrderMapper.selectByPrimaryKeys(Collections.singletonList(ids));
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            for (XxShopOrder xxShopOrder : orders) {
                if (xxShopOrder.getIsDeleted() == 1) {
                    errorOrderNos += xxShopOrder.getOrderNo() + " ";
                    continue;
                }
                if (xxShopOrder.getOrderStatus() != 1) {
                    errorOrderNos += xxShopOrder.getOrderNo() + " ";
                }
            }
            if (StringUtils.isEmpty(errorOrderNos)) {
                //订单状态正常 可以执行配货完成操作 修改订单状态和更新时间
                if (xxShopOrderMapper.checkDone(Collections.singletonList(ids)) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行出库操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单的状态不是支付成功无法执行出库操作";
                } else {
                    return "你选择了太多状态不是支付成功的订单，无法执行配货完成操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String checkOut(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        List<XxShopOrder> orders = xxShopOrderMapper.selectByPrimaryKeys(Collections.singletonList(ids));
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            for (XxShopOrder xxShopOrder : orders) {
                if (xxShopOrder.getIsDeleted() == 1) {
                    errorOrderNos += xxShopOrder.getOrderNo() + " ";
                    continue;
                }
                if (xxShopOrder.getOrderStatus() != 1 && xxShopOrder.getOrderStatus() != 2) {
                    errorOrderNos += xxShopOrder.getOrderNo() + " ";
                }
            }
            if (StringUtils.isEmpty(errorOrderNos)) {
                //订单状态正常 可以执行出库操作 修改订单状态和更新时间
                if (xxShopOrderMapper.checkOut(Collections.singletonList(ids)) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行出库操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单的状态不是支付成功或配货完成无法执行出库操作";
                } else {
                    return "你选择了太多状态不是支付成功或配货完成的订单，无法执行出库操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String closeOrder(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        List<XxShopOrder> orders = xxShopOrderMapper.selectByPrimaryKeys(Collections.singletonList(ids));
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            for (XxShopOrder xxShopOrder : orders) {
                // isDeleted=1 一定为已关闭订单
                if (xxShopOrder.getIsDeleted() == 1) {
                    errorOrderNos += xxShopOrder.getOrderNo() + " ";
                    continue;
                }
                //已关闭或者已完成无法关闭订单
                if (xxShopOrder.getOrderStatus() == 4 || xxShopOrder.getOrderStatus() < 0) {
                    errorOrderNos += xxShopOrder.getOrderNo() + " ";
                }
            }
            if (StringUtils.isEmpty(errorOrderNos)) {
                //订单状态正常 可以执行关闭操作 修改订单状态和更新时间&&恢复库存
                if (xxShopOrderMapper.closeOrder(Collections.singletonList(ids), OrderStatusEnum.ORDER_CLOSED_BY_JUDGE.getOrderStatus()) > 0 && recoverStockNum(Arrays.asList(ids))) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行关闭操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单不能执行关闭操作";
                } else {
                    return "你选择的订单不能执行关闭操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    public List<XxShopOrderItemVO> getOrderItems(Long orderId) {
        XxShopOrder xxShopOrder = xxShopOrderMapper.selectByPrimaryKey(orderId);
        if (xxShopOrder != null) {
            List<XxShopOrderItem> orderItems = xxShopOrderItemMapper.selectByOrderId(xxShopOrder.getOrderId());
            //获取订单项数据
            if (!CollectionUtils.isEmpty(orderItems)) {
                List<XxShopOrderItemVO> xxShopOrderItemVOS = BeanUtil.copyList(orderItems, XxShopOrderItemVO.class);
                return xxShopOrderItemVOS;
            }
        }
        return null;
    }

    /**
     * 根据订单号恢复库存
     *
     * @param orderIds 订单号
     * @return 结果
     */
    public Boolean recoverStockNum(List<Long> orderIds) {
        //查询对应的订单项
        List<XxShopOrderItem> xxShopOrderItems = orderItemService.list(new LambdaQueryWrapper<XxShopOrderItem>().in(XxShopOrderItem::getOrderId, orderIds));
        try {
            xxShopOrderItems.forEach(item -> goodsInfoMapper.recoverStockNum(item.getGoodsId(), item.getGoodsCount()));
        } catch (Exception e) {
            XxShopException.fail(ErrorEnum.ERROR);
        }
        return true;
    }
}
