/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本软件已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2021 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package top.bielai.shop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.bielai.shop.api.mall.vo.XxShopOrderDetailVO;
import top.bielai.shop.api.mall.vo.XxShopOrderItemVO;
import top.bielai.shop.api.mall.vo.XxShopOrderListVO;
import top.bielai.shop.api.mall.vo.XxShopShoppingCartItemVO;
import top.bielai.shop.common.*;
import top.bielai.shop.dao.*;
import top.bielai.shop.entity.*;
import top.bielai.shop.service.XxShopOrderService;
import top.bielai.shop.util.BeanUtil;
import top.bielai.shop.util.NumberUtil;
import top.bielai.shop.util.PageQueryUtil;
import top.bielai.shop.util.PageResult;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class XxShopOrderServiceImpl implements XxShopOrderService {

    @Autowired
    private XxShopOrderMapper xxShopOrderMapper;
    @Autowired
    private XxShopOrderItemMapper xxShopOrderItemMapper;
    @Autowired
    private XxShopShoppingCartItemMapper xxShopShoppingCartItemMapper;
    @Autowired
    private XxShopGoodsMapper xxShopGoodsMapper;
    @Autowired
    private XxShopOrderAddressMapper xxShopOrderAddressMapper;

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
            xxShopOrderDetailVO.setOrderStatusString(XxShopOrderStatusEnum.getXxShopOrderStatusEnumByStatus(xxShopOrderDetailVO.getOrderStatus()).getName());
            xxShopOrderDetailVO.setPayTypeString(PayTypeEnum.getPayTypeEnumByType(xxShopOrderDetailVO.getPayType()).getName());
            xxShopOrderDetailVO.setXxShopOrderItemVOS(xxShopOrderItemVOS);
            return xxShopOrderDetailVO;
        } else {
            XxShopException.fail(ServiceResultEnum.ORDER_ITEM_NULL_ERROR.getResult());
            return null;
        }
    }

    @Override
    public XxShopOrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId) {
        XxShopOrder xxShopOrder = xxShopOrderMapper.selectByOrderNo(orderNo);
        if (xxShopOrder == null) {
            XxShopException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        if (!userId.equals(xxShopOrder.getUserId())) {
            XxShopException.fail(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }
        List<XxShopOrderItem> orderItems = xxShopOrderItemMapper.selectByOrderId(xxShopOrder.getOrderId());
        //获取订单项数据
        if (CollectionUtils.isEmpty(orderItems)) {
            XxShopException.fail(ServiceResultEnum.ORDER_ITEM_NOT_EXIST_ERROR.getResult());
        }
        List<XxShopOrderItemVO> xxShopOrderItemVOS = BeanUtil.copyList(orderItems, XxShopOrderItemVO.class);
        XxShopOrderDetailVO xxShopOrderDetailVO = new XxShopOrderDetailVO();
        BeanUtil.copyProperties(xxShopOrder, xxShopOrderDetailVO);
        xxShopOrderDetailVO.setOrderStatusString(XxShopOrderStatusEnum.getXxShopOrderStatusEnumByStatus(xxShopOrderDetailVO.getOrderStatus()).getName());
        xxShopOrderDetailVO.setPayTypeString(PayTypeEnum.getPayTypeEnumByType(xxShopOrderDetailVO.getPayType()).getName());
        xxShopOrderDetailVO.setXxShopOrderItemVOS(xxShopOrderItemVOS);
        return xxShopOrderDetailVO;
    }


    @Override
    public PageResult getMyOrders(PageQueryUtil pageUtil) {
        int total = xxShopOrderMapper.getTotalXxShopOrders(pageUtil);
        List<XxShopOrder> xxShopOrders = xxShopOrderMapper.findXxShopOrderList(pageUtil);
        List<XxShopOrderListVO> orderListVOS = new ArrayList<>();
        if (total > 0) {
            //数据转换 将实体类转成vo
            orderListVOS = BeanUtil.copyList(xxShopOrders, XxShopOrderListVO.class);
            //设置订单状态中文显示值
            for (XxShopOrderListVO xxShopOrderListVO : orderListVOS) {
                xxShopOrderListVO.setOrderStatusString(XxShopOrderStatusEnum.getXxShopOrderStatusEnumByStatus(xxShopOrderListVO.getOrderStatus()).getName());
            }
            List<Long> orderIds = xxShopOrders.stream().map(XxShopOrder::getOrderId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(orderIds)) {
                List<XxShopOrderItem> orderItems = xxShopOrderItemMapper.selectByOrderIds(orderIds);
                Map<Long, List<XxShopOrderItem>> itemByOrderIdMap = orderItems.stream().collect(groupingBy(XxShopOrderItem::getOrderId));
                for (XxShopOrderListVO xxShopOrderListVO : orderListVOS) {
                    //封装每个订单列表对象的订单项数据
                    if (itemByOrderIdMap.containsKey(xxShopOrderListVO.getOrderId())) {
                        List<XxShopOrderItem> orderItemListTemp = itemByOrderIdMap.get(xxShopOrderListVO.getOrderId());
                        //将XxShopOrderItem对象列表转换成XxShopOrderItemVO对象列表
                        List<XxShopOrderItemVO> xxShopOrderItemVOS = BeanUtil.copyList(orderItemListTemp, XxShopOrderItemVO.class);
                        xxShopOrderListVO.setXxShopOrderItemVOS(xxShopOrderItemVOS);
                    }
                }
            }
        }
        PageResult pageResult = new PageResult(orderListVOS, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    @Transactional
    public String cancelOrder(String orderNo, Long userId) {
        XxShopOrder xxShopOrder = xxShopOrderMapper.selectByOrderNo(orderNo);
        if (xxShopOrder != null) {
            //验证是否是当前userId下的订单，否则报错
            if (!userId.equals(xxShopOrder.getUserId())) {
                XxShopException.fail(ServiceResultEnum.NO_PERMISSION_ERROR.getResult());
            }
            //订单状态判断
            if (xxShopOrder.getOrderStatus().intValue() == XxShopOrderStatusEnum.ORDER_SUCCESS.getOrderStatus()
                    || xxShopOrder.getOrderStatus().intValue() == XxShopOrderStatusEnum.ORDER_CLOSED_BY_MALLUSER.getOrderStatus()
                    || xxShopOrder.getOrderStatus().intValue() == XxShopOrderStatusEnum.ORDER_CLOSED_BY_EXPIRED.getOrderStatus()
                    || xxShopOrder.getOrderStatus().intValue() == XxShopOrderStatusEnum.ORDER_CLOSED_BY_JUDGE.getOrderStatus()) {
                return ServiceResultEnum.ORDER_STATUS_ERROR.getResult();
            }
            //修改订单状态&&恢复库存
            if (xxShopOrderMapper.closeOrder(Collections.singletonList(xxShopOrder.getOrderId()), XxShopOrderStatusEnum.ORDER_CLOSED_BY_MALLUSER.getOrderStatus()) > 0 && recoverStockNum(Collections.singletonList(xxShopOrder.getOrderId()))) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public String finishOrder(String orderNo, Long userId) {
        XxShopOrder xxShopOrder = xxShopOrderMapper.selectByOrderNo(orderNo);
        if (xxShopOrder != null) {
            //验证是否是当前userId下的订单，否则报错
            if (!userId.equals(xxShopOrder.getUserId())) {
                return ServiceResultEnum.NO_PERMISSION_ERROR.getResult();
            }
            //订单状态判断 非出库状态下不进行修改操作
            if (xxShopOrder.getOrderStatus().intValue() != XxShopOrderStatusEnum.ORDER_EXPRESS.getOrderStatus()) {
                return ServiceResultEnum.ORDER_STATUS_ERROR.getResult();
            }
            xxShopOrder.setOrderStatus((byte) XxShopOrderStatusEnum.ORDER_SUCCESS.getOrderStatus());
            xxShopOrder.setUpdateTime(new Date());
            if (xxShopOrderMapper.updateByPrimaryKeySelective(xxShopOrder) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public String paySuccess(String orderNo, int payType) {
        XxShopOrder xxShopOrder = xxShopOrderMapper.selectByOrderNo(orderNo);
        if (xxShopOrder != null) {
            //订单状态判断 非待支付状态下不进行修改操作
            if (xxShopOrder.getOrderStatus().intValue() != XxShopOrderStatusEnum.ORDER_PRE_PAY.getOrderStatus()) {
                return ServiceResultEnum.ORDER_STATUS_ERROR.getResult();
            }
            xxShopOrder.setOrderStatus((byte) XxShopOrderStatusEnum.ORDER_PAID.getOrderStatus());
            xxShopOrder.setPayType((byte) payType);
            xxShopOrder.setPayStatus((byte) PayStatusEnum.PAY_SUCCESS.getPayStatus());
            xxShopOrder.setPayTime(new Date());
            xxShopOrder.setUpdateTime(new Date());
            if (xxShopOrderMapper.updateByPrimaryKeySelective(xxShopOrder) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    @Transactional
    public String saveOrder(ShopUser loginShopUser, ShopUserAddress address, List<XxShopShoppingCartItemVO> myShoppingCartItems) {
        List<Long> itemIdList = myShoppingCartItems.stream().map(XxShopShoppingCartItemVO::getCartItemId).collect(Collectors.toList());
        List<Long> goodsIds = myShoppingCartItems.stream().map(XxShopShoppingCartItemVO::getGoodsId).collect(Collectors.toList());
        List<XxShopGoods> xxShopGoods = xxShopGoodsMapper.selectByPrimaryKeys(goodsIds);
        //检查是否包含已下架商品
        List<XxShopGoods> goodsListNotSelling = xxShopGoods.stream()
                .filter(xxShopGoodsTemp -> xxShopGoodsTemp.getGoodsSellStatus() != Constants.SELL_STATUS_UP)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(goodsListNotSelling)) {
            //goodsListNotSelling 对象非空则表示有下架商品
            XxShopException.fail(goodsListNotSelling.get(0).getGoodsName() + "已下架，无法生成订单");
        }
        Map<Long, XxShopGoods> xxShopGoodsMap = xxShopGoods.stream().collect(Collectors.toMap(XxShopGoods::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
        //判断商品库存
        for (XxShopShoppingCartItemVO shoppingCartItemVO : myShoppingCartItems) {
            //查出的商品中不存在购物车中的这条关联商品数据，直接返回错误提醒
            if (!xxShopGoodsMap.containsKey(shoppingCartItemVO.getGoodsId())) {
                XxShopException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
            }
            //存在数量大于库存的情况，直接返回错误提醒
            if (shoppingCartItemVO.getGoodsCount() > xxShopGoodsMap.get(shoppingCartItemVO.getGoodsId()).getStockNum()) {
                XxShopException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
            }
        }
        //删除购物项
        if (!CollectionUtils.isEmpty(itemIdList) && !CollectionUtils.isEmpty(goodsIds) && !CollectionUtils.isEmpty(xxShopGoods)) {
            if (xxShopShoppingCartItemMapper.deleteBatch(itemIdList, loginShopUser.getUserId()) > 0) {
                List<StockNumDTO> stockNumDTOS = BeanUtil.copyList(myShoppingCartItems, StockNumDTO.class);
                int updateStockNumResult = xxShopGoodsMapper.updateStockNum(stockNumDTOS);
                if (updateStockNumResult < 1) {
                    XxShopException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
                }
                //生成订单号
                String orderNo = NumberUtil.genOrderNo();
                int priceTotal = 0;
                //保存订单
                XxShopOrder xxShopOrder = new XxShopOrder();
                xxShopOrder.setOrderNo(orderNo);
                xxShopOrder.setUserId(loginShopUser.getUserId());
                //总价
                for (XxShopShoppingCartItemVO xxShopShoppingCartItemVO : myShoppingCartItems) {
                    priceTotal += xxShopShoppingCartItemVO.getGoodsCount() * xxShopShoppingCartItemVO.getSellingPrice();
                }
                if (priceTotal < 1) {
                    XxShopException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
                }
                xxShopOrder.setTotalPrice(priceTotal);
                String extraInfo = "";
                xxShopOrder.setExtraInfo(extraInfo);
                //生成订单项并保存订单项纪录
                if (xxShopOrderMapper.insertSelective(xxShopOrder) > 0) {
                    //生成订单收货地址快照，并保存至数据库
                    XxShopOrderAddress xxShopOrderAddress = new XxShopOrderAddress();
                    BeanUtil.copyProperties(address, xxShopOrderAddress);
                    xxShopOrderAddress.setOrderId(xxShopOrder.getOrderId());
                    //生成所有的订单项快照，并保存至数据库
                    List<XxShopOrderItem> xxShopOrderItems = new ArrayList<>();
                    for (XxShopShoppingCartItemVO xxShopShoppingCartItemVO : myShoppingCartItems) {
                        XxShopOrderItem xxShopOrderItem = new XxShopOrderItem();
                        //使用BeanUtil工具类将xxShopShoppingCartItemVO中的属性复制到xxShopOrderItem对象中
                        BeanUtil.copyProperties(xxShopShoppingCartItemVO, xxShopOrderItem);
                        //XxShopOrderMapper文件insert()方法中使用了useGeneratedKeys因此orderId可以获取到
                        xxShopOrderItem.setOrderId(xxShopOrder.getOrderId());
                        xxShopOrderItems.add(xxShopOrderItem);
                    }
                    //保存至数据库
                    if (xxShopOrderItemMapper.insertBatch(xxShopOrderItems) > 0 && xxShopOrderAddressMapper.insertSelective(xxShopOrderAddress) > 0) {
                        //所有操作成功后，将订单号返回，以供Controller方法跳转到订单详情
                        return orderNo;
                    }
                    XxShopException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
                }
                XxShopException.fail(ServiceResultEnum.DB_ERROR.getResult());
            }
            XxShopException.fail(ServiceResultEnum.DB_ERROR.getResult());
        }
        XxShopException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
        return ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult();
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
        List<XxShopOrder> orders = xxShopOrderMapper.selectByPrimaryKeys(Arrays.asList(ids));
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
                if (xxShopOrderMapper.checkDone(Arrays.asList(ids)) > 0) {
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
        List<XxShopOrder> orders = xxShopOrderMapper.selectByPrimaryKeys(Arrays.asList(ids));
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
                if (xxShopOrderMapper.checkOut(Arrays.asList(ids)) > 0) {
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
        List<XxShopOrder> orders = xxShopOrderMapper.selectByPrimaryKeys(Arrays.asList(ids));
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
                if (xxShopOrderMapper.closeOrder(Arrays.asList(ids), XxShopOrderStatusEnum.ORDER_CLOSED_BY_JUDGE.getOrderStatus()) > 0 && recoverStockNum(Arrays.asList(ids))) {
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
     * 恢复库存
     *
     * @param orderIds
     * @return
     */
    public Boolean recoverStockNum(List<Long> orderIds) {
        //查询对应的订单项
        List<XxShopOrderItem> xxShopOrderItems = xxShopOrderItemMapper.selectByOrderIds(orderIds);
        //获取对应的商品id和商品数量并赋值到StockNumDTO对象中
        List<StockNumDTO> stockNumDTOS = BeanUtil.copyList(xxShopOrderItems, StockNumDTO.class);
        //执行恢复库存的操作
        int updateStockNumResult = xxShopGoodsMapper.recoverStockNum(stockNumDTOS);
        if (updateStockNumResult < 1) {
            XxShopException.fail(ServiceResultEnum.CLOSE_ORDER_ERROR.getResult());
            return false;
        } else {
            return true;
        }
    }
}