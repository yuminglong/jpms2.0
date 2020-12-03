package com.jiebao.jpms.controller;

import com.central.common.model.Result;
import com.github.pagehelper.PageInfo;
import com.jiebao.jpms.model.JpmsProposal;
import com.jiebao.jpms.model.JpmsPunit;
import com.jiebao.jpms.model.JpmsReply;
import com.jiebao.jpms.model.JpmsUnit;
import com.jiebao.jpms.service.IJpmsPunitService;
import com.jiebao.system.model.JpmsUser;
import com.jiebao.system.service.IJpmsProposalService;
import com.jiebao.jpms.service.IJpmsReplyService;
import com.jiebao.jpms.service.IJpmsUnitService;
import com.jiebao.system.service.IJpmsUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;;


@Slf4j
@RestController
@RequestMapping("/front/unit")
@Api(tags = "办理单位API文档")
public class JpmsUnitController {

	@Autowired
	private IJpmsProposalService jpmsProposalService;

	@Autowired
	private IJpmsReplyService jpmsReplyService;

	@Autowired
	private IJpmsUnitService jpmsUnitService;

	@Autowired
	private IJpmsUserService jpmsUserService;

	@Autowired
	private IJpmsPunitService jpmsPunitService;

	@ApiOperation(value = "查看全部单位")
	@GetMapping("/list")
	public Result listUnit(@RequestParam(defaultValue = "1") Integer pageNumber, @RequestParam(defaultValue = "20") Integer pageSize, @RequestParam(required = false) String unitName) {
		PageInfo<JpmsUnit> list = jpmsUnitService.listUnit(pageNumber, pageSize, unitName);
		return Result.succeedWith(list, 200, "");
	}

	@ApiOperation(value = "查看全部单位")
	@GetMapping("/listunitse")
	public List<JpmsUnit> listunitse() {
		List<JpmsUnit> list = jpmsUnitService.list();
		for(int i = 0; i < list.size() - 1; i++) {
			if(list.get(i).getUnitId() == 0) {
				list.remove(i);
			}
		}
		return list;
	}

	@ApiOperation(value = "根据单位ID 查看单位信息")
	@GetMapping("/find/{unitId}")
	public JpmsUnit find(@PathVariable Integer unitId) {
		return jpmsUnitService.getById(unitId);
	}

	@ApiOperation(value = "查看提案答复")
	@GetMapping("/listReply/{proposalId}")
	public JpmsReply listReply(@PathVariable Integer proposalId) {
		return jpmsReplyService.listReply(proposalId);
	}

	@ApiOperation(value = "新增单位·修改")
	@PostMapping("/add")
	public Result add(JpmsUnit jpmsUnit) {
		return jpmsUnitService.saveOrUpdate(jpmsUnit) ? Result.succeedWith(jpmsUnit, 200, "成功！") : Result.failed("失败");
	}

	@ApiOperation(value = "删除办理单位")
	@GetMapping("/delete")
	public Result deleteUnit(Integer unitId) {
		JpmsUnit unit = jpmsUnitService.getById(unitId);
		unit.setStatus(-1);
		return jpmsUnitService.saveOrUpdate(unit) ? Result.succeedWith(unit, 200, "删除成功") : Result.failed("删除失败");
	}

	@ApiOperation(value = "彻底删除单位")
	@GetMapping("/deleteUnit")
	public Result deleteUnit(int[] ids) {
		for(int unitId: ids) {
			jpmsUnitService.removeById(unitId);
		}
		return Result.succeed("删除成功");
	}

	@ApiOperation(value = "单位答复")
	@PostMapping("/answer")
	public Result satisfaction(Integer proposalId, String answer, HttpSession session) {
		JpmsUser user = (JpmsUser) session.getAttribute("user");
		if(user.getType() == 1) {
			List<JpmsPunit> listh = jpmsUserService.jointpunit(proposalId, null, user.getUnitId());
			for(JpmsPunit p: listh) {
				if(p.getAnswer() != null) {
					if(p.getAnswerAfter()!=null){
						p.setAnswerAfter(p.getAnswerAfter() + p.getAnswer());//之前办理
					}else {
						p.setAnswerAfter(p.getAnswer());//之前办理
					}
				}

				p.setAnswer(answer);
				jpmsPunitService.saveOrUpdate(p);
                JpmsProposal byId = jpmsProposalService.findById(proposalId);
                Integer status = byId.getStatus();
                if(status == 4){
                    jpmsProposalService.updatebyProId(proposalId);
                }
			}
		}
		return Result.succeed("答复成功！");
	}

	@ApiOperation(value = "单位代传满意度")
	@PostMapping("/satisfactionUnit")
	public Result satisfactionUnit(Integer proposalId, HttpSession session) {
		JpmsUser user = (JpmsUser) session.getAttribute("user");
		JpmsProposal jpmsProposal = jpmsProposalService.getById(proposalId);
		if (jpmsProposal.getStatus() <6){
			return Result.failed("请上传单位意见");
		}
		else {
			jpmsProposal.setStatus(8);//已测评
			jpmsProposalService.saveOrUpdate(jpmsProposal);
			return Result.succeed("测评成功！");
		}
	}
}
