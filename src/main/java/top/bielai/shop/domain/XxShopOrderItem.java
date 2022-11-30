package top.bielai.shop.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Administrator
 * @TableName tb_xx_shop_order_item
 */
@TableName(value = "tb_xx_shop_order_item")
@Data
public class XxShopOrderItem implements Serializable {
    /**
     * 订单关联购物项主键id
     */
    @TableId(value = "order_item_id", type = IdType.AUTO)
    private Long orderItemId;

    /**
     * 订单主键id
     */
    @TableField(value = "order_id")
    private Long orderId;

    /**
     * 关联商品id
     */
    @TableField(value = "goods_id")
    private Long goodsId;

    /**
     * 下单时商品的名称(订单快照)
     */
    @TableField(value = "goods_name")
    private String goodsName;

    /**
     * 下单时商品的主图(订单快照)
     */
    @TableField(value = "goods_cover_img")
    private String goodsCoverImg;

    /**
     * 下单时商品的价格(订单快照)
     */
    @TableField(value = "selling_price")
    private Integer sellingPrice;

    /**
     * 数量(订单快照)
     */
    @TableField(value = "goods_count")
    private Integer goodsCount;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

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
        XxShopOrderItem other = (XxShopOrderItem) that;
        return (this.getOrderItemId() == null ? other.getOrderItemId() == null : this.getOrderItemId().equals(other.getOrderItemId()))
                && (this.getOrderId() == null ? other.getOrderId() == null : this.getOrderId().equals(other.getOrderId()))
                && (this.getGoodsId() == null ? other.getGoodsId() == null : this.getGoodsId().equals(other.getGoodsId()))
                && (this.getGoodsName() == null ? other.getGoodsName() == null : this.getGoodsName().equals(other.getGoodsName()))
                && (this.getGoodsCoverImg() == null ? other.getGoodsCoverImg() == null : this.getGoodsCoverImg().equals(other.getGoodsCoverImg()))
                && (this.getSellingPrice() == null ? other.getSellingPrice() == null : this.getSellingPrice().equals(other.getSellingPrice()))
                && (this.getGoodsCount() == null ? other.getGoodsCount() == null : this.getGoodsCount().equals(other.getGoodsCount()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getOrderItemId() == null) ? 0 : getOrderItemId().hashCode());
        result = prime * result + ((getOrderId() == null) ? 0 : getOrderId().hashCode());
        result = prime * result + ((getGoodsId() == null) ? 0 : getGoodsId().hashCode());
        result = prime * result + ((getGoodsName() == null) ? 0 : getGoodsName().hashCode());
        result = prime * result + ((getGoodsCoverImg() == null) ? 0 : getGoodsCoverImg().hashCode());
        result = prime * result + ((getSellingPrice() == null) ? 0 : getSellingPrice().hashCode());
        result = prime * result + ((getGoodsCount() == null) ? 0 : getGoodsCount().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                " [" +
                "Hash = " + hashCode() +
                ", orderItemId=" + orderItemId +
                ", orderId=" + orderId +
                ", goodsId=" + goodsId +
                ", goodsName=" + goodsName +
                ", goodsCoverImg=" + goodsCoverImg +
                ", sellingPrice=" + sellingPrice +
                ", goodsCount=" + goodsCount +
                ", createTime=" + createTime +
                ", serialVersionUID=" + serialVersionUID +
                "]";
    }
}