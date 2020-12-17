package com.jiebao.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;



import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.central.common.model.Result;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiebao.jpms.mapper.JpmsAppendixMapper;
import com.jiebao.jpms.mapper.JpmsPunitMapper;
import com.jiebao.jpms.mapper.JpmsUnitMapper;
import com.jiebao.jpms.model.*;
import com.jiebao.jpms.service.IJpmsAppendixService;
import com.jiebao.jpms.service.IJpmsPunitService;
import com.jiebao.jpms.service.IJpmsUnitService;

import com.jiebao.system.mapper.JpmsProposalMapper;
import com.jiebao.system.model.JpmsUser;
import com.jiebao.system.service.IJpmsProposalService;
import com.jiebao.system.service.IJpmsUserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
public class JwsProposalServiceImpl extends ServiceImpl<JpmsProposalMapper, JpmsProposal> implements IJpmsProposalService {


	@Resource
	private JpmsProposalMapper jpmsProposalMapper;

	@Resource
	private IJpmsUserService iJpmsUserService;

	@Resource
	private JpmsUnitMapper jpmsUnitService;

    @Resource
    private JpmsUnitMapper jpmsUnitMapper;

    @Resource
	private JpmsPunitMapper jpmsPunitMapper;

	@Resource
	private IJpmsPunitService jpmsPunitService;

	@Resource
	private JpmsAppendixMapper jpmsAppendixMapper;


	@Resource
	private IJpmsAppendixService jpmsAppendixService;


	@Override
	public JpmsProposal findById(Integer proposalId) {
		JpmsProposal jpmsProposal = baseMapper.findById(proposalId);
		jpmsProposal.setJuser(iJpmsUserService.getById(jpmsProposal.getUserId()));
		//System.out.println(">>"+jpmsUnitMapper.selectByProId(proposalId));
//		if(jpmsUnitMapper.selectByProId(proposalId)!=null){
		jpmsProposal.setOverAnswerType(jpmsUnitMapper.selectByProId(proposalId));
//		}


		return jpmsProposal;
	}

	@Override
	public Result checkProposal(Integer proposalId, Integer status, Integer unitId) {
		JpmsProposal jpmsProposal = jpmsProposalMapper.selectById(proposalId);
		if(jpmsProposal.getStatus() == 1) {
			if(status == -1) {
				jpmsProposal.setStatus(status);//提案不通过
			}else {
				jpmsProposal.setUnitId(unitId);//指定承办单位
				jpmsProposal.setStatus(4);//单位办理
			}
			return jpmsProposalMapper.updateById(jpmsProposal) > 0 ? Result.succeed(jpmsProposal) : Result.failed("失败");
		}
		return Result.failed("该提案还在办理中");
	}


	@Override
	public Result adjust(Integer proposalId, Integer status, Integer unitId) {//调整办理单位
		if(status == -1) {
			return Result.succeed("申请不通过");
		}
		JpmsProposal jpmsProposal = jpmsProposalMapper.selectById(proposalId);
		if(jpmsProposal.getStatus() == 5) {
			jpmsProposal.setUnitId(unitId);
			jpmsProposal.setStatus(4);
			return jpmsProposalMapper.updateById(jpmsProposal) > 0 ? Result.succeed(jpmsProposal, "调整成功") : Result.failed("调整失败");
		}
		return Result.failed(jpmsProposal, "该提案还在办理中");
	}

	@Override
	public Result postpone(Integer proposalId, Integer status) {//审核延期交办
		if(status == -1) {
			return Result.succeed("申请不通过");
		}
		JpmsProposal jpmsProposal = jpmsProposalMapper.selectById(proposalId);
		if(jpmsProposal.getStatus() == 6) {
			jpmsProposal.setLastTime(jpmsProposal.getLateTime());
			jpmsProposal.setStatus(4);
			return jpmsProposalMapper.updateById(jpmsProposal) > 0 ? Result.succeed(jpmsProposal, "调整成功") : Result.failed("调整失败");
		}
		return Result.failed(jpmsProposal, "该提案还在办理中");
	}

	@Override
	public PageInfo<JpmsProposal> findList(Integer pageNumber, Integer pageSize, String cause, Integer status,String startDate,String endDate, @RequestParam(required = false) Integer unitId) {
		Page page = PageHelper.startPage(pageNumber, pageSize);
		List<JpmsProposal> list = baseMapper.findList(cause, status,startDate,endDate);
		for(int p = 0; p < list.size(); p++) {
			if(!list.get(p).getType().equals("个人提案")) {
				List<JpmsPersons> list2 = iJpmsUserService.joint(list.get(p).getProposalId());
				list.get(p).setUserList(list2);//联名人
			}
			List<JpmsPunit> list3 = iJpmsUserService.jointpunit(list.get(p).getProposalId(), null,null);//承办单位
			boolean fla = false;
			if(unitId != null) {
				for(JpmsPunit u: list3) {
					if(u.getUnitId() == unitId) {
						fla = true;
						//list.remove(p);
					}
				}

			}

			List<JpmsPunit> listj = new ArrayList<>();
			List<JpmsPunit> listz = new ArrayList<>();
			List<JpmsPunit> listh = new ArrayList<>();
			for(JpmsPunit u: list3) {
				if(u.getType() == 3) {
					listz.add(u);
				}else if(u.getType() == 4) {
					listh.add(u);
				}else if(u.getType() == 1) {
					listj.add(u);
				}
			}
			list.get(p).setJList(listj);//建议单位
			list.get(p).setZList(listz);//主办单位
			list.get(p).setHList(listh);// 会办单位;
			if(!fla && unitId != null) {
				list.remove(p);
				p--;
			}else if(list3.size() == 0 && unitId != null) {
				list.remove(p);
			}

		}
		PageInfo info = new PageInfo<>(page.getResult());
		return info;
	}

	@Override
	public PageInfo<JpmsProposal> membersProposal(Integer pageNumber, Integer pageSize, Integer userId, String cause,Integer status,String startDate,String endDate) {
		Page page = PageHelper.startPage(pageNumber, pageSize);
		List<JpmsProposal> list = baseMapper.membersProposal(userId, cause,status,startDate,endDate);
		for(JpmsProposal j: list) {
			List<JpmsAppendix> list2 = jpmsProposalMapper.appendix(j.getProposalId(), 4);//单位对提案的回复


			if(list2.size() > 0) {
				j.setStatusUnit(1);
			}else {
				j.setStatusUnit(0);
			}
		}
		PageInfo info = new PageInfo<>(page.getResult());
		return info;
	}


	@Override
	public List<JpmsAppendix> appendix(Integer proposalId, Integer type) {
		return baseMapper.appendix(proposalId, type);
	}

	@Override
	public Integer deletepunit(Integer proposalId) {
		return baseMapper.deletepunit(proposalId);
	}

	@Override
	public Integer deletepersons(Integer proposalId) {
		return baseMapper.deletepersons(proposalId);
	}

	@Override
	public Result listStrip(HttpSession session) {
		List<JpmsProposal> list = null;
		JpmsUser user = (JpmsUser) session.getAttribute("user");
		switch(user.getType()) {
			case 1:// 1单位
				list = jpmsUnitService.findList(user.getUnitId(), null,null,null,null);
				break;
			case 2:  // 2委员
				list = jpmsProposalMapper.membersProposal(user.getUserId(), null,null,null,null);
				break;
			default:
				list = jpmsProposalMapper.findList(null, null,null,null);
		}
		Integer p = 0;
		Integer z = 0;
		for(JpmsProposal j: list) {
			if(j.getStatus() < 10) {
				p++;
			}else {
				z++;
			}
		}
		//return baseMapper.listStrip(userId);
		return Result.succeedWith(p, z, null);
	}

	@Override
	public Result listStrip(Integer userId, Integer type) {
		List<JpmsProposal> list = null;
		switch(type) {
			case 1:// 1单位
				list = jpmsUnitService.findList(userId, null,null,null,null);
				break;
			case 2:  // 2委员
				list = jpmsProposalMapper.membersProposal(userId, null,null,null,null);
				break;
			default:
				list = jpmsProposalMapper.findList(null, null,null,null);
		}
		Integer p = 0;
		Integer z = 0;
		for(JpmsProposal j: list) {
			if(j.getStatus() < 10) {
				p++;
			}else {
				z++;
			}
		}
		//return baseMapper.listStrip(userId);
		return Result.succeedWith(p, z, null);
	}

	@Override
	public Integer maxnumber() {
		return baseMapper.maxnumber();
	}

	@Override
	public boolean  updatebyProId(Integer proposalId){
		return jpmsProposalMapper.updatebyProId(proposalId);
	}

	@Override
	public PageInfo<JpmsProposal> getProposalList(Integer pageNumber, Integer pageSize) {
		Page page = PageHelper.startPage(pageNumber, pageSize);
		Map<String, Object> map = new HashMap<>();
		map.put("status",4);
		map.put("is_docking",0);
		List<JpmsProposal> jpmsProposals = jpmsProposalMapper.selectByMap(map);
		for (JpmsProposal p: jpmsProposals
			 ) {
			List<JpmsUnit>  pUnits = new ArrayList<>();
			List appendixList = new ArrayList<>();
			List<JpmsPunit> unit = jpmsPunitMapper.getUnit(p.getProposalId());
			HashMap<String, Object> fileMap = new HashMap<>();
			fileMap.put("proposal_id",p.getProposalId());
			List<JpmsAppendix> fileList = jpmsAppendixMapper.selectByMap(fileMap);
			for (JpmsAppendix file: fileList
				 ) {
				appendixList.add(file.getAppendixId());
			}
			p.setDockingAppendixIds(appendixList);
			for (JpmsPunit u:unit
				 ) {
				JpmsUnit jpmsUnit = jpmsUnitService.selectById(u.getUnitId());
				pUnits.add(jpmsUnit);
			}
			p.setDockingUnits(pUnits);
			JpmsUser byId = iJpmsUserService.getById(p.getUserId());
			p.setUser(byId);
		}
		PageInfo info = new PageInfo<>(page.getResult());
		return info;
	}

	@Override
	public boolean updateIsDocking(Integer proposalId) {
		return jpmsProposalMapper.updateIsDocking(proposalId);
	}


}

