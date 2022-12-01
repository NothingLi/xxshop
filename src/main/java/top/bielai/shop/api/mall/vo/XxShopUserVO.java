
package top.bielai.shop.api.mall.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class XxShopUserVO implements Serializable {

    
    /**
     * 用户昵称
     */
    private String nickName;

    
    /**
     * 用户登录名
     */
    private String loginName;

    
    /**
     * 个性签名
     */
    private String introduceSign;
}
