package com.jiebao.jpms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.google.common.primitives.Ints;
import com.jiebao.jpms.mapper.JpmsMessageMapper;
import com.jiebao.jpms.model.JpmsFolder;
import com.jiebao.jpms.model.JpmsMessage;
import com.jiebao.jpms.service.IJpmsMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Slf4j
@Service
public class JpmsMessageServiceImpl extends ServiceImpl<JpmsMessageMapper, JpmsMessage> implements IJpmsMessageService {


	@Override
	public Boolean send(JpmsMessage jpmsMessage) {
		if(baseMapper.insert(jpmsMessage) > 0) {
			//接收者
			baseMapper.insert(new JpmsMessage(jpmsMessage.getMessageId() + 1, jpmsMessage.getSendId(), jpmsMessage.getRecId(), jpmsMessage.getCreateAt(), 2, jpmsMessage.getTitle(), jpmsMessage.getContent(), null));
			return true;
		}else {
			jpmsMessage.setStatus(1);
			return false;
		}
	}


	@Override
	public Boolean custodian(int[] messageIds, Integer status, Integer folderId) {
		if(folderId != null) {//加入文件夹
			for(int messageId: messageIds) {
				JpmsMessage jpmsMessage = baseMapper.selectById(messageId);
				jpmsMessage.setFolderId(folderId);
				if(baseMapper.updateById(jpmsMessage) < 1) {
					return false;
				}
			}
			return true;
		}else if(status != null) {
			if(status == 0) return baseMapper.deleteBatchIds(Ints.asList(messageIds)) > 0 ? true : false;//回收站删除
			else {
				for(int messageId: messageIds) {//状态修改
					JpmsMessage jpmsMessage = baseMapper.selectById(messageId);
					jpmsMessage.setStatus(status);
					if(baseMapper.updateById(jpmsMessage) < 1) {
						return false;
					}
				}
			}
		}
		return true;
	}

	@Override
	public List<JpmsMessage> findList(Integer pageNumber, Integer pageSize, String title, Integer userId, Integer status, Integer folderId) {
		/**
		 * 发件箱· 发送者 4
		 * 收件箱·接收者  2,3
		 * 草搞箱·发送者 1
		 * 回收站·发送者&接收者 -1
		 */
		PageHelper.startPage(pageNumber, pageSize);
		return baseMapper.findList(title, userId, status, folderId);


	}


}
