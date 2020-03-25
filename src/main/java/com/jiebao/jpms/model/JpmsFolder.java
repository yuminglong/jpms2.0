package com.jiebao.jpms.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


/**
 * 办理单位
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@TableName("jiebao_jpms_folder")
public class JpmsFolder extends Model<JpmsFolder> {

	@TableId(type = IdType.INPUT)
	@ApiModelProperty(value = "文件夹ID", required = true)
	private Integer folderId;

	@ApiModelProperty(value = "用户ID", required = true)
	private Integer userId;

	@ApiModelProperty(value = "文件夹名称", required = true)
	private String folderName;

	@ApiModelProperty(value = "创建时间", required = true)
	private Date createTime;


}
