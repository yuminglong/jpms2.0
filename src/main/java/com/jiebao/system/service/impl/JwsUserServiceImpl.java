package com.jiebao.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiebao.jpms.model.JpmsPersons;
import com.jiebao.jpms.model.JpmsPunit;
import com.jiebao.system.mapper.JpmsUserMapper;
import com.jiebao.system.model.*;
import com.jiebao.system.service.IJpmsUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Slf4j
@Service
public class JwsUserServiceImpl extends ServiceImpl<JpmsUserMapper, JpmsUser> implements IJpmsUserService {

	@Override
	public List<JpmsRole> findRoles(Integer userId) {
		return baseMapper.findRoles(userId);
	}

	@Override
	public List<JpmsPermission> findPermission(Integer roleId) {
		return baseMapper.findPermission(roleId);
	}

	@Override
	public JpmsUser findByName(String nickName) {
		return baseMapper.findByName(nickName);
	}

	@Override
	public PageInfo<JpmsUser> findList(Integer pageNumber, Integer pageSize, String realName, Integer type) {
		Page page = PageHelper.startPage(pageNumber, pageSize);
		List<JpmsUser> list = baseMapper.findList(realName, type);
		PageInfo info = new PageInfo<>(page.getResult());
		return info;
	}

	@Override
	public PageInfo<JpmsUserRole> roleList(Integer pageNumber, Integer pageSize, Integer id) {
		Page page = PageHelper.startPage(pageNumber, pageSize);
		List<JpmsUserRole> list = baseMapper.roleList(id);
		PageInfo info = new PageInfo<>(page.getResult());
		return info;
	}

	@Override
	public List<JpmsRole> ListRoles() {
		return baseMapper.ListRoles();
	}

	@Override
	public List<JpmsPermission> proList() {
		return baseMapper.proList();
	}

	@Override
	public List<JpmsPermission> proType(String type) {
		return baseMapper.proType(type);
	}

	@Override
	public List<JpmsUser> useList() {
		return baseMapper.useList();
	}

	@Override
	public List<JpmsUser> useType(Integer type) {
		return baseMapper.useType(type);
	}

	@Override
	public List<JpmsUser> userType(Integer type) {
		return baseMapper.userType(type);
	}

	@Override
	public List<JpmsUserRole> userRoles(Integer roleId) {
		return baseMapper.userRoles(roleId);
	}

	@Override
	public List<JpmsRolePermission> perRoles(Integer roleId) {
		return baseMapper.perRoles(roleId);
	}

	@Override
	public PageInfo<JpmsRolePermission> perList(Integer pageNumber, Integer pageSize, Integer id) {
		Page page = PageHelper.startPage(pageNumber, pageSize);
		List<JpmsRolePermission> list = baseMapper.perList(id);
		PageInfo info = new PageInfo<>(page.getResult());
		return info;
	}

	@Override
	public List<Map<String, Object>> unitAnaly() {
		return baseMapper.unitAnaly();
	}

	@Override
	public List<Map<String, Object>> staAnaly() {
		return baseMapper.staAnaly();
	}

	@Override
	public List<Map<String, Object>> classify() {
		return baseMapper.classify();
	}
	@Override
	public List<Map<String, Object>> propType() {
		return baseMapper.propType();
	}
	@Override
	public List<Map<String, Object>> satisfactions() {
		return baseMapper.satisfactions();
	}

    @Override
    public List<Map<String, Object>> collective() {
        return baseMapper.collective();
    }

    @Override
    public List<Map<String, Object>> overAnswer() {
        return baseMapper.overAnswer();
    }

	@Override
	public List<JpmsPersons> joint(Integer proposalId) {
		return baseMapper.joint(proposalId);
	}

	@Override
	public List<JpmsPersons> jointlm(Integer proposalId) {
		return baseMapper.jointlm(proposalId);
	}

	@Override
	public List<JpmsPunit> jointpunit(Integer proposalId, Integer type,Integer unitId) {
		return baseMapper.jointpunit(proposalId, type,unitId);
	}

	@Override
	public Integer deleterole(Integer userId) {
		return baseMapper.deleterole(userId);
	}

	@Override
	public JpmsUser wxlogin(String openid) {
		return baseMapper.wxlogin(openid);
	}
}
