package com.hiscene.flytech.excel;

import com.github.weiss.core.utils.CollectionUtils;
import com.github.weiss.core.utils.FileUtils;
import com.github.weiss.core.utils.LogUtils;
import com.github.weiss.core.utils.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hiscene.flytech.C;
import com.hiscene.flytech.entity.AttachFirstModel;
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
    public List<AttachFirstModel> attachFirstModels = new ArrayList<>();

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
            LogUtils.d("sheet.getLastRowNum(): "+sheet.getLastRowNum());
            LogUtils.d("processExcelList: "+processExcelList.size());
            wb.close();
            attachFirstModels.add(new AttachFirstModel(1,"111","1111"));
            attachFirstModels.add(new AttachFirstModel(2,"222","2222"));
            attachFirstModels.add(new AttachFirstModel(3,"333","3333"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write() {
        try {
            List<String> resultList=new ArrayList<>();
            String result="";
            for(ProcessModel processModel:processExcelList){
                switch (processModel.getResult()){
                    case 0:
                        result="确认(×)";
                        break;
                    case 1:
                        result="确认(√)";
                        break;
                    case -1:
                        result="确认(无)";
                        break;
                }
                resultList.add(result);
            }
            POIUtil.setCellValueAt(C.ASSETS_PATH + C.PROCESS_FILE,OUTPUT_PATH+C.PROCESS_FILE,2,4,resultList);
            LogUtils.d("已成功修改表格内容");

            //写附表1
        } catch (Exception e) {
            LogUtils.d(e.getMessage());
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
        read_content=FileUtils.readFile2String(C.TEMP_ATTACH_FIRST_FILE,"utf-8");
        type=new TypeToken<AttachFirstModel>(){}.getType();
        attachFirstModels=new Gson().fromJson(read_content,type);
    }

    @Override
    public void svae() {
        //将List转化为json,并写入缓存目录
        if(processExcelList!=null&&processExcelList.size()>0){
            String write_content=GsonUtil.gsonString(processExcelList.toArray());
            if(!StringUtils.isEmpty(write_content)){
                if(FileUtils.createFileByDeleteOldFile(C.TEMP_PROCESS_FILE)){
                    boolean result=FileUtils.writeFileFromString(C.TEMP_PROCESS_FILE,write_content,true);
                    LogUtils.d("save 缓存:"+result);
                }
            }
        }
        if(!CollectionUtils.isEmpty(attachFirstModels)){
            String write_content=GsonUtil.gsonString(attachFirstModels.toArray());
            if(!StringUtils.isEmpty(write_content)) {
                if (FileUtils.createFileByDeleteOldFile(C.TEMP_ATTACH_FIRST_FILE)) {
                    boolean result = FileUtils.writeFileFromString(C.TEMP_ATTACH_FIRST_FILE, write_content, true);
                    LogUtils.d("save 缓存:" + result);
                }
            }
        }
    }

}
