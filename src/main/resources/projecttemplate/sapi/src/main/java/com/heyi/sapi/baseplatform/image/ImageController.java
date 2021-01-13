package com.heyi.${project.dataBase}.baseplatform.image;


import com.heyi.${project.dataBase}.basecommon.BaseController;
import com.heyi.${project.dataBase}.basecommon.BaseResponse;
import cn.hutool.http.HttpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static com.heyi.${project.dataBase}.basecommon.BaseConstant.DEFAULT_LENGTH;


@RestController
@Scope("request")
@Api(value = "上传图片", tags = "上传图片到oss服务器")
@PropertySource("classpath:oss.properties")
public class ImageController extends BaseController {

    @Resource
    private ImageService imageService;

    @ApiOperation(value = "图片上传", notes = "上传图片")
    @PostMapping("uploadoss")
    public BaseResponse uploadoss(@RequestParam("file") MultipartFile uploadfile) throws Exception {
        if (uploadfile.isEmpty()) {
            return new BaseResponse(false, "上传文件不允许为空");
        }
        String suffix = uploadfile.getOriginalFilename().substring(uploadfile.getOriginalFilename().lastIndexOf("."));
        String url = imageService.uploadoss(sysUser, uploadfile.getInputStream(),suffix);
        return new BaseResponse(true, "上传成功", url);
    }
    @ApiOperation(value = "多图片上传", notes = "上传图片")
    @PostMapping("uploadossMany")
    public BaseResponse uploadossMany(@RequestParam("files") MultipartFile[] uploadfiles) throws Exception {
        if (uploadfiles.length == DEFAULT_LENGTH) {
            return new BaseResponse(false, "上传文件不允许为空");
        }
        Map<String, List<String>> resultMap = imageService.uploadossMany(uploadfiles,sysUser);
        return new BaseResponse(true, "上传成功", resultMap);
    }
    @ApiOperation(value = "文件删除", notes = "文件删除")
    @PostMapping("deleteoss")
    public BaseResponse deleteoss(@RequestParam("filePath") String filePath) {
        imageService.deleteoss(filePath);
        return new BaseResponse(true, "删除成功");
    }

    @ApiOperation(value = "文件转发", notes = "文件转发")
    @GetMapping("redirect")
    public String redirect(@RequestParam("url") String url) {
        return HttpUtil.get(url);
    }
}
