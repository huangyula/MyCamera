package com.hiscene.flytech.excel;

import android.content.Intent;
import android.net.Uri;

import com.github.weiss.core.base.BaseApp;
import com.github.weiss.core.utils.CollectionUtils;
import com.github.weiss.core.utils.FileUtils;
import com.github.weiss.core.utils.LogUtils;
import com.github.weiss.core.utils.SPUtils;
import com.github.weiss.core.utils.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hiscene.flytech.C;
import com.hiscene.flytech.entity.ExecuteModel;
import com.hiscene.flytech.entity.ProcessModel;
import com.hiscene.flytech.entity.Setting;
import com.hiscene.flytech.event.EventCenter;
import com.hiscene.flytech.util.GsonUtil;
import com.hiscene.flytech.util.POIUtil;
import com.hiscene.flytech.util.PositionUtil;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.util.ArrayUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bouncycastle.math.ec.ScaleYPointMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.hiscene.flytech.C.EXCEL_WRITE_ERROR;
import static com.hiscene.flytech.C.OUTPUT_PATH;
import static com.hiscene.flytech.C.START_TIME_BEGIN;
import static com.hiscene.flytech.ui.dialog.ExcelDialogManager.EXCEL_END_TIME;
import static com.hiscene.flytech.ui.fragment.ExcelFragmentManager.START_TIME;

/**
 * @author huangyu
 * @des 二次措施单表格
 */
public class ExecuteExcel implements IExcel {

    public List<ExecuteModel> executeModelList = new ArrayList<>();
    private List<Setting> settingList=new ArrayList<>();
    List<String> skipList=new ArrayList<>();

    @Override
    public void read() {
        try {
            boolean flag=false;
            executeModelList.clear();
            XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(C.ASSETS_PATH + C.EXECUTE_FILE));
            // replace the dummy-content to show that we could write and read the cell-values
            Sheet sheet = wb.getSheetAt(0);
            for (int i = C.EXECUTE_BEGIN-1; i <= C.EXECUTE_END-1; i++) {
                flag=false;
                for(String s:skipList){//跳过不需要读写的行数
                    if(i==Integer.parseInt(s)-1){
                        flag=true;
                        break;
                    }

                }
                if(flag){
                    continue;
                }
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
            LogUtils.d("sheet.getLastRowNum(): "+sheet.getLastRowNum());
            LogUtils.d("executeModelList: "+executeModelList.size());
            wb.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write() {
        try {
            String path= OUTPUT_PATH+SPUtils.getString(EXCEL_END_TIME)+File.separator+C.EXECUTE_FILE;
            POIUtil.setCellValueAtExecute(C.ASSETS_PATH + C.EXECUTE_FILE,path,executeModelList,skipList);
            LogUtils.d("已成功修改表格内容");
            //及时刷新文件
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(new File((path)))); // 需要更新的文件路径
            BaseApp.getAppContext().sendBroadcast(intent);
        } catch (Exception e) {
            EventCenter.getInstance().post(EXCEL_WRITE_ERROR);
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
        String read_content=FileUtils.readFile2String(C.TEMP_EXCUTE_FILE,"utf-8");
        Type type = new TypeToken<List<ExecuteModel>>(){}.getType();
        executeModelList=new Gson().fromJson(read_content,type);
    }

    @Override
    public void svae() {
        //将List转化为json,并写入缓存目录
        if(executeModelList!=null&&executeModelList.size()>0){
            String write_content=GsonUtil.gsonString(executeModelList.toArray());
            if(!StringUtils.isEmpty(write_content)){
                if(FileUtils.createFileByDeleteOldFile(C.TEMP_EXCUTE_FILE)){
                    boolean result=FileUtils.writeFileFromString(C.TEMP_EXCUTE_FILE,write_content,true);
                    LogUtils.d("save 缓存:"+result);
                }
            }
        }

    }

    public void setSettingList( List<Setting> settingList ) {
        clearSettingList();
        this.settingList = settingList;
        C.EXECUTE_BEGIN=StringUtils.strArrayToIntArray(settingList.get(0).start_end.split("\\."))[0];
        C.EXECUTE_END=StringUtils.strArrayToIntArray(settingList.get(0).start_end.split("\\."))[1];
        String skip=settingList.get(0).skip;
        if(skip.length()>0){
            if(settingList.get(0).skip.contains(".")){
                skipList= CollectionUtils.arrayToList(skip.split("\\."));
            }else {
                skipList.add(skip);
            }
        }
    }

    private void clearSettingList(){
        settingList.clear();
        skipList.clear();
    }
}
