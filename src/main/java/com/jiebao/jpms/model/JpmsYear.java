package com.jiebao.jpms.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;

@Data
@TableName("jiebao_jpms_year")
public class JpmsYear {
	private Long yearId;

	private LocalDate date;

	private boolean status;
}
