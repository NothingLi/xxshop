package top.bielai.shop.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author bielai
 * @TableName tb_xx_shop_goods_info
 */
@TableName(value = "tb_xx_shop_goods_info")
@Data
public class XxShopGoodsInfo implements Serializable {
    /**
     * 商品表主键id
     */
    @TableId(value = "goods_id", type = IdType.AUTO)
    private Long goodsId;

    /**
     * 商品名
     */
    @TableField(value = "goods_name")
    private String goodsName;

    /**
     * 商品简介
     */
    @TableField(value = "goods_intro")
    private String goodsIntro;

    /**
     * 关联分类id
     */
    @TableField(value = "goods_category_id")
    private Long goodsCategoryId;

    /**
     * 商品主图
     */
    @TableField(value = "goods_cover_img")
    private String goodsCoverImg;

    /**
     * 商品轮播图
     */
    @TableField(value = "goods_carousel")
    private String goodsCarousel;

    /**
     * 商品详情
     */
    @TableField(value = "goods_detail_content")
    private String goodsDetailContent;

    /**
     * 商品价格
     */
    @TableField(value = "original_price")
    private BigDecimal originalPrice;

    /**
     * 商品实际售价
     */
    @TableField(value = "selling_price")
    private BigDecimal sellingPrice;

    /**
     * 商品库存数量
     */
    @TableField(value = "stock_num")
    private Integer stockNum;

    /**
     * 商品标签
     */
    @TableField(value = "tag")
    private String tag;

    /**
     * 商品上架状态 1-下架 0-上架
     */
    @TableField(value = "goods_sell_status")
    private Byte goodsSellStatus;

    /**
     * 添加者主键id
     */
    @TableField(value = "create_user")
    private Integer createUser;

    /**
     * 商品添加时间
     */
    @TableField(value = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 修改者主键id
     */
    @TableField(value = "update_user")
    private Integer updateUser;

    /**
     * 商品修改时间
     */
    @TableField(value = "update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        XxShopGoodsInfo other = (XxShopGoodsInfo) that;
        return (this.getGoodsId() == null ? other.getGoodsId() == null : this.getGoodsId().equals(other.getGoodsId()))
                && (this.getGoodsName() == null ? other.getGoodsName() == null : this.getGoodsName().equals(other.getGoodsName()))
                && (this.getGoodsIntro() == null ? other.getGoodsIntro() == null : this.getGoodsIntro().equals(other.getGoodsIntro()))
                && (this.getGoodsCategoryId() == null ? other.getGoodsCategoryId() == null : this.getGoodsCategoryId().equals(other.getGoodsCategoryId()))
                && (this.getGoodsCoverImg() == null ? other.getGoodsCoverImg() == null : this.getGoodsCoverImg().equals(other.getGoodsCoverImg()))
                && (this.getGoodsCarousel() == null ? other.getGoodsCarousel() == null : this.getGoodsCarousel().equals(other.getGoodsCarousel()))
                && (this.getGoodsDetailContent() == null ? other.getGoodsDetailContent() == null : this.getGoodsDetailContent().equals(other.getGoodsDetailContent()))
                && (this.getOriginalPrice() == null ? other.getOriginalPrice() == null : this.getOriginalPrice().equals(other.getOriginalPrice()))
                && (this.getSellingPrice() == null ? other.getSellingPrice() == null : this.getSellingPrice().equals(other.getSellingPrice()))
                && (this.getStockNum() == null ? other.getStockNum() == null : this.getStockNum().equals(other.getStockNum()))
                && (this.getTag() == null ? other.getTag() == null : this.getTag().equals(other.getTag()))
                && (this.getGoodsSellStatus() == null ? other.getGoodsSellStatus() == null : this.getGoodsSellStatus().equals(other.getGoodsSellStatus()))
                && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateUser() == null ? other.getUpdateUser() == null : this.getUpdateUser().equals(other.getUpdateUser()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getGoodsId() == null) ? 0 : getGoodsId().hashCode());
        result = prime * result + ((getGoodsName() == null) ? 0 : getGoodsName().hashCode());
        result = prime * result + ((getGoodsIntro() == null) ? 0 : getGoodsIntro().hashCode());
        result = prime * result + ((getGoodsCategoryId() == null) ? 0 : getGoodsCategoryId().hashCode());
        result = prime * result + ((getGoodsCoverImg() == null) ? 0 : getGoodsCoverImg().hashCode());
        result = prime * result + ((getGoodsCarousel() == null) ? 0 : getGoodsCarousel().hashCode());
        result = prime * result + ((getGoodsDetailContent() == null) ? 0 : getGoodsDetailContent().hashCode());
        result = prime * result + ((getOriginalPrice() == null) ? 0 : getOriginalPrice().hashCode());
        result = prime * result + ((getSellingPrice() == null) ? 0 : getSellingPrice().hashCode());
        result = prime * result + ((getStockNum() == null) ? 0 : getStockNum().hashCode());
        result = prime * result + ((getTag() == null) ? 0 : getTag().hashCode());
        result = prime * result + ((getGoodsSellStatus() == null) ? 0 : getGoodsSellStatus().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateUser() == null) ? 0 : getUpdateUser().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                " [" +
                "Hash = " + hashCode() +
                ", goodsId=" + goodsId +
                ", goodsName=" + goodsName +
                ", goodsIntro=" + goodsIntro +
                ", goodsCategoryId=" + goodsCategoryId +
                ", goodsCoverImg=" + goodsCoverImg +
                ", goodsCarousel=" + goodsCarousel +
                ", goodsDetailContent=" + goodsDetailContent +
                ", originalPrice=" + originalPrice +
                ", sellingPrice=" + sellingPrice +
                ", stockNum=" + stockNum +
                ", tag=" + tag +
                ", goodsSellStatus=" + goodsSellStatus +
                ", createUser=" + createUser +
                ", createTime=" + createTime +
                ", updateUser=" + updateUser +
                ", updateTime=" + updateTime +
                ", serialVersionUID=" + serialVersionUID +
                "]";
    }
}