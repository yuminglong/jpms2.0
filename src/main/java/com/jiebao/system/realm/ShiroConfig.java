package com.jiebao.system.realm;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Shiro的配置类
 * @author lenovo
 */
@Configuration
public class ShiroConfig {

	/**
	 * 创建ShiroFilterFactoryBean
	 */
	@Bean
	public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		//设置安全管理器
		shiroFilterFactoryBean.setSecurityManager(securityManager);

		Map<String, String> filterMap = new LinkedHashMap<>();

		//放行login.html页面
		filterMap.put("/login", "anon");
		filterMap.put("/docking/**", "anon");
		filterMap.put("/index", "authc");
		filterMap.put("/", "authc");
		filterMap.put("/system/user/login", "anon");
		filterMap.put("/front/user/login", "anon");
		//授权过滤器
		//filterMap.put("/front/user/index","authc,perms[add]");

		//放过  js css
		filterMap.put("/layui/**", "anon");
		filterMap.put("/libs/**", "anon");
		filterMap.put("/wx/**", "anon");
		filterMap.put("/parent/**", "anon");
		filterMap.put("/wxdetaile/**", "anon");


		//注意：当前授权拦截后，shiro会自动跳转到未授权页面
		filterMap.put("/**", "authc");

		//修改调整的登录页面
		shiroFilterFactoryBean.setLoginUrl("/login");
		//设置未授权提示页面
		shiroFilterFactoryBean.setUnauthorizedUrl("/login");

		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);

		return shiroFilterFactoryBean;
	}

	/**
	 * 创建DefaultWebSecurityManager
	 */
	@Bean(name = "securityManager")
	public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		//关联realm
		securityManager.setRealm(userRealm);
		return securityManager;
	}

	@Bean(name = "sessionManager")
	public DefaultWebSessionManager sessionManager() {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		// 设置session过期时间3h
		sessionManager.setGlobalSessionTimeout(10800000L);
		return sessionManager;
	}

	/**
	 * 创建Realm
	 */
	@Bean(name = "userRealm")
	public UserRealm getRealm() {
		return new UserRealm();
	}

	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(UserRealm userRealm) {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(getDefaultWebSecurityManager(userRealm));
		return authorizationAttributeSourceAdvisor;
	}

	/**
	 * 注解权限 拦截
	 * @return
	 */

	@Bean
	public SimpleMappingExceptionResolver simpleMappingExceptionResolver() {
		SimpleMappingExceptionResolver resolver = new SimpleMappingExceptionResolver();
		Properties properties = new Properties();
		/*未授权处理页*/
		properties.setProperty("org.apache.shiro.authz.UnauthorizedException", "/unauthor");
		/*身份没有验证*/
		properties.setProperty("org.apache.shiro.authz.UnauthenticatedException", "/login");
		resolver.setExceptionMappings(properties);
		return resolver;
	}

	@Bean
	public ShiroDialect shiroDialect() {
		return new ShiroDialect();
	}


}
