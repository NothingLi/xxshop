package top.bielai.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.bielai.shop.api.mall.vo.XxShopShoppingCartItemVO;
import top.bielai.shop.domain.XxShopOrder;
import top.bielai.shop.domain.XxShopUserAddress;

import java.util.List;

/**
 * @author Administrator
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
}
