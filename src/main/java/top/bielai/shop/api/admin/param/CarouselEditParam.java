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
public class CarouselEditParam {


    /**
     * 待修改轮播图id
     */
    @NotNull(message = "轮播图id不能为空")
    private Integer carouselId;


    /**
     * 轮播图URL地址
     */
    @NotEmpty(message = "轮播图URL不能为空")
    private String carouselUrl;


    /**
     * 轮播图跳转地址
     */
    @NotEmpty(message = "轮播图跳转地址不能为空")
    private String redirectUrl;


    /**
     * 排序值
     */
    @Min(value = 1, message = "排序值最低为1")
    @Max(value = 200, message = "排序值最高为200")
    @NotNull(message = "排序值不能为空")
    private Integer carouselRank;
}
