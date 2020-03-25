package com.jiebao.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.jiebao.system.model.JpmsDownload;
import com.jiebao.system.model.JpmsInform;

import java.util.List;


public interface IJpmsDownloadService extends IService<JpmsDownload> {

	/**
	 * 分页  查看所有资料下载
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	PageInfo<JpmsDownload> findList(Integer pageNumber, Integer pageSize, String title);


}