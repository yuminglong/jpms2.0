package com.jiebao.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.jiebao.system.model.JpmsExchange;
import com.jiebao.system.model.JpmsInform;


public interface IJpmsExchangeService extends IService<JpmsExchange> {

	/**
	 * 分页  查看所有经验交流
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	PageInfo<JpmsExchange> findList(Integer pageNumber, Integer pageSize, String title);

}