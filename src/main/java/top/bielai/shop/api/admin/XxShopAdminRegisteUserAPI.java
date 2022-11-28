/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本系统已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2021 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package top.bielai.shop.api.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import top.bielai.shop.api.admin.param.BatchIdParam;
import top.bielai.shop.config.annotation.TokenToAdminUser;
import top.bielai.shop.entity.AdminUserToken;
import top.bielai.shop.service.XxShopUserService;
import top.bielai.shop.util.PageQueryUtil;
import top.bielai.shop.util.Result;
import top.bielai.shop.util.ResultGenerator;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author bielai
 */
@RestController
@Api(value = "v1", tags = "8-6.后台管理系统注册用户模块接口")
@RequestMapping("/manage-api/v1")
public class XxShopAdminRegisteUserAPI {

    private static final Logger logger = LoggerFactory.getLogger(XxShopAdminRegisteUserAPI.class);

    @Resource
    private XxShopUserService xxShopUserService;

    /**
     * 列表
     */
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @ApiOperation(value = "商城注册用户列表", notes = "商城注册用户列表")
    public Result list(@RequestParam(required = false) @ApiParam(value = "页码") Integer pageNumber,
                       @RequestParam(required = false) @ApiParam(value = "每页条数") Integer pageSize,
                       @RequestParam(required = false) @ApiParam(value = "用户状态") Integer lockStatus, @TokenToAdminUser AdminUserToken adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        if (pageNumber == null || pageNumber < 1 || pageSize == null || pageSize < 10) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        Map params = new HashMap(8);
        params.put("page", pageNumber);
        params.put("limit", pageSize);
        if (lockStatus != null) {
            params.put("orderStatus", lockStatus);
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(xxShopUserService.getXxShopUsersPage(pageUtil));
    }

    /**
     * 用户禁用与解除禁用(0-未锁定 1-已锁定)
     */
    @RequestMapping(value = "/users/{lockStatus}", method = RequestMethod.PUT)
    @ApiOperation(value = "修改用户状态", notes = "批量修改，用户禁用与解除禁用(0-未锁定 1-已锁定)")
    public Result lockUser(@RequestBody BatchIdParam batchIdParam, @PathVariable int lockStatus, @TokenToAdminUser AdminUserToken adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        if (batchIdParam==null||batchIdParam.getIds().length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (lockStatus != 0 && lockStatus != 1) {
            return ResultGenerator.genFailResult("操作非法！");
        }
        if (xxShopUserService.lockUsers(batchIdParam.getIds(), lockStatus)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("禁用失败");
        }
    }
}