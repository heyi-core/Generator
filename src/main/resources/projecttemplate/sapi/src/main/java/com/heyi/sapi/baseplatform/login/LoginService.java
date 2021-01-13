package com.heyi.${project.dataBase}.baseplatform.login;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.heyi.${project.dataBase}.basecommon.BaseException;
import com.heyi.${project.dataBase}.baseutil.*;
import com.heyi.${project.dataBase}.platform.sysuser.SysUserService;
import com.heyi.${project.dataBase}.platform.sysuser.SysUser;
import com.heyi.${project.dataBase}.baseservice.RabbitmqService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import static com.heyi.${project.dataBase}.basecommon.BaseConstant.FAILED;

@Service
public class LoginService {
    @Value("${r'${'}token.timeout}")
    private String timeout;
    @Value("${r'${'}token.key}")
    private String key;
    @Resource
    private SysUserService sysUserService;
    @Resource
    private RabbitmqService rabbitmqService;
    /**
     * 账户密码登录
     */
    public SysUser loginByPassword(SysUser sysUser) {
        SysUser sysUser1=new SysUser();
        sysUser1.setSysUserPhone(sysUser.getSysUserPhone());
        SysUser oldsysUser = sysUserService.getone(sysUser1);
        if (oldsysUser == null) {
            throw new BaseException(FAILED, "没有找到此用户");
        }
        if (!this.verifyPassword(sysUser, oldsysUser)) {
            throw new BaseException(FAILED, "密码错误");
        }
        oldsysUser.setSysUserSalt(null);
        oldsysUser.setSysUserPassword(null);
        oldsysUser.setToken(PasswordUtil.createJWT(JSON.toJSONString(oldsysUser), timeout, key));
        return oldsysUser;
    }

    /*
     * 验证密码
     * */
    private boolean verifyPassword(SysUser sysUser, SysUser oldsysUser) {
        String password = PasswordUtil.encryptPassword(sysUser.getSysUserPassword(), oldsysUser.getSysUserSalt());
        return password.equals(oldsysUser.getSysUserPassword());
    }

    /**
     * 找回密码
     */
    public SysUser foundPassword(SysUser sysUser,SysUser loginUser) {
        SysUser sysUser1=new SysUser();
        sysUser1.setSysUserPhone(sysUser.getSysUserPhone());
        SysUser oldsysUser = sysUserService.getone(sysUser1);
        if (oldsysUser == null) {
            throw new BaseException(FAILED, "没有该用户，无法完成密码修改操作");
        }
        String password = RandomUtil.randomString(6);
        sysUser.setSysUserPassword(PasswordUtil.encryptPassword(password, oldsysUser.getSysUserSalt()));
        sysUser.setSysUserId(oldsysUser.getSysUserId());
        rabbitmqService.sysuserPasswordFound(sysUser,password);
        sysUserService.update(sysUser,loginUser);
        return sysUser;
    }
}
