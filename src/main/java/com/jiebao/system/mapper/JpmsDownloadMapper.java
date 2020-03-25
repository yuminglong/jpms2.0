package com.jiebao.system.mapper;

import com.central.db.mapper.SuperMapper;
import com.jiebao.system.model.JpmsDownload;
import com.jiebao.system.model.JpmsInform;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface JpmsDownloadMapper extends SuperMapper<JpmsDownload> {

	List<JpmsDownload> findAllList(@Param("title") String title);


}
