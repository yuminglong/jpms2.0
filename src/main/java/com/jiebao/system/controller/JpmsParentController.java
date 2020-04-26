package com.jiebao.system.controller;

import com.central.common.model.Result;
import com.github.pagehelper.PageInfo;
import com.jiebao.jpms.model.JpmsAppendix;
import com.jiebao.jpms.model.JpmsProposal;
import com.jiebao.jpms.model.JpmsReply;
import com.jiebao.jpms.service.IJpmsAppendixService;
import com.jiebao.jpms.service.IJpmsReplyService;
import com.jiebao.jpms.service.IJpmsUnitService;
import com.jiebao.system.model.JpmsUser;
import com.jiebao.system.service.IJpmsProposalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Arrays;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/parent")
@Api(tags = "提案")
public class JpmsParentController {

    @Autowired
    private IJpmsProposalService jpmsProposalService;

    @Autowired
    private IJpmsUnitService jpmsUnitService;

    @Autowired
    private IJpmsAppendixService jpmsAppendixService;

    @Autowired
    private IJpmsReplyService jpmsReplyService;

    @ApiOperation(value = "查看全部提案")
    @GetMapping("/findList")
    public Result findList(@RequestParam(defaultValue = "1") Integer pageNumber, @RequestParam(defaultValue = "20") Integer pageSize, @RequestParam(required = false) String cause, @RequestParam(required = false) Integer status,@RequestParam(required = false) String startDate,@RequestParam(required = false) String endDate , @RequestParam(required = false) Integer unitId, HttpSession session) {
        JpmsUser user = (JpmsUser) session.getAttribute("user");
        PageInfo<JpmsProposal> list = null;
        switch (user.getType()) {
            case 1:// 1单位
                list = jpmsUnitService.findList(pageNumber, pageSize, user.getUnitId(), cause, status,startDate,endDate);
                break;
            case 2:  // 2委员
                list = jpmsProposalService.membersProposal(pageNumber, pageSize, user.getUserId(), cause, status,startDate,endDate);
                break;
			/*case 3://督察室
				break;
			case 4://4提案委
				break;*/
            default:
                list = jpmsProposalService.findList(pageNumber, pageSize, cause, status,startDate,endDate,unitId);
        }
        return Result.succeedWith(list, 200, "cg");
    }

    @ApiOperation(value = "根据id查看提案")
    @GetMapping("/lookProposal/{proposalId}")
    public JpmsProposal lookProposa(@PathVariable Integer proposalId) {
        return jpmsProposalService.findById(proposalId);
    }

    @ApiOperation(value = "根据id删除提案")
    @GetMapping("/delete")
    public Result delete(Integer proposalId) {
        JpmsProposal proposal = jpmsProposalService.getById(proposalId);
        proposal.setStatus(-1);
        return jpmsProposalService.saveOrUpdate(proposal) ? Result.succeed("删除成功") : Result.failed("删除失败!");
    }

    @ApiOperation(value = "彻底删除提案")
    @GetMapping("/deleteProposa")
    public Result deleteUserRole(int[] ids) {
        for (int i : ids) {
            //后面提案号顶上（连号）
           /* int number = jpmsProposalService.getById(i).getProposalNumber();//要删除的提案号
            List<JpmsProposal> listp = jpmsProposalService.list();
            for (JpmsProposal j : listp) {
                if (j.getProposalNumber() > number) {
                    j.setProposalNumber(j.getProposalNumber() - 1);
                    jpmsProposalService.saveOrUpdate(j);
                }
            }*/
            //删除附件
            List<JpmsAppendix> list = jpmsProposalService.appendix(i, null);
            for (JpmsAppendix appendix : list) {
                if (appendix != null) {
                    jpmsAppendixService.removeById(appendix.getAppendixId());
                    File file = new File(appendix.getFileName());
                    if (file.exists()) {
                        file.delete();
                    }
                }
            }

            //删除答复
            JpmsReply jpmsReply = jpmsReplyService.listReply(i);
            if (jpmsReply != null) {
                jpmsReplyService.removeById(jpmsReply.getReplyId());
            }
            //删除承办 |会办 单位
            jpmsProposalService.deletepunit(i);
            //联名提案 还要删除联名人
            jpmsProposalService.deletepersons(i);
            //删除该提案
            jpmsProposalService.removeById(i);
        }
        return Result.succeed("删除成功");
    }


}
