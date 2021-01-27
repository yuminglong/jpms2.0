package com.jiebao.jpms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageInfo;
import com.jiebao.jpms.mapper.JpmsAppendixMapper;
import com.jiebao.jpms.mapper.JpmsPunitMapper;
import com.jiebao.jpms.model.*;
import com.jiebao.jpms.service.IJpmsAppendixService;
import com.jiebao.jpms.service.IJpmsPunitService;
import com.jiebao.jpms.service.IJpmsUnitService;
import com.jiebao.system.service.IJpmsProposalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/docking")
@Api(tags = "对接接口")
public class JpmsDockingController extends BaseController {

    @Autowired
    private IJpmsProposalService jpmsProposalService;

    @Autowired
    private IJpmsPunitService jpmsPunitService;

    @Autowired
    private JpmsPunitMapper jpmsPunitMapper;

    @Autowired
    private IJpmsAppendixService jpmsAppendixService;

    @Autowired
    private IJpmsUnitService jpmsUnitService;
    @Autowired
    private JpmsAppendixMapper jpmsAppendixMapper;

    static final String JieBaoId = "b1c8adc7ad74b8b520438c0b58038202";

    @GetMapping("/dockingInfo")
    @ApiOperation(value = "查询未提交未对接单位意见的提案", notes = "查询未提交未对接单位意见的提案", response = JiebaoResponse.class, httpMethod = "GET")
    public JiebaoResponse getProposalList(Integer pageNumber, Integer pageSize, String jieBaoId) {
        if (JieBaoId.equals(jieBaoId)) {
            PageInfo<JpmsProposal> exchangeList = jpmsProposalService.getProposalList(pageNumber, pageSize);

            return new JiebaoResponse().data(exchangeList);
        } else {
            return new JiebaoResponse().failMessage("密钥错误");
        }
    }

    @GetMapping("/getJieBaoId")
    @ApiOperation(value = "获取密钥", notes = "获取密钥", response = JiebaoResponse.class, httpMethod = "GET")
    public JiebaoResponse getJieBaoId() {
        String JieBaoId = "b1c8adc7ad74b8b520438c0b58038202";
        return new JiebaoResponse().data(JieBaoId);
    }


    @ApiOperation(value = "单位答复")
    @PostMapping("/dockingAnswer")
    public synchronized JiebaoResponse satisfaction(@RequestParam("file") MultipartFile file, Integer proposalId, String answer, Integer unitId, String jieBaoId) {
        if (JieBaoId.equals(jieBaoId)) {
            // List<JpmsPunit> listh = jpmsUserService.jointpunit(proposalId, null, user.getUnitId());
            JpmsPunit jpmsPunit = jpmsPunitMapper.selectOne(new LambdaQueryWrapper<JpmsPunit>().eq(JpmsPunit::getProposalId, proposalId).eq(JpmsPunit::getUnitId, unitId).and(wrapper -> wrapper.eq(JpmsPunit::getType, 3).or().eq(JpmsPunit::getType, 4)));
            if (jpmsPunit.getAnswer() != null) {
                jpmsPunit.setAnswerAfter(jpmsPunit.getAnswer());
            }
            jpmsPunit.setAnswer(answer);
            jpmsPunitService.saveOrUpdate(jpmsPunit);


            //上传附件
            String pathString = null;
            JpmsAppendix jpmsAppendix = new JpmsAppendix();
            jpmsAppendix.setUnitId(unitId);
            if (file != null) {
                pathString = "D:/jpms/jpmsupload/" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + file.getOriginalFilename();
            }
            List<JpmsAppendix> list = jpmsProposalService.appendix(proposalId, 3);
            for (JpmsAppendix j : list) {
                if (j.getProposalId() == proposalId && 3 == j.getType() && j.getUnitId() == unitId) {
                    deleteAppendix(j.getAppendixId());//存在删除 旧附件
                } else if (j.getProposalId() == proposalId && j.getType() == 3) {
                    deleteAppendix(j.getAppendixId());//存在删除 旧附件
                }
            }
            //附件表 生成记录

            jpmsAppendix.setAppendixName(file.getOriginalFilename());//附件名
            jpmsAppendix.setType(3);
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

            JpmsProposal byId = jpmsProposalService.findById(proposalId);
            Integer status = byId.getStatus();
            if (status == 4) {
                jpmsProposalService.updatebyProId(proposalId);
            }
            return new JiebaoResponse().okMessage("答复成功");
        } else {
            return new JiebaoResponse().failMessage("密钥错误");
        }
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


    @ApiOperation(value = "提案附件下载")
    @GetMapping("/file/{appendixId}")
    @ResponseBody
    private void downloadFile(@PathVariable Integer appendixId, HttpServletResponse response) {
        JpmsAppendix appendix = jpmsAppendixService.getById(appendixId);
        String downloadFilePath = appendix.getFileName();//被下载的文件在服务器中的路径,
        String fileName = appendix.getAppendixName();//被下载文件的名称
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


    @GetMapping("/updateDocking")
    @ApiOperation(value = "批量更新对接标识", notes = "批量更新对接标识", response = JiebaoResponse.class, httpMethod = "GET")
    public JiebaoResponse updateDocking(Integer[] proposalIds, String jieBaoId) {
        if (JieBaoId.equals(jieBaoId)) {
            Arrays.stream(proposalIds).forEach(proposalId -> {
                jpmsProposalService.updateIsDocking(proposalId);
            });
            return new JiebaoResponse().okMessage("更新成功");
        } else {
            return new JiebaoResponse().failMessage("密钥错误");
        }
    }

    @GetMapping("/getUnit")
    @ApiOperation(value = "获取单位", notes = "获取单位", response = JiebaoResponse.class, httpMethod = "GET")
    public JiebaoResponse getUnit(String jieBaoId) {
        if (JieBaoId.equals(jieBaoId)) {
            List<JpmsUnit> list = jpmsUnitService.list();
            return new JiebaoResponse().data(list).okMessage("查询成功");
        } else {
            return new JiebaoResponse().failMessage("查询成功");
        }
    }


    @PostMapping("/updateYear")
    @ApiOperation("添加或者修改年度")
    public JiebaoResponse updateYear(JpmsYear year) {
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        try {
            if (year.getYearId() == null) {
                jpmsAppendixMapper.addLK(year.getDate(), year.getStatus());
            } else {
                jpmsAppendixMapper.updateSetStatus(year.getStatus(), year.getYearId());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            jiebaoResponse.failMessage("操作失败");
        }
        return jiebaoResponse;
    }

    @ApiOperation("查询年度")
    @GetMapping("/listYear")  //查全部
    public JiebaoResponse listYear() {
        return new JiebaoResponse().data(jpmsAppendixMapper.listYear()).okMessage("查询成功");
    }

    @Delete("/deleteYear")
    @ApiOperation("删除年度")
    public JiebaoResponse deleteYear(Long id) {
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        try {
            jpmsAppendixMapper.deleteById(id);
            jiebaoResponse.okMessage("操作成功");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            jiebaoResponse.failMessage("操作失败");
        }
        return jiebaoResponse;
    }

    @GetMapping("/sort")
    @ApiOperation("提案排序 参数year年份 ps 2020")
    @Transactional(rollbackFor = Exception.class)
    public JiebaoResponse sort(Integer year) {
        if (year == null) {
            return new JiebaoResponse().failMessage("请选择年份");
        }
        String startDate = (year - 1) + "-12" + "-00";
        String endDate = year + "-12" + "-12" + "-00";
        List<JpmsProposal> jpmsProposals = jpmsAppendixMapper.listByYear(startDate, endDate);
        int i = 1;
        for (JpmsProposal proposal : jpmsProposals) {
            proposal.setProposalNumber(i++);
        }
        boolean b = jpmsProposalService.updateBatchById(jpmsProposals);
        return b ? new JiebaoResponse().okMessage("操作成功") : new JiebaoResponse().failMessage("操作失败");
    }
}
