package com.jiebao.jpms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.central.db.mapper.SuperMapper;
import com.jiebao.jpms.model.JpmsPunit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface JpmsPunitMapper extends SuperMapper<JpmsPunit> {


    @Update(" UPDATE  jiebao_jpms_punit p   SET  p.answer = null where p.unit_id = #{unitId} and  p.proposal_id = #{proposalId}")
    boolean updateByproId(@Param("proposalId")Integer ProposalId, @Param("unitId")Integer unitId);


    @Select("SELECT * FROM `jiebao_jpms_punit` WHERE (type = 3 or type = 4) and proposal_id =#{proposalId}")
    List<JpmsPunit> getUnit(Integer proposalId);

}
