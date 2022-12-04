package top.bielai.shop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.bielai.shop.domain.XxShopUserAddress;

/**
 * @author bielai
 * @description 针对表【tb_xx_shop_user_address(收货地址表)】的数据库操作Mapper
 * @createDate 2022-11-30 13:58:39
 * @Entity top.bielai.shop.domain.XxShopUserAddress
 */
@Mapper
public interface XxShopUserAddressMapper extends BaseMapper<XxShopUserAddress> {

}




