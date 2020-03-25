package com.jiebao.jpms.model;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * 提案
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@TableName("jiebao_jpms_reply")
public class JpmsReply extends Model<JpmsReply> {

	@TableId(type = IdType.INPUT)
	@ApiModelProperty(value = "答复ID", required = true)
	private Integer replyId;

	@ApiModelProperty(value = "提案ID", required = true)
	private Integer proposalId;

	@ApiModelProperty(value = "答复标题", required = true)
	private String replyTitle;

	@ApiModelProperty(value = "答复内容", required = true)
	private String replyInfo;

	@ApiModelProperty(value = "答复时间", required = true)
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date replyTime;

	@ApiModelProperty(value = "答复方式", required = true)
	private Integer replyHair;

	@ApiModelProperty(value = "办复类型", required = true)
	private Integer replyType;

	@ApiModelProperty(value = "签发人", required = true)
	private String replyPeople;

	@ApiModelProperty(value = "联系电话", required = true)
	private String phone;

	@ApiModelProperty(value = "满意度", required = true)
	private Integer satisfaction;

}
