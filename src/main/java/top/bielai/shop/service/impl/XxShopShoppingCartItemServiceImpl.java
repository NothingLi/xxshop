package top.bielai.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.bielai.shop.api.mall.param.SaveCartItemParam;
import top.bielai.shop.api.mall.param.UpdateCartItemParam;
import top.bielai.shop.api.mall.vo.XxShopShoppingCartItemVO;
import top.bielai.shop.common.Constants;
import top.bielai.shop.common.ErrorEnum;
import top.bielai.shop.common.XxShopException;
import top.bielai.shop.domain.XxShopGoodsInfo;
import top.bielai.shop.domain.XxShopShoppingCartItem;
import top.bielai.shop.mapper.XxShopGoodsInfoMapper;
import top.bielai.shop.mapper.XxShopShoppingCartItemMapper;
import top.bielai.shop.service.XxShopShoppingCartItemService;
import top.bielai.shop.util.BeanUtil;

import java.util.*;
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

    private final XxShopGoodsInfoMapper goodsInfoMapper;

    public XxShopShoppingCartItemServiceImpl(XxShopGoodsInfoMapper goodsInfoMapper) {
        this.goodsInfoMapper = goodsInfoMapper;
    }

    @Override
    public List<XxShopShoppingCartItemVO> getCartItemsForSettle(List<Long> cartItemIds, Long xxShopUserId) {
        List<XxShopShoppingCartItem> list = list(new LambdaQueryWrapper<XxShopShoppingCartItem>().eq(XxShopShoppingCartItem::getUserId, xxShopUserId).in(XxShopShoppingCartItem::getCartItemId, cartItemIds));
        if (list.isEmpty() || list.size() != cartItemIds.size()) {
            XxShopException.fail(ErrorEnum.CART_ITEM_ERROR);
        }
        return getXxShopShoppingCartItemVOList(list);
    }

    @Override
    public Page<XxShopShoppingCartItemVO> pageVo(Page<XxShopShoppingCartItem> pageParam, LambdaQueryWrapper<XxShopShoppingCartItem> queryWrapper) {
        Page<XxShopShoppingCartItem> page = page(pageParam, queryWrapper);
        Page<XxShopShoppingCartItemVO> result = new Page<>();
        BeanUtils.copyProperties(page, result, "records");
        result.setRecords(getXxShopShoppingCartItemVOList(page.getRecords()));
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveXxShopCartItem(SaveCartItemParam saveCartItemParam, Long userId) {
        XxShopGoodsInfo xxShopGoodsInfo = goodsInfoMapper.selectById(saveCartItemParam.getGoodsId());
        if (ObjectUtils.isEmpty(xxShopGoodsInfo)) {
            XxShopException.fail(ErrorEnum.GOODS_NOT_EXIST_ERROR);
        }
        if (saveCartItemParam.getGoodsCount() > xxShopGoodsInfo.getStockNum()) {
            XxShopException.fail(ErrorEnum.CART_ITEM_GOODS_NUM_ERROR);
        }
        XxShopShoppingCartItem exist = getOne(new LambdaQueryWrapper<XxShopShoppingCartItem>().eq(XxShopShoppingCartItem::getGoodsId, saveCartItemParam.getGoodsId()).eq(XxShopShoppingCartItem::getUserId, userId));
        if (ObjectUtils.isNotEmpty(exist)) {
            int i = exist.getGoodsCount() + saveCartItemParam.getGoodsCount();
            if (i > xxShopGoodsInfo.getStockNum()) {
                XxShopException.fail(ErrorEnum.CART_ITEM_GOODS_NUM_ERROR);
            }
            exist.setGoodsCount(i);
            return baseMapper.updateById(exist) > 0;
        }
        long count = count(new LambdaQueryWrapper<XxShopShoppingCartItem>().eq(XxShopShoppingCartItem::getUserId, userId));
        if (count >= Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER) {
            XxShopException.fail(ErrorEnum.CART_ITEM_LIMIT_ERROR);
        }
        XxShopShoppingCartItem xxShopShoppingCartItem = new XxShopShoppingCartItem();
        BeanUtil.copyProperties(saveCartItemParam, xxShopShoppingCartItem);
        xxShopShoppingCartItem.setUserId(userId);
        return baseMapper.insert(xxShopShoppingCartItem) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateXxShopCartItem(UpdateCartItemParam updateCartItemParam, Long userId) {
        XxShopShoppingCartItem exist = getOne(new LambdaQueryWrapper<XxShopShoppingCartItem>().eq(XxShopShoppingCartItem::getCartItemId, updateCartItemParam.getCartItemId()).eq(XxShopShoppingCartItem::getUserId, userId));
        if (ObjectUtils.isEmpty(exist)) {
            XxShopException.fail(ErrorEnum.DATA_NOT_EXIST);
        }
        if (exist.getGoodsCount().equals(updateCartItemParam.getGoodsCount())) {
            return true;
        }
        XxShopGoodsInfo xxShopGoodsInfo = goodsInfoMapper.selectById(exist.getGoodsId());
        if (ObjectUtils.isEmpty(xxShopGoodsInfo)) {
            XxShopException.fail(ErrorEnum.GOODS_NOT_EXIST_ERROR);
        }
        if (exist.getGoodsCount() > xxShopGoodsInfo.getStockNum()) {
            XxShopException.fail(ErrorEnum.CART_ITEM_GOODS_NUM_ERROR);
        }
        exist.setGoodsCount(updateCartItemParam.getGoodsCount());
        return baseMapper.updateById(exist) > 0;
    }

    @Override
    public List<XxShopShoppingCartItemVO> immediatelySettle(SaveCartItemParam saveCartItemParam, Long userId) {
        XxShopGoodsInfo xxShopGoodsInfo = goodsInfoMapper.selectById(saveCartItemParam.getGoodsId());
        if (ObjectUtils.isEmpty(xxShopGoodsInfo)) {
            XxShopException.fail(ErrorEnum.GOODS_NOT_EXIST_ERROR);
        }
        if (saveCartItemParam.getGoodsCount() > xxShopGoodsInfo.getStockNum()) {
            XxShopException.fail(ErrorEnum.CART_ITEM_GOODS_NUM_ERROR);
        }
        XxShopShoppingCartItem exist = getOne(new LambdaQueryWrapper<XxShopShoppingCartItem>().eq(XxShopShoppingCartItem::getGoodsId, saveCartItemParam.getGoodsId()).eq(XxShopShoppingCartItem::getUserId, userId));
        if (ObjectUtils.isNotEmpty(exist)) {
            if (saveCartItemParam.getGoodsCount() > xxShopGoodsInfo.getStockNum()) {
                XxShopException.fail(ErrorEnum.CART_ITEM_GOODS_NUM_ERROR);
            }
            exist.setGoodsCount(saveCartItemParam.getGoodsCount());
            baseMapper.updateById(exist);
        } else {
            BeanUtil.copyProperties(saveCartItemParam, exist);
            exist.setUserId(userId);
            baseMapper.insert(exist);
        }
        return getCartItemsForSettle(Collections.singletonList(exist.getCartItemId()), userId);
    }

    private List<XxShopShoppingCartItemVO> getXxShopShoppingCartItemVOList(List<XxShopShoppingCartItem> list) {
        Set<Long> goodIds = list.stream().map(XxShopShoppingCartItem::getGoodsId).collect(Collectors.toSet());
        List<XxShopGoodsInfo> xxShopGoodsInfos = goodsInfoMapper.selectList(new LambdaQueryWrapper<XxShopGoodsInfo>().in(XxShopGoodsInfo::getGoodsId, goodIds));
        Map<Long, XxShopGoodsInfo> collect = xxShopGoodsInfos.stream().collect(Collectors.toMap(XxShopGoodsInfo::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
        List<XxShopShoppingCartItemVO> voList = new ArrayList<>(list.size());
        for (XxShopShoppingCartItem xxShopShoppingCartItem : list) {
            XxShopShoppingCartItemVO vo = new XxShopShoppingCartItemVO();
            BeanUtils.copyProperties(xxShopShoppingCartItem, vo);
            XxShopGoodsInfo xxShopGoodsInfo = collect.get(vo.getGoodsId());
            if (ObjectUtils.isEmpty(xxShopGoodsInfo)) {
                XxShopException.fail(ErrorEnum.CART_ITEM_ERROR);
            }
            if (vo.getGoodsCount() > xxShopGoodsInfo.getStockNum()) {
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




