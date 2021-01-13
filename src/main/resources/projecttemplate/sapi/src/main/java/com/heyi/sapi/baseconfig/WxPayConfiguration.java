package com.heyi.${project.dataBase}.baseconfig;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Binary Wang
 */
@Configuration
@NoArgsConstructor
@ConditionalOnClass(WxPayService.class)
@PropertySource("classpath:wxpay.properties")
public class WxPayConfiguration {
  @Value("${r'${'}appId}")
  private String appId;
  @Value("${r'${'}mchId}")
  private String mchId;
  @Value("${r'${'}mchKey}")
  private String mchKey;
  @Value("${r'${'}keyPath}")
  private String keyPath;

  @ConditionalOnMissingBean
  @Bean
  public WxPayService wxService() {
    WxPayConfig payConfig = new WxPayConfig();
    payConfig.setAppId(StringUtils.trimToNull(appId));
    payConfig.setMchId(StringUtils.trimToNull(mchId));
    payConfig.setMchKey(StringUtils.trimToNull(mchKey));
    payConfig.setKeyPath(StringUtils.trimToNull(keyPath));
    // 可以指定是否使用沙箱环境
    payConfig.setUseSandboxEnv(false);
    WxPayService wxPayService = new WxPayServiceImpl();
    wxPayService.setConfig(payConfig);
    return wxPayService;
  }

}
