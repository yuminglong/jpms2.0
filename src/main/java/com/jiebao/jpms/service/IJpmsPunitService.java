package com.jiebao.jpms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiebao.jpms.model.JpmsPunit;
import org.apache.ibatis.annotations.Update;


public interface IJpmsPunitService extends IService<JpmsPunit> {


    JpmsPunit selectByProId(Integer ProposalId);

    boolean updateAnswer(Integer ProposalId,String overAnswer);


    boolean updateByproId(Integer ProposalId,Integer unitId);

}