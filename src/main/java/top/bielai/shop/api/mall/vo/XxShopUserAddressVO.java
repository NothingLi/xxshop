package top.bielai.shop.api.mall.vo;

import lombok.Data;

/**
 * 收货地址VO
 *
 * @author bielai
 */
@Data
public class XxShopUserAddressVO {


    /**
     * 地址id
     */
    private Long addressId;


    /**
     * 用户id
     */
    private Long userId;


    /**
     * 收件人名称
     */
    private String userName;


    /**
     * 收件人联系方式
     */
    private String userPhone;

    /**
     * 是否默认地址 0-不是 1-是
     */
    private Byte defaultFlag;


    /**
     * 收件地区
     */
    private String area;


    /**
     * 详细地址
     */
    private String detailAddress;
}
