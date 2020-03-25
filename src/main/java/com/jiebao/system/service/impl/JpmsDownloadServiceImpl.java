package com.jiebao.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiebao.system.mapper.JpmsDownloadMapper;
import com.jiebao.system.model.JpmsDownload;
import com.jiebao.system.service.IJpmsDownloadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
@SuppressWarnings("unchecked")
public class JpmsDownloadServiceImpl extends ServiceImpl<JpmsDownloadMapper, JpmsDownload> implements IJpmsDownloadService {

	@Override
	public PageInfo<JpmsDownload> findList(Integer pageNumber, Integer pageSize,String title) {
		Page page = PageHelper.startPage(pageNumber, pageSize);
		List<JpmsDownload> list= baseMapper.findAllList(title);
		PageInfo info = new PageInfo<>(page.getResult());
		return info;
	}

}
