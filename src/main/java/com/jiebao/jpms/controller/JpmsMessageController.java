package com.jiebao.jpms.controller;

import com.central.common.model.Result;
import com.jiebao.jpms.model.JpmsFolder;
import com.jiebao.jpms.model.JpmsMessage;
import com.jiebao.jpms.service.IJpmsFolderService;
import com.jiebao.jpms.service.IJpmsMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/front/message")
@Api(tags = "内部消息API文档")
public class JpmsMessageController {

	@Autowired
	private IJpmsMessageService jpmsMessageService;

	@Autowired
	private IJpmsFolderService jpmsFolderService;


	@ApiOperation(value = "发送消息")
	@PostMapping("/send")
	public Result send(@RequestBody JpmsMessage jpmsMessage) {
		return jpmsMessageService.send(jpmsMessage) ? Result.succeed(jpmsMessage, "发送成功") : Result.failed(jpmsMessage, "发送失败,已存入草稿箱");
	}

	@ApiOperation(value = "消息管理(加入文件夹 及 修改消息状态 及 回收站的删除)")
	@GetMapping("/custodian")
	public boolean custodian(@RequestParam int[] messageIds, @RequestParam(required = false) Integer status, @RequestParam(required = false) Integer folderId) {
		return jpmsMessageService.custodian(messageIds, status, folderId);
	}

	@ApiOperation(value = "删除消息")
	@GetMapping("/delMessage")
	public boolean delMessage(@RequestParam int[] messageIds) {
		for(int messageId: messageIds) {
			JpmsMessage jpmsMessage = jpmsMessageService.getById(messageId);
			jpmsMessage.setStatus(-1);
			if(!jpmsMessageService.saveOrUpdate(jpmsMessage)) {
				return false;
			}
		}
		return true;
	}


	@ApiOperation(value = "自建文件夹")
	@PostMapping("/folder")
	public Result folder(@RequestBody JpmsFolder jpmsFolder) {
		return jpmsFolderService.saveOrUpdate(jpmsFolder) ? Result.succeed(jpmsFolder, "创建成功") : Result.failed("创建失败");
	}

	@ApiOperation(value = "删除文件夹")
	@PostMapping("/delete")
	public Result delfol(@RequestParam Integer folderId) {
		return jpmsFolderService.removeById(folderId) ? Result.succeed("删除成功") : Result.failed("删除失败");
	}


	@ApiOperation(value = "查询用户文件夹")
	@PostMapping("/seltfol")
	public List<JpmsFolder> seltfol(@RequestParam Integer userId) {
		return jpmsFolderService.seltfol(userId);
	}

	@ApiOperation(value = "发件箱·收件箱·草搞箱·回收站·自建文件夹 根据标题搜索消息")
	@GetMapping("/box")
	public List<JpmsMessage> box(@RequestParam(defaultValue = "1") Integer pageNumber, @RequestParam(defaultValue = "20") Integer pageSize, @RequestParam(required = false) String title, @RequestParam Integer userId, @RequestParam(required = false) Integer status, @RequestParam(required = false) Integer folderId) {
		return jpmsMessageService.findList(pageNumber, pageSize, title, userId, status, folderId);
	}


}
