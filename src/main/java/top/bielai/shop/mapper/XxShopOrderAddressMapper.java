package top.bielai.shop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.bielai.shop.domain.XxShopOrderAddress;

/**
 * @author bielai
 * @description 针对表【tb_xx_shop_order_address(订单收货地址关联表)】的数据库操作Mapper
 * @createDate 2022-11-30 13:58:39
 * @Entity top.bielai.shop.domain.XxShopOrderAddress
 */
@Mapper
public interface XxShopOrderAddressMapper extends BaseMapper<XxShopOrderAddress> {

}




