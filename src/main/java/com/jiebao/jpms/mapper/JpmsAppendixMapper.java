package com.jiebao.jpms.mapper;

import com.central.db.mapper.SuperMapper;
import com.jiebao.jpms.model.JpmsAppendix;
import com.jiebao.jpms.model.JpmsProposal;
import com.jiebao.jpms.model.JpmsYear;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;


@Mapper
public interface JpmsAppendixMapper extends SuperMapper<JpmsAppendix> {

	@Select("select * from jiebao_jpms_year order by date desc")
	List<JpmsYear> listYear();


	@Delete("delete from  jiebao_jpms_year  where id=#{id}")
	Integer deleteById(@Param("id") Long id);

	@Update("update jiebao_jpms_year set status=#{status} where year_id=#{yearId}")
	Integer updateSetStatus(@Param("status")int status,@Param("yearId") Long yearId);

	@Update("insert into jiebao_jpms_year(date,status) values(#{date},#{status})")
	Integer addLK(@Param("date") String date, @Param("status") int status);

	/**
	 * 年度区间查询出列表
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Select("select * from jiebao_jpms_proposal where create_time>=#{startDate} and create_time<#{endDate}  order by unit_id asc,create_time ASC ")
	List<JpmsProposal> listByYear(@Param("startDate") String startDate,@Param("endDate") String endDate);


}
