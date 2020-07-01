package com.jiebao.jpms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.jpms.mapper.JpmsPunitMapper;
import com.jiebao.jpms.mapper.JpmsUnitMapper;
import com.jiebao.jpms.model.JpmsPunit;
import com.jiebao.jpms.service.IJpmsPunitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Slf4j
@Service
public class JpmsPunitServiceImpl extends ServiceImpl<JpmsPunitMapper, JpmsPunit> implements IJpmsPunitService {

    @Resource
    private JpmsUnitMapper jpmsUnitMapper;

    @Autowired
    private  JpmsPunitMapper jpmsPunitMapper;


    @Override
    public JpmsPunit selectByProId(Integer ProposalId) {

        return jpmsUnitMapper.selectByProId(ProposalId);
    }

    @Override
    public boolean updateAnswer(Integer ProposalId, String overAnswer) {
        return jpmsUnitMapper.updateAnswer(ProposalId,overAnswer);
    }

    @Override
    public  boolean updateByproId(Integer ProposalId,Integer unitId){
        return jpmsPunitMapper.updateByproId(ProposalId,unitId);
    }
}
