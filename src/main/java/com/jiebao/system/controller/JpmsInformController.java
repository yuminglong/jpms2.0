package com.jiebao.system.controller;

import com.central.common.model.Result;
import com.github.pagehelper.PageInfo;
import com.jiebao.system.model.JpmsInform;
import com.jiebao.system.model.JpmsUser;
import com.jiebao.system.service.IJpmsInformService;
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
@RequestMapping("/system/inform")
@Api(tags = "通知公告API文档")
public class JpmsInformController {

	@Autowired
	private IJpmsInformService iJpmsInformService;

	@Autowired
	private IJpmsUserService jpmsUserService;

	@ApiOperation(value = "根据标题分页 搜索通知公告")
	@GetMapping("/findList")
	public Result findList(@RequestParam(defaultValue = "1") Integer pageNumber, @RequestParam(defaultValue = "20") Integer pageSize, @RequestParam(required = false) String title) {
		PageInfo<JpmsInform> list = iJpmsInformService.findList(pageNumber, pageSize, title);
		return Result.succeedWith(list, 200, "cg");
	}

	@ApiOperation(value = "根据ID查询通知公告")
	@GetMapping("/lookInform/{informId}")
	public JpmsInform lookInform(@PathVariable Integer informId) {
		return iJpmsInformService.getById(informId);
	}

	@ApiOperation(value = "新增")
	@PostMapping("/save")
	public Result addInform(JpmsInform jpmsInform, HttpSession session) {
		JpmsUser user = (JpmsUser) session.getAttribute("user");
		jpmsInform.setCreateUser(user.getRealName());
		String phone = "";
		//获取所有用户的手机
		List<JpmsUser> list = jpmsUserService.list();
		for(int i = 0; i < list.size(); i++) {
			phone += list.get(i).getMobile();
			if(i < list.size() - 1) {
				phone += ",";
			}
			//群发送短信
		}
		//SMSUtils.sendSMS(true, "507000", null, phone, "0");
		return iJpmsInformService.saveOrUpdate(jpmsInform) ? Result.succeedWith(jpmsInform, 200, "新增成功") : Result.failed("失败");
	}

	@ApiOperation(value = "修改")
	@PostMapping("/update")
	public Result update(JpmsInform jpmsInform) {
		return iJpmsInformService.saveOrUpdate(jpmsInform) ? Result.succeedWith(jpmsInform, 200, "修改成功") : Result.failed("失败");
	}

	@ApiOperation(value = "根据id删除通知公告")
	@GetMapping("/delete")
	public Result delete(Integer informId) {
		/*System.out.println("——>" + lawsId);
		JpmsLaws jpmsLaws = jpmsLawsService.getById(lawsId);
		jpmsLaws.setStatus(-1)*/
		;
		return iJpmsInformService.removeById(informId) ? Result.succeed("删除成功") : Result.failed("删除失败!");
	}

	@ApiOperation(value = "根据标题分页 搜索通知公告 5条")
	@GetMapping("/informList")
	public Result informList(@RequestParam(defaultValue = "1") Integer pageNumber, @RequestParam(defaultValue = "5") Integer pageSize, @RequestParam(required = false) String title) {
		PageInfo<JpmsInform> list = iJpmsInformService.findList(pageNumber, pageSize, title);
		return Result.succeedWith(list, 200, "cg");
	}


}
