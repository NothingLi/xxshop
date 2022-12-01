
package top.bielai.shop.api.mall.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 首页分类数据VO(第三级)
 */
@Data
public class ThirdLevelCategoryVO implements Serializable {

    
    /**
     * 当前三级分类id
     */
    private Long categoryId;

    
    /**
     * 当前分类级别
     */
    private Byte categoryLevel;

    
    /**
     * 当前三级分类名称
     */
    private String categoryName;
}
