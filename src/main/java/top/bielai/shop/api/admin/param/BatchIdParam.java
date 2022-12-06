package top.bielai.shop.api.admin.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author bielai
 */
@Data
public class BatchIdParam implements Serializable {
    @NotEmpty(message = "你要删除哪些呀？")
    Long[] ids;
}
