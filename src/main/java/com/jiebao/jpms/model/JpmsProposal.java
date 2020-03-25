package com.jiebao.jpms.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jiebao.system.model.JpmsUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;

/**
 * 提案
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@TableName("jiebao_jpms_proposal")
public class JpmsProposal extends Model<JpmsProposal> {



	@ApiModelProperty(value = "提案ID", required = true)
	@TableId
	private Integer proposalId;

	@ApiModelProperty(value = "提案号", required = true)
	private Integer proposalNumber;

	@ApiModelProperty(value = "用户ID", required = true)
	private Integer userId;

	@TableField(exist = false)
	@ApiModelProperty(value = "发稿人", required = true)
	private String realName;

	@ApiModelProperty(value = "提出时间", required = true)
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date createTime;

	@ApiModelProperty(value = "最迟办理时间", required = true)
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date lastTime;

	@ApiModelProperty(value = "办结时间", required = true)
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date endTime;

	@ApiModelProperty(value = "延期前的最迟办理时间", required = true)
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date lateTime;

	@ApiModelProperty(value = "案由", required = true)
	private String cause;

	@ApiModelProperty(value = "提案类型", required = true)
	private String type;

	@ApiModelProperty(value = "提案分类", required = true)
	private String classify;

	@ApiModelProperty(value = "提案状态", required = true)
	private Integer status;

	@ApiModelProperty(value = "提案内容", required = true)
	private String content;

	@ApiModelProperty(value = "办理单位", required = true)
	private Integer unitId;

	@TableField(exist = false)
	@ApiModelProperty(value = "办理单位名称", required = true)
	private String unitName;

	@ApiModelProperty(value = "来源", required = true)
	private String period;

	@ApiModelProperty(value = "是否本人撰写", required = true)
	private Integer isWrite;

	@ApiModelProperty(value = "是否经过调研", required = true)
	private Integer isResearch;

	@ApiModelProperty(value = "是否参考提案选题", required = true)
	private Integer isTopic;

	@ApiModelProperty(value = "是否转呈他人材料", required = true)
	private Integer isMaterials;

	@ApiModelProperty(value = "届数", required = true)
	private String anniversary;

	@ApiModelProperty(value = "次数", required = true)
	private String number;

	@ApiModelProperty(value = "提案主导人", required = true)
	private String leader;

	@ApiModelProperty(value = "联名人用户", required = true)
	private String peoples;

	@ApiModelProperty(value = "联名人", required = true)
	private String peoplestwo;

	@ApiModelProperty(value = "全部联名人", required = true)
	private String peoplesthree;

	@ApiModelProperty(value = "建议承办单位", required = true)
	private String units;

	@ApiModelProperty(value = "满意度测评", required = true)
	private String satisfaction;

	private String reserved;
	private String review;

	@ApiModelProperty(value = "提案负责人联系电话", required = true)
	private String mobile;
	@ApiModelProperty(value = "具体", required = true)
	private String specifica;

	@ApiModelProperty(value = "主办单位", required = true)
	private String zunits;

	@ApiModelProperty(value = "会办1单位", required = true)
	private String hunits;

	@TableField(exist = false)
	@ApiModelProperty(value = "状态信息", required = true)
	private String statusName;

	@TableField(exist = false)
	@ApiModelProperty(value = "单位答复状态", required = true)
	private Integer statusUnit;

	@TableField(exist = false)
	@ApiModelProperty(value = "主导人信息", required = true)
	private JpmsUser juser;

	@TableField(exist = false)
	@ApiModelProperty(value = "建议办理单位", required = true)
	private List<JpmsPunit> jList;

	@TableField(exist = false)
	@ApiModelProperty(value = "主办理单位", required = true)
	private List<JpmsPunit> zList;

	@TableField(exist = false)
	@ApiModelProperty(value = "会办理单位", required = true)
	private List<JpmsPunit> hList;

	@TableField(exist = false)
	@ApiModelProperty(value = "提案联名人", required = true)
	private List<JpmsPersons> userList;

    @TableField(exist = false)
    @ApiModelProperty(value = "ABC办结类型", required = true)
    private JpmsPunit overAnswerType;

}
