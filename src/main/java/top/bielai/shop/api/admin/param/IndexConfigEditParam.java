package top.bielai.shop.api.admin.param;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author bielai
 */
@Data
public class IndexConfigEditParam {


    /**
     * 待修改配置id
     */
    @NotNull(message = "configId不能为空")
    @Min(value = 1, message = "configId不能为空")
    private Long configId;


    /**
     * 配置的名称
     */
    @NotEmpty(message = "configName不能为空")
    private String configName;


    /**
     * 配置类别
     */
    @NotNull(message = "configType不能为空")
    @Min(value = 1, message = "configType最小为1")
    @Max(value = 5, message = "configType最大为5")
    private Byte configType;


    /**
     * 商品id
     */
    @NotNull(message = "商品id不能为空")
    @Min(value = 1, message = "商品id不能为空")
    private Long goodsId;


    /**
     * 排序值
     */
    @Min(value = 1, message = "configRank最低为1")
    @Max(value = 200, message = "configRank最高为200")
    @NotNull(message = "configRank不能为空")
    private Integer configRank;
}