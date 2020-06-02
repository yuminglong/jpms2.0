package com.jiebao.system.controller;

import com.central.common.model.Result;
import com.jiebao.jpms.model.JpmsAppendix;
import com.jiebao.jpms.model.JpmsProposal;

import com.jiebao.jpms.model.JpmsPunit;
import com.jiebao.jpms.model.JpmsUnit;
import com.jiebao.jpms.service.IJpmsPunitService;
import com.jiebao.jpms.service.IJpmsUnitService;
import com.jiebao.system.service.IJpmsProposalService;
import com.jiebao.system.service.IJpmsUserService;
import com.jiebao.system.unit.SMSUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/system/committee")
@Api(tags = "提案委API文档")
public class JpmsCommitteeController {

	@Autowired
	private IJpmsProposalService jpmsProposalService;

	@Autowired
	private IJpmsUnitService jpmsUnitService;

	@Autowired
	private IJpmsUserService jpmsUserService;


	@Autowired
	private IJpmsPunitService iJpmsPunitService;

	/*@ApiOperation(value = "审查提案并指定承办单位")
	@PostMapping(value = "/checkProposal/{proposalId}/{status}/{unitId}")
	@ResponseBody
	public Result checkProposal(@PathVariable Integer proposalId, @PathVariable Integer status, @PathVariable Integer unitId) {
		return jpmsProposalService.checkProposal(proposalId, status, unitId);
	}*/

	/*@ApiOperation(value = "提案分类")
	@GetMapping("/classUnit/{proposalId}/{classify}")
	@ResponseBody
	public Result classUnit(@PathVariable Integer proposalId, @PathVariable Integer classify) {
		JpmsProposal jpmsProposal = jpmsProposalService.getById(proposalId);
		jpmsProposal.setClassify(classify);
		return jpmsProposalService.saveOrUpdate(jpmsProposal) ? Result.succeed(jpmsProposal) : Result.failed("失败");
	}*/

	@ApiOperation(value = "两办提交")
	@PostMapping("/updateper")
	public Result updateper(JpmsProposal jpmsProposal, HttpServletRequest httpServletRequest) {
		String overAnswer = httpServletRequest.getParameter("overAnswer");
		Integer proposalId = jpmsProposal.getProposalId();
		iJpmsPunitService.updateAnswer(proposalId,overAnswer);
		JpmsProposal byId = jpmsProposalService.findById(proposalId);
		//JpmsProposal p = jpmsProposalService.getById(jpmsProposal.getProposalId());
		if(byId.getStatus() <= 4){
			jpmsProposal.setStatus(4);//单位办理
		}


		return jpmsProposalService.saveOrUpdate(jpmsProposal) ? Result.succeed(jpmsProposal, "提交成功") : Result.failed("请刷新重试");

	}


	@ApiOperation(value = "重新办理")
	@GetMapping("/conclude/{proposalId}")
	public Result conclude(@PathVariable Integer proposalId) {
		JpmsProposal jpmsProposal = jpmsProposalService.getById(proposalId);
		if(jpmsProposal.getStatus() == 8) {
			jpmsProposal.setStatus(9);
			//给委员和相关单位发送信息
			//给委员发送
			String phone = jpmsUserService.getById(jpmsProposal.getUserId()).getMobile();
			//给单位发送
			String phonetwo = "";
			List<JpmsPunit> listz = jpmsUserService.jointpunit(proposalId, null,null);//办理单位
			for(int i = 0; i < listz.size(); i++) {
				phonetwo += jpmsUnitService.getById(listz.get(i).getUnitId()).getPhone();
				if(i < listz.size() - 1) {
					phonetwo += ",";
				}
			}
			//System.out.println("重新办理" + phone + ',' + phonetwo);
		//	SMSUtils.sendSMS(true, "497977", jpmsProposal.getProposalId().toString(), phone+','+phonetwo, "0");
			return jpmsProposalService.saveOrUpdate(jpmsProposal) ? Result.succeed(jpmsProposal, "提案重新办理") : Result.failed("失败");
		}else if(jpmsProposal.getStatus() == 10) {
			return Result.succeed(jpmsProposal, "该提案已完成办理");
		}
		return Result.succeed(jpmsProposal, "该提案还在办理中");
	}

	@ApiOperation(value = "结办提案")
	@GetMapping("/ending/{proposalId}")
	public Result ending(@PathVariable Integer proposalId) {
		JpmsProposal jpmsProposal = jpmsProposalService.getById(proposalId);
		jpmsProposal.setEndTime(new Date());
		if(jpmsProposal.getStatus() == 8) {
			jpmsProposal.setStatus(10);
			//给委员发送
			String phone = jpmsUserService.getById(jpmsProposal.getUserId()).getMobile();
			//给单位发送
			String phonetwo = "";
			List<JpmsPunit> listz = jpmsUserService.jointpunit(proposalId, null,null);//办理单位
			for(int i = 0; i < listz.size(); i++) {
				phonetwo += jpmsUnitService.getById(listz.get(i).getUnitId()).getPhone();
				if(i < listz.size() - 1) {
					phonetwo += ",";
				}
			}
			//System.out.println("结办提案" + phone + ',' + phonetwo);
			//群发
			//SMSUtils.sendSMS(true, "497976", jpmsProposal.getProposalId().toString(), phone + ',' + phonetwo, "0");
			return jpmsProposalService.saveOrUpdate(jpmsProposal) ? Result.succeed(jpmsProposal, "该提案已完成办理") : Result.failed("失败");
		}else if(jpmsProposal.getStatus() == 10) {
			return Result.succeed(jpmsProposal, "该提案已完成办理");
		}
		return Result.failed(jpmsProposal, "该提案还在办理中");
	}

	@ApiOperation(value = "还原提案")
	@GetMapping("/goout/{proposalId}")
	public Result goout(@PathVariable Integer proposalId) {
		JpmsProposal jpmsProposal = jpmsProposalService.getById(proposalId);
		jpmsProposal.setStatus(1);
		jpmsProposalService.saveOrUpdate(jpmsProposal);
		return Result.failed(jpmsProposal, "提案成功还原，等待办理");
	}

	@ApiOperation(value = "催办提案")
	@GetMapping("/supervise/{proposalId}")
	public Result supervise(@PathVariable Integer proposalId) {
		JpmsProposal jpmsProposal = jpmsProposalService.getById(proposalId);
		Date date = new Date();

		long stateTimeLong = jpmsProposal.getLateTime().getTime();
		long endTimeLong = date.getTime();
		System.out.println(endTimeLong - stateTimeLong);
		long oneDay = 86400000;
		long time = endTimeLong - stateTimeLong;

		if(time > oneDay) {
			jpmsProposal.setLateTime(date);
			jpmsProposalService.saveOrUpdate(jpmsProposal);
			//发送短信
			//boolean b = SMSUtils.sendSMS(false, "497410", jpmsProposal.getCause(), phone, "0");
			// 催办未办理提案的单位
			List<JpmsPunit> listz = jpmsUserService.jointpunit(proposalId, 3,null);//主办单位
			List<JpmsPunit> listh = jpmsUserService.jointpunit(proposalId, 4,null);//会办单位
			List<JpmsAppendix> list = jpmsProposalService.appendix(proposalId, 3);//单位回复附件
			boolean flag = false;
			for(int p = 0; p < listz.size(); p++) {//主办
				for(JpmsAppendix j: list) {
					if(j.getUnitId() == listz.get(p).getUnitId()) {//已经回复了
						listz.remove(p);
						p--;
					}
				}
			}
			for(int p = 0; p < listh.size(); p++) {//会办
				for(JpmsAppendix j: list) {
					if(j.getUnitId() == listh.get(p).getUnitId()) {//已经回复了
						listh.remove(p);
						p--;
					}
				}
			}
			for(JpmsPunit p: listz) {
				JpmsUnit unit = jpmsUnitService.getById(p.getUnitId());
				//SMSUtils.sendSMS(false, "497410", null, unit.getPhone() , "0");
				//System.out.println("催办的主办单位" + unit.getPhone());
			}
			for(JpmsPunit p: listh) {
				JpmsUnit unit = jpmsUnitService.getById(p.getUnitId());
				//SMSUtils.sendSMS(false, "497410", null, unit.getPhone() , "0");
				//System.out.println("催办的会办单位" + unit.getPhone());
			}

			boolean b = true;
			return b ? Result.succeed("催办成功") : Result.failed("提案催办失败");
		}else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(jpmsProposal.getLateTime());
			cal.add(Calendar.DATE, 1);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String format = sdf.format(cal.getTime());
			return Result.failed("24小时内请勿重复操作，" + format);
		}

	}

}
