package com.jiebao.jpms.util;

import cn.afterturn.easypoi.word.WordExportUtil;
import cn.hutool.core.lang.Assert;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public  class WordFileUtil {
	/**
	 * 导出word
	 * <p>第一步生成替换后的word文件，只支持docx</p>
	 * <p>第二步下载生成的文件</p>
	 * <p>第三步删除生成的临时文件</p>
	 * 模版变量中变量格式：{{foo}}
	 * @param templatePath word模板地址
	 * @param temDir 生成临时文件存放地址
	 * @param fileName 文件名
	 * @param params 替换的参数
	 */
	public static void exportWord(String templatePath, String temDir, String fileName, Map<String, Object> params) {
		Assert.notNull(templatePath,"模板路径不能为空");
		Assert.notNull(temDir,"临时文件路径不能为空");
		Assert.notNull(fileName,"导出文件名不能为空");
		Assert.isTrue(fileName.endsWith(".docx"),"word导出请使用docx格式");
		if (!temDir.endsWith("/")){
			temDir = temDir + File.separator;
		}
		File dir = new File(temDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		CustomXWPFDocument document = null;
		try {
			XWPFDocument doc = WordExportUtil.exportWord07(templatePath, params);

			String tmpPath = temDir + fileName;
			FileOutputStream fos = new FileOutputStream(tmpPath);
			doc.write(fos);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// delAllFile(temDir);//这一步看具体需求，要不要删
		}

	}

	public static void main(String[] args){
		String templatePath = "D:/example/model.docx";
		String temDir = "D:/example/test/";
		String fileName = "11.docx";
		Map<String, Object> params = new HashMap<>();
		params.put("name","mrssjhut");
		params.put("number","51");
		//需要进行动态生成的信息
		List<Object> mapList = new ArrayList<Object>();

		//第一个动态生成的数据列表
		List<String[]> list01 = new ArrayList<String[]>();
		list01.add(new String[]{"A","11111111111","22","22"});
		list01.add(new String[]{"B","22222222222","33","22"});
		list01.add(new String[]{"C","33333333333","44","22"});
		list01.add(new String[]{"D","44444444444","55","22"});

		mapList.add(list01);

		//需要动态改变表格的位置；第一个表格的位置为0
		int[] placeList = {0};
		try{
			CustomXWPFDocument doc = WorderToNewWordUtils.changWord(templatePath,params,mapList,placeList);
			FileOutputStream fopts = new FileOutputStream("D:\\example\\test\\呵呵.docx");
			doc.write(fopts);
			fopts.close();
		} catch (Exception e){

		}

	}

}
