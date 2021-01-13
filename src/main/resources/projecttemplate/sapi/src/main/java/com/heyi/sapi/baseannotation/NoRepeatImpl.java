package com.heyi.${project.dataBase}.baseannotation;

import com.heyi.${project.dataBase}.basecommon.BaseController;
import com.heyi.${project.dataBase}.basecommon.BaseException;
import com.heyi.${project.dataBase}.baseservice.RedisService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.concurrent.TimeUnit;

import static com.heyi.${project.dataBase}.basecommon.BaseConstant.FAILED;

@Aspect
@Component
public class NoRepeatImpl {

    private static String lock="lock";
    @Resource
    private RedisService redisService;

    @Pointcut("@annotation(NoRepeat)")
    public void cut() {
    }

    //  不允许重复提交10秒钟
    @Before("cut()&&@annotation(NoRepeat)")
    public void before(JoinPoint joinPoint) {
        BaseController baseController = ((BaseController) joinPoint.getTarget());
        String key = joinPoint.getSignature().getDeclaringType().getSimpleName() + joinPoint.getSignature().getName() + baseController.sysUser.getSysUserId();
        synchronized (lock) {
            boolean isHave = redisService.hasKey(key);
            if (isHave) {
                throw new BaseException(FAILED, "不允许重复提交");

            }else{
                redisService.setEx(key, "0", 2, TimeUnit.SECONDS);
                return;
            }
        }
    }

}
