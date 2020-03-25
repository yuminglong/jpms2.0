package com.jiebao.system.controller;

import com.central.common.model.Result;
import com.github.pagehelper.PageInfo;
import com.google.common.primitives.Ints;
import com.jiebao.system.model.JpmsRole;
import com.jiebao.system.model.JpmsRolePermission;
import com.jiebao.system.model.JpmsUserRole;
import com.jiebao.system.service.IJpmsRolePermissionService;
import com.jiebao.system.service.IJpmsRoleService;
import com.jiebao.system.service.IJpmsUserRoleService;
import com.jiebao.system.service.IJpmsUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author jiebao
 * @date 2019年7月12日14:23:21
 */

@Slf4j
@RestController
@RequestMapping("/system/role")
@Api(tags = "角色管理API文档")
//@RequiresRoles("admin")
public class JpmsRoleController {

	@Autowired
	private IJpmsRoleService iJpmsRoleService;

	@Autowired
	private IJpmsUserService iJpmsUserService;

	@Autowired
	private IJpmsUserRoleService iJpmsUserRoleService;

	@Autowired
	private IJpmsRolePermissionService iJpmsRolePermissionService;

	@ApiOperation(value = "角色添加权限")
	@GetMapping("/addPermission")
	public Result addPermission(int[] pids, int rid) {
		if(rid != 0) {
			//删除角色 对应的全部权限 重新添加
			List<JpmsRolePermission> list = iJpmsUserService.perRoles(rid);
			for(JpmsRolePermission ur: list) {
				if(ur.getStatus() == 1) {
					iJpmsRolePermissionService.removeById(ur.getId());
				}
			}
			for(int pid: pids) {
				JpmsRolePermission rolePermission = new JpmsRolePermission();
				rolePermission.setPid(pid);
				rolePermission.setRid(rid);
				iJpmsRolePermissionService.saveOrUpdate(rolePermission);
			}
			return Result.succeed(1, "修改成功");
		}else {
			return Result.succeed(1, "请重新选择");
		}

	}

	@ApiOperation(value = "角色删除权限")
	@GetMapping("/deletePermission")
	public Result deletePermission(int[] ids) {
		for(int i: ids) {
			iJpmsRolePermissionService.removeById(i);
		}
		return Result.succeed("删除成功");
	}


	@ApiOperation(value = "角色新增·修改")
	@PostMapping("/addRole")
	public Result addRole(@RequestBody JpmsRole role) {
		return iJpmsRoleService.saveOrUpdate(role) ? Result.succeed(role, "成功") : Result.failed("失败");
	}


	@ApiOperation(value = "角色列表")
	@GetMapping("/roleList")
	public Result roleList(@RequestParam(defaultValue = "1") Integer pageNumber, @RequestParam(defaultValue = "20") Integer pageSize, @RequestParam(required = false) Integer id) {
		PageInfo<JpmsUserRole> userRole = iJpmsUserService.roleList(pageNumber, pageSize, id);
		return Result.succeedWith(userRole, 200, "");
	}

	@ApiOperation(value = "权限列表")
	@GetMapping("/perList")
	public Result perList(@RequestParam(defaultValue = "1") Integer pageNumber, @RequestParam(defaultValue = "20") Integer pageSize, @RequestParam(required = false) Integer id) {
		PageInfo<JpmsRolePermission> userRole = iJpmsUserService.perList(pageNumber, pageSize, id);
		return Result.succeedWith(userRole, 200, "");
	}


	@ApiOperation(value = "删除用户角色")
	@GetMapping("/delur")
	public Result delur(Integer id) {
		JpmsUserRole userRole = iJpmsUserRoleService.getById(id);
		userRole.setStatus(-1);
		return iJpmsUserRoleService.saveOrUpdate(userRole) ? Result.succeedWith(userRole, 200, "删除成功") : Result.failed("删除失败！");
	}

	@ApiOperation(value = "删除用户权限")
	@GetMapping("/delup")
	public Result delurp(Integer id) {
		JpmsRolePermission userRole = iJpmsRolePermissionService.getById(id);
		userRole.setStatus(-1);
		return iJpmsRolePermissionService.saveOrUpdate(userRole) ? Result.succeedWith(userRole, 200, "删除成功") : Result.failed("删除失败！");
	}


	@ApiOperation(value = "彻底删除用户角色")
	@GetMapping("/deleteUserRole")
	public Result deleteUserRole(int[] ids) {
		//ids = ids.substring(1, ids.length() - 1);
		for(int i: ids) {
			iJpmsUserRoleService.removeById(i);
		}
		return Result.succeed("删除成功");
	}

	@ApiOperation(value = "删除用户角色")
	@GetMapping("/ListRoles")
	public List<JpmsRole> ListRoles() {
		return iJpmsUserService.ListRoles();
	}

	@ApiOperation(value = "用户角色新增or修改")
	@GetMapping("/saveordel")
	public Result saveordel(JpmsUserRole userRole) {
		return iJpmsUserRoleService.saveOrUpdate(userRole) ? Result.succeedWith(userRole, 200, "成功") : Result.failed("失败");
	}

	@ApiOperation(value = "角色权限新增or修改")
	@GetMapping("/saveorpr")
	public Result saveorpr(JpmsRolePermission rolePermission) {
		return iJpmsRolePermissionService.saveOrUpdate(rolePermission) ? Result.succeedWith(rolePermission, 200, "成功") : Result.failed("失败");
	}

}
