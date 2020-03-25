package com.jiebao.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiebao.system.mapper.JpmsExchangeMapper;
import com.jiebao.system.model.JpmsExchange;
import com.jiebao.system.service.IJpmsExchangeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class JpmsExchangeServiceImpl extends ServiceImpl<JpmsExchangeMapper, JpmsExchange> implements IJpmsExchangeService {

	@Override
	public PageInfo<JpmsExchange> findList(Integer pageNumber, Integer pageSize, String title) {
		Page page = PageHelper.startPage(pageNumber, pageSize);
		List<JpmsExchange> list= baseMapper.findAllList(title);
		PageInfo info = new PageInfo<>(page.getResult());
		return info;
	}
}
