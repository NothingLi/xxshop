package top.bielai.shop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.bielai.shop.domain.XxShopGoodsInfo;

/**
 * @author bielai
 * @description 针对表【tb_xx_shop_goods_info】的数据库操作Mapper
 * @createDate 2022-11-30 13:58:39
 * @Entity top.bielai.shop.domain.XxShopGoodsInfo
 */
@Mapper
public interface XxShopGoodsInfoMapper extends BaseMapper<XxShopGoodsInfo> {
    /**
     * 根据商品id削减库存
     *
     * @param id       商品id
     * @param stockNum 库存
     * @return 影响条数
     */
    int reduceStockNum(Long id, Integer stockNum);

    /**
     * 根据商品id回复库存
     *
     * @param id       商品id
     * @param stockNum 库存
     * @return 影响条数
     */
    int recoverStockNum(Long id, Integer stockNum);
}




