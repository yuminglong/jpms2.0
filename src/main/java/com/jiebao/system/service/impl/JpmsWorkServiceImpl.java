package com.jiebao.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiebao.system.mapper.JpmsInformMapper;
import com.jiebao.system.mapper.JpmsWorkMapper;
import com.jiebao.system.model.JpmsInform;
import com.jiebao.system.model.JpmsWork;
import com.jiebao.system.service.IJpmsInformService;
import com.jiebao.system.service.IJpmsWorkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class JpmsWorkServiceImpl extends ServiceImpl<JpmsWorkMapper, JpmsWork> implements IJpmsWorkService {

	@Override
	public PageInfo<JpmsWork> findList(Integer pageNumber, Integer pageSize,String title) {
		Page page = PageHelper.startPage(pageNumber, pageSize);
		List<JpmsWork> list= baseMapper.findAllList(title);
		PageInfo info = new PageInfo<>(page.getResult());
		return info;
	}
}
