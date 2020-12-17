package com.jiebao.system.controller;

import com.central.common.model.Result;
import com.github.pagehelper.PageInfo;
import com.jiebao.system.model.JpmsExchange;
import com.jiebao.system.model.JpmsUser;
import com.jiebao.system.service.IJpmsExchangeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


@Slf4j
@RestController
@RequestMapping("/system/exchange")
@Api(tags = "经验交流API文档")
public class JpmsExchangeController {

	@Autowired
	private IJpmsExchangeService iJpmsExchangeService;

	@ApiOperation(value = "根据标题分页 搜索经验交流")
	@GetMapping("/findList")

	public Result findList(@RequestParam(defaultValue = "1") Integer pageNumber, @RequestParam(defaultValue = "20") Integer pageSize, @RequestParam(required = false) String title) {
		PageInfo<JpmsExchange> list = iJpmsExchangeService.findList(pageNumber, pageSize, title);
		return Result.succeedWith(list, 200, "cg");
	}

	@ApiOperation(value = "根据ID查询经验交流")
	@GetMapping("/lookExchange/{exchangeId}")
	public JpmsExchange lookExchange(@PathVariable Integer exchangeId) {
		return iJpmsExchangeService.getById(exchangeId);
	}

	@ApiOperation(value = "新增")
	@PostMapping("/save")
	public Result addExchange(JpmsExchange jpmsExchange, HttpSession session) {
		JpmsUser user = (JpmsUser) session.getAttribute("user");
        jpmsExchange.setCreateUser(user.getRealName());
		return iJpmsExchangeService.saveOrUpdate(jpmsExchange) ? Result.succeedWith(jpmsExchange, 200, "新增成功") : Result.failed("失败");
	}

	@ApiOperation(value = "修改")
	@PostMapping("/update")
	public Result update(JpmsExchange jpmsExchange) {
		return iJpmsExchangeService.saveOrUpdate(jpmsExchange) ? Result.succeedWith(jpmsExchange, 200, "修改成功") : Result.failed("失败");
	}

	@ApiOperation(value = "根据id删除经验交流")
	@PostMapping("/delete")
	public Result delete(Integer exchangeId) {
		/*System.out.println("——>" + lawsId);
		JpmsLaws jpmsLaws = jpmsLawsService.getById(lawsId);
		jpmsLaws.setStatus(-1)*/;
		return iJpmsExchangeService.removeById(exchangeId) ? Result.succeed("删除成功") : Result.failed("删除失败!");
	}
    @ApiOperation(value = "根据标题分页 搜索经验交流 5条")
    @GetMapping("/exchangeList")
    public Result exchangeList(@RequestParam(defaultValue = "1") Integer pageNumber, @RequestParam(defaultValue = "5") Integer pageSize, @RequestParam(required = false) String title) {
        PageInfo<JpmsExchange> list = iJpmsExchangeService.findList(pageNumber, pageSize, title);
        return Result.succeedWith(list, 200, "cg");
    }


}
