package com.sy.controller;


import com.sy.constant.HttpStatusConstant;
import com.sy.dao.DeptDao;
import com.sy.dao.PersonDao;
import com.sy.entity.PersonEfficiency;
import com.sy.service.DeptService;
import com.sy.service.PersonEfficiencyService;
import com.sy.utils.DateUtils;
import com.sy.vo.JsonResult;
import com.sy.vo.PageJsonResult;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("personEfficiency")
public class PersonEfficiencyController {

    @Autowired
    private PersonEfficiencyService personEfficiencyService;

    @Autowired
    private DeptService deptService;

    @Autowired
    private DeptDao deptDao;

    @Autowired
    private PersonDao personDao;

    /**
     *
     * @param personName 输入框查询的人员
     * @param deptId    默认当前登录人员所属部门，选了左边部门列表就是所选部门Id
     * @param page
     * @param pageSize
     * @param beginTime
     * @param endTime
     * @return
     */
    @RequestMapping(value = "all",method = RequestMethod.GET)  //pc查询个人工效
    public JsonResult getAllData(String personName,int deptId,Integer page,Integer pageSize,String beginTime,String endTime){
       /* if(deptId!=0&&!"".equals(personName)){
            Integer leader = deptDao.getLeaderById(deptId);
            Integer personId = personDao.getIdByName(personName);

            if(leader==personId){
                personName="";
            }
        }*/

        /*try {
            personEfficiencyService.calculateData(personName,deptId,beginTime =="" ?null:DateUtils.parseDate(beginTime),endTime =="" ?null:DateUtils.getNextDay(endTime));
        } catch (Exception e) {
            e.printStackTrace();
            return PageJsonResult.buildFailurePage(404,e.getMessage());
        }

        return PageJsonResult.buildSuccessPage(HttpStatusConstant.SUCCESS,personEfficiencyService.initAllData(page,pageSize));//原来的*/

        try {

            return JsonResult.buildSuccess(HttpStatusConstant.SUCCESS, personEfficiencyService.initDeptData(personName, deptId,beginTime =="" ?null:DateUtils.parseDate(beginTime),endTime =="" ?null:DateUtils.parseDate(endTime)));
        }catch (Exception e){
            e.printStackTrace();
            return JsonResult.buildFailure(404,e.getMessage());
        }
    }

    @RequestMapping(value = "select",method = RequestMethod.GET)
    public JsonResult getAllDataBySelect(String personName,int deptId,Integer page,Integer pageSize,String beginTime,String endTime){

       /* try {
            personEfficiencyService.calculateData(personName,deptId,beginTime =="" ?null:DateUtils.parseDate(beginTime),endTime =="" ?null:DateUtils.getNextDay(endTime));
        } catch (Exception e) {
            e.printStackTrace();
            return PageJsonResult.buildFailurePage(404,e.getMessage());
        }

        return PageJsonResult.buildSuccessPage(HttpStatusConstant.SUCCESS,personEfficiencyService.initAllData(page,pageSize));*/

        try {
            return JsonResult.buildSuccess(HttpStatusConstant.SUCCESS, personEfficiencyService.initDeptData(personName, deptId,beginTime =="" ?null:DateUtils.parseDate(beginTime),endTime =="" ?null:DateUtils.parseDate(endTime)));
        }catch (Exception e){
            e.printStackTrace();
            return JsonResult.buildFailure(404,e.getMessage());
        }
    }


    //文件下载：导出excel表
//    @RequestMapping(value = "/exportExcel",method = RequestMethod.GET)
//    @ResponseBody
//    public void exportExcel(String beginTime,String endTime,String personName,HttpServletRequest request,
//                            HttpServletResponse response) throws UnsupportedEncodingException {
//        //一、从后台拿数据
//        if (null == request || null == response)
//        {
//            return;
//        }
//        List<PersonEfficiency> list = null;
//        /*String beginTime = request.getParameter("beginTime");
//        String endTime = request.getParameter("endTime");
//        String personName = request.getParameter("personName");*/
////        int deptId= Integer.parseInt(request.getParameter("deptId"));
//        int deptId = 0;
//        if(personName == null || "".equals(personName)){
//            deptId = 1;
//        }else{
//            deptId = personDao.getByName(personName).getDept().getId();
//        }
//
//        try {
//            list = personEfficiencyService.initDeptData(personName, deptId, beginTime == "" ? null : DateUtils.parseDate(beginTime), endTime == "" ? null : DateUtils.parseDate(endTime));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        //二、 数据转成excel
//        request.setCharacterEncoding("UTF-8");
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("application/x-download");
//
//        String fileName = "个人功效.xlsx";
//        fileName = URLEncoder.encode(fileName, "UTF-8");
//        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
//        // 第一步：定义一个新的工作簿
//        XSSFWorkbook wb = new XSSFWorkbook();
//        // 第二步：创建一个Sheet页
//        XSSFSheet sheet = wb.createSheet("personEfficiency");
//        sheet.setDefaultRowHeight((short) (2 * 256));//设置行高
//        sheet.setColumnWidth(0, 4000);//设置列宽
//        sheet.setColumnWidth(1,5500);
//        sheet.setColumnWidth(2,5500);
//        sheet.setColumnWidth(3,5500);
//        sheet.setColumnWidth(11,3000);
//        sheet.setColumnWidth(12,3000);
//        sheet.setColumnWidth(13,3000);
//        XSSFFont font = wb.createFont();
//        font.setFontName("宋体");
//        font.setFontHeightInPoints((short) 16);
//
//        XSSFRow row = sheet.createRow(0);
//        XSSFCell cell = row.createCell(0);
//        cell.setCellValue("人员编号 ");
//        cell = row.createCell(1);
//        cell.setCellValue("人员姓名 ");
//        cell = row.createCell(2);
//        cell.setCellValue("在岗时间");
//        cell = row.createCell(3);
//        cell.setCellValue("有效时间");
//        cell = row.createCell(4);
//        cell.setCellValue("工效");
//        cell = row.createCell(5);
//        cell.setCellValue("工作总用电量(kwh) ");
//        cell = row.createCell(6);
//        cell.setCellValue("空载总用电量(kwh) ");
//        cell = row.createCell(7);
//        cell.setCellValue("电流超限次数");
//        cell = row.createCell(8);
//        cell.setCellValue("超限时长");
//
//        XSSFRow rows;
//        XSSFCell cells;
//        for (int i = 0; i < list.size(); i++) {
//            // 第三步：在这个sheet页里创建一行
//            rows = sheet.createRow(i+1);
//            // 第四步：在该行创建一个单元格
//            cells = rows.createCell(0);
//            // 第五步：在该单元格里设置值
//            cells.setCellValue(list.get(i).getPersonId());
//            cells = rows.createCell(1);
//            cells.setCellValue(list.get(i).getName());
//            cells = rows.createCell(2);
//            cells.setCellValue(list.get(i).getTime());
//            cells = rows.createCell(3);
//            cells.setCellValue(list.get(i).getWorkingTime());
//            cells = rows.createCell(4);
//            cells.setCellValue(list.get(i).getEfficiency());
//            cells = rows.createCell(5);
//            cells.setCellValue(list.get(i).getWorkingPower());
//            cells = rows.createCell(6);
//            cells.setCellValue(list.get(i).getNoloadingPower());
//            cells = rows.createCell(7);
//            cells.setCellValue(list.get(i).getCounts());
//            cells = rows.createCell(8);
//            Integer overTime = list.get(i).getOverTime();
//            if(overTime == null){
//                overTime = 0;
//            }
//            cells.setCellValue(overTime);
//        }
//
//        try {
//            OutputStream out = response.getOutputStream();
//            wb.write(out);
//            out.close();
//            wb.close();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//    }

}
