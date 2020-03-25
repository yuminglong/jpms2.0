package com.jiebao.system.controller;

import com.central.common.model.Result;
import com.github.pagehelper.PageInfo;
import com.jiebao.system.model.JpmsUser;
import com.jiebao.system.model.JpmsWork;
import com.jiebao.system.service.IJpmsWorkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


@Slf4j
@RestController
@RequestMapping("/system/work")
@Api(tags = "工作动态API文档")
public class JpmsWorkController {

	@Autowired
	private IJpmsWorkService IJpmsWorkService;

	@ApiOperation(value = "根据标题分页 搜索工作动态")
	@GetMapping("/findList")
	public Result findList(@RequestParam(defaultValue = "1") Integer pageNumber, @RequestParam(defaultValue = "20") Integer pageSize, @RequestParam(required = false) String title) {
		PageInfo<JpmsWork> list = IJpmsWorkService.findList(pageNumber, pageSize, title);
		return Result.succeedWith(list, 200, "cg");
	}

	@ApiOperation(value = "根据ID查询工作动态")
	@GetMapping("/lookWork/{workId}")
	public JpmsWork lookWork(@PathVariable Integer workId) {
		//System.out.println("workid"+workId);
		return IJpmsWorkService.getById(workId);
	}

	@ApiOperation(value = "新增")
	@PostMapping("/save")
	public Result addWork(JpmsWork jpmsWork, HttpSession session) {
		JpmsUser user = (JpmsUser) session.getAttribute("user");
        jpmsWork.setCreateUser(user.getRealName());
		return IJpmsWorkService.saveOrUpdate(jpmsWork) ? Result.succeedWith(jpmsWork, 200, "新增成功") : Result.failed("失败");
	}

	@ApiOperation(value = "修改")
	@PostMapping("/update")
	public Result update(JpmsWork jpmsWork) {
		return IJpmsWorkService.saveOrUpdate(jpmsWork) ? Result.succeedWith(jpmsWork, 200, "修改成功") : Result.failed("失败");
	}

	@ApiOperation(value = "根据id删除通知公告")
	@GetMapping("/delete")
	public Result delete(Integer workId) {
		/*System.out.println("——>" + lawsId);
		JpmsLaws jpmsLaws = jpmsLawsService.getById(lawsId);
		jpmsLaws.setStatus(-1)*/;
		return IJpmsWorkService.removeById(workId) ? Result.succeed("删除成功") : Result.failed("删除失败!");
	}

    @ApiOperation(value = "根据标题分页 搜索工作动态")
    @GetMapping("/workList")
    public Result workList(@RequestParam(defaultValue = "1") Integer pageNumber, @RequestParam(defaultValue = "5") Integer pageSize, @RequestParam(required = false) String title) {
        PageInfo<JpmsWork> list = IJpmsWorkService.findList(pageNumber, pageSize, title);
        return Result.succeedWith(list, 200, "cg");
    }


}
