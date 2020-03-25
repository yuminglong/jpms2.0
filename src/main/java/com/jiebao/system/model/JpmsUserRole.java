package com.jiebao.system.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 角色和用户中间表
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@TableName("jiebao_jpms_role_user")
public class JpmsUserRole extends Model<JpmsUserRole> {

	@ApiModelProperty(value = "用户角色中间表ID", required = true)
	private Integer id;

	@ApiModelProperty(value = "用户ID", required = true)
	private Integer userId;

	@ApiModelProperty(value = "角色ID", required = true)
	private Integer roleId;

	@ApiModelProperty(value = "状态", required = true)
	private Integer status;

	@ApiModelProperty(value = "修改时间", required = true)
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date creatTime;

	@TableField(exist = false)
	@ApiModelProperty(value = "用户名", required = true)
	private String nickName;

	@TableField(exist = false)
	@ApiModelProperty(value = "角色名", required = true)
	private String roleName;

	@TableField(exist = false)
	@ApiModelProperty(value = "角色名", required = true)
	private String remark;



}