package com.jiebao.jpms.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;


/**
 * 办理单位
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@TableName("jiebao_jpms_appendix")
public class JpmsAppendix extends Model<JpmsAppendix> {

	@TableId
	@ApiModelProperty(value = "附件ID", required = true)
	private Integer appendixId;


	@ApiModelProperty(value = "单位ID", required = true)
	private Integer unitId;

	@TableField(exist = false)
	@ApiModelProperty(value = "单位名称", required = true)
	private String unitName;

	@ApiModelProperty(value = "所属提案ID", required = true)
	private Integer proposalId;

	@ApiModelProperty(value = "创建时间", required = true)
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date createTime;

	@ApiModelProperty(value = "附件名", required = true)
	private String appendixName;

	@TableField(exist = false)
	@ApiModelProperty(value = "单位答复类型", required = true)
	private String answer;

	@TableField(exist = false)
	@ApiModelProperty(value = "之前答复", required = true)
	private String answerAfter;

	@ApiModelProperty(value = "存储文件名", required = true)
	private String fileName;

	@ApiModelProperty(value = "附件类型", required = true)
	private Integer type;


}
