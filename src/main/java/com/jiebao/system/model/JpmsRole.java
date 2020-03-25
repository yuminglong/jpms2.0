package com.jiebao.system.model;


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
 * @author jiebao
 * @date 2019年7月12日14:25:52
 * 角色表
 * 验证还没写，后面根据实际情况再添加
 * like this↓↓↓↓↓↓
 * @Size (min = 6, max = 12, message = " 最小6，最大12")
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@TableName("jiebao_jpms_role")
public class JpmsRole  extends Model<JpmsRole> {

	@TableId(type = IdType.INPUT)
	@ApiModelProperty(value = "角色ID", required = true)
	private Integer roleId;

	@ApiModelProperty(value = "角色名称", required = true)
	private String roleName;

    @ApiModelProperty(value = "角色描述", required = true)
    private  String remark;

    @ApiModelProperty(value = "创建时间", required = true)
    private Date creatTime;

    @ApiModelProperty(value = "修改时间", required = true)
    private Date modifyTime;



}