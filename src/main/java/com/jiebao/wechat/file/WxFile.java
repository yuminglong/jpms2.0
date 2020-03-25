package com.jiebao.wechat.file;

import com.central.common.model.Result;
import com.github.pagehelper.PageInfo;
import com.jiebao.jpms.controller.JpmsUserController;
import com.jiebao.jpms.model.JpmsAppendix;
import com.jiebao.jpms.model.JpmsProposal;
import com.jiebao.jpms.model.JpmsPunit;
import com.jiebao.jpms.service.IJpmsAppendixService;
import com.jiebao.jpms.service.IJpmsUnitService;
import com.jiebao.system.model.*;
import com.jiebao.system.service.*;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wx")
public class WxFile {

	@Autowired
	private IJpmsLawsService jpmsLawsService;

	@Autowired
	private IJpmsExchangeService iJpmsExchangeService;

	@Autowired
	private IJpmsProposalService jpmsProposalService;

	@Autowired
	private IJpmsInformService iJpmsInformService;

	@Autowired
	private IJpmsDownloadService iJpmsDownloadService;

	@Autowired
	private IJpmsWorkService IJpmsWorkService;
	@Autowired
	private IJpmsUserService jpmsUserService;

	@Autowired
	private IJpmsUnitService jpmsUnitService;

	@Autowired
	private IJpmsAppendixService jpmsAppendixService;


	@ApiOperation(value = "查看全部提案")
	@PostMapping("/findList")
	public Result findList(@RequestBody JpmsUser user) {
		// value 当冲 pageSize
		//name 当冲 cause
		if(user.getValue() == null) {
			user.setValue(5);
		}
		System.out.println("user" + user.getType() + "pageSize" + user.getValue());
		PageInfo<JpmsProposal> list = null;
		switch(user.getType()) {
			case 1:// 1单位
				list = jpmsUnitService.findList(1, user.getValue(), user.getUnitId(), user.getName(), null);
				break;
			case 2:  // 2委员
				list = jpmsProposalService.membersProposal(1, user.getValue(), user.getUserId(), user.getName(), null);
				break;
			/*case 3://督察室
				break;
			case 4://4提案委
				break;*/
			default:
				list = jpmsProposalService.findList(1, user.getValue(), null, null, null);
		}
		return Result.succeedWith(list, 200, "cg");
	}

	@ApiOperation(value = "根据id查看提案")
	@GetMapping("/lookProposal/{proposalId}")
	public JpmsProposal lookProposa(@PathVariable Integer proposalId) {
		return jpmsProposalService.findById(proposalId);
	}

	@ApiOperation(value = "政策法规")
	@GetMapping("/laws/{pageSize}")
	public Result laws(@PathVariable Integer pageSize) {
		PageInfo<JpmsLaws> list = jpmsLawsService.findList(1, pageSize, null);
		for(JpmsLaws j: list.getList()) {
			j.setFileId(j.getLawsId());
		}
		return Result.succeedWith(list, 200, "cg");
	}

	@ApiOperation(value = "根据ID查询政策法规")
	@GetMapping("/lookLaws/{lawsId}")
	public JpmsLaws lookLaws(@PathVariable Integer lawsId) {
		return jpmsLawsService.getById(lawsId);
	}

	@ApiOperation(value = "新增")
	@PostMapping("/addLaws")
	public Result addLaws(@RequestBody JpmsLaws jpmsLaws) {
		return jpmsLawsService.saveOrUpdate(jpmsLaws) ? Result.succeedWith(jpmsLaws, 200, "新增成功") : Result.failed("失败");
	}


	@ApiOperation(value = "经验交流")
	@GetMapping("/exchange/{pageSize}")
	public Result exchange(@PathVariable Integer pageSize) {
		//List<JpmsExchange> list = iJpmsExchangeService.list();
		PageInfo<JpmsExchange> list = iJpmsExchangeService.findList(1, pageSize, null);
		for(JpmsExchange j: list.getList()) {
			j.setFileId(j.getExchangeId());
		}
		return Result.succeedWith(list, 200, "cg");
	}

	@ApiOperation(value = "根据ID查询经验交流")
	@GetMapping("/lookExchange/{exchangeId}")
	public JpmsExchange lookExchange(@PathVariable Integer exchangeId) {
		return iJpmsExchangeService.getById(exchangeId);
	}

	@ApiOperation(value = "新增经验交流")
	@PostMapping("/addExchange")
	public Result addExchange(@RequestBody JpmsExchange jpmsExchange) {
		return iJpmsExchangeService.saveOrUpdate(jpmsExchange) ? Result.succeedWith(jpmsExchange, 200, "新增成功") : Result.failed("失败");
	}

	@ApiOperation(value = "通知公告")
	@GetMapping("/inform/{pageSize}")
	public Result inform(@PathVariable Integer pageSize) {
		PageInfo<JpmsInform> list = iJpmsInformService.findList(1, pageSize, null);
		for(JpmsInform j: list.getList()) {
			j.setFileId(j.getInformId());
		}
		return Result.succeedWith(list, 200, "cg");
	}

	@ApiOperation(value = "根据ID查询通知公告")
	@GetMapping("/lookInform/{informId}")
	public JpmsInform lookInform(@PathVariable Integer informId) {
		return iJpmsInformService.getById(informId);
	}

	@ApiOperation(value = "新增")
	@PostMapping("/addInform")
	public Result addInform(JpmsInform jpmsInform) {
		/*String phone = "";
		//获取所有用户的手机
		List<JpmsUser> list = jpmsUserService.list();
		for(int i = 0; i < list.size(); i++) {
			phone += list.get(i).getMobile();
			if(i < list.size() - 1) {
				phone += ",";
			}
			//群发送短信
		}
		//SMSUtils.sendSMS(true, "507000", null, phone, "0");*/
		return iJpmsInformService.saveOrUpdate(jpmsInform) ? Result.succeedWith(jpmsInform, 200, "新增成功") : Result.failed("失败");
	}

	@ApiOperation(value = "工作动态")
	@GetMapping("/work/{pageSize}")
	public Result work(@PathVariable Integer pageSize) {
		PageInfo<JpmsWork> list = IJpmsWorkService.findList(1, pageSize, null);
		for(JpmsWork j: list.getList()) {
			j.setFileId(j.getWorkId());
		}
		return Result.succeedWith(list, 200, "cg");
	}

	@ApiOperation(value = "根据ID查询工作动态")
	@GetMapping("/lookWork/{workId}")
	public JpmsWork lookWork(@PathVariable Integer workId) {
		return IJpmsWorkService.getById(workId);
	}

	@ApiOperation(value = "新增工作动态")
	@PostMapping("/addWork")
	public Result addWork(JpmsWork jpmsWork) {
		return IJpmsWorkService.saveOrUpdate(jpmsWork) ? Result.succeedWith(jpmsWork, 200, "新增成功") : Result.failed("失败");
	}

	@ApiOperation(value = "资料下载")
	@GetMapping("/download/{pageSize}")
	public Result download(@PathVariable Integer pageSize) {
		PageInfo<JpmsDownload> list = iJpmsDownloadService.findList(1, pageSize, null);
		for(JpmsDownload j: list.getList()) {
			j.setFileId(j.getDownloadId());
		}
		return Result.succeedWith(list, 200, "cg");
	}

	@ApiOperation(value = "根据ID查询资料下载")
	@GetMapping("/lookDownload/{downloadId}")
	public JpmsDownload lookDownload(@PathVariable Integer downloadId) {
		return iJpmsDownloadService.getById(downloadId);
	}

	@ApiOperation(value = "用户信息查看")
	@GetMapping("/findbyId/{userId}")
	public JpmsUser findbyId(@PathVariable Integer userId) {
		return jpmsUserService.getById(userId);
	}

	@ApiOperation(value = "用户修改")
	@PostMapping("/update")
	public JpmsUser update(@RequestBody JpmsUser user) {
		return jpmsUserService.saveOrUpdate(user) ? user : null;
	}

	@ApiOperation(value = "未办理条数")
	@GetMapping("/listStrip/{userId}/{type}")
	public Result listStrip(@PathVariable Integer userId, @PathVariable Integer type) {
		Result p = jpmsProposalService.listStrip(userId, type);
		return p;
	}

	@ApiOperation(value = "新增提案")
	@PostMapping("/save")
	public synchronized Result save(@RequestBody JpmsProposal jpmsProposal) {
		JpmsUser user = jpmsUserService.getById(jpmsProposal.getUserId());
		//给提案委发送短信
		List<JpmsUser> jpmsUserList = jpmsUserService.userType(4);
		for(JpmsUser j: jpmsUserList) {
			//System.out.println("提案委手机" + j.getMobile());
			//SMSUtils.sendSMS(false, "497409", "null", j.getMobile(), "0");
		}
		jpmsProposal.setUserId(user.getUserId());
		jpmsProposal.setLeader(user.getRealName());//主导人
		jpmsProposal.setMobile(user.getMobile());//手机
		return jpmsProposalService.saveOrUpdate(jpmsProposal) ? Result.succeedWith(jpmsProposal, 200, "新增成功") : Result.failed("新增失败");
	}


	@ApiOperation(value = "登陆")
	@GetMapping("/login/{openid}")
	public JpmsUser login(@PathVariable String openid) {
		System.out.println("openid-->" + openid);
		JpmsUser user = jpmsUserService.wxlogin(openid);
		if(user == null) {//数据库wxopenid  不存在
			return null;
		}
		return user;
	}

	@ApiOperation(value = "账号，密码登陆")
	@PostMapping("/islogin")
	public Result islogin(@RequestBody JpmsUser user) {
		//登陆
		Subject subject = SecurityUtils.getSubject();
		//2.封装用户数据
		UsernamePasswordToken token = new UsernamePasswordToken(user.getNickName(), user.getPassword());
		//3.执行登录方法
		try {
			subject.login(token);
			JpmsUser user2 = jpmsUserService.findByName(token.getUsername());
			System.out.println("opid" + user.getWxopenid());
			user2.setWxopenid(user.getWxopenid());
			jpmsUserService.saveOrUpdate(user2);
			return Result.succeed(user2, "登陆成功");
		}catch(UnknownAccountException e) {
			return Result.succeed(-1, "账号或密码错误");
		}
	}

	@ApiOperation(value = "文件动态下载")
	@GetMapping("file/{downloadId}")
	@ResponseBody
	private void file(HttpServletResponse response, @PathVariable Integer downloadId) {
		JpmsDownload jpmsDownload = iJpmsDownloadService.getById(downloadId);
		String downloadFilePath = jpmsDownload.getFileUrl();//被下载的文件在服务器中的路径,
		String fileName = jpmsDownload.getTitle();//被下载文件的名称
		File file = new File(downloadFilePath);
		if(file.exists()) {
			//response.setContentType("application/force-download");// 设置强制下载不打开  
			try {
				response.addHeader("Content-Disposition", "attachment;fileName=" + new String(fileName.getBytes("UTF-8"), "ISO8859-1"));
			}catch(UnsupportedEncodingException e) {
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
				while(i != -1) {
					outputStream.write(buffer, 0, i);
					i = bis.read(buffer);
				}
				return;
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				if(bis != null) {
					try {
						bis.close();
					}catch(IOException e) {
						e.printStackTrace();
					}
				}
				if(fis != null) {
					try {
						fis.close();
					}catch(IOException e) {
						e.printStackTrace();
					}
				}
			}
		}else {
			return;
		}
		return;
	}


	@ApiOperation(value = "提案附件文件下载")
	@GetMapping("/downloadFile/{appendixId}")
	@ResponseBody
	private void downloadFile(@PathVariable Integer appendixId, HttpServletResponse response) {
		//JpmsAppendix appendix = jpmsProposalService.appendix(proposalId);
		JpmsAppendix appendix = jpmsAppendixService.getById(appendixId);
		String downloadFilePath = appendix.getFileName();//被下载的文件在服务器中的路径,
		String fileName = appendix.getAppendixName();//被下载文件的名称
		//System.out.println(fileName);
		File file = new File(downloadFilePath);
		if(file.exists()) {
		//	response.setContentType("application/force-download");// 设置强制下载不打开  
			try {
				response.addHeader("Content-Disposition", "attachment;fileName=" + new String(fileName.getBytes("GB2312"), "ISO8859-1"));
			}catch(UnsupportedEncodingException e) {
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
				while(i != -1) {
					outputStream.write(buffer, 0, i);
					i = bis.read(buffer);
				}
				return;
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				if(bis != null) {
					try {
						bis.close();
					}catch(IOException e) {
						e.printStackTrace();
					}
				}
				if(fis != null) {
					try {
						fis.close();
					}catch(IOException e) {
						e.printStackTrace();
					}
				}
			}
		}else {
			return;
		}
		return;
	}

	@ApiOperation(value = "详情页面")
	@GetMapping("detaile/{proposalId}")
	public Map detaile(@PathVariable Integer proposalId) {
		Map<String, Object> map = new HashMap();

		for(int i = 1; i < 5; i++) {
			if(i == 3) {//单位回复
				List<JpmsAppendix> zlist = new ArrayList<>();//主办
				List<JpmsAppendix> hlist = new ArrayList<>();//会办
				List<JpmsPunit> listz = jpmsUserService.jointpunit(proposalId, 3, null);//主办单位
				List<JpmsPunit> listh = jpmsUserService.jointpunit(proposalId, 4, null);//会办单位
				List<JpmsAppendix> list = jpmsProposalService.appendix(proposalId, 3);//单位回复附件

				for(JpmsAppendix j: list) {
					for(JpmsPunit p: listz) {//主办
						if(j.getUnitId() == p.getUnitId()) {
							j.setAnswer(p.getAnswer());
							j.setAnswerAfter(p.getAnswerAfter());
							j.setUnitName(p.getUnitName());
							zlist.add(j);
						}
					}
					for(JpmsPunit p: listh) {//会办
						if(j.getUnitId() == p.getUnitId()) {
							j.setAnswer(p.getAnswer());
							j.setAnswerAfter(p.getAnswerAfter());
							j.setUnitName(p.getUnitName());
							hlist.add(j);
						}
					}
				}
				//model.addAttribute("JpmsAppendixz", zlist);
				map.put("JpmsAppendixz", list);
				//model.addAttribute("JpmsAppendixh", hlist);
				map.put("JpmsAppendixh", list);

			}else {
				List<JpmsAppendix> list = jpmsProposalService.appendix(proposalId, i);//提案相关附件
				if(i == 1) {//提案
					//model.addAttribute("JpmsAppendione", list);
					map.put("JpmsAppendione", list);
				}else if(i == 2) {//提案委
				//	model.addAttribute("JpmsAppendixtwo", list);
					map.put("JpmsAppendixtwo", list);
				}else if(i == 4) {//满意度
					//model.addAttribute("JpmsAppendixthree", list);
					map.put("JpmsAppendixthree", list);
				}
			}
			List<JpmsAppendix> two = new ArrayList<>();//两办意见
			List<JpmsAppendix> zf = jpmsProposalService.appendix(proposalId, 5);
			List<JpmsAppendix> sw = jpmsProposalService.appendix(proposalId, 6);
			for(JpmsAppendix p: zf) {
				two.add(p);
			}
			for(JpmsAppendix p: sw) {
				two.add(p);
			}
			//model.addAttribute("two", two);
			map.put("two", two);

		}

	return map;
	}


}
