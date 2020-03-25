package com.jiebao.system.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


/**
 * @author jiebao
 * @date 2019年7月12日14:25:52
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@TableName("jiebao_jpms_role_permission")
public class JpmsRolePermission extends Model<JpmsRolePermission> {

	@TableId(type = IdType.INPUT)
	@ApiModelProperty(value = "ID", required = true)
	private Integer id;

	@ApiModelProperty(value = "角色ID", required = true)
	private Integer rid;

	@ApiModelProperty(value = "权限ID", required = true)
	private Integer pid;

	@ApiModelProperty(value = "状态", required = true)
	private Integer status;

	@ApiModelProperty(value = "修改时间", required = true)
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date creatTime;

	@TableField(exist = false)
	@ApiModelProperty(value = "权限名", required = true)
	private String name;

	@TableField(exist = false)
	@ApiModelProperty(value = "角色名", required = true)
	private String remark;


}