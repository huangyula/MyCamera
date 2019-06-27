package com.hiscene.flytech.excel;

import com.github.weiss.core.utils.FileUtils;
import com.github.weiss.core.utils.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hiscene.flytech.C;
import com.hiscene.flytech.entity.ExecuteModel;
import com.hiscene.flytech.entity.ProcessModel;
import com.hiscene.flytech.util.GsonUtil;
import com.hiscene.flytech.util.POIUtil;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bouncycastle.math.ec.ScaleYPointMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.hiscene.flytech.C.OUTPUT_PATH;

/**
 * @author huangyu
 * @des 二次措施单表格
 */
public class ExecuteExcel implements IExcel {

    public List<ExecuteModel> executeModelList = new ArrayList<>();

    @Override
    public void read() {
        try {
            XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(C.ASSETS_PATH + C.EXECUTE_FILE));
            // replace the dummy-content to show that we could write and read the cell-values
            Sheet sheet = wb.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if(row==null){
                    break;
                }
                if (row != null) {
                            row.getCell(0).setCellType(CellType.STRING);
                            executeModelList.add(new ExecuteModel(
                            row.getCell(0).getStringCellValue() ,//此处类型不一致
                            row.getCell(3).getStringCellValue()
                    ));
                }
            }
            System.out.println("sheet.getLastRowNum(): "+sheet.getLastRowNum());
            System.out.println("executeModelList: "+executeModelList.size());
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
            for(ExecuteModel executeModel:executeModelList){
                switch (executeModel.getExcute_result()){
                    case 0:
                        result="×";
                        break;
                    case 1:
                        result="√";
                        break;
                    case -1:
                        result="无";
                        break;
                }
                resultList.add(result);
            }
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
        String read_content=FileUtils.readFile2String(C.TEMP_EXCUTE_FILE,"utf-8");
        Type type = new TypeToken<List<ExecuteModel>>(){}.getType();
        executeModelList=new Gson().fromJson(read_content,type);
//        System.out.println(processExcelList);
    }

    @Override
    public void svae() {
        //将List转化为json,并写入缓存目录
        if(executeModelList!=null&&executeModelList.size()>0){
            String write_content=GsonUtil.gsonString(executeModelList.toArray());
            if(!StringUtils.isEmpty(write_content)){
                if(FileUtils.createFileByDeleteOldFile(C.TEMP_EXCUTE_FILE)){
                    boolean a=FileUtils.writeFileFromString(C.TEMP_EXCUTE_FILE,write_content,true);
                }
            }
        }

        restore();

    }
}
