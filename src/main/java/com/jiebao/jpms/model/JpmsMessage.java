package com.jiebao.jpms.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Date;


/**
 * 办理单位
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("jiebao_jpms_message")
public class JpmsMessage extends Model<JpmsMessage> {

	@TableId(type = IdType.INPUT)
	@ApiModelProperty(value = "消息ID", required = true)
	private Integer messageId;

	@ApiModelProperty(value = "发送者ID", required = true)
	private Integer sendId;

	@ApiModelProperty(value = "接收者ID", required = true)
	private Integer recId;

	@ApiModelProperty(value = "发送时间", required = true)
	private Date createAt;

	@ApiModelProperty(value = "状态", required = true)
	private Integer status;

	@ApiModelProperty(value = "标题", required = true)
	private String title;

	@ApiModelProperty(value = "内容", required = true)
	private String content;

	@ApiModelProperty(value = "所属文件夹ID", required = true)
	private Integer folderId;


}
