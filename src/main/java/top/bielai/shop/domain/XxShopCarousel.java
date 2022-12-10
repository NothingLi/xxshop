package top.bielai.shop.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author bielai
 * @TableName tb_xx_shop_carousel
 */
@TableName(value = "tb_xx_shop_carousel")
@Data
public class XxShopCarousel implements Serializable {
    /**
     * 首页轮播图主键id
     */
    @TableId(value = "carousel_id", type = IdType.AUTO)
    private Integer carouselId;

    /**
     * 轮播图
     */
    @TableField(value = "carousel_url")
    private String carouselUrl;

    /**
     * 点击后的跳转地址(默认不跳转)
     */
    @TableField(value = "redirect_url")
    private String redirectUrl;

    /**
     * 排序值(字段越大越靠前)
     */
    @TableField(value = "carousel_rank")
    private Integer carouselRank;

    /**
     * 删除标识字段(0-未删除 1-已删除)
     */
    @TableLogic
    @TableField(value = "is_deleted")
    private Byte isDeleted;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 创建者id
     */
    @TableField(value = "create_user")
    private Integer createUser;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    /**
     * 修改者id
     */
    @TableField(value = "update_user")
    private Integer updateUser;

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
        XxShopCarousel other = (XxShopCarousel) that;
        return (this.getCarouselId() == null ? other.getCarouselId() == null : this.getCarouselId().equals(other.getCarouselId()))
                && (this.getCarouselUrl() == null ? other.getCarouselUrl() == null : this.getCarouselUrl().equals(other.getCarouselUrl()))
                && (this.getRedirectUrl() == null ? other.getRedirectUrl() == null : this.getRedirectUrl().equals(other.getRedirectUrl()))
                && (this.getCarouselRank() == null ? other.getCarouselRank() == null : this.getCarouselRank().equals(other.getCarouselRank()))
                && (this.getIsDeleted() == null ? other.getIsDeleted() == null : this.getIsDeleted().equals(other.getIsDeleted()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
                && (this.getUpdateUser() == null ? other.getUpdateUser() == null : this.getUpdateUser().equals(other.getUpdateUser()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCarouselId() == null) ? 0 : getCarouselId().hashCode());
        result = prime * result + ((getCarouselUrl() == null) ? 0 : getCarouselUrl().hashCode());
        result = prime * result + ((getRedirectUrl() == null) ? 0 : getRedirectUrl().hashCode());
        result = prime * result + ((getCarouselRank() == null) ? 0 : getCarouselRank().hashCode());
        result = prime * result + ((getIsDeleted() == null) ? 0 : getIsDeleted().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getUpdateUser() == null) ? 0 : getUpdateUser().hashCode());
        return result;
    }

    @Override
    public String toString() {
        String sb = getClass().getSimpleName() +
                " [" +
                "Hash = " + hashCode() +
                ", carouselId=" + carouselId +
                ", carouselUrl=" + carouselUrl +
                ", redirectUrl=" + redirectUrl +
                ", carouselRank=" + carouselRank +
                ", isDeleted=" + isDeleted +
                ", createTime=" + createTime +
                ", createUser=" + createUser +
                ", updateTime=" + updateTime +
                ", updateUser=" + updateUser +
                ", serialVersionUID=" + serialVersionUID +
                "]";
        return sb;
    }
}