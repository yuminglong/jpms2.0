package com.jiebao.jpms.controller;

import com.jiebao.jpms.model.JpmsPersons;
import com.jiebao.jpms.model.JpmsProposal;
import com.jiebao.jpms.service.IJpmsAppendixService;
import com.jiebao.jpms.service.IJpmsFolderService;
import com.jiebao.jpms.service.IJpmsMessageService;
import com.jiebao.system.model.JpmsUser;
import com.jiebao.system.service.IJpmsProposalService;
import com.jiebao.system.service.IJpmsUserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.List;


@Slf4j
@RestController
@Api(tags = "文档")
public class JpmsWordController {

    @Autowired
    private IJpmsProposalService jpmsProposalService;

    @Autowired
    private IJpmsUserService jpmsUserService;

    @Autowired
    private IJpmsAppendixService jpmsAppendixService;

    @GetMapping("/word/{proposalId}")
    public void testExportWord2(@PathVariable Integer proposalId, HttpServletRequest request, HttpServletResponse response) throws Exception {

        JpmsProposal byId = jpmsProposalService.findById(proposalId);
        if(byId.getZunits()==null)
            byId.setZunits("");

        if(byId.getHunits()==null)
            byId.setHunits("");

        if(byId.getReview()==null)
            byId.setReview("");

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(byId.getCreateTime());

        //联名用户
        String person="";
        List<JpmsPersons> userList = jpmsUserService.jointlm(proposalId);
        for(JpmsPersons p:userList){
          JpmsUser user= jpmsUserService.getById(p.getUserId());
            person+=" <tr>\n" +
                    "  <td>"+user.getRealName()+"</td>\n" +
                    "  <td>"+user.getSubsector()+"</td>\n" +
                    "  <td>"+user.getAddress()+"</td>\n" +
                    "  <td>"+user.getMobile()+"</td>\n" +
                    "</tr>";
        }



        try {
            //word内容
            String content = "<head>\n" +
                    "    <meta charset=\"utf-8\"/>\n" +
                    "    <title>提案详情</title>\n" +
                    "    <style>\n" +
                    "        html, body {\n" +
                    "            font-size: 30px;\n" +
                    "            font-family: \"宋体\";\n" +
                    "            color: #222222;\n" +
                    "        }\n" +
                    "\n" +
                    "        .detail {\n" +
                    "            width: 800px;\n" +
                    "            margin: 50px auto;\n" +
                    "        }\n" +
                    "\n" +
                    "        .title {\n" +
                    "            font-size: 20px;\n" +
                    "            text-align: center;\n" +
                    "        }\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "        .table th {\n" +
                    "            text-align: center;\n" +
                    "            width: 110px;\n" +
                    "        }\n" +
                    "\n" +
                  /*  "        .table th, td {\n" +
                    "            padding: 8px;\n" +
                    "            border-left: 1px solid #000000;\n" +
                    "            border-bottom: 1px solid #000000;\n" +*/
                    "        }\n" +
                    "\n" +
                    "        .content {\n" +
                    "            /*min-height: 400px;*/\n" +
                    "            padding: 0 8px;\n" +
                    "        }\n" +
                    "\n" +
                    "        .content p {\n" +
                    "            margin: 0;\n" +
                    "            text-align: justify;\n" +
                    "            font-size: 16px;\n" +
                    "            padding: 2px 0;\n" +
                    "            line-height: 24px !important;\n" +
                    "        }\n" +
                    "\n" +
                    "        .t1 {\n" +
                    "            font-weight: 700;\n" +
                    "            font-size: 27px;\n" +
                    "        }\n" +
                    "\n" +
                    "        .t2 {\n" +
                    "            margin-right: 10px;\n" +
                    "            padding-right: 10px;\n" +
                    "            font-weight: 700;\n" +
                    "            font-size: 23px;\n" +
                    "        }\n" +
                    "\n" +
                    "        .tab {\n" +
                    "            padding-top: 35px;\n" +
                    "            line-height: 60px;\n" +
                    "        }\n" +
                    "        .table1 {\n" +
                    "            font-family: \"宋体\";\n" +
                    "            font-size: 20px;\n" +
                    "            width: 100%;\n" +
                   /* "            border-spacing: 0;\n" +
                    "            border-collapse: separate;\n" +*/
                    "            border: 1px solid #000000;\n" +
                    "            border-left: 0;\n" +
                    "            border-bottom: 0;\n" +
                    "        }\n" +
                    "\n" +
                    "        .table1 th {\n" +
                    "            text-align: center;\n" +
                    "            width: 150px;\n" +
                    "            height: 65px;\n" +
                    "        }\n" +
                    "\n" +
                    "        .table1 th, td {\n" +
                    "            padding: 8px;\n" +
                    "            border-left: 1px solid #000000;\n" +
                    "            border-bottom: 1px solid #000000;\n" +
                    "            border-right: 1px solid #000000;\n" +
                    "            border-top: 1px solid #000000;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "    <style id=\"print-style\">\n" +
                    "        .title {\n" +
                    "            font-size: 30px;\n" +
                    "            text-align: center;\n" +
                    "        }\n" +
                    "\n" +
                    "        .table {\n" +
                    "            font-family: \"宋体\";\n" +
                    "            font-size: 16px;\n" +
                    "            width: 100%;\n" +
                    "            border-spacing: 0;\n" +
                    "            border-collapse: separate;\n" +
                    "            border: 1px solid #000000;\n" +
                    "            border-left: 0;\n" +
                    "            border-bottom: 0;\n" +
                    "        }\n" +
                    "\n" +
                    "        .table th {\n" +
                    "            text-align: center;\n" +
                    "            width: 135px;\n" +
                    "        }\n" +
                    "\n" +
                    "        .table th, td {\n" +
                    "            padding: 8px;\n" +
                    "            border-left: 1px solid #000000;\n" +
                    "            border-bottom: 1px solid #000000;\n" +
                    "border-right: 1px solid #000000;\n" +
                    "border-top: 1px solid #000000;\n" +
                    "        }\n" +
                    "\n" +
                    "        /*  .content {\n" +
                    "              !*min-height: 400px;*!\n" +
                    "              padding: 0 8px;\n" +
                    "          }\n" +
                    "\n" +
                    "          .content p {\n" +
                    "              margin: 0;\n" +
                    "              text-align: justify;\n" +
                    "              font-size: 16px;\n" +
                    "              padding: 2px 0;\n" +
                    "              line-height: 24px !important;\n" +
                    "          }*/\n" +
                    "    </style>\n" +
                    "</head><body>" +

                    "<p style=\" font-size:14pt\">提案类型:"+byId.getType()+"</p>\n" +
                    "    <p style=\" font-size:14pt\">提案类型:"+byId.getClassify()+"</p>\n" +
                    "    <p style=\" font-size:14pt\">收到提案时间:"+dateString+"</p>"+
                    "    <p style=\"font-size:24pt; line-height:150%; margin:0pt; orphans:0; text-align:center; widows:0\"><span\n" +
                    "            style=\"font-family:宋体; font-size:24pt; font-weight:bold\">&#xa0;</span></p>\n" +
                    "    <p style=\"font-size:24pt; line-height:150%; margin:0pt; orphans:0; text-align:center; widows:0\">\n" +
                    "       </br></br></br></br> <span style=\"font-family:宋体; font-size:30pt; font-weight:bold\"></br></br>政协常宁市第<span>" + byId.getAnniversary() + "</span>届第<span\n" +
                    "               >" + byId.getNumber() + "</span>次会议</span></p>\n" +
                    "    <p style=\"background-color:#ffffff; font-size:20pt; line-height:150%; margin:0pt; text-align:center\"><span\n" +
                    "            style=\"font-family:宋体; font-size:25pt; font-weight:bold\">第<span>" + byId.getProposalNumber() + "</span>号提案</span></p>\n" +
                    " <div class=\"tab\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                    "        <p><span class=\"t1\">案  &nbsp;&nbsp;由: </span> <u  class=\"t2\">" + byId.getCause() + "</u></p>\n" +
                    "        <p><span class=\"t1\">主办单位: </span> <u  class=\"t2\">" + byId.getZunits() + "</u></p>\n" +
                    "        <p><span class=\"t1\">会办单位: </span> <u  class=\"t2\">" + byId.getHunits() + "</u></p>\n" +
                    "        <p><span class=\"t1\">审查意见: </span> <u  class=\"t2\">" + byId.getReview() + "</u></p>\n" +
                    "\n" +
                    "    </div>\n" +
                    "\n" +
                    "   <table class=\"table1\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                    "   <tr>\n" +
                    "            <td>提案人姓名</td>\n" +
                    "            <td>界别</td>\n" +
                    "            <td>通讯地址</td>\n" +
                    "            <td>联系电话</td>\n" +
                    "        </tr>"+
                    person+

//                    "        <tr>\n" +
//                    "            <th>提案者</th>\n" +
//                    "            <td><span >" + byId.getRealName() + "</span></td>\n" +
//                    "            <th>移动电话</th>\n" +
//                    "            <td><span >" + byId.getMobile() + "</span></td>\n" +
//                    "        </tr>\n" +
//                    "        <tr>\n" +
//                    "            <th>界别</th>\n" +
//                    "            <td><span >" + byId.getJuser().getSubsector() + "</span></td>\n" +
//                    "            <th>工作单位</th>\n" +
//                    "            <td><span >" + byId.getJuser().getUnitName() + "</span></td>\n" +
//                    "        </tr>\n" +
//                    "        <tr>\n" +
//                    "            <th>提案类型</th>\n" +
//                    "            <td><span >" + byId.getType() + "</span></td>\n" +
//                    "            <th>提案类别</th>\n" +
//                    "            <td><span >" + byId.getClassify() + "</span></td>\n" +
//                    "        </tr>\n" +
//                    "        <tr>\n" +
//                    "            <th>联名人</th>\n" +
//                    "            <td><span >" + byId.getPeoplestwo() + "</span></td>\n" +
//                    "            <th>通讯地址</th>\n" +
//                    "            <td><span >" + byId.getJuser().getAddress() + "</span></td>\n" +
//                    "        </tr>\n" +
                    "    </table>\n<br><br>" +
                    "\n" +
                    "\n" +
                    "<p align=\"center\" style=\"font-family:宋体;  font-size:14pt; font-weight:bold\">提案正文</p >"+
                    "<div class=\"content\">\n"+
                    "                "+byId.getContent()+"</div>\n" +
                   /* "    <table class=\"table \" cellpadding=\"0\" cellspacing=\"0\">\n" +
                    "        <tr>\n" +
                    "            <th colspan=\"4\"  style=\"font-family:宋体; font-size:14pt; font-weight:normal\">提案正文</th>\n" +
                    "        </tr>\n" +
                    "        <tr>\n" +
                    "            <td colspan=\"4\">\n" +
                    "                <div class=\"content\" >\n" +
                    "\n" +
                    "                " + byId.getContent() + "</div>\n" +
                    "            </td>\n" +
                    "        </tr>\n" +
                    "    </table>\n" +*/
                    "</div></body>";


            byte b[] = content.getBytes("GBK");  //这里是必须要设置编码的，不然导出中文就会乱码。
            ByteArrayInputStream bais = new ByteArrayInputStream(b);//将字节数组包装到流中

            /*
             * 关键地方
             * 生成word格式 */
            POIFSFileSystem poifs = new POIFSFileSystem();
            DirectoryEntry directory = poifs.getRoot();
            DocumentEntry documentEntry = directory.createDocument("WordDocument", bais);
            //输出文件
            request.setCharacterEncoding("utf-8");
            response.setContentType("application/msword");//导出word格式
            response.addHeader("Content-Disposition", "attachment;filename=" +
                    new String(byId.getCause().getBytes("GB2312"), "iso8859-1") + ".doc");
            ServletOutputStream ostream = response.getOutputStream();
            poifs.writeFilesystem(ostream);
            bais.close();
            ostream.close();
           // poifs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
