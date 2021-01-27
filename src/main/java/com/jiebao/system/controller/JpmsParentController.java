package com.jiebao.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.central.common.model.Result;
import com.github.pagehelper.PageInfo;
import com.jiebao.jpms.model.*;
import com.jiebao.jpms.service.IJpmsAppendixService;
import com.jiebao.jpms.service.IJpmsReplyService;
import com.jiebao.jpms.service.IJpmsUnitService;
import com.jiebao.system.mapper.JpmsProposalMapper;
import com.jiebao.system.model.JpmsUser;
import com.jiebao.system.service.IJpmsProposalService;
import com.jiebao.system.service.IJpmsUserService;
import com.jiebao.util.WorderToNewWordUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.hibernate.validator.internal.util.privilegedactions.GetResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.*;


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

    @Autowired
    private JpmsProposalMapper jpmsProposalMapper;

    @Autowired
    private IJpmsUserService jpmsUserService;

    @ApiOperation(value = "查看全部提案")
    @GetMapping("/findList")
    public Result findList(@RequestParam(defaultValue = "1") Integer pageNumber, @RequestParam(defaultValue = "20") Integer pageSize, @RequestParam(required = false) String cause, @RequestParam(required = false) Integer status, @RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate, @RequestParam(required = false) Integer unitId, HttpSession session) {
        JpmsUser user = (JpmsUser) session.getAttribute("user");
        PageInfo<JpmsProposal> list = null;
        if (startDate == null) {
            startDate = String.valueOf(Calendar.getInstance().get(Calendar.YEAR) + 1);
        }
        int startDateInt = Integer.parseInt(startDate);
        endDate = startDate + "-11-30 23:59:59";
        startDate = (startDateInt - 1) + "-12-1 00:00:00";
        switch (user.getType()) {
            case 1:// 1单位
                list = jpmsUnitService.findList(pageNumber, pageSize, user.getUnitId(), cause, status, startDate, endDate);
                break;
            case 2:  // 2委员
                list = jpmsProposalService.membersProposal(pageNumber, pageSize, user.getUserId(), cause, status, startDate, endDate);
                break;
			/*case 3://督察室
				break;
			case 4://4提案委
				break;*/
            default:
                list = jpmsProposalService.findList(pageNumber, pageSize, cause, status, startDate, endDate, unitId);
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


    @GetMapping("/excelWord")
    @ApiOperation(value = "生成提案", notes = "生成提案", response = JiebaoResponse.class, httpMethod = "GET")
    public JiebaoResponse briefingWord(Integer[] proposalIds) {
        if (proposalIds == null) {
            return new JiebaoResponse().failMessage("请勾选需要生成的提案");
        }
        Arrays.stream(proposalIds).forEach(proposalId -> {
            JpmsProposal proposal = jpmsProposalService.getById(proposalId);
            Map<String, String> map = new HashMap<>();

            //案由
            map.put("cause", proposal.getCause());
            //提案类型（个人还是联名）
            map.put("type", proposal.getType());
            //提案分类（经济建设或者社会建设等等）
            map.put("classify", proposal.getClassify());
            //提案内容
            map.put("content", proposal.getContent());
            //届数
            map.put("anniversary", proposal.getAnniversary());
            //次数
            map.put("number", proposal.getNumber());
            //主导人
            map.put("lead", proposal.getLeader());
            //全部联名人
            map.put("peoplesthree", proposal.getPeoplesthree());
            //主办单位
            map.put("zunits", proposal.getZunits());
            //会办单位
            map.put("hunits", proposal.getHunits());
            //审查意见
            map.put("review", proposal.getReview());


            List<JpmsPersons> userList = jpmsUserService.jointlm(proposalId);
            for (JpmsPersons p : userList) {
                JpmsUser byId = jpmsUserService.getById(p.getUserId());
                p.setJpmsUser(byId);
            }
            //迭代器移除比较靠谱
            Iterator<JpmsPersons> iter = userList.iterator();
            while (iter.hasNext()) {
                JpmsPersons s = iter.next();
                if (s.getJpmsUser().getUserId().equals(proposal.getUserId())) {
                    s.setRealName(s.getRealName() + "(主导人)");
                }
            }
            List<String[]> testList = new ArrayList<>();
            for (JpmsPersons p :
                    userList) {
                testList.add(new String[]{p.getJpmsUser().getRealName(), p.getJpmsUser().getSubsector(), p.getJpmsUser().getAddress(), p.getJpmsUser().getMobile()});
            }

            String inputUrl = "E:\\tempDoc.doc";

            String outPath =  "D:\\"+ proposal.getCause() + ".doc";

            WorderToNewWordUtils.changWord(inputUrl, outPath, map, testList);
        });

        return new JiebaoResponse().okMessage("成功");
    }


    @ApiOperation(value = "设置为重要提案")
    @GetMapping("/importProposal")
    public Result importProposal(Integer[] ids) {
        Arrays.stream(ids).forEach(id -> {
            jpmsProposalMapper.importProposal(id);
        });
        return Result.succeed("成功");
    }

    @ApiOperation(value = "取消设置为重要提案")
    @GetMapping("/NotImportProposal")
    public Result NotImportProposal(Integer[] ids) {
        Arrays.stream(ids).forEach(id -> {
            jpmsProposalMapper.NotImportProposal(id);
        });
        return Result.succeed("成功");
    }
}
