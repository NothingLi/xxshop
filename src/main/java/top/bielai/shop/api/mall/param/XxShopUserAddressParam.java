package top.bielai.shop.api.mall.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 添加收货地址param
 *
 * @author bielai
 */
@Data
public class XxShopUserAddressParam {

    public interface Add {
    }

    public interface Update {
    }

    /**
     * 地址id
     */
    @NotNull(message = "修改哪个地址呀！", groups = Update.class)
    private Long addressId;

    /**
     * 收件人名称
     */
    @NotBlank(message = "你要送给谁呀！")
    private String userName;


    /**
     * 收件人联系方式
     */
    @NotBlank(message = "我咋联系他呢！")
    private String userPhone;

    /**
     * 是否默认地址 0-不是 1-是
     */
    @NotNull(message = "以后都送这儿么？")
    private Byte defaultFlag;


    /**
     * 收件地区，填写省市区
     */
    @NotBlank(message = "收件地区得填写省市区噢")
    private String area;


    /**
     * 详细地址
     */
    @NotBlank(message = "再具体点呢？")
    private String detailAddress;
}
