package com.jiebao.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.jiebao.system.model.JpmsLaws;


public interface IJpmsLawsService extends IService<JpmsLaws> {

	/**
	 * 分页  查看所有政策法规
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	PageInfo<JpmsLaws> findList(Integer pageNumber, Integer pageSize, String title);

}