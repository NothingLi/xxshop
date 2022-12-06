package top.bielai.shop.api.mall.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 修改收货地址param
 *
 * @author bielai
 */
@Data
public class UpdateShopUserAddressParam {


    /**
     * 地址id
     */
    @NotNull(message = "修改哪个地址呀！")
    private Long addressId;


    /**
     * 收件人名称
     */
    @NotBlank(message = "送给谁啊！")
    private String userName;


    /**
     * 收件人联系方式
     */
    private String userPhone;

    @ApiModelProperty("是否默认地址 0-不是 1-是")
    private Byte defaultFlag;


    /**
     * 省
     */
    private String provinceName;


    /**
     * 市
     */
    private String cityName;


    /**
     * 区/县
     */
    private String regionName;


    /**
     * 详细地址
     */
    private String detailAddress;
}
