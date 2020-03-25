package com.jiebao.system.controller;

import com.central.common.model.Result;
import com.jiebao.system.service.IJpmsProposalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/system/supervise")
@Api(tags = "督察室API文档")
@RequiresRoles("supervise")
public class JpmsSuperviseController {

	@Autowired
	private IJpmsProposalService jpmsProposalService;

	@ApiOperation(value = "调整办理单位")
	@GetMapping("/adjust")
	public Result adjust(@RequestParam Integer proposalId, @RequestParam(defaultValue = "1") Integer status, @RequestParam(required = false) Integer unitId) {
		return jpmsProposalService.adjust(proposalId, status, unitId);
	}

	@ApiOperation(value = "审核延期交办")
	@GetMapping("/postpone/{proposalId}/{status}")
	public Result postpone(@PathVariable Integer proposalId, @PathVariable Integer status) {
		return jpmsProposalService.postpone(proposalId, status);
	}

}
