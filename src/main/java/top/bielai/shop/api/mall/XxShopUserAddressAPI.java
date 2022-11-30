
package top.bielai.shop.api.mall;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import top.bielai.shop.api.mall.param.SaveShopUserAddressParam;
import top.bielai.shop.api.mall.param.UpdateShopUserAddressParam;
import top.bielai.shop.api.mall.vo.XxShopUserAddressVO;
import top.bielai.shop.common.ServiceResultEnum;
import top.bielai.shop.config.annotation.TokenToShopUser;
import top.bielai.shop.domain.XxShopUser;
import top.bielai.shop.domain.XxShopUserAddress;
import top.bielai.shop.service.XxShopUserAddressService;
import top.bielai.shop.util.BeanUtil;
import top.bielai.shop.util.Result;
import top.bielai.shop.util.ResultGenerator;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(value = "v1", tags = "6.小新商城个人地址相关接口")
@RequestMapping("/api/v1")
public class XxShopUserAddressAPI {

    @Resource
    private XxShopUserAddressService mallUserAddressService;

    @GetMapping("/address")
    @ApiOperation(value = "我的收货地址列表", notes = "")
    public Result<List<XxShopUserAddressVO>> addressList(@TokenToShopUser XxShopUser loginShopUser) {
        return ResultGenerator.genSuccessResult(mallUserAddressService.getMyAddresses(loginShopUser.getUserId()));
    }

    @PostMapping("/address")
    @ApiOperation(value = "添加地址", notes = "")
    public Result<Boolean> saveUserAddress(@RequestBody SaveShopUserAddressParam saveShopUserAddressParam,
                                           @TokenToShopUser XxShopUser loginShopUser) {
        ShopUserAddress userAddress = new ShopUserAddress();
        BeanUtil.copyProperties(saveShopUserAddressParam, userAddress);
        userAddress.setUserId(loginShopUser.getUserId());
        Boolean saveResult = mallUserAddressService.saveUserAddress(userAddress);
        //添加成功
        if (saveResult) {
            return ResultGenerator.genSuccessResult();
        }
        //添加失败
        return ResultGenerator.genFailResult("添加失败");
    }

    @PutMapping("/address")
    @ApiOperation(value = "修改地址", notes = "")
    public Result<Boolean> updateShopUserAddress(@RequestBody UpdateShopUserAddressParam updateShopUserAddressParam,
                                                 @TokenToShopUser XxShopUser loginShopUser) {
        ShopUserAddress mallUserAddressById = mallUserAddressService.getShopUserAddressById(updateShopUserAddressParam.getAddressId());
        if (!loginShopUser.getUserId().equals(mallUserAddressById.getUserId())) {
            return ResultGenerator.genFailResult(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }
        ShopUserAddress userAddress = new ShopUserAddress();
        BeanUtil.copyProperties(updateShopUserAddressParam, userAddress);
        userAddress.setUserId(loginShopUser.getUserId());
        Boolean updateResult = mallUserAddressService.updateShopUserAddress(userAddress);
        //修改成功
        if (updateResult) {
            return ResultGenerator.genSuccessResult();
        }
        //修改失败
        return ResultGenerator.genFailResult("修改失败");
    }

    @GetMapping("/address/{addressId}")
    @ApiOperation(value = "获取收货地址详情", notes = "传参为地址id")
    public Result<XxShopUserAddressVO> getShopUserAddress(@PathVariable("addressId") Long addressId,
                                                              @TokenToShopUser XxShopUser loginShopUser) {
        ShopUserAddress mallUserAddressById = mallUserAddressService.getShopUserAddressById(addressId);
        XxShopUserAddressVO xxShopUserAddressVO = new XxShopUserAddressVO();
        BeanUtil.copyProperties(mallUserAddressById, xxShopUserAddressVO);
        if (!loginShopUser.getUserId().equals(mallUserAddressById.getUserId())) {
            return ResultGenerator.genFailResult(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }
        return ResultGenerator.genSuccessResult(xxShopUserAddressVO);
    }

    @GetMapping("/address/default")
    @ApiOperation(value = "获取默认收货地址", notes = "无传参")
    public Result getDefaultShopUserAddress(@TokenToShopUser XxShopUser loginShopUser) {
        ShopUserAddress mallUserAddressById = mallUserAddressService.getMyDefaultAddressByUserId(loginShopUser.getUserId());
        return ResultGenerator.genSuccessResult(mallUserAddressById);
    }

    @DeleteMapping("/address/{addressId}")
    @ApiOperation(value = "删除收货地址", notes = "传参为地址id")
    public Result deleteAddress(@PathVariable("addressId") Long addressId,
                                @TokenToShopUser XxShopUser loginShopUser) {
        ShopUserAddress mallUserAddressById = mallUserAddressService.getShopUserAddressById(addressId);
        if (!loginShopUser.getUserId().equals(mallUserAddressById.getUserId())) {
            return ResultGenerator.genFailResult(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }
        Boolean deleteResult = mallUserAddressService.deleteById(addressId);
        //删除成功
        if (deleteResult) {
            return ResultGenerator.genSuccessResult();
        }
        //删除失败
        return ResultGenerator.genFailResult(ServiceResultEnum.OPERATE_ERROR.getResult());
    }
}
