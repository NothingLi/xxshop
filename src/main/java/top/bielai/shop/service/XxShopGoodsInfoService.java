package top.bielai.shop.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import top.bielai.shop.api.mall.vo.XxShopSearchGoodsVO;
import top.bielai.shop.domain.XxShopGoodsInfo;

/**
 * @author Administrator
 * @description 针对表【tb_xx_shop_goods_info】的数据库操作Service
 * @createDate 2022-11-30 13:58:39
 */
public interface XxShopGoodsInfoService extends IService<XxShopGoodsInfo> {

    /**
     * 分页搜索商品
     *
     * @param page         分页条件
     * @param queryWrapper 查询条件
     * @return 分页结果
     */
    Page<XxShopSearchGoodsVO> searchXxShopGoods(Page<XxShopGoodsInfo> page, LambdaQueryWrapper<XxShopGoodsInfo> queryWrapper);
}
