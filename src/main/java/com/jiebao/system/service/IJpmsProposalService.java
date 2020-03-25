package com.jiebao.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.central.common.model.Result;
import com.github.pagehelper.PageInfo;
import com.jiebao.jpms.model.JpmsAppendix;
import com.jiebao.jpms.model.JpmsProposal;
import com.jiebao.jpms.model.JpmsPunit;
import com.jiebao.jpms.model.JpmsUnit;

import javax.servlet.http.HttpSession;
import java.util.List;


public interface IJpmsProposalService extends IService<JpmsProposal> {

	/**
	 * 根据id查看提案
	 * @param proposalId
	 * @return
	 */
	JpmsProposal findById(Integer proposalId);


	/**
	 * 分页查询提案
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */

	PageInfo<JpmsProposal> findList(Integer pageNumber, Integer pageSize, String cause ,Integer status,Integer unitId);

	/**
	 * 委员查看提案
	 * @param pageNumber
	 * @param pageSize
	 * @param userId
	 * @param cause
	 * @return
	 */
	PageInfo<JpmsProposal> membersProposal(Integer pageNumber, Integer pageSize, Integer userId, String cause,Integer status);


	/**
	 * 查看 | 审查提案并指定承办单位
	 * @param proposalId
	 * @param status     -1  审查不通过
	 * @param unitId
	 * @return
	 */
	Result checkProposal(Integer proposalId, Integer status, Integer unitId);


	/**
	 * 调整办理单位
	 * @param proposalId
	 * @param status     状态
	 * @param unitId     调办单位
	 * @return
	 */
	Result adjust(Integer proposalId, Integer status, Integer unitId);


	/**
	 * 审核延期交办
	 * @param proposalId
	 * @param status
	 * @return
	 */
	Result postpone(Integer proposalId, Integer status);

	/**
	 * 根据提案查询附件
	 * @param proposalId
	 * @return
	 */
	List<JpmsAppendix> appendix(Integer proposalId, Integer type);

	/**
	 * 根据提案 删除承办单位
	 * @param proposalId
	 * @return
	 */
	Integer deletepunit(Integer proposalId);

	/**
	 * 根据提案 删除联名人
	 * @param proposalId
	 * @return
	 */
	Integer deletepersons(Integer proposalId);

    /**
     * 根据userId查询提案条数
     * @return
     */
    Result listStrip(HttpSession session);

	Result listStrip(Integer userId,Integer type);

	/**
	 * 查询当前最大提案号
	 * @return
	 */
	Integer maxnumber();


}