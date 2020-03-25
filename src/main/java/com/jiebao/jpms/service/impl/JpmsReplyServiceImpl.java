package com.jiebao.jpms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.jiebao.jpms.mapper.JpmsReplyMapper;
import com.jiebao.jpms.model.JpmsReply;
import com.jiebao.jpms.service.IJpmsReplyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class JpmsReplyServiceImpl extends ServiceImpl<JpmsReplyMapper, JpmsReply> implements IJpmsReplyService {



	@Override
	public JpmsReply listReply( Integer proposalId) {
		return baseMapper.listReply(proposalId);
	}
}
