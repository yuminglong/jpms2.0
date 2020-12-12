package com.jiebao.jpms.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

/**
 * Created by 王景伟 on 2018/12/19.
 */
public class WorderToNewWordUtils {
	/**
	 * 根据模板生成word文档
	 * @param inputUrl 模板路径
	 * @param textMap 需要替换的文本内容
	 * @param mapList 需要动态生成的内容
	 * @return
	 */
	public static CustomXWPFDocument changWord(String inputUrl, Map<String, Object> textMap, List<Object> mapList,int[] placeList) {
		CustomXWPFDocument document = null;
		try {
			//获取docx解析对象

			document = new CustomXWPFDocument(POIXMLDocument.openPackage(inputUrl));
			//解析替换文本段落对象
			WorderToNewWordUtils.changeText(document, textMap);

			//解析替换表格对象
			WorderToNewWordUtils.changeTable(document, textMap, mapList,placeList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return document;
	}

	/**
	 * 替换段落文本
	 * @param document docx解析对象
	 * @param textMap 需要替换的信息集合
	 */
	public static void changeText(CustomXWPFDocument document, Map<String, Object> textMap){
		//获取段落集合
		List<XWPFParagraph> paragraphs = document.getParagraphs();

		for (XWPFParagraph paragraph : paragraphs) {
			//判断此段落时候需要进行替换
			String text = paragraph.getText();
			if(checkText(text)){
				List<XWPFRun> runs = paragraph.getRuns();
				for (XWPFRun run : runs) {
					//替换模板原来位置
					Object ob = changeValue(run.toString(), textMap);
					System.out.println("段落："+run.toString());
					if (ob instanceof String){
						run.setText((String)ob,0);
					}
				}
			}
		}
	}

	/**
	 * 替换表格对象方法
	 * @param document docx解析对象
	 * @param textMap 需要替换的信息集合
	 * @param mapList 需要动态生成的内容
	 */
	public static void changeTable(CustomXWPFDocument document, Map<String, Object> textMap, List<Object> mapList,int[] placeList){
		//获取表格对象集合
		List<XWPFTable> tables = document.getTables();

		//循环所有需要进行替换的文本，进行替换
		/*for (int i = 0; i < tables.size(); i++) {
			XWPFTable table = tables.get(i);
			if(checkText(table.getText())){
				List<XWPFTableRow> rows = table.getRows();
				System.out.println("简单表格替换："+rows);
				//遍历表格,并替换模板
				eachTable(document,rows, textMap);
			}
		}*/

		int index=0;
		//操作word中的表格
		for (int i = 0; i < tables.size(); i++) {
			//只处理行数大于等于2的表格，且不循环表头
			XWPFTable table = tables.get(i);
			if(index<placeList.length&&placeList[index]==i){
				List<String[]> list = (List<String[]>) mapList.get(index);
				//第二个表格使用daList，插入数据
				if (null != list && 0 < list.size()){
					insertTable(table, null,list,2);
					//List<Integer[]> indexList = startEnd(list);
					//for (int c=0;c<indexList.size();c++){
						//合并行
						//mergeCellVertically(table,0,indexList.get(c)[0]+1,indexList.get(c)[1]+1);
					//}
				}
				index++;
			}

		}
	}
	/**
	 * 遍历表格
	 * @param rows 表格行对象
	 * @param textMap 需要替换的信息集合
	 */
	public static void eachTable(CustomXWPFDocument document, List<XWPFTableRow> rows , Map<String, Object> textMap){
		for (XWPFTableRow row : rows) {
			List<XWPFTableCell> cells = row.getTableCells();
			for (XWPFTableCell cell : cells) {
				//判断单元格是否需要替换
				if(checkText(cell.getText())){
					List<XWPFParagraph> paragraphs = cell.getParagraphs();
					for (XWPFParagraph paragraph : paragraphs) {
						List<XWPFRun> runs = paragraph.getRuns();
						for (XWPFRun run : runs) {
							Object ob = changeValue(run.toString(), textMap);
							if (ob instanceof String){
								run.setText((String)ob,0);
							}else if (ob instanceof Map){
								run.setText("",0);
								Map pic = (Map)ob;
								int width = Integer.parseInt(pic.get("width").toString());
								int height = Integer.parseInt(pic.get("height").toString());
								int picType = getPictureType(pic.get("type").toString());
								byte[] byteArray = (byte[]) pic.get("content");
								ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteArray);
								try {
									int ind = document.addPicture(byteInputStream,picType);
									document.createPicture(ind, width , height,paragraph);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 为表格插入数据，行数不够添加新行
	 * @param table 需要插入数据的表格
	 * @param tableList 第四个表格的插入数据
	 * @param daList 第二个表格的插入数据
	 * @param type 表格类型：1-第一个表格 2-第二个表格 3-第三个表格 4-第四个表格
	 */
	public static void insertTable(XWPFTable table, List<String> tableList,List<String[]> daList,Integer type){
		if (2 == type){
			//创建行和创建需要的列
			for(int i = 1; i < daList.size(); i++){
				//添加一个新行
				XWPFTableRow row = table.insertNewTableRow(2);
				for(int k=0; k<daList.get(0).length;k++){
					row.setHeight(800);
					row.createCell();//根据String数组第一条数据的长度动态创建列
					CTTc ctTc = row.getCell(k+2).getCTTc();
					CTTcPr tcPr = ctTc.getTcPr();
					CTTcBorders border = tcPr.addNewTcBorders();
					border.addNewBottom().setVal(STBorder.SINGLE);
					System.out.println("Height:::"+row.getHeight());
				}
			}



			//创建行,根据需要插入的数据添加新行，不处理表头
			for(int i = 0; i < daList.size(); i++){
				List<XWPFTableCell> cells = null;
				cells = table.getRow(i+1).getTableCells();
				for(int j = 0; j < cells.size(); j++){
					XWPFTableCell cell02 = cells.get(j);
					cell02.setText(daList.get(i)[j]);
				}
			}
		}else if (4 == type){
			//插入表头下面第一行的数据
			for(int i = 0; i < tableList.size(); i++){
				XWPFTableRow row = table.createRow();
				List<XWPFTableCell> cells = row.getTableCells();
				cells.get(0).setText(tableList.get(i));
			}
		}
	}

	/**
	 * 判断文本中时候包含$
	 * @param text 文本
	 * @return 包含返回true,不包含返回false
	 */
	public static boolean checkText(String text){
		boolean check  =  false;
		if(text.indexOf("$")!= -1){
			check = true;
		}
		return check;
	}

	/**
	 * 匹配传入信息集合与模板
	 * @param value 模板需要替换的区域
	 * @param textMap 传入信息集合
	 * @return 模板需要替换区域信息集合对应值
	 */
	public static Object changeValue(String value, Map<String, Object> textMap){
		Set<Map.Entry<String, Object>> textSets = textMap.entrySet();
		Object valu = value;
		for (Map.Entry<String, Object> textSet : textSets) {
			//匹配模板与替换值 格式${key}
			String key = textSet.getKey();
			if(value.indexOf(key)!= -1){
				valu = textSet.getValue();
			}
		}
		return valu;
	}

	/**
	 * 将输入流中的数据写入字节数组
	 * @param in
	 * @return
	 */
	public static byte[] inputStream2ByteArray(InputStream in, boolean isClose){
		byte[] byteArray = null;
		try {
			int total = in.available();
			byteArray = new byte[total];
			in.read(byteArray);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(isClose){
				try {
					in.close();
				} catch (Exception e2) {
					System.out.println("关闭流失败");
				}
			}
		}
		return byteArray;
	}

	/**
	 * 根据图片类型，取得对应的图片类型代码
	 * @param picType
	 * @return int
	 */
	private static int getPictureType(String picType){
		int res = CustomXWPFDocument.PICTURE_TYPE_PICT;
		if(picType != null){
			if(picType.equalsIgnoreCase("png")){
				res = CustomXWPFDocument.PICTURE_TYPE_PNG;
			}else if(picType.equalsIgnoreCase("dib")){
				res = CustomXWPFDocument.PICTURE_TYPE_DIB;
			}else if(picType.equalsIgnoreCase("emf")){
				res = CustomXWPFDocument.PICTURE_TYPE_EMF;
			}else if(picType.equalsIgnoreCase("jpg") || picType.equalsIgnoreCase("jpeg")){
				res = CustomXWPFDocument.PICTURE_TYPE_JPEG;
			}else if(picType.equalsIgnoreCase("wmf")){
				res = CustomXWPFDocument.PICTURE_TYPE_WMF;
			}
		}
		return res;
	}

	/**
	 * 合并行
	 * @param table
	 * @param col 需要合并的列
	 * @param fromRow 开始行
	 * @param toRow 结束行
	 */
	public static void mergeCellVertically(XWPFTable table, int col, int fromRow, int toRow) {
		for(int rowIndex = fromRow; rowIndex <= toRow; rowIndex++){
			CTVMerge vmerge = CTVMerge.Factory.newInstance();
			if(rowIndex == fromRow){
				vmerge.setVal(STMerge.RESTART);
			} else {
				vmerge.setVal(STMerge.CONTINUE);
			}
			XWPFTableCell cell = table.getRow(rowIndex).getCell(col);
			CTTcPr tcPr = cell.getCTTc().getTcPr();
			if (tcPr != null) {
				tcPr.setVMerge(vmerge);
			} else {
				tcPr = CTTcPr.Factory.newInstance();
				tcPr.setVMerge(vmerge);
				cell.getCTTc().setTcPr(tcPr);
			}
		}
	}
	/**
	 * 获取需要合并单元格的下标
	 * @return
	 */
	public static List<Integer[]> startEnd(List<String[]> daList){
		List<Integer[]> indexList = new ArrayList<Integer[]>();

		List<String> list = new ArrayList<String>();
		for (int i=0;i<daList.size();i++){
			list.add(daList.get(i)[0]);
		}
		Map<Object, Integer> tm = new HashMap<Object, Integer>();
		for (int i=0;i<daList.size();i++){
			if (!tm.containsKey(daList.get(i)[0])) {
				tm.put(daList.get(i)[0], 1);
			} else {
				int count = tm.get(daList.get(i)[0]) + 1;
				tm.put(daList.get(i)[0], count);
			}
		}
		for (Map.Entry<Object, Integer> entry : tm.entrySet()) {
			String key = entry.getKey().toString();
			String value = entry.getValue().toString();
			if (list.indexOf(key) != (-1)){
				Integer[] index = new Integer[2];
				index[0] = list.indexOf(key);
				index[1] = list.lastIndexOf(key);
				indexList.add(index);
			}
		}
		return indexList;
	}

}
