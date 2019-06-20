package com.hiscene.flytech.excel;

import com.hiscene.flytech.C;
import com.hiscene.flytech.util.GsonUtil;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Minamo
 * @e-mail kleinminamo@gmail.com
 * @time 2019/6/17
 * @des 工作过程表格
 */
public class ProcessExcel implements IExcel {

    public List<ProcessExcel> processExcelList = new ArrayList<>();

    //序号
    public int id;

    //作业内容
    public String content;

    //作业标准
    public String standard;

    //作业结果 false=0, true=1, null=-1
    public int result;

    public ProcessExcel() {
    }

    public ProcessExcel(int id, String content, String standard) {
        this(id, content, standard, -1);
    }

    public ProcessExcel(int id, String content, String standard, int result) {
        this.id = id;
        this.content = content;
        this.standard = standard;
        this.result = result;
    }

    @Override
    public void read() {
        try {
            XSSFWorkbook wb = new XSSFWorkbook(new File(C.ASSETS_PATH + C.PROCESS_FILE));
            // replace the dummy-content to show that we could write and read the cell-values
            Sheet sheet = wb.getSheetAt(0);
            for (int i = 1; i < sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    processExcelList.add(new ProcessExcel((int) row.getCell(0).getNumericCellValue(),
                            row.getCell(1).getStringCellValue(), row.getCell(2).getStringCellValue()));
                }
            }
            wb.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write() {
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

    }

    @Override
    public void svae() {
        //将List转化为json,并写入缓存目录
        if(processExcelList!=null&&processExcelList.size()>0){
            String string=GsonUtil.gsonString(processExcelList);
            System.out.println(string);
        }
    }
}
