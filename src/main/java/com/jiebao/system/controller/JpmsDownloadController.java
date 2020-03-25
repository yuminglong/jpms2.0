package com.jiebao.system.controller;

import com.central.common.model.Result;
import com.github.pagehelper.PageInfo;
import com.jiebao.system.model.JpmsDownload;
import com.jiebao.system.model.JpmsUser;
import com.jiebao.system.service.IJpmsDownloadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/system/download")
@Api(tags = "资料下载API文档")
public class JpmsDownloadController {

	@Autowired
	private IJpmsDownloadService iJpmsDownloadService;

	@ApiOperation(value = "根据标题分页 搜索资料")
	@GetMapping("/findList")
	public Result findList(@RequestParam(defaultValue = "1") Integer pageNumber, @RequestParam(defaultValue = "20") Integer pageSize, @RequestParam(required = false) String title) {
		PageInfo<JpmsDownload> list = iJpmsDownloadService.findList(pageNumber, pageSize, title);
		return Result.succeedWith(list, 200, "cg");
	}

	@ApiOperation(value = "根据ID查询资料下载")
	@GetMapping("/lookDownload/{downloadId}")
	public JpmsDownload lookDownload(@PathVariable Integer downloadId) {
		return iJpmsDownloadService.getById(downloadId);
	}


	@ApiOperation(value = "修改")
	@GetMapping("/update")
	public Result update(JpmsDownload jpmsDownload) {
		return iJpmsDownloadService.saveOrUpdate(jpmsDownload) ? Result.succeedWith(jpmsDownload, 200, "修改成功") : Result.failed("失败");
	}

	@ApiOperation(value = "根据id删除资料")
	@PostMapping("/delete")
	public Result delete(Integer downloadId) {
		JpmsDownload download = iJpmsDownloadService.getById(downloadId);
		File file = new File(download.getFileUrl());
		if(file.exists()) {
			file.delete();
		}
		return iJpmsDownloadService.removeById(downloadId) ? Result.succeed("删除成功") : Result.failed("删除失败!");
	}

	/**
	 * @param file
	 * @return
	 */
	@ApiOperation(value = "文件上传")
	@PostMapping("upload")
	@ResponseBody
	public synchronized Result upload(@RequestParam("file") MultipartFile file, JpmsDownload jpmsDownload, HttpSession session) {
		String pathString = null;
		if(file != null) {
			pathString = "D:/jpms/jpmsupload/download/" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + file.getOriginalFilename();
		}
		//System.out.println(pathString);
		jpmsDownload.setFileUrl(pathString);
		jpmsDownload.setTitle(file.getOriginalFilename());
		JpmsUser user = (JpmsUser) session.getAttribute("user");
		jpmsDownload.setCreateUser(user.getRealName());
		iJpmsDownloadService.saveOrUpdate(jpmsDownload);
		try {
			File files = new File(pathString);

			if(!files.getParentFile().exists()) {
				files.getParentFile().mkdirs();
			}
			file.transferTo(files);
		}catch(Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Result.succeedWith(jpmsDownload.getDownloadId(), 200, "新增成功");
	}

	@ApiOperation(value = "文件下载")
	@GetMapping("file/{downloadId}")
	@ResponseBody
	private void file(HttpServletResponse response, @PathVariable Integer downloadId) {
		JpmsDownload jpmsDownload = iJpmsDownloadService.getById(downloadId);
		String downloadFilePath = jpmsDownload.getFileUrl();//被下载的文件在服务器中的路径,
		String fileName = jpmsDownload.getTitle();//被下载文件的名称
		File file = new File(downloadFilePath);
		if(file.exists()) {
			response.setContentType("application/force-download");// 设置强制下载不打开  
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

	@ApiOperation(value = "根据标题分页 搜索资料")
	@GetMapping("/findLists")
	public Result findLists(@RequestParam(defaultValue = "1") Integer pageNumber, @RequestParam(defaultValue = "5") Integer pageSize, @RequestParam(required = false) String title) {
		PageInfo<JpmsDownload> list = iJpmsDownloadService.findList(pageNumber, pageSize, title);
		return Result.succeedWith(list, 200, "cg");
	}

}
