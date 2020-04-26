package com.jiebao.system.mapper;

import com.central.db.mapper.SuperMapper;
import com.jiebao.jpms.model.JpmsAppendix;
import com.jiebao.jpms.model.JpmsProposal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface JpmsProposalMapper extends SuperMapper<JpmsProposal> {

	List<JpmsProposal> findList(@Param("cause") String cause, @Param("status") Integer stutus,@Param("startDate") String startDate,@Param("endDate") String endDate);

	JpmsProposal findById(@Param("proposalId") Integer proposalId);

	List<JpmsProposal> membersProposal(@Param("userId") Integer userId, @Param("cause") String cause,@Param("status")Integer status,@Param("startDate")String startDate,@Param("endDate")String endDate);

	List<JpmsAppendix> appendix(@Param("proposalId") Integer proposalId,@Param("type")Integer type);

	Integer deletepunit(@Param("proposalId") Integer proposalId);

	Integer deletepersons(@Param("proposalId") Integer proposalId);

	Integer maxnumber();
}
