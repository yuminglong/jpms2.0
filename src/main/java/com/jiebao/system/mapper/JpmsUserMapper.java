package com.jiebao.system.mapper;

import com.central.db.mapper.SuperMapper;
import com.jiebao.jpms.model.JpmsPersons;
import com.jiebao.jpms.model.JpmsPunit;
import com.jiebao.system.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface JpmsUserMapper extends SuperMapper<JpmsUser> {


	List<JpmsRole> findRoles(Integer userId);

	List<JpmsPermission> findPermission(Integer roleId);

	JpmsUser findByName(String nickName);

	List<JpmsUser> findList(@Param("realName") String realName, @Param("type") Integer type);

	List<JpmsUserRole> roleList(@Param("rid") Integer rid);

	List<JpmsRole> ListRoles();

	List<JpmsPermission> proList();

	List<JpmsPermission> proType(@Param("type") String type);

	List<JpmsUser> useList();

	List<JpmsUser> useType(@Param("type") Integer type);

	List<JpmsUser> userType(@Param("type") Integer type);

	List<JpmsUserRole> userRoles(@Param("roleId") Integer roleId);

	List<JpmsRolePermission> perList(@Param("id") Integer id);

	List<JpmsRolePermission> perRoles(@Param("roleId") Integer roleId);

	List<Map<String, Object>> unitAnaly();

	List<Map<String, Object>> staAnaly();

	List<JpmsPersons> joint(@Param("proposalId") Integer proposalId);
	List<JpmsPersons> jointlm(@Param("proposalId") Integer proposalId);

	List<JpmsPunit> jointpunit(@Param("proposalId") Integer proposalId, @Param("type") Integer type, @Param("unitId") Integer unitId);

	Integer deleterole(@Param("userId") Integer userId);

	List<Map<String, Object>> classify();

	List<Map<String, Object>> propType();

	List<Map<String, Object>> satisfactions();

    List<Map<String, Object>> collective();

    List<Map<String, Object>> overAnswer();

	JpmsUser wxlogin(@Param("openid") String openid);
}
