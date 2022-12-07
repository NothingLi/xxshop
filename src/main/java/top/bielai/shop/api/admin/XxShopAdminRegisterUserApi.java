package top.bielai.shop.api.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.validator.constraints.Range;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.bielai.shop.api.admin.param.BatchIdParam;
import top.bielai.shop.domain.XxShopUser;
import top.bielai.shop.service.XxShopUserService;
import top.bielai.shop.util.Result;
import top.bielai.shop.util.ResultGenerator;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Arrays;

/**
 * 后台管理系统注册用户模块接口
 *
 * @author bielai
 */
@Valid
@Validated
@RestController
@RequestMapping("/manage-api/v2/users")
public class XxShopAdminRegisterUserApi {


    @Resource
    private XxShopUserService xxShopUserService;

    /**
     * 分页查询商城用户注册列表
     *
     * @param pageNumber 页码
     * @param pageSize   页数
     * @param lockStatus 用户状态
     * @return 分页结果
     */
    @GetMapping
    public Result<Page<XxShopUser>> page(@RequestParam @Min(value = 1, message = "页码输入不对！") Integer pageNumber,
                                         @RequestParam @Min(value = 10, message = "每页几条啊") Integer pageSize,
                                         @RequestParam(required = false) @Range(min = 0, max = 1, message = "用户状态选择不对噢") Integer lockStatus) {
        return ResultGenerator.genSuccessResult(xxShopUserService.page(new Page<>(pageNumber, pageSize),
                new LambdaQueryWrapper<XxShopUser>().eq(ObjectUtils.isNotEmpty(lockStatus), XxShopUser::getLockedFlag, lockStatus)));
    }

    /**
     * 批量设置用户锁定/用户解锁
     *
     * @param batchIdParam ids
     * @param lockStatus   用户状态
     * @return 结果
     */
    @PutMapping("/{lockStatus}")
    @Transactional(rollbackFor = Exception.class)
    public Result<String> changeUserLock(@Validated @RequestBody BatchIdParam batchIdParam, @PathVariable @Range(min = 0, max = 1, message = "用户状态选择不对噢") int lockStatus) {
        if (xxShopUserService.update(new LambdaUpdateWrapper<XxShopUser>().in(XxShopUser::getUserId, Arrays.asList(batchIdParam.getIds()))
                .set(XxShopUser::getLockedFlag, lockStatus))) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult();
        }
    }
}