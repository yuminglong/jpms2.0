package com.jiebao.jpms.controller;

import com.central.common.model.Result;
import com.jiebao.jpms.model.JpmsAppendix;
import com.jiebao.jpms.model.JpmsPersons;
import com.jiebao.jpms.model.JpmsProposal;
import com.jiebao.jpms.model.JpmsPunit;
import com.jiebao.jpms.service.IJpmsAppendixService;
import com.jiebao.jpms.service.IJpmsPunitService;
import com.jiebao.system.model.JpmsDownload;
import com.jiebao.system.model.JpmsUser;
import com.jiebao.system.service.IJpmsDownloadService;
import com.jiebao.system.service.IJpmsProposalService;
import com.jiebao.system.service.IJpmsUserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
public class JpmsStateController {

    @Autowired
    private IJpmsDownloadService iJpmsDownloadService;

    @Autowired
    private IJpmsProposalService jpmsProposalService;

    @Autowired
    private IJpmsUserService jpmsUserService;

    @Autowired
    private IJpmsAppendixService jpmsAppendixService;

    @Autowired
    private IJpmsPunitService iJpmsPunitService;


    @RequestMapping("login")
    public String login() {
        return "login";
    }

    @GetMapping("detaile/wySatisfaction")
    public String wySatisfaction() {
        return "wySatisfaction";
    }

    @GetMapping("detaile/tawSatisfaction")
    public String tawSatisfaction() {
        return "tawSatisfaction";
    }

    @GetMapping("tawloadSat")
    public String tawloadSat() {
        return "tawloadSat";
    }

    @RequestMapping({"", "index"})
    public String index() {
        return "front/index";
    }

    @ApiOperation(value = "页面跳转")
    @GetMapping("/skip")
    public String skip(@RequestParam String table) {
        return table;
    }

    @ApiOperation(value = "详情页面")
    @GetMapping("detaile/{proposalId}")
    public String detaile(@PathVariable Integer proposalId, Model model) {
        model.addAttribute("proposalId", proposalId);
        JpmsProposal byId1 = jpmsProposalService.findById(proposalId);
        Integer userId = byId1.getUserId();

        //联名用户
        List<JpmsPersons> userList = jpmsUserService.jointlm(proposalId);
		for(JpmsPersons p:userList){
            JpmsUser byId = jpmsUserService.getById(p.getUserId());
                p.setJpmsUser(byId);
		}
		//迭代器移除比较靠谱
        Iterator<JpmsPersons> iter = userList.iterator();
        while(iter.hasNext()){
            JpmsPersons s = iter.next();
            if(s.getJpmsUser().getUserId().equals(userId)){
                iter.remove();
            }
        }
        model.addAttribute("userList", userList);
        for (int i = 1; i < 5; i++) {
            if (i == 3) {//单位回复
                List<JpmsAppendix> zlist = new ArrayList<>();//主办
                List<JpmsAppendix> hlist = new ArrayList<>();//会办
                List<JpmsPunit> listz = jpmsUserService.jointpunit(proposalId, 3, null);//主办单位
                List<JpmsPunit> listh = jpmsUserService.jointpunit(proposalId, 4, null);//会办单位
                List<JpmsAppendix> list = jpmsProposalService.appendix(proposalId, 3);//单位回复附件

                for (JpmsAppendix j : list) {
                    for (JpmsPunit p : listz) {//主办
                        if (j.getUnitId() == p.getUnitId()) {
                            j.setAnswer(p.getAnswer());
                            j.setAnswerAfter(p.getAnswerAfter());
                            j.setUnitName(p.getUnitName());
                            zlist.add(j);
                        }
                    }
                    for (JpmsPunit p : listh) {//会办
                        if (j.getUnitId() == p.getUnitId()) {
                            j.setAnswer(p.getAnswer());
                            j.setAnswerAfter(p.getAnswerAfter());
                            j.setUnitName(p.getUnitName());
                            hlist.add(j);
                        }
                    }
                }
                model.addAttribute("JpmsAppendixz", zlist);
                model.addAttribute("JpmsAppendixh", hlist);

            } else {
                List<JpmsAppendix> list = jpmsProposalService.appendix(proposalId, i);//提案相关附件
                if (i == 1) {//提案
                    model.addAttribute("JpmsAppendione", list);
                } else if (i == 2) {//提案委
                    model.addAttribute("JpmsAppendixtwo", list);
                } else if (i == 4) {//满意度
                    model.addAttribute("JpmsAppendixthree", list);
                }
            }
            List<JpmsAppendix> two = new ArrayList<>();//两办意见
            List<JpmsAppendix> zf = jpmsProposalService.appendix(proposalId, 5);
            List<JpmsAppendix> sw = jpmsProposalService.appendix(proposalId, 6);
            for (JpmsAppendix p : zf) {
                two.add(p);
            }
            for (JpmsAppendix p : sw) {
                two.add(p);
            }
            model.addAttribute("two", two);

        }

        //	return "details";
        return "print";
    }

    @ApiOperation(value = "后台答复页面跳转")
    @GetMapping("/skiprReplyMotion")
    public String skiprReplyMotion(Integer proposalId, Model model) {
        model.addAttribute("proposalId", proposalId);
        return "system/replyMotion";
    }

    @ApiOperation(value = "前台答复页面跳转")
    @GetMapping("/ReplyMotion")
    public String ReplyMotion(Integer proposalId, Model model, HttpSession session) {
        model.addAttribute("proposalId", proposalId);
        JpmsUser user = (JpmsUser) session.getAttribute("user");
        if (user.getType() == 1) {//单位
            return "front/funit/frontreplyMotion";
        } else if (user.getType() == 2) {//委员
            return "front/members/frontreplyMotionw";
        }
        return "front/funit/frontreplyMotion";
    }


    @ApiOperation(value = "前台答复页面跳转")
    @GetMapping("/Reply")
    public String Reply(Integer proposalId, Model model, HttpSession session) {
        model.addAttribute("proposalId", proposalId);
        JpmsUser user = (JpmsUser) session.getAttribute("user");
        if (user.getType() == 1) {//单位
            return "front/funit/replyMotion";
        } else if (user.getType() == 2) {//委员
            return "front/members/frontreplyMotionw";
        }
        return "front/funit/replyMotion";
    }



    @ApiOperation(value = "页面跳转")
    @GetMapping("/amendUser")
    public String amendUser(Integer UserId, Model model) {
        model.addAttribute("UserId", UserId);
        return "system/user/amendUser";
    }

    @GetMapping("/amendPart")
    public String amendPart(Integer rid, Model model) {
        model.addAttribute("roleId", rid);
        return "system/user/amendPart";
    }

    @GetMapping("/amendPermission")
    public String amendPermission(Integer id, Model model) {
        model.addAttribute("id", id);
        return "system/user/amendPermission";
    }

    @GetMapping("/amendUnit")
    public String amendUnit(Integer unitId, Model model) {
        model.addAttribute("unitId", unitId);
        return "system/unit/amendUnit";
    }

    @GetMapping("checkMotion")
    public String checkMotion(Integer proposalId, Model model) {
        model.addAttribute("proposalId", proposalId);
        return "front/checkMotion1";
    }

    @GetMapping("systemcheckMotion")
    public String systemcheckMotion(Integer proposalId, Model model, HttpSession session) {
        model.addAttribute("proposalId", proposalId);
        JpmsUser user = (JpmsUser) session.getAttribute("user");
        if (user.getType() == 4) {//提案委
            return "system/syscheckMotion";
        } else if (user.getType() == 6) {//市委办
            return "system/cityCheckMotion";
        }
        return "system/systemCheckMotion";
    }

    @GetMapping("motion_index")
    public String motion_index(HttpSession session) {
        JpmsUser user = (JpmsUser) session.getAttribute("user");
        if (user.getType() == 1) {//单位
            return "front/funit/motion_indexw";
        } else if (user.getType() == 2) {//委员
            return "front/members/motion_index";
        }
        return "front/members/motion_index";
    }

    @GetMapping("amendLaws")
    public String amendLaws(Integer lawsId, Model model) {
        model.addAttribute("lawsId", lawsId);
        return "system/laws/amendLaws";
    }


    @GetMapping("informUpdate")
    public String informUpdate(Integer informId, Model model) {
        model.addAttribute("informId", informId);
        return "system/inform/editInform";
    }

    @GetMapping("Laws")
    public String Laws(Integer lawsId, Model model) {
        model.addAttribute("lawsId", lawsId);
        return "system/laws/Laws";
    }

    @GetMapping("workUpdate")
    public String workUpdate(Integer workId, Model model) {
        model.addAttribute("workId", workId);
        return "system/workTrend/editWork";
    }


    @GetMapping("exchangeUpdate")
    public String exchangeUpdate(Integer exchangeId, Model model) {
        model.addAttribute("exchangeId", exchangeId);
        return "system/exchange/editExchange";
    }


    @GetMapping("inform")
    public String inform(Integer informId, Model model) {
        model.addAttribute("informId", informId);
        return "front/inform/checkFile";
    }

    @GetMapping("work")
    public String work(Integer workId, Model model) {
        model.addAttribute("workId", workId);
        return "front/workTrend/checkFile";
    }

    @GetMapping("exchange")
    public String exchange(Integer exchangeId, Model model) {
        model.addAttribute("exchangeId", exchangeId);
        return "front/exchange/checkFile";
    }


    @PostMapping("/picture")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file, HttpSession session) throws Exception {
        String pathString = "D:/jpms/jpmsupload/picture";
        JpmsDownload jpmsDownload = new JpmsDownload();
        String src = "";
        if (file != null) {
            src = "/" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + file.getOriginalFilename();
        }
        pathString = pathString + src;

		/*jpmsDownload.setFileUrl(pathString);
		jpmsDownload.setTitle(file.getOriginalFilename());
		JpmsUser user = (JpmsUser) session.getAttribute("user");
		jpmsDownload.setCreateUser(user.getRealName());
		iJpmsDownloadService.saveOrUpdate(jpmsDownload);*/

        try {
            File files = new File(pathString);
            if (!files.getParentFile().exists()) {
                files.getParentFile().mkdirs();
            }
            file.transferTo(files);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> map2 = new HashMap<>();
        map.put("code", 0);//0表示成功，1失败
        map.put("msg", "上传成功");//提示消息
        map.put("data", map2);
        map2.put("src", "http://61.187.179.214/jpms" + src);//图片url
        //map2.put("src", "http://192.168.20.100/jpms" + src);//图片url
        map2.put("title", file.getOriginalFilename());//图片名称，这个会显示在输入框里
        String result = new JSONObject(map).toString();
        return result;
    }

    //上传  附件
    @ApiOperation(value = "文件上传")
    @RequestMapping("upload")
    @ResponseBody
    public synchronized Result upload(@RequestParam("file") MultipartFile file, HttpSession session, @RequestParam(required = false) Integer proposalId, Integer type) {
        String pathString = null;
        JpmsAppendix jpmsAppendix = new JpmsAppendix();

        JpmsUser user = (JpmsUser) session.getAttribute("user");
        if (user.getType() == 1) {//单位
            jpmsAppendix.setUnitId(user.getUnitId());
        }
        if (file != null) {
            pathString = "D:/jpms/jpmsupload/" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + file.getOriginalFilename();
        }
        List<JpmsAppendix> list = jpmsProposalService.appendix(proposalId, type);
        for (JpmsAppendix j : list) {
            if (j.getProposalId() == proposalId && type == j.getType() && j.getUnitId() == user.getUnitId()) {
                deleteAppendix(j.getAppendixId());//存在删除 旧附件
            } else if (j.getProposalId() == proposalId && type == j.getType() && type != 3) {
                deleteAppendix(j.getAppendixId());//存在删除 旧附件
            }
        }

        //附件表 生成记录

        jpmsAppendix.setAppendixName(file.getOriginalFilename());//附件名
        jpmsAppendix.setType(type);
        jpmsAppendix.setProposalId(proposalId);
        jpmsAppendix.setFileName(pathString);//存储文件名
        jpmsAppendixService.save(jpmsAppendix);
        try {
            File files = new File(pathString);
            if (!files.getParentFile().exists()) {
                files.getParentFile().mkdirs();
            }
            file.transferTo(files);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Result.succeedWith(jpmsAppendix, 200, "1");
    }

    @GetMapping("/updateAP")
    @ResponseBody
    private Result updateAP(Integer proposalId, Integer appendixId) {
        JpmsAppendix jpmsAppendix = jpmsAppendixService.getById(appendixId);
        jpmsAppendix.setProposalId(proposalId);
        return jpmsAppendixService.saveOrUpdate(jpmsAppendix) ? Result.succeedWith("1", 200, "成功") : Result.failed("失败");
    }

    @ApiOperation(value = "文件下载")
    @GetMapping("/file/{appendixId}")
    @ResponseBody
    private void downloadFile(@PathVariable Integer appendixId, HttpServletResponse response) {
        //JpmsAppendix appendix = jpmsProposalService.appendix(proposalId);
        JpmsAppendix appendix = jpmsAppendixService.getById(appendixId);
        String downloadFilePath = appendix.getFileName();//被下载的文件在服务器中的路径,
        String fileName = appendix.getAppendixName();//被下载文件的名称
        //System.out.println(fileName);
        File file = new File(downloadFilePath);
        if (file.exists()) {
            response.setContentType("application/force-download");// 设置强制下载不打开  
            try {
                response.addHeader("Content-Disposition", "attachment;fileName=" + new String(fileName.getBytes("GB2312"), "ISO8859-1"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream outputStream = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    outputStream.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                return;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            return;
        }
        return;
    }

    @ApiOperation(value = "附件删除")
    public boolean deleteAppendix(Integer AppendixId) {
        JpmsAppendix appendix = jpmsAppendixService.getById(AppendixId);
        File file = new File(appendix.getFileName());
        if (file.exists()) {
            file.delete();
        }
        return jpmsAppendixService.removeById(AppendixId);
    }

    @ApiOperation(value = "主办单位附件删除")
    @GetMapping("delete/{appendixId}")
    public String deleteAppendixByUnit(@PathVariable Integer appendixId, HttpSession session) {
        JpmsAppendix appendix = jpmsAppendixService.getById(appendixId);
        File file = new File(appendix.getFileName());
        if (file.exists()) {
            file.delete();
        }
        Integer ProposalId = appendix.getProposalId();
        JpmsUser user = (JpmsUser) session.getAttribute("user");
        Integer unitId = user.getUnitId();
        try {
            boolean b = iJpmsPunitService.updateByproId(ProposalId,unitId);
        }catch (Exception e){
            e.printStackTrace();
        }
        jpmsAppendixService.removeById(appendixId);
        return "front/funit/frontreplyMotion";

    }




    @ApiOperation(value = "查询附件")
    @GetMapping("/selectapd/{proposalId}/{type}")
    @ResponseBody
    public Result selectapd(@PathVariable Integer proposalId, @PathVariable Integer type) {
        List<JpmsAppendix> appendix = jpmsProposalService.appendix(proposalId, type);
        if (appendix.size() < 1) {
            return Result.succeed(-1, "");
        } else {
            return Result.succeed(appendix.get(0));
        }

    }

    @ApiOperation(value = "提案委满意度测评")
    @PostMapping("/tawSatisfactionLoad")
    @ResponseBody
    public Result tawSatisfactionLoad(Integer proposalId, String tawSatisfaction)  {
        JpmsProposal jpmsProposal = jpmsProposalService.getById(proposalId);
        jpmsProposal.setTawSatisfaction(tawSatisfaction);
        return jpmsProposalService.saveOrUpdate(jpmsProposal) ? Result.succeed("测评成功！") : Result.failed("测评失败");

    }
}
