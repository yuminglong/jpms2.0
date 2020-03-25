package com.jiebao.jpms.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.central.common.model.Result;
import com.jiebao.jpms.model.JpmsPersons;
import com.jiebao.jpms.model.JpmsPunit;
import com.jiebao.jpms.model.JpmsUnit;
import com.jiebao.jpms.service.IJpmsPersonsService;
import com.jiebao.jpms.service.IJpmsPunitService;
import com.jiebao.jpms.service.IJpmsUnitService;
import com.jiebao.system.model.JpmsUser;
import com.jiebao.system.service.IJpmsProposalService;
import com.jiebao.system.service.IJpmsUserService;
import com.jiebao.system.unit.SMSUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jiebao
 * @date 2019年7月12日14:24:20
 */
@Slf4j
@RestController
@RequestMapping("/front/user")
@Api(tags = "前台用户API文档")
public class JpmsUserController {

	@Autowired
	private IJpmsUserService iJpmsUserService;

	@Autowired
	private IJpmsUnitService ijpmsUnitService;

	@Autowired
	private IJpmsPunitService iJpmsPunitService;

	@Autowired
	private IJpmsPersonsService iJpmsPersonsService;

	@Autowired
	private IJpmsProposalService jpmsProposalService;

	@ApiOperation(value = "用户新增·修改")
	@PostMapping("/add")
	public Result addUser(@RequestBody JpmsUser user) {
		user.setPassword(new SimpleHash("MD5", user.getPassword(), user.getNickName(), 1024).toString());
		return iJpmsUserService.saveOrUpdate(user) ? Result.succeed(user, "用户新增成功") : Result.failed("用户新增失败");
	}


	@ApiOperation(value = "用户信息查看")
	@GetMapping("/find")
	public JpmsUser finduser(HttpSession session) {
		JpmsUser user = (JpmsUser) session.getAttribute("user");
		return iJpmsUserService.getById(user.getUserId());
	}

	@ApiOperation(value = "用户信息查看")
	@GetMapping("/findsel")
	public List<JpmsUser> findsel() {
		List<JpmsUser> list = iJpmsUserService.list();//全部用户
		for(int i = 0; i < list.size() - 1; i++) {
			if(list.get(i).getUserId() == 1) {
				list.remove(i);//去掉超级管理员
			}
		}
		return list;
	}

	@ApiOperation(value = "联名用户")
	@GetMapping("/jointuser")
	public String joint(@RequestParam(required = false) Integer proposalId) {
		/*List<JpmsUser> list = iJpmsUserService.list();//全部用户
		List jointuser = new ArrayList();
		if(proposalId != null) {
			List<JpmsPersons> list2 = iJpmsUserService.joint(proposalId);
			for(JpmsUser u: list) {
				for(JpmsPersons p: list2) {
					if(u.getUserId() == p.getUserId()) {
						jointuser.add(u.getUserId());
					}
				}
			}
		}*/
		List jointuser = new ArrayList();
		List<JpmsPersons> list2 = iJpmsUserService.joint(proposalId);
		for(JpmsPersons p: list2) {
			jointuser.add(p.getUserId());
		}
		//System.out.println(jointuser);
		String userData = JSON.toJSONString(jointuser);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("userData", userData);
		String result = JSON.toJSONString(jsonObject);
		return result;
	}

	@ApiOperation(value = "单位")
	@GetMapping("/jointpunit")
	public String jointpunit(@RequestParam(required = false) Integer proposalId, @RequestParam(required = false) Integer type) {
		List<JpmsUnit> list = ijpmsUnitService.list();//单位列表
		List jointpunit = new ArrayList();
		if(proposalId != null) {
			List<JpmsPunit> list2 = iJpmsUserService.jointpunit(proposalId, null, null);//提案相关单位
			for(JpmsUnit u: list) {
				for(JpmsPunit p: list2) {
					if(type != null) {
						if(u.getUnitId() == p.getUnitId() && p.getType() == type) {
							jointpunit.add(u.getUnitId());
						}
					}else if(u.getUnitId() == p.getUnitId()) {
						jointpunit.add(u.getUnitId());
					}

				}
			}
		}
		//System.out.println(jointuser);
		String userData = JSON.toJSONString(jointpunit);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("userData", userData);
		String result = JSON.toJSONString(jsonObject);
		return result;
	}

	@ApiOperation(value = "添加提案承办单位")
	@GetMapping("/addpunit")
	public Result addpunit(int proposalId, int[] units, int type) {
		List<JpmsPunit> list = iJpmsUserService.jointpunit(proposalId, type, null);//提案相关单位
		//jpmsProposalService.deletepunit(proposalId); //删除提案原单位  重新分配
		for(int a: units) {//现有 原没有 新增操作
			boolean isend = false;//没有
			for(JpmsPunit b: list) {
				if(a == b.getUnitId()) {
					isend = true;//有则不做操作
				}
			}
			//没有则新增
			if(!isend) {
				JpmsPunit jpmsPunit = new JpmsPunit(0, proposalId, a, type, null, null, null,null);
				iJpmsPunitService.save(jpmsPunit);
			}

		}

		for(JpmsPunit b: list) {//原有 现没有 删除操作
			boolean isend = false;//没有
			for(int a: units) {
				if(a == b.getUnitId()) {
					isend = true;
				}
			}
			if(!isend) {
				iJpmsPunitService.removeById(b.getPunitId());
			}

		}


		/*List<Integer> send = new ArrayList<>();
		for(int i = 0; i < units.length; i++) {
			JpmsPunit jpmsPunit = new JpmsPunit(0, proposalId, units[i], type, null, null, null);//1建议办理单位 2办理单位

			if(type == 3 || type == 4) {//主办与会办单位
				for(JpmsPunit p: list) {
					if(p.getType() == 3 || p.getType() == 4) {
						if(units[i] == p.getUnitId()) {//并集
							send.add(units[i]);
						}
					}
				}
			}
			iJpmsPunitService.save(jpmsPunit);
		}
		if(type == 3 || type == 4) {
			for(int i = 0; i < units.length; i++) {
				String phone = "";
				boolean flag = true;
				for(int j: send) {
					if(units[i] == j) {
						flag = false;
					}
				}
				if(flag) {
					//给单位 发送办理提案 短信
					phone = ijpmsUnitService.getById(units[i]).getPhone();//获取单位负责人的手机
					//System.out.println("单位负责人手机" + phone);
					//SMSUtils.sendSMS(true, "497409", null, phone, "0");//群发送短信
				}
			}
		}*/

		return Result.succeed(1, "");
	}

	@ApiOperation(value = "添加提案联名人")
	@PostMapping("/addPersons")
	public Result addPersons(int proposalId, int[] users) {
		//先删除原来联名提案人
		jpmsProposalService.deletepersons(proposalId);
		for(int i: users) {
			JpmsPersons jpmsPersons = new JpmsPersons(0, i, proposalId, null,null);
			iJpmsPersonsService.saveOrUpdate(jpmsPersons);
		}
		return Result.succeed(1, "");
	}

	@ApiOperation(value = "用户登陆")
	@GetMapping("/login")
	public Result login(@RequestParam String nickName, @RequestParam String password, HttpSession session) {
		//1.获取Subject
		Subject subject = SecurityUtils.getSubject();
		//2.封装用户数据
		UsernamePasswordToken token = new UsernamePasswordToken(nickName, password);
		//3.执行登录方法
		try {
			subject.login(token);
			JpmsUser user = iJpmsUserService.findByName(token.getUsername());
			session.setAttribute("user", user);
			if(user.getStatus() == 1) {
				if(user.getType() == 1) {//单位
					if(user.getUnitId() != null) {
						JpmsUnit unit = ijpmsUnitService.getById(user.getUnitId());
						if(unit != null) {
							if(unit.getStatus() == 0) {
								return Result.succeed(-1, "您的单位已停用");
							}else if(unit.getStatus() == -1) {
								subject.logout();
								return Result.succeed(-1, "单位不存在,联系管理员重新分配单位");
							}
						}else {
							return Result.succeed(-1, "单位不存在,联系管理员重新分配单位");
						}
					}
				}
				return Result.succeed(user.getType(), "登陆成功");
			}else if(user.getStatus() == 0) {
				subject.logout();
				return Result.succeed(-1, "账号已停用");
			}else if(user.getStatus() == -1) {
				subject.logout();
				return Result.succeed(-1, "账号或密码错误");
			}
			subject.logout();
			return Result.succeed(-1, "账号或密码错误");
		}catch(UnknownAccountException e) {
			return Result.succeed(-1, "账号或密码错误");
		}catch(IncorrectCredentialsException e) {
			return Result.succeed(-1, "账号或密码错误");
		}
	}

	@ApiOperation(value = "注销")
	@GetMapping("/logout")
	public Result logout() {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return Result.succeed(1, "退出成功");
	}


}
