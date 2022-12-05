
package top.bielai.shop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.bielai.shop.api.mall.param.SaveCartItemParam;
import top.bielai.shop.api.mall.param.UpdateCartItemParam;
import top.bielai.shop.api.mall.vo.XxShopShoppingCartItemVO;
import top.bielai.shop.common.Constants;
import top.bielai.shop.common.ServiceResultEnum;
import top.bielai.shop.common.XxShopException;
import top.bielai.shop.config.annotation.TokenToShopUser;
import top.bielai.shop.dao.XxShopGoodsMapper;
import top.bielai.shop.dao.XxShopShoppingCartItemMapper;
import top.bielai.shop.domain.XxShopGoodsInfo;
import top.bielai.shop.domain.XxShopShoppingCartItem;
import top.bielai.shop.service.XxShopShoppingCartService;
import top.bielai.shop.util.BeanUtil;
import top.bielai.shop.util.PageResult;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class XxShopShoppingCartServiceImpl implements XxShopShoppingCartService {

    @Autowired
    private XxShopShoppingCartItemMapper xxShopShoppingCartItemMapper;

    @Autowired
    private XxShopGoodsMapper xxShopGoodsMapper;

    @Override
    public String saveXxShopCartItem(SaveCartItemParam saveCartItemParam, @TokenToShopUser Long userId) {

        XxShopGoodsInfo xxShopGoods = xxShopGoodsMapper.selectByPrimaryKey(saveCartItemParam.getGoodsId());
        //商品为空
        if (xxShopGoods == null) {
            return ServiceResultEnum.GOODS_NOT_EXIST.getResult();
        }
        //超出单个商品的最大数量
        if (saveCartItemParam.getGoodsCount() < 1) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_NUMBER_ERROR.getResult();
        }
        //超出单个商品的最大数量
        if (saveCartItemParam.getGoodsCount() > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        XxShopShoppingCartItem temp = xxShopShoppingCartItemMapper.selectByUserIdAndGoodsId(userId, saveCartItemParam.getGoodsId());
        if (temp != null) {
            //已存在则修改该记录
            int i = temp.getGoodsCount() + saveCartItemParam.getGoodsCount();
            if(i > xxShopGoods.getStockNum()){
                return ServiceResultEnum.GOODS_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
            }
            temp.setGoodsCount(i);
            if (xxShopShoppingCartItemMapper.updateByPrimaryKeySelective(temp) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            }
//            XxShopException.fail(ServiceResultEnum.SHOPPING_CART_ITEM_EXIST_ERROR.getResult());
        }

        int totalItem = xxShopShoppingCartItemMapper.selectCountByUserId(userId);

        //超出最大数量
        if (totalItem > Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_TOTAL_NUMBER_ERROR.getResult();
        }
        XxShopShoppingCartItem xxShopShoppingCartItem = new XxShopShoppingCartItem();
        BeanUtil.copyProperties(saveCartItemParam, xxShopShoppingCartItem);
        xxShopShoppingCartItem.setUserId(userId);
        //保存记录
        if (xxShopShoppingCartItemMapper.insertSelective(xxShopShoppingCartItem) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateXxShopCartItem(UpdateCartItemParam updateCartItemParam, @TokenToShopUser Long userId) {

        XxShopShoppingCartItem xxShopShoppingCartItemUpdate = xxShopShoppingCartItemMapper.selectByPrimaryKey(updateCartItemParam.getCartItemId());
        if (xxShopShoppingCartItemUpdate == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        //当前登录账号的userId与待修改的cartItem中userId不同，返回错误
        if (!xxShopShoppingCartItemUpdate.getUserId().equals(userId)) {
            return ServiceResultEnum.NO_PERMISSION_ERROR.getResult();
        }
        //数值相同，则不执行数据操作
        if (updateCartItemParam.getGoodsCount().equals(xxShopShoppingCartItemUpdate.getGoodsCount())) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        int i = updateCartItemParam.getGoodsCount() + xxShopShoppingCartItemUpdate.getGoodsCount();
        //超出单个商品的最大数量
        if (updateCartItemParam.getGoodsCount() > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER || i > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        XxShopGoodsInfo xxShopGoods = xxShopGoodsMapper.selectByPrimaryKey(xxShopShoppingCartItemUpdate.getGoodsId());
        if(i > xxShopGoods.getStockNum()){
            return ServiceResultEnum.GOODS_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        xxShopShoppingCartItemUpdate.setGoodsCount(updateCartItemParam.getGoodsCount());
        xxShopShoppingCartItemUpdate.setUpdateTime(new Date());
        //修改记录
        if (xxShopShoppingCartItemMapper.updateByPrimaryKeySelective(xxShopShoppingCartItemUpdate) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public XxShopShoppingCartItem getXxShopCartItemById(Long xxShopShoppingCartItemId) {
        XxShopShoppingCartItem xxShopShoppingCartItem = xxShopShoppingCartItemMapper.selectByPrimaryKey(xxShopShoppingCartItemId);
        if (xxShopShoppingCartItem == null) {
            XxShopException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        return xxShopShoppingCartItem;
    }

    @Override
    public Boolean deleteById(Long shoppingCartItemId, @TokenToShopUser Long userId) {
        XxShopShoppingCartItem xxShopShoppingCartItem = xxShopShoppingCartItemMapper.selectByPrimaryKey(shoppingCartItemId);
        if (xxShopShoppingCartItem == null) {
            return false;
        }
        //userId不同不能删除
        if (!userId.equals(xxShopShoppingCartItem.getUserId())) {
            return false;
        }
        return xxShopShoppingCartItemMapper.deleteByPrimaryKey(shoppingCartItemId) > 0;
    }

    @Override
    public Boolean deleteBatchById(Long[] shoppingCartItemId, @TokenToShopUser Long userId) {
        return xxShopShoppingCartItemMapper.deleteBatch(Arrays.asList(shoppingCartItemId),userId) > 0;
    }

    @Override
    public List<XxShopShoppingCartItemVO> getMyShoppingCartItems(Long xxShopUserId) {
        List<XxShopShoppingCartItemVO> xxShopShoppingCartItemVOS = new ArrayList<>();
        List<XxShopShoppingCartItem> xxShopShoppingCartItems = xxShopShoppingCartItemMapper.selectByUserId(xxShopUserId, Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER);
        return getXxShopShoppingCartItemVOS(xxShopShoppingCartItemVOS, xxShopShoppingCartItems);
    }

    @Override
    public List<XxShopShoppingCartItemVO> getCartItemsForSettle(List<Long> cartItemIds, Long xxShopUserId) {
        List<XxShopShoppingCartItemVO> xxShopShoppingCartItemVOS = new ArrayList<>();
        if (CollectionUtils.isEmpty(cartItemIds)) {
            XxShopException.fail("购物项不能为空");
        }
        List<XxShopShoppingCartItem> xxShopShoppingCartItems = xxShopShoppingCartItemMapper.selectByUserIdAndCartItemIds(xxShopUserId, cartItemIds);
        if (CollectionUtils.isEmpty(xxShopShoppingCartItems)) {
            XxShopException.fail("购物项不能为空");
        }
        if (xxShopShoppingCartItems.size() != cartItemIds.size()) {
            XxShopException.fail("参数异常");
        }
        return getXxShopShoppingCartItemVOS(xxShopShoppingCartItemVOS, xxShopShoppingCartItems);
    }

    /**
     * 数据转换
     *
     * @param xxShopShoppingCartItemVOS
     * @param xxShopShoppingCartItems
     * @return
     */
    private List<XxShopShoppingCartItemVO> getXxShopShoppingCartItemVOS(List<XxShopShoppingCartItemVO> xxShopShoppingCartItemVOS, List<XxShopShoppingCartItem> xxShopShoppingCartItems) {
        if (!CollectionUtils.isEmpty(xxShopShoppingCartItems)) {
            //查询商品信息并做数据转换
            List<Long> xxShopGoodsIds = xxShopShoppingCartItems.stream().map(XxShopShoppingCartItem::getGoodsId).collect(Collectors.toList());
            List<XxShopGoodsInfo> xxShopGoods = xxShopGoodsMapper.selectByPrimaryKeys(xxShopGoodsIds);
            Map<Long, XxShopGoodsInfo> xxShopGoodsMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(xxShopGoods)) {
                xxShopGoodsMap = xxShopGoods.stream().collect(Collectors.toMap(XxShopGoodsInfo::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
            }
            for (XxShopShoppingCartItem xxShopShoppingCartItem : xxShopShoppingCartItems) {
                XxShopShoppingCartItemVO xxShopShoppingCartItemVO = new XxShopShoppingCartItemVO();
                BeanUtil.copyProperties(xxShopShoppingCartItem, xxShopShoppingCartItemVO);
                if (xxShopGoodsMap.containsKey(xxShopShoppingCartItem.getGoodsId())) {
                    XxShopGoodsInfo xxShopGoodsTemp = xxShopGoodsMap.get(xxShopShoppingCartItem.getGoodsId());
                    xxShopShoppingCartItemVO.setGoodsCoverImg(xxShopGoodsTemp.getGoodsCoverImg());
                    String goodsName = xxShopGoodsTemp.getGoodsName();
                    // 字符串过长导致文字超出的问题
                    if (goodsName.length() > 28) {
                        goodsName = goodsName.substring(0, 28) + "...";
                    }
                    xxShopShoppingCartItemVO.setGoodsName(goodsName);
                    xxShopShoppingCartItemVO.setSellingPrice(xxShopGoodsTemp.getSellingPrice());
                    xxShopShoppingCartItemVOS.add(xxShopShoppingCartItemVO);
                }
            }
        }
        return xxShopShoppingCartItemVOS;
    }

    @Override
    public PageResult getMyShoppingCartItems(PageQueryUtil pageUtil) {
        List<XxShopShoppingCartItemVO> xxShopShoppingCartItemVOS = new ArrayList<>();
        List<XxShopShoppingCartItem> xxShopShoppingCartItems = xxShopShoppingCartItemMapper.findMyXxShopCartItems(pageUtil);
        int total = xxShopShoppingCartItemMapper.getTotalMyXxShopCartItems(pageUtil);
        PageResult pageResult = new PageResult(getXxShopShoppingCartItemVOS(xxShopShoppingCartItemVOS, xxShopShoppingCartItems), total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }
}
