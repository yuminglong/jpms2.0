package com.jiebao.jpms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.jiebao.jpms.model.JpmsProposal;
import com.jiebao.jpms.model.JpmsUnit;

import java.util.List;


public interface IJpmsUnitService extends IService<JpmsUnit> {

	/**
	 * 分页查询全部 单位
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	PageInfo<JpmsUnit> listUnit(Integer pageNumber, Integer pageSize,String unitName);


	/**
	 *  查询 分配给单位 的提案
	 * @param pageNumber
	 * @param pageSize
	 * @param unitId 单位id
	 * @param cause  标题
	 * @return
	 */
	PageInfo<JpmsProposal> findList(Integer pageNumber, Integer pageSize, Integer unitId, String cause,Integer status,String startDate,String endDate);


}