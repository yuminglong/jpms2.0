package com.jiebao.jpms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.jpms.mapper.JpmsFolderMapper;
import com.jiebao.jpms.model.JpmsFolder;
import com.jiebao.jpms.service.IJpmsFolderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class JpmsFolderServiceImpl extends ServiceImpl<JpmsFolderMapper, JpmsFolder> implements IJpmsFolderService {

	@Override
	public List<JpmsFolder> seltfol(Integer userId) {
		return baseMapper.seltfol(userId);
	}

}
