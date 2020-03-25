package com.jiebao.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.jiebao.jpms.model.JpmsPersons;
import com.jiebao.jpms.model.JpmsPunit;
import com.jiebao.system.model.*;

import java.util.List;
import java.util.Map;

public interface IJpmsUserService extends IService<JpmsUser> {

	/**
	 * 查询用户 对应的 角色
	 * @param userId 用户ID
	 * @return
	 */
	List<JpmsRole> findRoles(Integer userId);

	/**
	 * 根据用户id 删除用户的角色
	 * @param userId
	 * @return
	 */
	Integer deleterole(Integer userId);

	/**
	 * 查询角色 对应的 用户
	 * @param roleId 用户ID
	 * @return
	 */
	List<JpmsUserRole> userRoles(Integer roleId);

	/**
	 * 查询角色 对应的 权限
	 * @param roleId 角色ID
	 * @return
	 */
	List<JpmsPermission> findPermission(Integer roleId);

	/**
	 * 查询角色对应的权限
	 * @param roleId
	 * @return
	 */
	List<JpmsRolePermission> perRoles(Integer roleId);

	/**
	 * 根据用户名查询 用户
	 * @param nickName
	 * @return
	 */
	JpmsUser findByName(String nickName);

	/**
	 * 根据用户名 分页搜索 用户信息
	 * @param pageNumber
	 * @param pageSize
	 * @param realName
	 * @return
	 */
	PageInfo<JpmsUser> findList(Integer pageNumber, Integer pageSize, String realName, Integer type);


	/**
	 * 查询角色 列表信息
	 * @return
	 */
	PageInfo<JpmsUserRole> roleList(Integer pageNumber, Integer pageSize, Integer id);

	/**
	 * 查询权限 列表信息
	 * @return
	 */
	PageInfo<JpmsRolePermission> perList(Integer pageNumber, Integer pageSize, Integer id);

	/**
	 * 查询所有 角色 下拉用
	 * @return
	 */
	List<JpmsRole> ListRoles();


	/**
	 * 查询全部 权限 类型
	 * @return
	 */
	List<JpmsPermission> proList();

	/**
	 * 根据类型查询权限
	 * @param type
	 * @return
	 */
	List<JpmsPermission> proType(String type);

	/**
	 * 查询全部 用户 类型
	 * @return
	 */
	List<JpmsUser> useList();

	/**
	 * 根据类型查询用户
	 * @param type
	 * @return
	 */
	List<JpmsUser> useType(Integer type);//做联名用户

	List<JpmsUser> userType(Integer type);


	/**
	 * 单位办理情况统计
	 * @return
	 */
	List<Map<String, Object>> unitAnaly();

	/**
	 * 提案状态办理情况
	 * @return
	 */
	List<Map<String, Object>> staAnaly();

	/**
	 * 提案分类情况
	 * @return
	 */
	List<Map<String, Object>> classify();


	/**
	 * 提案类型统计
	 * @return
	 */
	List<Map<String, Object>> propType();




	/**
	 * 提案满意度统计
	 * @return
	 */
	List<Map<String, Object>> satisfactions();

    /**
     * 集体提案四大类统计
     * @return
     */
    List<Map<String, Object>> collective();


    /**
     * ABC办结类型统计
     * @return
     */
    List<Map<String, Object>> overAnswer();



	/**
	 * 查询提案联名人
	 * @return
	 */
	List<JpmsPersons> joint(Integer proposalId);

	List<JpmsPersons> jointlm(Integer proposalId);

	List<JpmsPunit> jointpunit(Integer proposalId, Integer type, Integer unitId);


	JpmsUser wxlogin(String openid);

}