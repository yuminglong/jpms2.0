package com.jiebao.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;

import com.jiebao.system.model.JpmsWork;


public interface IJpmsWorkService extends IService<JpmsWork> {

	/**
	 * 分页  查看所有工作动态
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	PageInfo<JpmsWork> findList(Integer pageNumber, Integer pageSize, String title);

}