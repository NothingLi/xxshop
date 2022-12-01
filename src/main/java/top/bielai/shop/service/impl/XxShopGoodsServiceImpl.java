
package top.bielai.shop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.bielai.shop.common.CategoryLevelEnum;
import top.bielai.shop.common.ServiceResultEnum;
import top.bielai.shop.common.XxShopException;
import top.bielai.shop.dao.GoodsCategoryMapper;
import top.bielai.shop.dao.XxShopGoodsMapper;
import top.bielai.shop.domain.XxShopGoodsInfo;
import top.bielai.shop.service.XxShopGoodsService;
import top.bielai.shop.util.PageQueryUtil;
import top.bielai.shop.util.PageResult;

import java.util.Date;
import java.util.List;

@Service
public class XxShopGoodsServiceImpl implements XxShopGoodsService {

    @Autowired
    private XxShopGoodsMapper goodsMapper;
    @Autowired
    private GoodsCategoryMapper goodsCategoryMapper;

    @Override
    public PageResult getXxShopGoodsPage(PageQueryUtil pageUtil) {
        List<XxShopGoodsInfo> goodsList = goodsMapper.findXxShopGoodsList(pageUtil);
        int total = goodsMapper.getTotalXxShopGoods(pageUtil);
        PageResult pageResult = new PageResult(goodsList, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public String saveXxShopGoods(XxShopGoodsInfo goods) {
        GoodsCategory goodsCategory = goodsCategoryMapper.selectByPrimaryKey(goods.getGoodsCategoryId());
        // 分类不存在或者不是三级分类，则该参数字段异常
        if (goodsCategory == null || goodsCategory.getCategoryLevel().intValue() != CategoryLevelEnum.LEVEL_THREE.getLevel()) {
            return ServiceResultEnum.GOODS_CATEGORY_ERROR.getResult();
        }
        if (goodsMapper.selectByCategoryIdAndName(goods.getGoodsName(), goods.getGoodsCategoryId()) != null) {
            return ServiceResultEnum.SAME_GOODS_EXIST.getResult();
        }
        if (goodsMapper.insertSelective(goods) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public void batchSaveXxShopGoods(List<XxShopGoodsInfo> xxShopGoodsList) {
        if (!CollectionUtils.isEmpty(xxShopGoodsList)) {
            goodsMapper.batchInsert(xxShopGoodsList);
        }
    }

    @Override
    public String updateXxShopGoods(XxShopGoodsInfo goods) {
        GoodsCategory goodsCategory = goodsCategoryMapper.selectByPrimaryKey(goods.getGoodsCategoryId());
        // 分类不存在或者不是三级分类，则该参数字段异常
        if (goodsCategory == null || goodsCategory.getCategoryLevel().intValue() != CategoryLevelEnum.LEVEL_THREE.getLevel()) {
            return ServiceResultEnum.GOODS_CATEGORY_ERROR.getResult();
        }
        XxShopGoodsInfo temp = goodsMapper.selectByPrimaryKey(goods.getGoodsId());
        if (temp == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        XxShopGoodsInfo temp2 = goodsMapper.selectByCategoryIdAndName(goods.getGoodsName(), goods.getGoodsCategoryId());
        if (temp2 != null && !temp2.getGoodsId().equals(goods.getGoodsId())) {
            //name和分类id相同且不同id 不能继续修改
            return ServiceResultEnum.SAME_GOODS_EXIST.getResult();
        }
        goods.setUpdateTime(new Date());
        if (goodsMapper.updateByPrimaryKeySelective(goods) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public XxShopGoodsInfo getXxShopGoodsById(Long id) {
        XxShopGoodsInfo xxShopGoods = goodsMapper.selectByPrimaryKey(id);
        if (xxShopGoods == null) {
            XxShopException.fail(ServiceResultEnum.GOODS_NOT_EXIST.getResult());
        }
        return xxShopGoods;
    }

    @Override
    public Boolean batchUpdateSellStatus(Long[] ids, int sellStatus) {
        return goodsMapper.batchUpdateSellStatus(ids, sellStatus) > 0;
    }

}
