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
import java.util.List;


/**
 * @author jiebao
 * @date 2019年7月12日14:25:52
 * 用户表
 * 验证还没写，后面根据实际情况再添加
 * like this↓↓↓↓↓↓
 * @Size (min = 6, max = 12, message = " 最小6 ， 最大12 ")
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@TableName("jiebao_jpms_user")
public class JpmsUser extends Model<JpmsUser> {

	@TableId
	@ApiModelProperty(value = "用户ID", required = true)
	private Integer userId;

	@ApiModelProperty(value = "类型", required = true)
	private Integer type;

	@ApiModelProperty(value = "用户分组", required = true)
	private String sector;

	@ApiModelProperty(value = "状态 1:有效，0:禁止登录", required = true)
	private Integer status;

	@ApiModelProperty(value = "用户昵称", required = true)
	private String nickName;

	@ApiModelProperty(value = "真实姓名", required = true)
	private String realName;

	@ApiModelProperty(value = "电子邮箱", required = true)
	private String email;

	@ApiModelProperty(value = "手机", required = true)
	private String mobile;

	@ApiModelProperty(value = "性别", required = true)
	private String sex;


	@ApiModelProperty(value = "创建时间", required = true)
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date createTime;

	@ApiModelProperty(value = "登陆时间", required = true)
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date loginTimes;

	@ApiModelProperty(value = "密码", required = true)
	private String password;

	@ApiModelProperty(value = "邮编", required = true)
	private String postcode;

	@ApiModelProperty(value = "界别", required = true)
	private String subsector;

	@ApiModelProperty(value = "党派", required = true)
	private String party;

	@ApiModelProperty(value = "单位ID", required = true)
	private Integer unitId;

	@ApiModelProperty(value = "工作单位", required = true)
	private String unitName;

	@ApiModelProperty(value = "通讯地址", required = true)
	private String address;

	@ApiModelProperty(value = "微信", required = true)
	private String wxopenid;

	@TableField(exist = false)
	private List<JpmsUser> list;

	@TableField(exist = false)
	private String name;

	@TableField(exist = false)
	private Integer value;

	@TableField(exist = false)
	private Boolean checked;


}