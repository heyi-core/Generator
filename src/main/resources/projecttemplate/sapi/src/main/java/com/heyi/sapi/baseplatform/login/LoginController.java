package com.heyi.${project.dataBase}.baseplatform.login;

import com.heyi.${project.dataBase}.basecommon.BaseController;
import com.heyi.${project.dataBase}.basecommon.BaseResponse;
import com.heyi.${project.dataBase}.platform.sysuser.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

@RestController
@Scope("request")
@RequestMapping("/login")
@Api(value = "用户登陆", tags = "用户登陆操作接口")
public class LoginController extends BaseController {
    @Resource
    private LoginService loginService;

    @ApiOperation(value = "账户密码登录", notes = "密码登录")
    @PostMapping("/loginByPassword")
    public BaseResponse loginByPassword(@RequestBody SysUser sysUser) {
        SysUser resultSysUser = loginService.loginByPassword(sysUser);
        return new BaseResponse(true, "登陆成功", resultSysUser);
    }
    @ApiOperation(value = "密码找回", notes = "密码找回")
    @PostMapping("/foundPassword")
    public BaseResponse foundPassword(@RequestBody SysUser sysUser) throws Exception {
        loginService.foundPassword(sysUser,this.sysUser);
        return new BaseResponse (true, "申请成功，请查看钉钉的通知信息");
    }
}