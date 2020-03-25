package com.jiebao.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.jiebao.system.model.JpmsInform;
import com.jiebao.system.model.JpmsLaws;


public interface IJpmsInformService extends IService<JpmsInform> {

	/**
	 * 分页  查看所有通知公告
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	PageInfo<JpmsInform> findList(Integer pageNumber, Integer pageSize, String title);

}