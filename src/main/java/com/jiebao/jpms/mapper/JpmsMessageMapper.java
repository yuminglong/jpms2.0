package com.jiebao.jpms.mapper;

import com.central.db.mapper.SuperMapper;
import com.jiebao.jpms.model.JpmsMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface JpmsMessageMapper extends SuperMapper<JpmsMessage> {

	List<JpmsMessage> findList(@Param("title") String title, @Param("userId") Integer userId, @Param("status") Integer status, @Param("folderId") Integer folderId);

}
