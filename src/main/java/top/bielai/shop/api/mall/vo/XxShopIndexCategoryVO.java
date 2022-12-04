
package top.bielai.shop.api.mall.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 首页分类数据VO
 *
 * @author bielai
 */
@Data
public class XxShopIndexCategoryVO implements Serializable {

    /**
     * 当前分类id
     */
    private Long categoryId;

    /**
     * 当前分类级别
     */
    private Byte categoryLevel;

    /**
     * 当前分类名称
     */
    private String categoryName;

    /**
     * 父级分类id
     */
    private Long parentId;
    /**
     * 二级分类列表
     */
    private List<XxShopIndexCategoryVO> childrenCategoryVO;
}
