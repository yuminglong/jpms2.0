package com.jiebao.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiebao.system.mapper.JpmsLawsMapper;
import com.jiebao.system.model.JpmsLaws;
import com.jiebao.system.service.IJpmsLawsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class JpmsLawsServiceImpl extends ServiceImpl<JpmsLawsMapper, JpmsLaws> implements IJpmsLawsService {

	@Override
	public PageInfo<JpmsLaws> findList(Integer pageNumber, Integer pageSize,String title) {
		Page page = PageHelper.startPage(pageNumber, pageSize);
		List<JpmsLaws>list= baseMapper.findList(title);
		PageInfo info = new PageInfo<>(page.getResult());
		return info;
	}
}
