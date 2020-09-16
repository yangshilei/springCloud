package com.demo.shiro.config;

import com.demo.shiro.shiro.CustomRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    //将自己的验证方式加入容器
    @Bean
    public CustomRealm myShiroRealm(){
        CustomRealm customRealm = new CustomRealm();
        return customRealm;
    }

    @Bean
    public SecurityManager securityManager(){
        SecurityManager securityManager = new DefaultWebSecurityManager();
        ((DefaultWebSecurityManager) securityManager).setRealm(this.myShiroRealm());
        return securityManager;
    }

    /**
     * Filter工厂，设置对应的过滤条件和跳转条件
     * authc:所有url都必须认证通过才可以访问
     * anon:所有url都都可以匿名访问
     * 过滤链定义，从上向下顺序执行，一般将/**放在最为下边
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String,String> map = new HashMap<>();
        // 退出无需认证：用anon
        map.put("/logout","anon");
        // 静态文件
        map.put("/static","anon");
        // 其它特殊的需要免登陆访问接口
        map.put("/other/**","anon");

        // 对所有用户进行认证：用authc
//        map.put("/**","authc");
        map.put("/**","anon"); // 测试其它功能，默认所有接口无需鉴权，若需要鉴权，放开上一行代码，注释本行
        // 设置登陆url
        shiroFilterFactoryBean.setLoginUrl("/login");
        // 设置首页
        shiroFilterFactoryBean.setSuccessUrl("/index");
        // 设置错误页面，认证不通过跳转
        shiroFilterFactoryBean.setUnauthorizedUrl("/error");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        return shiroFilterFactoryBean;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager  securityManager){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}
