package top.bielai.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import top.bielai.shop.api.mall.vo.*;
import top.bielai.shop.common.*;
import top.bielai.shop.domain.*;
import top.bielai.shop.mapper.XxShopGoodsInfoMapper;
import top.bielai.shop.mapper.XxShopOrderMapper;
import top.bielai.shop.mapper.XxShopShoppingCartItemMapper;
import top.bielai.shop.service.XxShopOrderAddressService;
import top.bielai.shop.service.XxShopOrderItemService;
import top.bielai.shop.service.XxShopOrderService;
import top.bielai.shop.util.BeanUtil;
import top.bielai.shop.util.NumberUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    private final XxShopShoppingCartItemMapper cartItemMapper;

    private final XxShopGoodsInfoMapper goodsInfoMapper;

    private final XxShopOrderItemService orderItemService;

    private final XxShopOrderAddressService orderAddressService;

    public XxShopOrderServiceImpl(XxShopShoppingCartItemMapper cartItemMapper, XxShopGoodsInfoMapper goodsInfoMapper, XxShopOrderItemService orderItemService, XxShopOrderAddressService orderAddressService) {
        this.cartItemMapper = cartItemMapper;
        this.goodsInfoMapper = goodsInfoMapper;
        this.orderItemService = orderItemService;
        this.orderAddressService = orderAddressService;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveOrder(Long userId, XxShopUserAddress address, List<XxShopShoppingCartItemVO> shoppingCartItems) {
        List<Long> itemIdList = shoppingCartItems.stream().map(XxShopShoppingCartItemVO::getCartItemId).collect(Collectors.toList());
        Set<Long> goodIds = shoppingCartItems.stream().map(XxShopShoppingCartItemVO::getGoodsId).collect(Collectors.toSet());
        List<XxShopGoodsInfo> xxShopGoodsInfos = goodsInfoMapper.selectList(new LambdaQueryWrapper<XxShopGoodsInfo>().in(XxShopGoodsInfo::getGoodsId, goodIds));
        List<XxShopGoodsInfo> putDownGoods = xxShopGoodsInfos.stream().filter(good -> Constants.SELL_STATUS_DOWN == good.getGoodsSellStatus()).collect(Collectors.toList());
        if (!putDownGoods.isEmpty() || xxShopGoodsInfos.size() != goodIds.size()) {
            throw new XxShopException(ErrorEnum.CART_ITEM_ERROR);
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
            if (priceTotal.compareTo(BigDecimal.ZERO) < 1) {
                throw new XxShopException(ErrorEnum.PRICE_ERROR);
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
            throw new XxShopException(ErrorEnum.ERROR);
        } else {
            throw new XxShopException(ErrorEnum.CART_ITEM_ERROR);
        }
    }

    @Override
    public XxShopOrderDetailVO getDetailVO(Long orderId, Long userId, String orderNo) {
        XxShopOrder one = getOne(new LambdaQueryWrapper<XxShopOrder>()
                .eq(ObjectUtils.isNotEmpty(orderId), XxShopOrder::getOrderId, orderId)
                .eq(ObjectUtils.isNotEmpty(userId), XxShopOrder::getUserId, userId)
                .eq(StringUtils.isNotBlank(orderNo), XxShopOrder::getOrderNo, orderNo));
        if (org.apache.commons.lang3.ObjectUtils.isEmpty(one)) {
            throw new XxShopException(ErrorEnum.DATA_NOT_EXIST);
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
        return xxShopOrderDetailVO;
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
            throw new XxShopException(ErrorEnum.ORDER_NOT_EXIST);
        }
        if (order.getOrderStatus() == OrderStatusEnum.ORDER_SUCCESS.getOrderStatus()
                || order.getOrderStatus() == OrderStatusEnum.ORDER_CLOSED_BY_USER.getOrderStatus()
                || order.getOrderStatus() == OrderStatusEnum.ORDER_CLOSED_BY_EXPIRED.getOrderStatus()
                || order.getOrderStatus() == OrderStatusEnum.ORDER_CLOSED_BY_JUDGE.getOrderStatus()) {
            throw new XxShopException(ErrorEnum.ORDER_STATUS_ERROR);
        }
        order.setOrderStatus(OrderStatusEnum.ORDER_CLOSED_BY_USER.getOrderStatus());
        order.setUpdateTime(LocalDateTime.now());
        return baseMapper.updateById(order) > 0 && recoverStockNum(Collections.singletonList(order.getOrderId()));
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean finishOrder(String orderNo, Long userId) {
        XxShopOrder xxShopOrder = getOrderByNo(orderNo, userId);
        if (ObjectUtils.isEmpty(xxShopOrder)) {
            throw new XxShopException(ErrorEnum.ORDER_NOT_EXIST);
        }
        if (xxShopOrder.getOrderStatus() != OrderStatusEnum.ORDER_EXPRESS.getOrderStatus()) {
            throw new XxShopException(ErrorEnum.ORDER_STATUS_ERROR);
        }
        xxShopOrder.setOrderStatus(OrderStatusEnum.ORDER_SUCCESS.getOrderStatus());
        xxShopOrder.setUpdateTime(LocalDateTime.now());
        return baseMapper.updateById(xxShopOrder) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean paySuccess(String orderNo, int payType, Long userId) {
        XxShopOrder xxShopOrder = getOrderByNo(orderNo, userId);
        if (ObjectUtils.isEmpty(xxShopOrder)) {
            throw new XxShopException(ErrorEnum.ORDER_NOT_EXIST);
        }
        if (xxShopOrder.getOrderStatus() != OrderStatusEnum.ORDER_WAIT_PAY.getOrderStatus()) {
            throw new XxShopException(ErrorEnum.ORDER_STATUS_ERROR);
        }
        xxShopOrder.setOrderStatus(OrderStatusEnum.ORDER_PAID.getOrderStatus());
        xxShopOrder.setPayType((byte) payType);
        xxShopOrder.setPayStatus(PayStatusEnum.PAY_SUCCESS.getPayStatus());
        xxShopOrder.setPayTime(LocalDateTime.now());
        xxShopOrder.setUpdateTime(LocalDateTime.now());
        return baseMapper.updateById(xxShopOrder) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean checkDone(List<Long> ids) {
        return changeStatus(ids, 2, Collections.singletonList((byte) 1));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean checkOut(List<Long> ids) {
        return changeStatus(ids, 3, Arrays.asList((byte) 1, (byte) 2));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean closeOrder(List<Long> ids) {
        boolean close = changeStatus(ids, -3, Arrays.asList((byte) 0, (byte) 1, (byte) 2, (byte) 3));
        if (close) {
            return recoverStockNum(ids);
        }
        return false;
    }

    private XxShopOrder getOrderByNo(String orderNo, Long userId) {
        return baseMapper.selectOne(new LambdaQueryWrapper<XxShopOrder>().eq(XxShopOrder::getOrderNo, orderNo).eq(XxShopOrder::getUserId, userId));
    }

    private boolean changeStatus(List<Long> ids, int status, List<Byte> allowStatus) {
        List<XxShopOrder> xxShopOrders = baseMapper.selectBatchIds(ids);
        if (CollectionUtils.isEmpty(xxShopOrders) || xxShopOrders.size() != ids.size()) {
            throw new XxShopException(ErrorEnum.ORDER_STATUS_ERROR);
        }
        List<XxShopOrder> collect = xxShopOrders.stream().filter(order -> allowStatus.contains(order.getOrderStatus())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(collect)) {
            String errorOrder = collect.stream().map(XxShopOrder::getOrderNo).collect(Collectors.joining(","));
            throw new XxShopException("这些：" + errorOrder + " 订单的状态不允许这么操作噢");
        }
        xxShopOrders.forEach(order -> order.setOrderStatus((byte) status));
        return updateBatchById(xxShopOrders);
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
            throw new XxShopException(ErrorEnum.ERROR);
        }
        return true;
    }
}
