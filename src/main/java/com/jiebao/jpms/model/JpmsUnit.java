package com.jiebao.jpms.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


/**
 * 办理单位
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@TableName("jiebao_jpms_unit")
public class JpmsUnit extends Model<JpmsUnit> {

	@TableId(type = IdType.INPUT)
	@ApiModelProperty(value = "单位ID", required = true)
	private Integer unitId;

	@ApiModelProperty(value = "办理单位名称", required = true)
	private String unitName;

	@ApiModelProperty(value = "状态", required = true)
	private Integer status;

	@ApiModelProperty(value = "办理单位类型", required = true)
	private Integer type;

	@ApiModelProperty(value = "单位负责人", required = true)
	private String userName;

	@ApiModelProperty(value = "联系电话", required = true)
	private String phone;

}
