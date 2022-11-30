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
 * @TableName tb_xx_shop_user
 */
@TableName(value = "tb_xx_shop_user")
@Data
public class XxShopUser implements Serializable {
    /**
     * 用户主键id
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    /**
     * 用户昵称
     */
    @TableField(value = "nick_name")
    private String nickName;

    /**
     * 登陆名称(默认为手机号)
     */
    @TableField(value = "login_name")
    private String loginName;

    /**
     * MD5加密后的密码
     */
    @TableField(value = "password_md5")
    private String passwordMd5;

    /**
     * 个性签名
     */
    @TableField(value = "introduce_sign")
    private String introduceSign;

    /**
     * 注销标识字段(0-正常 1-已注销)
     */
    @TableField(value = "is_deleted")
    private Integer isDeleted;

    /**
     * 锁定标识字段(0-未锁定 1-已锁定)
     */
    @TableField(value = "locked_flag")
    private Integer lockedFlag;

    /**
     * 注册时间
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
        XxShopUser other = (XxShopUser) that;
        return (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
                && (this.getNickName() == null ? other.getNickName() == null : this.getNickName().equals(other.getNickName()))
                && (this.getLoginName() == null ? other.getLoginName() == null : this.getLoginName().equals(other.getLoginName()))
                && (this.getPasswordMd5() == null ? other.getPasswordMd5() == null : this.getPasswordMd5().equals(other.getPasswordMd5()))
                && (this.getIntroduceSign() == null ? other.getIntroduceSign() == null : this.getIntroduceSign().equals(other.getIntroduceSign()))
                && (this.getIsDeleted() == null ? other.getIsDeleted() == null : this.getIsDeleted().equals(other.getIsDeleted()))
                && (this.getLockedFlag() == null ? other.getLockedFlag() == null : this.getLockedFlag().equals(other.getLockedFlag()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getNickName() == null) ? 0 : getNickName().hashCode());
        result = prime * result + ((getLoginName() == null) ? 0 : getLoginName().hashCode());
        result = prime * result + ((getPasswordMd5() == null) ? 0 : getPasswordMd5().hashCode());
        result = prime * result + ((getIntroduceSign() == null) ? 0 : getIntroduceSign().hashCode());
        result = prime * result + ((getIsDeleted() == null) ? 0 : getIsDeleted().hashCode());
        result = prime * result + ((getLockedFlag() == null) ? 0 : getLockedFlag().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                " [" +
                "Hash = " + hashCode() +
                ", userId=" + userId +
                ", nickName=" + nickName +
                ", loginName=" + loginName +
                ", passwordMd5=" + passwordMd5 +
                ", introduceSign=" + introduceSign +
                ", isDeleted=" + isDeleted +
                ", lockedFlag=" + lockedFlag +
                ", createTime=" + createTime +
                ", serialVersionUID=" + serialVersionUID +
                "]";
    }
}