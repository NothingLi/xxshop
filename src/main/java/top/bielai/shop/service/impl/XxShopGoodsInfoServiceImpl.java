package top.bielai.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.bielai.shop.api.mall.vo.XxShopSearchGoodsVO;
import top.bielai.shop.domain.XxShopGoodsInfo;
import top.bielai.shop.mapper.XxShopGoodsInfoMapper;
import top.bielai.shop.service.XxShopGoodsInfoService;
import top.bielai.shop.util.BeanUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @description 针对表【tb_xx_shop_goods_info】的数据库操作Service实现
 * @createDate 2022-11-30 13:58:39
 */
@Service
public class XxShopGoodsInfoServiceImpl extends ServiceImpl<XxShopGoodsInfoMapper, XxShopGoodsInfo>
        implements XxShopGoodsInfoService {

    @Override
    public Page<XxShopSearchGoodsVO> searchXxShopGoods(Page<XxShopGoodsInfo> pageParam, LambdaQueryWrapper<XxShopGoodsInfo> queryWrapper) {
        Page<XxShopGoodsInfo> page = page(pageParam, queryWrapper);
        List<XxShopSearchGoodsVO> xxShopSearchGoods = new ArrayList<>(page.getRecords().size());
        if (!CollectionUtils.isEmpty(page.getRecords())) {
            xxShopSearchGoods = BeanUtil.copyList(page.getRecords(), XxShopSearchGoodsVO.class);
            for (XxShopSearchGoodsVO xxShopSearchGoodsVO : xxShopSearchGoods) {
                String goodsName = xxShopSearchGoodsVO.getGoodsName();
                String goodsIntro = xxShopSearchGoodsVO.getGoodsIntro();
                // 字符串过长导致文字超出的问题
                if (goodsName.length() > 28) {
                    goodsName = goodsName.substring(0, 28) + "...";
                    xxShopSearchGoodsVO.setGoodsName(goodsName);
                }
                if (goodsIntro.length() > 30) {
                    goodsIntro = goodsIntro.substring(0, 30) + "...";
                    xxShopSearchGoodsVO.setGoodsIntro(goodsIntro);
                }
            }
        }
        Page<XxShopSearchGoodsVO> result = new Page<>();
        BeanUtils.copyProperties(page, result, "records");
        result.setRecords(xxShopSearchGoods);
        return result;
    }
}




