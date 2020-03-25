package com.jiebao.jpms.model;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("jiebao_jpms_punit")
public class JpmsPunit extends Model<JpmsPunit> {

	@TableId(type = IdType.INPUT)
	@ApiModelProperty(value = "承办ID", required = true)
	private Integer  punitId;

	@ApiModelProperty(value = "提案id", required = true)
	private Integer proposalId;

	@ApiModelProperty(value = "单位id", required = true)
	private Integer unitId;

	@ApiModelProperty(value = "类型", required = true)
	private Integer type;

	@ApiModelProperty(value = "单位答复类型", required = true)
	private String answer;

	@ApiModelProperty(value = "之前答复", required = true)
	private String answerAfter;

	@TableField(exist = false)
	@ApiModelProperty(value = "办理单位名称", required = true)
	private String unitName;

    @ApiModelProperty(value = "ABC类型答复", required = true)
    private String overAnswer;

}