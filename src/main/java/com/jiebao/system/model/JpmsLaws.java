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
 * 政策法规
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@TableName("jiebao_jpms_laws")
public class JpmsLaws extends Model<JpmsLaws> {

	@TableId(type = IdType.INPUT)
	@ApiModelProperty(value = "政策法规ID", required = true)
	private Integer lawsId;

	@ApiModelProperty(value = "标题", required = true)
	private String title;

	@ApiModelProperty(value = "内容", required = true)
	private String content;

	@ApiModelProperty(value = "来源", required = true)
	private String period;

	@ApiModelProperty(value = "发布时间", required = true)
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	private Date createTime;

	@ApiModelProperty(value = "发布用户", required = true)
	private String createUser;

	@ApiModelProperty(value = "状态", required = true)
	private Integer status;

	@TableField(exist = false)
	private Integer fileId;

}
