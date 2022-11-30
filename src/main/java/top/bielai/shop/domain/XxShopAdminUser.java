package top.bielai.shop.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Administrator
 * @TableName tb_xx_shop_admin_user
 */
@TableName(value = "tb_xx_shop_admin_user")
@Data
public class XxShopAdminUser implements Serializable {
    /**
     * 管理员id
     */
    @TableId(value = "admin_user_id", type = IdType.AUTO)
    private Long adminUserId;

    /**
     * 管理员登陆名称
     */
    @TableField(value = "login_user_name")
    private String loginUserName;

    /**
     * 管理员登陆密码
     */
    @TableField(value = "login_password")
    private String loginPassword;

    /**
     * 管理员显示昵称
     */
    @TableField(value = "nick_name")
    private String nickName;

    /**
     * 是否锁定 0未锁定 1已锁定无法登陆
     */
    @TableField(value = "locked")
    private Integer locked;

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
        XxShopAdminUser other = (XxShopAdminUser) that;
        return (this.getAdminUserId() == null ? other.getAdminUserId() == null : this.getAdminUserId().equals(other.getAdminUserId()))
                && (this.getLoginUserName() == null ? other.getLoginUserName() == null : this.getLoginUserName().equals(other.getLoginUserName()))
                && (this.getLoginPassword() == null ? other.getLoginPassword() == null : this.getLoginPassword().equals(other.getLoginPassword()))
                && (this.getNickName() == null ? other.getNickName() == null : this.getNickName().equals(other.getNickName()))
                && (this.getLocked() == null ? other.getLocked() == null : this.getLocked().equals(other.getLocked()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getAdminUserId() == null) ? 0 : getAdminUserId().hashCode());
        result = prime * result + ((getLoginUserName() == null) ? 0 : getLoginUserName().hashCode());
        result = prime * result + ((getLoginPassword() == null) ? 0 : getLoginPassword().hashCode());
        result = prime * result + ((getNickName() == null) ? 0 : getNickName().hashCode());
        result = prime * result + ((getLocked() == null) ? 0 : getLocked().hashCode());
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                " [" +
                "Hash = " + hashCode() +
                ", adminUserId=" + adminUserId +
                ", loginUserName=" + loginUserName +
                ", loginPassword=" + loginPassword +
                ", nickName=" + nickName +
                ", locked=" + locked +
                ", serialVersionUID=" + serialVersionUID +
                "]";
    }
}