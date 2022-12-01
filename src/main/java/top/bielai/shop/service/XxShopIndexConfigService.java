package top.bielai.shop.service;

import top.bielai.shop.api.mall.vo.XxShopIndexConfigGoodsVO;
import top.bielai.shop.domain.XxShopIndexConfig;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Administrator
* @description 针对表【tb_xx_shop_index_config】的数据库操作Service
* @createDate 2022-11-30 13:58:39
*/
public interface XxShopIndexConfigService extends IService<XxShopIndexConfig> {

    /**
     * 根据类型查询配置商品
     * @param type 类型
     * @param limit 数量
     * @return 商品列表
     */
    List<XxShopIndexConfigGoodsVO>  getConfigGoodsForIndex(int type, int limit);
}
