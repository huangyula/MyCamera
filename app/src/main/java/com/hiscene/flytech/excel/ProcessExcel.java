package com.hiscene.flytech.excel;

import com.github.weiss.core.utils.FileUtils;
import com.github.weiss.core.utils.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hiscene.flytech.C;
import com.hiscene.flytech.entity.ProcessModel;
import com.hiscene.flytech.util.GsonUtil;
import com.hiscene.flytech.util.POIUtil;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipFile;

import static com.hiscene.flytech.C.OUTPUT_PATH;

/**
 * @author Minamo
 * @e-mail kleinminamo@gmail.com
 * @time 2019/6/17
 * @des 工作过程表格
 */
public class ProcessExcel implements IExcel {

    public List<ProcessModel> processExcelList = new ArrayList<>();

    @Override
    public void read() {
        try {
            // 延迟解析比率
//            ZipSecureFile.setMinInflateRatio(-1.0d);
            XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(new File(C.ASSETS_PATH + C.PROCESS_FILE)));
            // replace the dummy-content to show that we could write and read the cell-values
            Sheet sheet = wb.getSheetAt(0);
            for (int i = 1; i <=sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if(row==null){//防止空行导致获取到的总行数不正确
                    break;
                }
                if (row != null) {
                    processExcelList.add(new ProcessModel((int) row.getCell(0).getNumericCellValue(),
                            row.getCell(1).getStringCellValue(), row.getCell(2).getStringCellValue()));
                }
            }
            System.out.println("sheet.getLastRowNum(): "+sheet.getLastRowNum());
            System.out.println("processExcelList: "+processExcelList.size());
            wb.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write() {
        try {
            List<String> resultList=new ArrayList<>();
            String result="";
//            for(ProcessModel processModel:processExcelList){
//                switch (processModel.getResult()){
//                    case 0:
//                        result="确认(×)";
//                        break;
//                    case 1:
//                        result="确认(√)";
//                        break;
//                    case -1:
//                        result="无";
//                        break;
//                }
//                resultList.add(result);
//            }
            resultList.add("1");
            resultList.add("2");
            POIUtil.setCellValueAt(C.ASSETS_PATH + C.PROCESS_FILE,OUTPUT_PATH+C.PROCESS_FILE,2,4,resultList);
            System.out.println("已成功修改表格内容");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
/*        Workbook wb = new XSSFWorkbook();
        try {
            Sheet sheet = wb.createSheet("Sheet1");
            Row row = sheet.createRow(0);
            Cell cell = row.createCell(0);
            cell.setCellValue("cell-1");
            cell = row.createCell(1);
            cell.setCellValue("cell-2");
            cell = row.createCell(2);
            cell.setCellValue("cell-3");

            XSSFCellStyle style = (XSSFCellStyle) wb.createCellStyle();
            style.setFillBackgroundColor(new XSSFColor(new org.apache.poi.java.awt.Color(1, 2, 3)));

            Hyperlink link = wb.getCreationHelper().createHyperlink(HyperlinkType.URL);
            link.setAddress("http://www.google.at");
            link.setLabel("Google");
            cell.setHyperlink(link);

            cell.setCellStyle(style);

            sheet.setPrintGridlines(true);

            OutputStream stream = openFileOutput("test.xlsx", Context.MODE_PRIVATE);
            try {
                wb.write(stream);
            } finally {
                stream.close();
            }
        } finally {
            wb.close();
        }*/
    }

    @Override
    public void restore() {
        //从缓存文件中读取内容
        String read_content=FileUtils.readFile2String(C.TEMP_PROCESS_FILE,"utf-8");
        Type type = new TypeToken<List<ProcessModel>>(){}.getType();
        processExcelList=new Gson().fromJson(read_content,type);
//        System.out.println(processExcelList);
    }

    @Override
    public void svae() {
        //将List转化为json,并写入缓存目录
        if(processExcelList!=null&&processExcelList.size()>0){
            String write_content=GsonUtil.gsonString(processExcelList.toArray());
            if(!StringUtils.isEmpty(write_content)){
                if(FileUtils.createFileByDeleteOldFile(C.TEMP_PROCESS_FILE)){
                    boolean a=FileUtils.writeFileFromString(C.TEMP_PROCESS_FILE,write_content,true);
                }
            }
        }
    }

}
