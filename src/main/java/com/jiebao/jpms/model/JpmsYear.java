package com.jiebao.jpms.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.xmlbeans.GDate;

import java.util.Date;

@Data
@TableName("jiebao_jpms_year")
public class JpmsYear {
	@TableId(type = IdType.INPUT)
	private Long yearId;

	private String date;

	private int status;

}
