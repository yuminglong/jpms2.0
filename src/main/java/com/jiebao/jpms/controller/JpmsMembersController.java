package com.jiebao.jpms.controller;

import com.central.common.model.Result;
import com.jiebao.jpms.model.JpmsProposal;
import com.jiebao.jpms.model.JpmsReply;
import com.jiebao.jpms.service.IJpmsPunitService;
import com.jiebao.system.model.JpmsUser;
import com.jiebao.system.service.IJpmsProposalService;
import com.jiebao.jpms.service.IJpmsReplyService;
import com.jiebao.system.service.IJpmsUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/front/members")
@Api(tags = "政协委员API文档")
public class JpmsMembersController {

    @Autowired
    private IJpmsProposalService jpmsProposalService;

    @Autowired
    private IJpmsReplyService jpmsReplyService;

    @Autowired
    private IJpmsPunitService iJpmsPunitService;

    @Autowired
    private IJpmsUserService jpmsUserService;

    @ApiOperation(value = "新增提案")
    @PostMapping("/save")
    public synchronized Result save(JpmsProposal jpmsProposal, HttpSession session) {
        JpmsUser user = (JpmsUser) session.getAttribute("user");
        if (user == null) {
            return Result.failedWith(user, 200, "新增失败 用户对象丢失,请重新登陆!");
        }
        //给提案委发送短信
        List<JpmsUser> jpmsUserList = jpmsUserService.userType(4);
        for (JpmsUser j : jpmsUserList) {
            //System.out.println("提案委手机" + j.getMobile());
            //SMSUtils.sendSMS(false, "497409", "null", j.getMobile(), "0");
        }
        //查询当前最大提案号


        jpmsProposal.setUserId(user.getUserId());
        jpmsProposal.setLeader(user.getRealName());//主导人
        jpmsProposal.setMobile(user.getMobile());//手机
        jpmsProposal.setStatus(1);
       /* if (jpmsProposal.getPeoplestwo() != null && jpmsProposal.getPeoplestwo() != "") {
            jpmsProposal.setPeoplesthree(jpmsProposal.getPeoplestwo() + ',' + jpmsProposal.getPeoples());
        } else {
            jpmsProposal.setPeoplesthree(jpmsProposal.getPeoples());
        }*/
        jpmsProposal.setPeoplesthree(jpmsProposal.getPeoplestwo());

        return jpmsProposalService.saveOrUpdate(jpmsProposal) ? Result.succeedWith(jpmsProposal, 200, "新增成功") : Result.failed("新增失败");
    }


    @ApiOperation(value = "查看提案答复")
    @GetMapping("/listReply/{proposalId}")
    public JpmsReply listReply(@PathVariable Integer proposalId) {
        return jpmsReplyService.listReply(proposalId);
    }

    @ApiOperation(value = "满意度测评")
    @PostMapping("/satisfaction")
    public Result satisfaction(Integer proposalId, String satisfaction) {
        JpmsProposal jpmsProposal = jpmsProposalService.getById(proposalId);
        jpmsProposal.setSatisfaction(satisfaction);
        jpmsProposal.setStatus(8);//已测评
        return jpmsProposalService.saveOrUpdate(jpmsProposal) ? Result.succeed("测评成功！") : Result.failed("测评失败");

    }

    @ApiOperation(value = "根据提案ID 查看提案答复详情")
    @GetMapping("/findReply/{proposalId}")
    public JpmsReply findReply(@PathVariable Integer replyId) {
        return jpmsReplyService.getById(replyId);
    }

    @ApiOperation(value = "提案委审查提案提案")
    @PostMapping("/SubReply")
    public Result lookState(JpmsProposal jpmsProposal , HttpServletRequest httpServletRequest) {
        String overAnswer = httpServletRequest.getParameter("overAnswer");
        Integer proposalId = jpmsProposal.getProposalId();
        iJpmsPunitService.updateAnswer(proposalId,overAnswer);
        //JpmsProposal p = jpmsProposalService.getById(jpmsProposal.getProposalId());
        if (jpmsProposal.getStatus() == 0) {//提案不通过 给提案人发送短信
            //System.out.println("提案未通过"+jpmsProposal.getMobile());
            //SMSUtils.sendSMS(true, "497977", jpmsProposal.getProposalId().toString(), jpmsProposal.getMobile(), "0");
        } else if (jpmsProposal.getStatus() == 3) {//立案 给两办人员发送短信
           // jpmsProposal.setProposalNumber(jpmsProposalService.maxnumber()+1);//提案号
            List<JpmsUser> jpmsUserList = jpmsUserService.userType(3);
            for (JpmsUser j : jpmsUserList) {
                //System.out.println("政府办" + j.getMobile());
                //SMSUtils.sendSMS(false, "497409", "null", j.getMobile(), "0");
            }
            List<JpmsUser> jpmsUserListT = jpmsUserService.userType(6);
            for (JpmsUser j : jpmsUserListT) {
                //System.out.println("市委办" + j.getMobile());
                //SMSUtils.sendSMS(false, "497409", "null", j.getMobile(), "0");18974787233李主任
            }
        }
      /*  if (jpmsProposal.getPeoplestwo() != null && jpmsProposal.getPeoplestwo() != "") {
            jpmsProposal.setPeoplesthree(jpmsProposal.getPeoplestwo() + ',' + jpmsProposal.getPeoples());
        } else {
            jpmsProposal.setPeoplesthree(jpmsProposal.getPeoples());
        }*/
        jpmsProposal.setPeoplesthree(jpmsProposal.getPeoplestwo());

        return jpmsProposalService.saveOrUpdate(jpmsProposal) ? Result.succeedWith(jpmsProposal, 200, "修改成功") : Result.failed("请刷新重试");
    }

    @ApiOperation(value = "提案委审查提案提案")
    @PostMapping("/mbSubReply")
    public Result mbSubReply(JpmsProposal jpmsProposal) {
        //System.out.println("mbSubReply"+jpmsProposal.getProposalId());
        JpmsProposal proposal = jpmsProposalService.getById(jpmsProposal.getProposalId());
        if (proposal.getStatus() < 3) {
            jpmsProposalService.saveOrUpdate(jpmsProposal);
            return jpmsProposalService.saveOrUpdate(jpmsProposal) ? Result.succeedWith(jpmsProposal, 200, "修改成功") : Result.failed("请刷新重试");
        }

        return Result.succeedWith(jpmsProposal, 200, "此状态不能修改");
    }

    @ApiOperation(value = "未办理条数")
    @GetMapping("/listStrip")
    public Result listStrip(HttpSession session) {
        Result p = jpmsProposalService.listStrip(session);
        return p;
    }

}
