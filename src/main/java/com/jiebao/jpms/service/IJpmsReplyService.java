package com.jiebao.jpms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiebao.jpms.model.JpmsReply;

import java.util.List;


public interface IJpmsReplyService extends IService<JpmsReply> {


	/**
	 * 根据提案ID查询答复反馈
	 * @param proposalId
	 * @return
	 */
	JpmsReply listReply( Integer proposalId);

}