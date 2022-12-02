package top.bielai.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import top.bielai.shop.api.mall.vo.XxShopIndexConfigGoodsVO;
import top.bielai.shop.common.IndexConfigTypeEnum;
import top.bielai.shop.dao.XxShopGoodsMapper;
import top.bielai.shop.domain.XxShopGoodsInfo;
import top.bielai.shop.domain.XxShopIndexConfig;
import top.bielai.shop.mapper.XxShopGoodsInfoMapper;
import top.bielai.shop.service.XxShopIndexConfigService;
import top.bielai.shop.mapper.XxShopIndexConfigMapper;
import org.springframework.stereotype.Service;
import top.bielai.shop.util.BeanUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @description 针对表【tb_xx_shop_index_config】的数据库操作Service实现
 * @createDate 2022-11-30 13:58:39
 */
@Service
public class XxShopIndexConfigServiceImpl extends ServiceImpl<XxShopIndexConfigMapper, XxShopIndexConfig>
        implements XxShopIndexConfigService {

    @Autowired
    private XxShopGoodsInfoMapper goodsMapper;
    @Override
    public List<XxShopIndexConfigGoodsVO> getConfigGoodsForIndex(int type, int limit) {
        List<XxShopIndexConfig> list = list(new LambdaQueryWrapper<XxShopIndexConfig>()
                .eq(XxShopIndexConfig::getConfigType, type)
                .orderByDesc(XxShopIndexConfig::getConfigRank)
                .last("limit " + limit));
        if(!CollectionUtils.isEmpty(list)){
            List<Long> goodIds = list.stream().map(XxShopIndexConfig::getGoodsId).collect(Collectors.toList());
            List<XxShopGoodsInfo> xxShopGoodsInfos = goodsMapper.selectBatchIds(goodIds);
            List<XxShopIndexConfigGoodsVO> xxShopIndexConfigGoodsVOList = BeanUtil.copyList(xxShopGoodsInfos, XxShopIndexConfigGoodsVO.class);
            for (XxShopIndexConfigGoodsVO xxShopIndexConfigGoodsVO : xxShopIndexConfigGoodsVOList) {
                String goodsName = xxShopIndexConfigGoodsVO.getGoodsName();
                String goodsIntro = xxShopIndexConfigGoodsVO.getGoodsIntro();
                // 字符串过长导致文字超出的问题
                if (goodsName.length() > 30) {
                    goodsName = goodsName.substring(0, 30) + "...";
                    xxShopIndexConfigGoodsVO.setGoodsName(goodsName);
                }
                if (goodsIntro.length() > 22) {
                    goodsIntro = goodsIntro.substring(0, 22) + "...";
                    xxShopIndexConfigGoodsVO.setGoodsIntro(goodsIntro);
                }
            }
            return xxShopIndexConfigGoodsVOList;
        }
        return null;
    }
}




