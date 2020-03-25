package com.jiebao.system.realm;

import com.jiebao.system.model.JpmsPermission;
import com.jiebao.system.model.JpmsRole;
import com.jiebao.system.model.JpmsUser;
import com.jiebao.system.service.IJpmsUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * 自定义Realm
 * @author lenovo
 */
public class  UserRealm extends AuthorizingRealm {

	@Autowired
	private IJpmsUserService jpmsUserService;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
	//	System.out.println("执行授权逻辑");
		//创建SimpleAuthorizationInfo
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		//获取当前登录用户
		Subject subject = SecurityUtils.getSubject();
		JpmsUser user = (JpmsUser) subject.getPrincipal();
		List<JpmsRole> roleList = jpmsUserService.findRoles(user.getUserId());
		Set<String> roles = new HashSet<>();
		for(JpmsRole role: roleList) {
			roles.add(role.getRoleName());//角色
			List<JpmsPermission> permissionList = jpmsUserService.findPermission(role.getRoleId());
			Set<String> premissionCollection = new HashSet<>();
			for(JpmsPermission permission: permissionList) {
				premissionCollection.add(permission.getUrl());//权限
			}
			info.addStringPermissions(premissionCollection);//添加权限
		}
		info.addRoles(roles);//添加角色
		return info;//返回SimpleAuthorizationInfo 对象

	}


	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken arg0) throws AuthenticationException {
		System.out.println("执行认证逻辑");
		UsernamePasswordToken token = (UsernamePasswordToken) arg0;
		String userName= token.getPrincipal().toString();
		String password =new String(token.getPassword());
		System.out.print("userName——>"+userName);
		JpmsUser user = jpmsUserService.findByName(userName);
		String result = new SimpleHash("MD5", token.getPassword(), "jiebao", 1024).toString();
		if(user == null || !user.getPassword().equals(result)) {
			throw new UnknownAccountException("账号或密码错误");
		}
		return new SimpleAuthenticationInfo(user, password, getName());

	}

}
