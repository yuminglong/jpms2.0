package com.jiebao.system.mapper;

import com.central.db.mapper.SuperMapper;
import com.jiebao.system.model.JpmsWork;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface JpmsWorkMapper extends SuperMapper<JpmsWork> {

	List<JpmsWork> findAllList(@Param("title") String title);

}
