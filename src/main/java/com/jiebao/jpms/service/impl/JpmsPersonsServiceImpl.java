package com.jiebao.jpms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.jpms.mapper.JpmsPersonsMapper;
import com.jiebao.jpms.model.JpmsPersons;
import com.jiebao.jpms.service.IJpmsPersonsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class JpmsPersonsServiceImpl extends ServiceImpl<JpmsPersonsMapper, JpmsPersons> implements IJpmsPersonsService {


}
