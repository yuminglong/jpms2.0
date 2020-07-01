package com.jiebao.jpms.mapper;

import com.central.db.mapper.SuperMapper;
import com.jiebao.jpms.model.JpmsProposal;
import com.jiebao.jpms.model.JpmsPunit;
import com.jiebao.jpms.model.JpmsUnit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface JpmsUnitMapper extends SuperMapper<JpmsUnit> {

	List<Map<String, Object>> selUnitsta();

	List<Map<String, Object>> selUnitype();

	List<JpmsUnit> listUnit(@Param("unitName") String unitName);

    JpmsPunit selectByProId(@Param("ProposalId")Integer ProposalId);

    boolean updateAnswer(@Param("ProposalId")Integer ProposalId ,@Param("overAnswer")String overAnswer);

	List<JpmsProposal> findList(@Param("unitId") Integer unitId, @Param("cause") String cause,@Param("status")Integer status,@Param("startDate")String startDate,@Param("endDate")String endDate);



}
