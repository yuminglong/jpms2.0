package com.jiebao.jpms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiebao.jpms.model.JpmsFolder;

import java.util.List;

public interface IJpmsFolderService extends IService<JpmsFolder> {

	/**
	 * 查询用户自建的文件夹
	 * @param userId
	 * @return
	 */
	List<JpmsFolder> seltfol(Integer userId);
}