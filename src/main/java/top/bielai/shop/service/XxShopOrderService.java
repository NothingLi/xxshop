package top.bielai.shop.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import top.bielai.shop.api.mall.vo.XxShopOrderDetailVO;
import top.bielai.shop.api.mall.vo.XxShopOrderListVO;
import top.bielai.shop.api.mall.vo.XxShopShoppingCartItemVO;
import top.bielai.shop.domain.XxShopOrder;
import top.bielai.shop.domain.XxShopUserAddress;

import java.util.List;

/**
 * @author bielai
 * @description 针对表【tb_xx_shop_order】的数据库操作Service
 * @createDate 2022-11-30 13:58:39
 */
public interface XxShopOrderService extends IService<XxShopOrder> {

    /**
     * 生成订单
     *
     * @param userId       用户id
     * @param address      收货地址
     * @param itemsForSave 购物车项
     * @return 订单编号
     */
    String saveOrder(Long userId, XxShopUserAddress address, List<XxShopShoppingCartItemVO> itemsForSave);

    /**
     * 查询订单详情
     *
     * @param orderId 订单id
     * @param userId  用户id
     * @param orderNo 订单编号
     * @return 订单详情
     */
    XxShopOrderDetailVO getDetailVO(Long orderId, Long userId, String orderNo);

    /**
     * 分页查询订单
     *
     * @param pageParam    分页参数
     * @param queryWrapper 查询条件
     * @return 分页结果
     */
    Page<XxShopOrderListVO> orderList(Page<XxShopOrder> pageParam, LambdaQueryWrapper<XxShopOrder> queryWrapper);

    /**
     * 根据订单号和用户id取消订单
     *
     * @param orderNo 订单号
     * @param userId  用户id
     * @return 结果
     */
    boolean cancelOrder(String orderNo, Long userId);

    /**
     * 模拟支付成功，修改订单状态
     *
     * @param orderNo 订单编号
     * @param payType 支付类型
     * @param userId  用户id
     * @return 结果
     */
    boolean paySuccess(String orderNo, int payType, Long userId);

    /**
     * 根据订单号和用户id完成订单
     *
     * @param orderNo 订单号
     * @param userId  用户id
     * @return 结果
     */
    boolean finishOrder(String orderNo, Long userId);

    /**
     * 批量修改订单配货完成
     *
     * @param ids 订单id
     * @return 结果
     */
    boolean checkDone(List<Long> ids);

    /**
     * 批量修改订单已出库
     *
     * @param ids 订单id
     * @return 结果
     */
    boolean checkOut(List<Long> ids);

    /**
     * 批量修改订单商家关闭
     *
     * @param ids 订单id
     * @return 结果
     */
    boolean closeOrder(List<Long> ids);

}
