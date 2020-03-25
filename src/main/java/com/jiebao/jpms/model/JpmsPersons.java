package com.jiebao.jpms.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.jiebao.system.model.JpmsUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("jiebao_jpms_persons")
public class JpmsPersons extends Model<JpmsPersons> {

	@TableId(type = IdType.INPUT)
	@ApiModelProperty(value = "联名ID", required = true)
	private Integer  personId;

	@ApiModelProperty(value = "用户id", required = true)
	private Integer userId;

	@ApiModelProperty(value = "用户id", required = true)
	private Integer proposalId;

	@TableField(exist = false)
	@ApiModelProperty(value = "真实姓名", required = true)
	private String realName;

	@TableField(exist = false)
	@ApiModelProperty(value = "用户", required = true)
	private JpmsUser jpmsUser;


}