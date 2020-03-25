package com.jiebao.system.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jiebao.system.service.IJpmsUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/analyze")
@Api(tags = "统计分析API文档")
public class JpmsAnalyzeController {


    @Autowired
	private IJpmsUserService jpmsUserService;





	@ApiOperation(value = "办理单位办理情况")
	@GetMapping("/rcbchart")
	public String rcbchart() {
		//存放Echart的category
		List unitName = new ArrayList();
		List overManage = new ArrayList();
		List managing = new ArrayList();
		List stayManage = new ArrayList();
		List notManage = new ArrayList();

		List<Map<String, Object>> list = jpmsUserService.unitAnaly();
		// List yet = iJpmsAnalyzeService.yet();
		//得到所有的UnitName
		for(Map<String, Object> item: list) {
			unitName.add(item.get("unitName"));
			overManage.add(item.get("overManage"));
			managing.add(item.get("managing"));
			stayManage.add(item.get("stayManage"));
			notManage.add(item.get("notManage"));
		}


		//将list集合转换为json数组
		String unitNameData = JSON.toJSONString(unitName);
		String overManageData = JSON.toJSONString(overManage);
		String managingData = JSON.toJSONString(managing);
		String stayManageData = JSON.toJSONString(stayManage);
		String notManageData = JSON.toJSONString(notManage);


		JSONObject jsonObject = new JSONObject();
		jsonObject.put("unitName", unitNameData);
		jsonObject.put("overManage", overManageData);
		jsonObject.put("managing", managingData);
		jsonObject.put("stayManage", stayManageData);
		jsonObject.put("notManage", notManageData);


		String result = JSON.toJSONString(jsonObject);

		return result;
	}

	@ApiOperation(value = "提案状态办理情况")
	@GetMapping("/staAnaly")
	public String staAnaly() {

		//存放Echart的category
		List unitName = new ArrayList();
		List overManage = new ArrayList();
		List managing = new ArrayList();
		List stayManage = new ArrayList();
		List notManage = new ArrayList();

		List<Map<String, Object>> list = jpmsUserService.staAnaly();
		// List yet = iJpmsAnalyzeService.yet();
		//得到所有的UnitName
		for(Map<String, Object> item: list) {
			unitName.add(item.get("unitName"));
			overManage.add(item.get("overManage"));
			managing.add(item.get("managing"));
			stayManage.add(item.get("stayManage"));
			notManage.add(item.get("notManage"));
		}


		//将list集合转换为json数组
		String unitNameData = JSON.toJSONString(unitName);
		String overManageData = JSON.toJSONString(overManage);
		String managingData = JSON.toJSONString(managing);
		String stayManageData = JSON.toJSONString(stayManage);
		String notManageData = JSON.toJSONString(notManage);


		JSONObject jsonObject = new JSONObject();
		jsonObject.put("unitName", unitNameData);
		jsonObject.put("overManage", overManageData);
		jsonObject.put("managing", managingData);
		jsonObject.put("stayManage", stayManageData);
		jsonObject.put("notManage", notManageData);
		String result = JSON.toJSONString(jsonObject);


		return result;
	}

    @ApiOperation(value = "提案分类情况")
    @GetMapping("/classify")
    public String classify() {

        //存放Echart的category
        List allClassify = new ArrayList();
        List classifyOne = new ArrayList();
        List classifyTwo = new ArrayList();
        List classifyThree = new ArrayList();
        List classifyFour = new ArrayList();
        List classifyFive = new ArrayList();

        List<Map<String, Object>> list = jpmsUserService.classify();
        // List yet = iJpmsAnalyzeService.yet();
        //得到所有的UnitName
        for(Map<String, Object> item: list) {
            allClassify.add(item.get("allClassify"));
            classifyOne.add(item.get("classifyOne"));
            classifyTwo.add(item.get("classifyTwo"));
            classifyThree.add(item.get("classifyThree"));
            classifyFour.add(item.get("classifyFour"));
            classifyFive.add(item.get("classifyFive"));
        }


        //将list集合转换为json数组
        String allClassifyData = JSON.toJSONString(allClassify);
        String classifyOneData = JSON.toJSONString(classifyOne);
        String classifyTwoData = JSON.toJSONString(classifyTwo);
        String classifyThreeData = JSON.toJSONString(classifyThree);
        String classifyFourData = JSON.toJSONString(classifyFour);
        String classifyFiveData = JSON.toJSONString(classifyFive);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("allClassify", allClassifyData);
        jsonObject.put("classifyOne", classifyOneData);
        jsonObject.put("classifyTwo", classifyTwoData);
        jsonObject.put("classifyThree", classifyThreeData);
        jsonObject.put("classifyFour", classifyFourData);
        jsonObject.put("classifyFive", classifyFiveData);
        String result = JSON.toJSONString(jsonObject);


        return result;
    }



	@ApiOperation(value = "提案类型统计")
	@GetMapping("/propType")
	public String propType() {

		//存放Echart的category
		List onePro = new ArrayList();
		List manyPro = new ArrayList();
		List ourPro = new ArrayList();

		List<Map<String, Object>> list = jpmsUserService.propType();
		// List yet = iJpmsAnalyzeService.yet();
		//得到所有的UnitName
		for(Map<String, Object> item: list) {
			onePro.add(item.get("onePro"));
			manyPro.add(item.get("manyPro"));
			ourPro.add(item.get("ourPro"));
		}


		//将list集合转换为json数组
		String oneProData = JSON.toJSONString(onePro);
		String manyProData = JSON.toJSONString(manyPro);
		String ourProData = JSON.toJSONString(ourPro);


		JSONObject jsonObject = new JSONObject();
		jsonObject.put("onePro", oneProData);
		jsonObject.put("manyPro", manyProData);
		jsonObject.put("ourPro", ourProData);
		String result = JSON.toJSONString(jsonObject);


		return result;
	}


	@ApiOperation(value = "提案满意度统计")
	@GetMapping("/satisfactions")
	public String satisfactions() {

		//存放Echart的category
		List veryGood = new ArrayList();
		List good = new ArrayList();
		List bad = new ArrayList();
		List veryBad = new ArrayList();

		List<Map<String, Object>> list = jpmsUserService.satisfactions();
		// List yet = iJpmsAnalyzeService.yet();
		//得到所有的UnitName
		for(Map<String, Object> item: list) {
			veryGood.add(item.get("veryGood"));
			good.add(item.get("good"));
			bad.add(item.get("bad"));
			veryBad.add(item.get("veryBad"));
		}


		//将list集合转换为json数组
		String veryGoodData = JSON.toJSONString(veryGood);
		String goodData = JSON.toJSONString(good);
		String badData = JSON.toJSONString(bad);
		String veryBadData = JSON.toJSONString(veryBad);

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("veryGood", veryGoodData);
		jsonObject.put("good", goodData);
		jsonObject.put("bad", badData);
		jsonObject.put("veryBad", veryBadData);
		String result = JSON.toJSONString(jsonObject);


		return result;
	}

    @ApiOperation(value = "集体提案四大类统计")
    @GetMapping("/collective")
    public String collective() {

        //存放Echart的category
        List jbPro = new ArrayList();
        List rmPro = new ArrayList();
        List wyPro = new ArrayList();
        List zmPro = new ArrayList();

        List<Map<String, Object>> list = jpmsUserService.collective();
        // List yet = iJpmsAnalyzeService.yet();
        //得到所有的UnitName
        for(Map<String, Object> item: list) {
            jbPro.add(item.get("jbPro"));
            rmPro.add(item.get("rmPro"));
            wyPro.add(item.get("wyPro"));
            zmPro.add(item.get("zmPro"));
        }


        //将list集合转换为json数组
        String jbProData = JSON.toJSONString(jbPro);
        String rmProData = JSON.toJSONString(rmPro);
        String wyProData = JSON.toJSONString(wyPro);
        String zmProData = JSON.toJSONString(zmPro);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("jbPro", jbProData);
        jsonObject.put("rmPro", rmProData);
        jsonObject.put("wyPro", wyProData);
        jsonObject.put("zmPro", zmProData);
        String result = JSON.toJSONString(jsonObject);


        return result;
    }


    @ApiOperation(value = "ABC办结类型统计")
    @GetMapping("/overAnswer")
    public String overAnswer() {

        //存放Echart的category
        List Atype = new ArrayList();
        List Btype = new ArrayList();
        List Ctype = new ArrayList();

        List<Map<String, Object>> list = jpmsUserService.overAnswer();
        // List yet = iJpmsAnalyzeService.yet();
        //得到所有的UnitName
        for(Map<String, Object> item: list) {
            Atype.add(item.get("Atype"));
            Btype.add(item.get("Btype"));
            Ctype.add(item.get("Ctype"));
        }


        //将list集合转换为json数组
        String AtypeData = JSON.toJSONString(Atype);
        String BtypeData = JSON.toJSONString(Btype);
        String CtypeData = JSON.toJSONString(Ctype);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Atype", AtypeData);
        jsonObject.put("Btype", BtypeData);
        jsonObject.put("Ctype", CtypeData);
        String result = JSON.toJSONString(jsonObject);


        return result;
    }



}
