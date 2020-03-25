package com.jiebao.jpms.mapper;

import com.central.db.mapper.SuperMapper;
import com.jiebao.jpms.model.JpmsReply;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface JpmsReplyMapper extends SuperMapper<JpmsReply> {

	JpmsReply listReply(Integer proposalId);

	List<Map<String, Object>> selReplytype();

}
