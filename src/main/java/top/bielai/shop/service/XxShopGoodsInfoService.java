package top.bielai.shop.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import top.bielai.shop.api.mall.vo.XxShopSearchGoodsVO;
import top.bielai.shop.domain.XxShopGoodsInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【tb_xx_shop_goods_info】的数据库操作Service
* @createDate 2022-11-30 13:58:39
*/
public interface XxShopGoodsInfoService extends IService<XxShopGoodsInfo> {

    Page<XxShopSearchGoodsVO> searchXxShopGoods(Page<XxShopGoodsInfo> page, LambdaQueryWrapper<XxShopGoodsInfo> queryWrapper);
}
