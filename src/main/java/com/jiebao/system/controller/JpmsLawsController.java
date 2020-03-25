package com.jiebao.system.controller;

import com.central.common.model.Result;
import com.github.pagehelper.PageInfo;
import com.jiebao.jpms.service.IJpmsUnitService;
import com.jiebao.system.model.JpmsLaws;
import com.jiebao.system.model.JpmsUser;
import com.jiebao.system.service.IJpmsLawsService;
import com.jiebao.system.service.IJpmsUserService;
import com.jiebao.system.unit.SMSUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/system/laws")
@Api(tags = "政策法规API文档")
public class JpmsLawsController {

    @Autowired
    private IJpmsLawsService jpmsLawsService;

    @Autowired
    private IJpmsUserService iJpmsUserService;

    @ApiOperation(value = "根据标题分页 搜索政策法规")
    @GetMapping("/findList")
    public Result findList(@RequestParam(defaultValue = "1") Integer pageNumber, @RequestParam(defaultValue = "20") Integer pageSize, @RequestParam(required = false) String title) {
        PageInfo<JpmsLaws> list = jpmsLawsService.findList(pageNumber, pageSize, title);
        return Result.succeedWith(list, 200, "cg");
    }

    @ApiOperation(value = "根据ID查询政策法规")
    @GetMapping("/lookLaws/{lawsId}")
    public JpmsLaws lookLaws(@PathVariable Integer lawsId) {
        return jpmsLawsService.getById(lawsId);
    }

    @ApiOperation(value = "新增")
    @PostMapping("/save")
    public Result addLaws(JpmsLaws jpmsLaws, HttpSession session) {
        JpmsUser user = (JpmsUser) session.getAttribute("user");
        jpmsLaws.setCreateUser(user.getRealName());
        return jpmsLawsService.saveOrUpdate(jpmsLaws) ? Result.succeedWith(jpmsLaws, 200, "新增成功") : Result.failed("失败");
    }

    @ApiOperation(value = "修改")
    @PostMapping("/update")
    public Result update(JpmsLaws jpmsLaws) {
        return jpmsLawsService.saveOrUpdate(jpmsLaws) ? Result.succeedWith(jpmsLaws, 200, "修改成功") : Result.failed("失败");
    }

    @ApiOperation(value = "根据id删除提案")
    @GetMapping("/delete")
    public Result delete(Integer lawsId) {
		/*System.out.println("——>" + lawsId);
		JpmsLaws jpmsLaws = jpmsLawsService.getById(lawsId);
		jpmsLaws.setStatus(-1)*/
        ;
        return jpmsLawsService.removeById(lawsId) ? Result.succeed("删除成功") : Result.failed("删除失败!");
    }


}
