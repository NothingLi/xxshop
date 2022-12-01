package top.bielai.shop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.bielai.shop.domain.XxShopGoodsInfo;

/**
 * @author Administrator
 * @description 针对表【tb_xx_shop_goods_info】的数据库操作Mapper
 * @createDate 2022-11-30 13:58:39
 * @Entity top.bielai.shop.domain.XxShopGoodsInfo
 */
@Mapper
public interface XxShopGoodsInfoMapper extends BaseMapper<XxShopGoodsInfo> {
    int updateStockNum(long id, int stockNum);

}



