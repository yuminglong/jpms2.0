package com.jiebao.system.mapper;

import com.central.db.mapper.SuperMapper;
import com.jiebao.system.model.JpmsLaws;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface JpmsLawsMapper extends SuperMapper<JpmsLaws> {

	List<JpmsLaws> findList(@Param("title") String title);

}
