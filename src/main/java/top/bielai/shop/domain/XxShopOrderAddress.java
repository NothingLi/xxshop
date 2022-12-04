package top.bielai.shop.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 订单收货地址关联表
 *
 * @author bielai
 * @TableName tb_xx_shop_order_address
 */
@TableName(value = "tb_xx_shop_order_address")
@Data
public class XxShopOrderAddress implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "order_id")
    private Long orderId;

    /**
     * 收货人姓名
     */
    @TableField(value = "user_name")
    private String userName;

    /**
     * 收货人手机号
     */
    @TableField(value = "user_phone")
    private String userPhone;

    /**
     * 省
     */
    @TableField(value = "province_name")
    private String provinceName;

    /**
     * 城
     */
    @TableField(value = "city_name")
    private String cityName;

    /**
     * 区
     */
    @TableField(value = "region_name")
    private String regionName;

    /**
     * 收件详细地址(街道/楼宇/单元)
     */
    @TableField(value = "detail_address")
    private String detailAddress;

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
        XxShopOrderAddress other = (XxShopOrderAddress) that;
        return (this.getOrderId() == null ? other.getOrderId() == null : this.getOrderId().equals(other.getOrderId()))
                && (this.getUserName() == null ? other.getUserName() == null : this.getUserName().equals(other.getUserName()))
                && (this.getUserPhone() == null ? other.getUserPhone() == null : this.getUserPhone().equals(other.getUserPhone()))
                && (this.getProvinceName() == null ? other.getProvinceName() == null : this.getProvinceName().equals(other.getProvinceName()))
                && (this.getCityName() == null ? other.getCityName() == null : this.getCityName().equals(other.getCityName()))
                && (this.getRegionName() == null ? other.getRegionName() == null : this.getRegionName().equals(other.getRegionName()))
                && (this.getDetailAddress() == null ? other.getDetailAddress() == null : this.getDetailAddress().equals(other.getDetailAddress()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getOrderId() == null) ? 0 : getOrderId().hashCode());
        result = prime * result + ((getUserName() == null) ? 0 : getUserName().hashCode());
        result = prime * result + ((getUserPhone() == null) ? 0 : getUserPhone().hashCode());
        result = prime * result + ((getProvinceName() == null) ? 0 : getProvinceName().hashCode());
        result = prime * result + ((getCityName() == null) ? 0 : getCityName().hashCode());
        result = prime * result + ((getRegionName() == null) ? 0 : getRegionName().hashCode());
        result = prime * result + ((getDetailAddress() == null) ? 0 : getDetailAddress().hashCode());
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                " [" +
                "Hash = " + hashCode() +
                ", orderId=" + orderId +
                ", userName=" + userName +
                ", userPhone=" + userPhone +
                ", provinceName=" + provinceName +
                ", cityName=" + cityName +
                ", regionName=" + regionName +
                ", detailAddress=" + detailAddress +
                ", serialVersionUID=" + serialVersionUID +
                "]";
    }
}