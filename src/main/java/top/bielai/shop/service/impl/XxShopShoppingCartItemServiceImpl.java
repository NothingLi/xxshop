package top.bielai.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.bielai.shop.api.mall.vo.XxShopShoppingCartItemVO;
import top.bielai.shop.common.Constants;
import top.bielai.shop.common.ErrorEnum;
import top.bielai.shop.common.XxShopException;
import top.bielai.shop.domain.XxShopGoodsInfo;
import top.bielai.shop.domain.XxShopShoppingCartItem;
import top.bielai.shop.mapper.XxShopGoodsInfoMapper;
import top.bielai.shop.mapper.XxShopShoppingCartItemMapper;
import top.bielai.shop.service.XxShopShoppingCartItemService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author bielai
 * @description 针对表【tb_xx_shop_shopping_cart_item】的数据库操作Service实现
 * @createDate 2022-11-30 13:58:39
 */
@Service
public class XxShopShoppingCartItemServiceImpl extends ServiceImpl<XxShopShoppingCartItemMapper, XxShopShoppingCartItem>
        implements XxShopShoppingCartItemService {

    @Autowired
    private XxShopGoodsInfoMapper goodsInfoMapper;

    @Override
    public List<XxShopShoppingCartItemVO> getCartItemsForSettle(List<Long> cartItemIds, Long xxShopUserId) {
        List<XxShopShoppingCartItem> list = list(new LambdaQueryWrapper<XxShopShoppingCartItem>().eq(XxShopShoppingCartItem::getUserId, xxShopUserId).in(XxShopShoppingCartItem::getCartItemId, cartItemIds));
        if (list.isEmpty() || list.size() != cartItemIds.size()) {
            XxShopException.fail(ErrorEnum.CART_ITEM_ERROR);
        }
        Set<Long> goodIds = list.stream().map(XxShopShoppingCartItem::getGoodsId).collect(Collectors.toSet());
        List<XxShopGoodsInfo> xxShopGoodsInfos = goodsInfoMapper.selectList(new LambdaQueryWrapper<XxShopGoodsInfo>().in(XxShopGoodsInfo::getGoodsId, goodIds));
        List<XxShopGoodsInfo> putDownGoods = xxShopGoodsInfos.stream().filter(good -> Constants.SELL_STATUS_DOWN == good.getGoodsSellStatus()).collect(Collectors.toList());
        if (!putDownGoods.isEmpty() || xxShopGoodsInfos.size() != goodIds.size()){
            XxShopException.fail(ErrorEnum.CART_ITEM_ERROR);
        }
        Map<Long, XxShopGoodsInfo> collect = xxShopGoodsInfos.stream().collect(Collectors.toMap(XxShopGoodsInfo::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
        List<XxShopShoppingCartItemVO> voList = new ArrayList<>(list.size());
        for (XxShopShoppingCartItem xxShopShoppingCartItem : list) {
            XxShopShoppingCartItemVO vo = new XxShopShoppingCartItemVO();
            BeanUtils.copyProperties(xxShopShoppingCartItem, vo);
            XxShopGoodsInfo xxShopGoodsInfo = collect.get(vo.getGoodsId());
            if (ObjectUtils.isEmpty(xxShopGoodsInfo)) {
                XxShopException.fail(ErrorEnum.CART_ITEM_ERROR);
            }
            if(vo.getGoodsCount() > xxShopGoodsInfo.getStockNum()){
                XxShopException.fail(ErrorEnum.CART_ITEM_GOODS_NUM_ERROR);
            }
            vo.setGoodsCoverImg(xxShopGoodsInfo.getGoodsCoverImg());
            String goodsName = xxShopGoodsInfo.getGoodsName();
            // 字符串过长导致文字超出的问题
            if (goodsName.length() > 28) {
                goodsName = goodsName.substring(0, 28) + "...";
            }
            vo.setGoodsName(goodsName);
            vo.setSellingPrice(xxShopGoodsInfo.getSellingPrice());
            voList.add(vo);
        }

        return voList;
    }
}




