package top.bielai.shop.api.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import top.bielai.shop.common.Constants;
import top.bielai.shop.common.ErrorEnum;
import top.bielai.shop.common.XxShopException;
import top.bielai.shop.domain.XxShopFile;
import top.bielai.shop.service.XxShopFileService;
import top.bielai.shop.util.Result;
import top.bielai.shop.util.ResultGenerator;
import top.bielai.shop.util.XxShopUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * 后台管理系统文件上传接口
 *
 * @author bielai
 */
@Slf4j
@RestController
@RequestMapping("/manage-api/v2/upload")
public class XxShopAdminUploadApi {


    private final StandardServletMultipartResolver standardServletMultipartResolver;

    @Resource
    private XxShopFileService fileService;

    public XxShopAdminUploadApi(StandardServletMultipartResolver standardServletMultipartResolver) {
        this.standardServletMultipartResolver = standardServletMultipartResolver;
    }

    /**
     * 单个文件上传
     *
     * @param file 文件
     * @return 文件路径
     * @throws URISyntaxException url错误异常
     */
    @PostMapping("/file")
    public Result<String> upload(HttpServletRequest httpServletRequest, @RequestParam("file") MultipartFile file) throws URISyntaxException {
        try {
            String filePath = uploadFile(file);
            return ResultGenerator.genSuccessResult(XxShopUtils.getHost(new URI(httpServletRequest.getRequestURL() + "")) + "/files/" + filePath);
        } catch (IOException e) {
            log.error(e.getMessage());
            return ResultGenerator.genFailResult("文件上传失败");
        }
    }

    /**
     * wangEditor图片上传
     *
     * @return 文件地址
     * @throws URISyntaxException url错误异常
     */
    @PostMapping("/files")
    public Result<List<String>> uploadV2(HttpServletRequest httpServletRequest) throws URISyntaxException {
        List<MultipartFile> multipartFiles = new ArrayList<>(8);
        if (standardServletMultipartResolver.isMultipart(httpServletRequest)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) httpServletRequest;
            Iterator<String> iter = multiRequest.getFileNames();
            int total = 0;
            while (iter.hasNext()) {
                if (total > 5) {
                    throw new XxShopException(ErrorEnum.IMG_TOO_MORE);
                }
                total += 1;
                MultipartFile file = multiRequest.getFile(iter.next());
                multipartFiles.add(file);
            }
        }
        if (CollectionUtils.isEmpty(multipartFiles)) {
            throw new XxShopException(ErrorEnum.ERROR_PARAM);
        }
        List<String> fileNames = new ArrayList<>(multipartFiles.size());
        for (MultipartFile multipartFile : multipartFiles) {
            try {
                fileNames.add(XxShopUtils.getHost(new URI(httpServletRequest.getRequestURL() + "")) + "/files/" + uploadFile(multipartFile));
            } catch (IOException e) {
                log.error(e.getMessage());
                throw new XxShopException(ErrorEnum.ERROR);
            }
        }
        return ResultGenerator.genSuccessResult(fileNames);
    }


    private String uploadFile(MultipartFile file) throws IOException {
        String fileMd5 = DigestUtils.md5Hex(file.getInputStream());
        XxShopFile one = fileService.getOne(new LambdaQueryWrapper<XxShopFile>().eq(XxShopFile::getFileMd5, fileMd5));
        if (ObjectUtils.isNotEmpty(one)) {
            return one.getFilePath();
        }
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //生成文件名称通用方法
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH/mm/");
        String filePath = sdf.format(new Date());
        String newFileName = filePath + fileMd5 + suffixName;
        File fileDirectory = new File(Constants.FILE_UPLOAD_DIC + filePath);
        //创建文件
        File destFile = new File(Constants.FILE_UPLOAD_DIC + newFileName);
        if (!fileDirectory.exists() || !fileDirectory.mkdirs()) {
            throw new IOException("文件夹创建失败,路径为：" + fileDirectory);
        }
        file.transferTo(destFile);
        XxShopFile shopFile = new XxShopFile();
        shopFile.setFileMd5(fileMd5);
        shopFile.setFileName(fileName);
        shopFile.setFilePath(newFileName);
        fileService.save(shopFile);
        return newFileName;
    }

}
