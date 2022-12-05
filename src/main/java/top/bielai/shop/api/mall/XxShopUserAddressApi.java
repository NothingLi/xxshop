package top.bielai.shop.api.mall;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.bielai.shop.api.mall.param.XxShopUserAddressParam;
import top.bielai.shop.api.mall.vo.XxShopUserAddressVO;
import top.bielai.shop.common.ErrorEnum;
import top.bielai.shop.common.XxShopException;
import top.bielai.shop.config.annotation.TokenToShopUser;
import top.bielai.shop.domain.XxShopUserAddress;
import top.bielai.shop.service.XxShopUserAddressService;
import top.bielai.shop.util.BeanUtil;
import top.bielai.shop.util.Result;
import top.bielai.shop.util.ResultGenerator;

import javax.annotation.Resource;
import java.util.List;

/**
 * 小新商城个人地址相关接口
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/api/v1/address")
public class XxShopUserAddressApi {

    @Resource
    private XxShopUserAddressService userAddressService;

    /**
     * 查询当前用户收货地址
     *
     * @return 收货地址列表
     */
    @GetMapping
    public Result<List<XxShopUserAddressVO>> addressList(@TokenToShopUser Long userId) {
        List<XxShopUserAddress> list = userAddressService.list(new LambdaQueryWrapper<XxShopUserAddress>().eq(XxShopUserAddress::getUserId, userId));
        return ResultGenerator.genSuccessResult(BeanUtil.copyList(list, XxShopUserAddressVO.class));
    }

    /**
     * 新增一个收货地址
     *
     * @param xxShopUserAddressParam 收货地址
     * @return 结果
     */
    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public Result<String> saveUserAddress(@Validated @RequestBody XxShopUserAddressParam xxShopUserAddressParam,
                                          @TokenToShopUser Long userId) {
        XxShopUserAddress userAddress = new XxShopUserAddress();
        BeanUtil.copyProperties(xxShopUserAddressParam, userAddress);
        userAddress.setUserId(userId);
        //添加成功
        if (userAddressService.save(userAddress)) {
            if (userAddress.getDefaultFlag() == 1) {
                // 此地址是默认的话把其他地址都改成非默认地址
                userAddressService.update(new LambdaUpdateWrapper<XxShopUserAddress>().eq(XxShopUserAddress::getUserId, userId)
                        .ne(XxShopUserAddress::getAddressId, userAddress.getAddressId())
                        .set(XxShopUserAddress::getDefaultFlag, 0));
            }
            return ResultGenerator.genSuccessResult();
        }
        //添加失败
        return ResultGenerator.genFailResult();
    }

    /**
     * 修改收货地址
     *
     * @param updateShopUserAddressParam 地址参数
     * @return 结果
     */
    @PutMapping
    @Transactional(rollbackFor = Exception.class)
    public Result<String> updateShopUserAddress(@Validated(value = {XxShopUserAddressParam.Update.class}) @RequestBody XxShopUserAddressParam updateShopUserAddressParam,
                                                @TokenToShopUser Long userId) {
        XxShopUserAddress one = userAddressService.getOne(new LambdaQueryWrapper<XxShopUserAddress>().eq(XxShopUserAddress::getAddressId, updateShopUserAddressParam.getAddressId())
                .eq(XxShopUserAddress::getUserId, userId));
        if (ObjectUtils.isEmpty(one)) {
            XxShopException.fail(ErrorEnum.DATA_NOT_EXIST);
        }
        XxShopUserAddress userAddress = new XxShopUserAddress();
        BeanUtil.copyProperties(updateShopUserAddressParam, userAddress);
        userAddress.setUserId(userId);
        //修改成功
        if (userAddressService.updateById(userAddress)) {
            if (userAddress.getDefaultFlag() == 1) {
                // 此地址是默认的话把其他地址都改成非默认地址
                userAddressService.update(new LambdaUpdateWrapper<XxShopUserAddress>().eq(XxShopUserAddress::getUserId, userId)
                        .ne(XxShopUserAddress::getAddressId, userAddress.getAddressId())
                        .set(XxShopUserAddress::getDefaultFlag, 0));
            }
            return ResultGenerator.genSuccessResult();
        }
        //修改失败
        return ResultGenerator.genFailResult("修改失败");
    }

    /**
     * 根据id获取收货地址详情
     *
     * @param addressId 收货地址
     * @return 地址详情
     */
    @GetMapping("/{addressId}")
    public Result<XxShopUserAddressVO> getShopUserAddress(@PathVariable("addressId") Long addressId,
                                                          @TokenToShopUser Long userId) {
        XxShopUserAddress one = userAddressService.getOne(new LambdaQueryWrapper<XxShopUserAddress>().eq(XxShopUserAddress::getAddressId, addressId)
                .eq(XxShopUserAddress::getUserId, userId));
        if (ObjectUtils.isEmpty(one)) {
            XxShopException.fail(ErrorEnum.DATA_NOT_EXIST);
        }
        XxShopUserAddressVO vo = new XxShopUserAddressVO();
        BeanUtil.copyProperties(one, vo);
        return ResultGenerator.genSuccessResult(vo);
    }

    /**
     * 获取当前用户默认收货地址
     *
     * @return 地址信息
     */
    @GetMapping("/address/default")
    public Result<XxShopUserAddressVO> getDefaultShopUserAddress(@TokenToShopUser Long userId) {
        XxShopUserAddress one = userAddressService.getOne(new LambdaQueryWrapper<XxShopUserAddress>().eq(XxShopUserAddress::getDefaultFlag, 1)
                .eq(XxShopUserAddress::getUserId, userId));
        top.bielai.shop.api.mall.vo.XxShopUserAddressVO vo = new top.bielai.shop.api.mall.vo.XxShopUserAddressVO();
        BeanUtil.copyProperties(one, vo);
        return ResultGenerator.genSuccessResult(vo);
    }

    /**
     * 删除收货地址
     *
     * @param addressId 地址id
     * @return 结果
     */
    @DeleteMapping("/{addressId}")
    @Transactional(rollbackFor = Exception.class)
    public Result<String> deleteAddress(@PathVariable("addressId") Long addressId,
                                        @TokenToShopUser Long userId) {
        XxShopUserAddress one = userAddressService.getOne(new LambdaQueryWrapper<XxShopUserAddress>().eq(XxShopUserAddress::getAddressId, addressId)
                .eq(XxShopUserAddress::getUserId, userId));
        if (ObjectUtils.isEmpty(one)) {
            XxShopException.fail(ErrorEnum.DATA_NOT_EXIST);
        }
        //删除成功
        if (userAddressService.removeById(one)) {
            return ResultGenerator.genSuccessResult();
        }
        //删除失败
        return ResultGenerator.genFailResult();
    }
}
