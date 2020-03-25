package com.jiebao.jpms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiebao.jpms.model.JpmsMessage;

import java.util.List;

public interface IJpmsMessageService extends IService<JpmsMessage> {


	/**
	 * 查询 消息 根据标题 分页搜索
	 * @param pageNumber 当前页数
	 * @param pageSize   显示数量
	 * @param title      标题
	 * @param userId     用户id
	 * @param status     状态
	 * @param folderId   文件夹id
	 * @return
	 */
	List<JpmsMessage> findList(Integer pageNumber, Integer pageSize, String title, Integer userId, Integer status, Integer folderId);


	/**
	 * 消息管理
	 * @param messageIds
	 * @param status
	 * @param folderId
	 * @return
	 */
	Boolean custodian(int[] messageIds, Integer status, Integer folderId);

	/**
	 * 发送消息
	 * @param jpmsMessage
	 * @return
	 */
	Boolean send(JpmsMessage jpmsMessage);


}