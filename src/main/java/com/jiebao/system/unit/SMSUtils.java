package com.jiebao.system.unit;

import com.alibaba.fastjson.JSONObject;

public class SMSUtils {

	static AbsRestClient InstantiationRestAPI() {
		return new JsonReqClient();
	}

	private static final String APP_ID = "feda85ae10b944b88f6cfbaab68bbf6a";
	private static final String ACCOUNT_SID = "420ee98cdca9986a3c323467dba9bedf";
	private static final String AUTH_TOKEN = "dbc4b99853bdbf7adf6fe8075c1fc990";

	/**
	 * @param isBatch    是否群发
	 * @param templateId 模板ID
	 * @param param      模板参数替换("参数值1,参数值2,参数值n")
	 * @param mobile     手机号码(群发用逗号隔开)
	 * @param uid        随便填
	 * @return
	 */
	public static boolean sendSMS(boolean isBatch, String templateId, String param, String mobile, String uid) {
		String result;
		if(isBatch) {
			result = InstantiationRestAPI().sendSmsBatch(ACCOUNT_SID, AUTH_TOKEN, APP_ID, templateId, param, mobile, uid);
		}else {
			result = InstantiationRestAPI().sendSms(ACCOUNT_SID, AUTH_TOKEN, APP_ID, templateId, param, mobile, uid);
		}
		JSONObject json = JSONObject.parseObject(result);
		if(json.get("code").equals("000000")) {
			System.out.println("验证码：" + param);
			return true;
		}
		return false;
	}

}
