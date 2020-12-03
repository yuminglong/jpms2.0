package com.jiebao.system.mapper;

import com.central.db.mapper.SuperMapper;
import com.jiebao.jpms.model.JpmsAppendix;
import com.jiebao.jpms.model.JpmsProposal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface JpmsProposalMapper extends SuperMapper<JpmsProposal> {

	List<JpmsProposal> findList(@Param("cause") String cause, @Param("status") Integer stutus,@Param("startDate") String startDate,@Param("endDate") String endDate);

	JpmsProposal findById(@Param("proposalId") Integer proposalId);

	List<JpmsProposal> membersProposal(@Param("userId") Integer userId, @Param("cause") String cause,@Param("status")Integer status,@Param("startDate")String startDate,@Param("endDate")String endDate);

	List<JpmsAppendix> appendix(@Param("proposalId") Integer proposalId,@Param("type")Integer type);

	Integer deletepunit(@Param("proposalId") Integer proposalId);

	Integer deletepersons(@Param("proposalId") Integer proposalId);

	Integer maxnumber();

	@Update("UPDATE  jiebao_jpms_proposal p   SET  p.`status` = 7 where p.proposal_id = #{proposalId} ")
	 boolean  updatebyProId(@Param("proposalId")Integer proposalId);

	@Update("UPDATE  jiebao_jpms_proposal  SET is_docking = 1 WHERE proposal_id =#{proposalId}")
	boolean updateIsDocking(Integer proposalId);
}
