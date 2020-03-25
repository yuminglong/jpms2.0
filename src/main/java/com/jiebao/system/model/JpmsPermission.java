package com.jiebao.system.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 权限表
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@TableName("jiebao_jpms_permission")
public class JpmsPermission  extends Model<JpmsPermission> {

	@TableId(type = IdType.INPUT)
	@ApiModelProperty(value = "权限ID", required = true)
	private Integer pid;

	@ApiModelProperty(value = "操作的url", required = true)
	private String url;

	@ApiModelProperty(value = "操作的名称", required = true)
	private String name;

	@ApiModelProperty(value = "权限类型", required = true)
	private String pType;

	@TableField(exist = false)
	private List<JpmsPermission> list;

	@TableField(exist = false)
	private String value;

	@TableField(exist = false)
	private Boolean checked;



}