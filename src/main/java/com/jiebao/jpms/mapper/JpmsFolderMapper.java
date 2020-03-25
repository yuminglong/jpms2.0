package com.jiebao.jpms.mapper;

import com.central.db.mapper.SuperMapper;
import com.jiebao.jpms.model.JpmsFolder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface JpmsFolderMapper extends SuperMapper<JpmsFolder> {

	List<JpmsFolder> seltfol(Integer userId);
}
