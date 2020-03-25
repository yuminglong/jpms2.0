package com.jiebao.jpms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiebao.jpms.mapper.JpmsUnitMapper;
import com.jiebao.jpms.model.JpmsAppendix;
import com.jiebao.jpms.model.JpmsProposal;
import com.jiebao.jpms.model.JpmsUnit;
import com.jiebao.jpms.service.IJpmsUnitService;
import com.jiebao.system.service.IJpmsProposalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class JpmsUnitServiceImpl extends ServiceImpl<JpmsUnitMapper, JpmsUnit> implements IJpmsUnitService {

	@Autowired
	private IJpmsProposalService jpmsProposalService;

	@Override
	public PageInfo<JpmsUnit> listUnit(Integer pageNumber, Integer pageSize, String unitName) {
		Page page = PageHelper.startPage(pageNumber, pageSize);
		List<JpmsUnit> list = baseMapper.listUnit(unitName);
		PageInfo info = new PageInfo<>(page.getResult());
		return info;
	}

	@Override
	public PageInfo<JpmsProposal> findList(Integer pageNumber, Integer pageSize, Integer unitId, String cause,Integer status) {
		Page page = PageHelper.startPage(pageNumber, pageSize);
		List<JpmsProposal> list = baseMapper.findList(unitId, cause,status);
		for(JpmsProposal j: list) {
			List<JpmsAppendix> list2 = jpmsProposalService.appendix(j.getProposalId(), 3);//单位对提案的回复
			boolean flag = false;
			for(JpmsAppendix ja: list2) {
				if(ja.getUnitId() == unitId) {//单位对提案进行了回复
					flag = true;
				}
			}
			if(flag) {
				j.setStatusUnit(1);
			}else {
				j.setStatusUnit(0);
			}
		}
		PageInfo info = new PageInfo<>(page.getResult());
		return info;
	}
}
