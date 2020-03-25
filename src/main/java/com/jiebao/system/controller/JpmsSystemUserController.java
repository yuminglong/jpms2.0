package com.jiebao.system.controller;

import com.central.common.model.Result;
import com.github.pagehelper.PageInfo;
import com.google.common.primitives.Ints;
import com.jiebao.system.model.JpmsPermission;
import com.jiebao.system.model.JpmsRolePermission;
import com.jiebao.system.model.JpmsUser;
import com.jiebao.system.model.JpmsUserRole;
import com.jiebao.system.service.IJpmsUserRoleService;
import com.jiebao.system.service.IJpmsUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * @author jiebao
 * @date 2019年7月12日14:24:20
 */
@Slf4j
@RestController
@RequestMapping("/system/user")
@Api(tags = "用户管理API文档")
public class JpmsSystemUserController {

	@Autowired
	private IJpmsUserService iJpmsUserService;

	@Autowired
	private IJpmsUserRoleService iJpmsUserRoleService;


	@ApiOperation(value = "给用户添加角色")
	@GetMapping("/addRole")
	public Result addRole(int roleId, int[] userIds) {
		if(roleId != 0) {
			//删除角色 对应的全部用户 重新添加
			List<JpmsUserRole> list = iJpmsUserService.userRoles(roleId);
			for(JpmsUserRole ur: list) {
				if(ur.getStatus() == 1) {
					iJpmsUserRoleService.removeById(ur.getId());
				}
			}
			//重新添加
			for(int userId: userIds) {
				JpmsUserRole userRole = new JpmsUserRole();
				userRole.setRoleId(roleId);
				userRole.setUserId(userId);
				iJpmsUserRoleService.saveOrUpdate(userRole);
			}
			return Result.succeed(1, "修改成功");
		}else {
			return Result.succeed(1, "请重新选择");
		}

	}

	@ApiOperation(value = "用户删除角色")
	@PostMapping("/deleteRole")
	//@RequiresRoles("admin")
	public Result deleteRole(@PathVariable int[] id) {
		return iJpmsUserRoleService.removeByIds(Ints.asList(id)) ? Result.succeed("删除成功") : Result.failed("删除失败");
	}


	@ApiOperation(value = "用户新增")
	@GetMapping("/addUser")
	public Result addUser(JpmsUser user) {
		List<JpmsUser> list = iJpmsUserService.list();
		for(JpmsUser i: list) {
			if(user.getNickName().equals(i.getNickName())) {
				return Result.failed(-1, "用户名重复");
			}
		}
		//密码
		user.setPassword(new SimpleHash("MD5", user.getPassword(), user.getNickName(), 1024).toString());

		iJpmsUserService.saveOrUpdate(user);//新增用户
		//给用户分配角色
		JpmsUserRole userRole = new JpmsUserRole();
		userRole.setUserId(user.getUserId());
		userRole.setRoleId(user.getType());


		return iJpmsUserRoleService.save(userRole) ? Result.succeed(1, "用户新增成功") : Result.failed(-1, "用户新增失败");

	}

	@ApiOperation(value = "用户修改")
	@GetMapping("/updateUser")
	public Result updateUser(JpmsUser user, HttpSession session) {

		JpmsUser user2 = (JpmsUser) session.getAttribute("user");
		if(user.getType() != user2.getType()) {//角色修改
			iJpmsUserService.deleterole(user.getUserId());//删除用户原来角色
			JpmsUserRole userRole = new JpmsUserRole();//重新添加
			userRole.setUserId(user.getUserId());
			userRole.setRoleId(user.getType());
			iJpmsUserRoleService.save(userRole);
		}
		return iJpmsUserService.saveOrUpdate(user) ? Result.succeedWith(user, 200, "成功") : Result.failed("用户新增失败");
	}

	@ApiOperation(value = "修改密码")
	@GetMapping("/updatepwd")
	public Result updatepwd(@RequestParam String password_old, @RequestParam String password_new, HttpSession session) {

		JpmsUser user = (JpmsUser) session.getAttribute("user");
		String oldpwd = new SimpleHash("MD5", password_old, "jiebao", 1024).toString();
		if(oldpwd.equals(user.getPassword())) {
			user.setPassword(new SimpleHash("MD5", password_new, "jiebao", 1024).toString());
			//return iJpmsUserService.saveOrUpdate(user) ? Result.succeed("修改成功") : Result.failed("修改失败！");
			if(iJpmsUserService.saveOrUpdate(user)) {
				Subject subject = SecurityUtils.getSubject();
				if(subject.isAuthenticated()) {
					subject.logout();
				}
				return Result.succeed("修改成功,请重新登录！");
			}else {
				return Result.failed("修改失败！");
			}
		}
		return Result.failed("原密码错误！");

	}

	public static void main(String[] args) {
		String oldpwd = new SimpleHash("MD5", "admin888", "jiebao", 1024).toString();
		System.out.println(oldpwd);
	}

	@ApiOperation(value = "重置密码")
	@GetMapping("/reset")
	public Result reset(Integer userId) {
		/*List<JpmsUser> users = iJpmsUserService.list();
		String oldpwd = new SimpleHash("MD5", "admin888", "jiebao", 1024).toString();
		for(JpmsUser u: users) {
			u.setPassword(oldpwd);
			iJpmsUserService.saveOrUpdate(u);
		}*/
		JpmsUser user = iJpmsUserService.getById(userId);
		String oldpwd = new SimpleHash("MD5", "admin888", "jiebao", 1024).toString();
		user.setPassword(oldpwd);
		return iJpmsUserService.saveOrUpdate(user) ? Result.succeed(userId, "重置成功") : Result.failed("重置失败！");
	}


	@ApiOperation(value = "用户删除")
	@GetMapping("/deleteUser")
	public Result deleteUser(Integer userId) {
		JpmsUser user = iJpmsUserService.getById(userId);
		user.setStatus(-1);
		return iJpmsUserService.saveOrUpdate(user) ? Result.succeed(userId, "用户删除成功") : Result.failed("用户删除失败");
	}

	@ApiOperation(value = "彻底删除用户")
	@GetMapping("/delUser")
	public Result delUser(int[] ids) {
		for(int i: ids) {
			iJpmsUserService.removeById(i);//删除用户
			iJpmsUserService.deleterole(i);//删除用户的角色信息
		}
		return Result.succeed("删除成功");
	}


	@ApiOperation(value = "用户列表查询")
	@GetMapping("/useList")
	public Result useList(@RequestParam(required = false) Integer roleId) {
		//System.out.println(">>" + roleId);
		List<JpmsUser> list = iJpmsUserService.useList();//查询用户组
		if(roleId == null || roleId == 0) {
			for(JpmsUser j: list) {
				j.setList(iJpmsUserService.useType(j.getValue()));
			}
		}else {
			List<JpmsUserRole> userRoles = iJpmsUserService.userRoles(roleId);//根据角色查信息
			for(JpmsUser j: list) {//一级
				List<JpmsUser> user = iJpmsUserService.useType(j.getValue()); //根据 用户组 查询 用户
				for(JpmsUserRole u: userRoles) {//循环 角色用户 信息
					for(JpmsUser y: user) {
						if(u.getUserId() == y.getValue()) {
							y.setChecked(true);
							j.setChecked(true);
						}
					}
				}
				j.setList(user);
			}
		}
		return Result.succeed(list, "x");
	}

	/*@ApiOperation(value = "权限列表查询")
	@GetMapping("/proList")
	public Result proList() {
		List<JpmsPermission> list = iJpmsUserService.proList();
		for(JpmsPermission j: list) {
			j.setList(iJpmsUserService.proType(j.getName()));
		}
		return Result.succeed(list, "x");
	}*/

	@ApiOperation(value = "权限列表查询")
	@GetMapping("/proList")
	public Result perList(@RequestParam(required = false) Integer roleId) {
		//System.out.println(">>" + roleId);
		List<JpmsPermission> list = iJpmsUserService.proList();
		if(roleId == null || roleId == 0) {
			for(JpmsPermission j: list) {
				j.setList(iJpmsUserService.proType(j.getName()));
			}
		}else {
			List<JpmsRolePermission> userRoles = iJpmsUserService.perRoles(roleId);//根据角色查信息 是否选中
			for(JpmsPermission j: list) {//一级
				List<JpmsPermission> user = iJpmsUserService.proType(j.getName()); //根据 类型 查询 权限
				for(JpmsRolePermission u: userRoles) {//循环 角色用户 信息
					for(JpmsPermission y: user) {

						if(u.getPid() == Integer.parseInt(y.getValue())) {
							y.setChecked(true);
							j.setChecked(true);
						}
					}
				}
				j.setList(user);
			}
		}
		return Result.succeed(list, "x");
	}

	@ApiOperation(value = "搜索 用户信息查看")
	@GetMapping("/findList")
	public Result findList(@RequestParam(defaultValue = "1") Integer pageNumber, @RequestParam(defaultValue = "20") Integer pageSize, @RequestParam(required = false) String realName, @RequestParam(required = false) Integer type) {
		PageInfo<JpmsUser> list = iJpmsUserService.findList(pageNumber, pageSize, realName, type);
		return Result.succeedWith(list, 200, "cg");
	}

	@ApiOperation(value = "用户信息查看")
	@GetMapping("/finduser/{userId}")
	public JpmsUser finduser(@PathVariable Integer userId) {
		return iJpmsUserService.getById(userId);
	}


/*	@ApiOperation(value = "用户登陆")
	@GetMapping("/login")
	public Boolean login(@RequestParam String nickName, @RequestParam String password, HttpSession session) {
		//1.获取Subject
		Subject subject = SecurityUtils.getSubject();

		//2.封装用户数据
		UsernamePasswordToken token = new UsernamePasswordToken(nickName, password);

		//3.执行登录方法
		try {
			subject.login(token);
			JpmsUser user = iJpmsUserService.findByName(token.getUsername());
			session.setAttribute("user", user);
			return true;
		}catch(UnknownAccountException e) {
			return false;
		}catch(IncorrectCredentialsException e) {
			return false;
		}
	}*/

	/*@ApiOperation(value = "用户登陆")
	@RequestMapping("/login")
	public Boolean login(HttpSession session) {
		//1.获取Subject
		Subject subject = SecurityUtils.getSubject();

		//2.封装用户数据
		UsernamePasswordToken token = new UsernamePasswordToken("admin", "111111");

		//3.执行登录方法
		try {
			subject.login(token);
			JpmsUser user = iJpmsUserService.findByName(token.getUsername());

			session.setAttribute("user", user);
			return true;
		}catch(UnknownAccountException e) {
			return false;
		}catch(IncorrectCredentialsException e) {
			return false;
		}
	}
*/
	@ApiOperation(value = "注销用户登录（登出）")
	@RequestMapping(value = "/logout")
	public Boolean logout(HttpSession session) {
		//使用权限管理工具进行用户的退出，跳出登录，给出提示信息
		Subject subject = SecurityUtils.getSubject();
		if(subject.isAuthenticated()) {
			subject.logout();
		}
		if(session != null) {
			session.removeAttribute("user");
		}
		return true;
	}


}
