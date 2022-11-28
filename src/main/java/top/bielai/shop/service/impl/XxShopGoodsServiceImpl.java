/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本软件已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2021 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package top.bielai.shop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.bielai.shop.api.mall.vo.XxShopSearchGoodsVO;
import top.bielai.shop.common.ServiceResultEnum;
import top.bielai.shop.common.XxShopCategoryLevelEnum;
import top.bielai.shop.common.XxShopException;
import top.bielai.shop.dao.GoodsCategoryMapper;
import top.bielai.shop.dao.XxShopGoodsMapper;
import top.bielai.shop.entity.GoodsCategory;
import top.bielai.shop.entity.XxShopGoods;
import top.bielai.shop.service.XxShopGoodsService;
import top.bielai.shop.util.BeanUtil;
import top.bielai.shop.util.PageQueryUtil;
import top.bielai.shop.util.PageResult;

import java.util.ArrayList;
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
        List<XxShopGoods> goodsList = goodsMapper.findXxShopGoodsList(pageUtil);
        int total = goodsMapper.getTotalXxShopGoods(pageUtil);
        PageResult pageResult = new PageResult(goodsList, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public String saveXxShopGoods(XxShopGoods goods) {
        GoodsCategory goodsCategory = goodsCategoryMapper.selectByPrimaryKey(goods.getGoodsCategoryId());
        // 分类不存在或者不是三级分类，则该参数字段异常
        if (goodsCategory == null || goodsCategory.getCategoryLevel().intValue() != XxShopCategoryLevelEnum.LEVEL_THREE.getLevel()) {
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
    public void batchSaveXxShopGoods(List<XxShopGoods> xxShopGoodsList) {
        if (!CollectionUtils.isEmpty(xxShopGoodsList)) {
            goodsMapper.batchInsert(xxShopGoodsList);
        }
    }

    @Override
    public String updateXxShopGoods(XxShopGoods goods) {
        GoodsCategory goodsCategory = goodsCategoryMapper.selectByPrimaryKey(goods.getGoodsCategoryId());
        // 分类不存在或者不是三级分类，则该参数字段异常
        if (goodsCategory == null || goodsCategory.getCategoryLevel().intValue() != XxShopCategoryLevelEnum.LEVEL_THREE.getLevel()) {
            return ServiceResultEnum.GOODS_CATEGORY_ERROR.getResult();
        }
        XxShopGoods temp = goodsMapper.selectByPrimaryKey(goods.getGoodsId());
        if (temp == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        XxShopGoods temp2 = goodsMapper.selectByCategoryIdAndName(goods.getGoodsName(), goods.getGoodsCategoryId());
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
    public XxShopGoods getXxShopGoodsById(Long id) {
        XxShopGoods xxShopGoods = goodsMapper.selectByPrimaryKey(id);
        if (xxShopGoods == null) {
            XxShopException.fail(ServiceResultEnum.GOODS_NOT_EXIST.getResult());
        }
        return xxShopGoods;
    }

    @Override
    public Boolean batchUpdateSellStatus(Long[] ids, int sellStatus) {
        return goodsMapper.batchUpdateSellStatus(ids, sellStatus) > 0;
    }

    @Override
    public PageResult searchXxShopGoods(PageQueryUtil pageUtil) {
        List<XxShopGoods> goodsList = goodsMapper.findXxShopGoodsListBySearch(pageUtil);
        int total = goodsMapper.getTotalXxShopGoodsBySearch(pageUtil);
        List<XxShopSearchGoodsVO> xxShopSearchGoodsVOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(goodsList)) {
            xxShopSearchGoodsVOS = BeanUtil.copyList(goodsList, XxShopSearchGoodsVO.class);
            for (XxShopSearchGoodsVO xxShopSearchGoodsVO : xxShopSearchGoodsVOS) {
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
        PageResult pageResult = new PageResult(xxShopSearchGoodsVOS, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }
}
