package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.MemberService;

import com.itheima.service.ReportService;
import com.itheima.service.SetmealService;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.dsig.spec.HMACParameterSpec;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {
    //δΌεζ°ι

    @Reference
    private MemberService memberService;

    @Reference
    private SetmealService setmealService;

    @RequestMapping("/getMemberReport")
    public Result getMemberResult() throws Exception {
        Map<String, Object> map = new HashMap<>();
        List<String> months = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -12);

        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH, 1);
            Date date = calendar.getTime();
            months.add(new SimpleDateFormat("yyyy.MM").format(date));
        }

        map.put("months", months);

        List<Integer> memberCount = memberService.findMemberCountByMonth(months);

        map.put("memberCount", memberCount);

        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
    }

    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport() {

        Map<String, Object> data = new HashMap<>();

        try {
            List<Map<String, Object>> setmealCount = setmealService.findSetmealCount();

            List<String> setmealNames = new ArrayList<>();

            for (Map<String, Object> map : setmealCount) {
                String name = (String) map.get("name");
                setmealNames.add(name);
            }

            data.put("setmealCount", setmealCount);
            data.put("setmealNames", setmealNames);
            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, data);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }

    }


    @Reference
    private ReportService reportService;

    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData(){
        try {
            Map<String, Object> result = reportService.getBusinessReport();
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,result);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        try{
            //θΏη¨θ°η¨ζ₯θ‘¨ζε‘θ·εζ₯θ‘¨ζ°ζ?
            Map<String, Object> result = reportService.getBusinessReport();

            //εεΊθΏεη»ζζ°ζ?οΌεε€ε°ζ₯θ‘¨ζ°ζ?εε₯ε°Excelζδ»ΆδΈ­
            String reportDate = (String) result.get("reportDate");
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");

            //θ·εΎExcelζ¨‘ζΏζδ»Άη»ε―Ήθ·―εΎ
            String temlateRealPath = request.getSession().getServletContext().getRealPath("template") +
                    File.separator + "report_template.xlsx";

            //θ―»εζ¨‘ζΏζδ»Άεε»ΊExcelθ‘¨ζ Όε―Ήθ±‘
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(temlateRealPath)));

            XSSFSheet sheet = workbook.getSheetAt(0);

            XSSFRow row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);//ζ₯ζ

            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//ζ°ε’δΌεζ°οΌζ¬ζ₯οΌ
            row.getCell(7).setCellValue(totalMember);//ζ»δΌεζ°

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//ζ¬ε¨ζ°ε’δΌεζ°
            row.getCell(7).setCellValue(thisMonthNewMember);//ζ¬ζζ°ε’δΌεζ°

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//δ»ζ₯ι’ηΊ¦ζ°
            row.getCell(7).setCellValue(todayVisitsNumber);//δ»ζ₯ε°θ―ζ°

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//ζ¬ε¨ι’ηΊ¦ζ°
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//ζ¬ε¨ε°θ―ζ°

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//ζ¬ζι’ηΊ¦ζ°
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//ζ¬ζε°θ―ζ°

            int rowNum = 12;
            for(Map map : hotSetmeal){//η­ι¨ε₯ι€
                String name = (String) map.get("name");
                Long setmeal_count = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                row = sheet.getRow(rowNum ++);
                row.getCell(4).setCellValue(name);//ε₯ι€εη§°
                row.getCell(5).setCellValue(setmeal_count);//ι’ηΊ¦ζ°ι
                row.getCell(6).setCellValue(proportion.doubleValue());//ε ζ―
            }

            //ιθΏθΎεΊζ΅θΏθ‘ζδ»ΆδΈθ½½
            ServletOutputStream out = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
            workbook.write(out);

            out.flush();
            out.close();
            workbook.close();

            return null;
        }catch (Exception e){
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL,null);
        }
    }


}
