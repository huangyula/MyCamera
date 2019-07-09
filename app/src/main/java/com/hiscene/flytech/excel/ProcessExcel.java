package com.hiscene.flytech.excel;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.github.weiss.core.base.BaseApp;
import com.github.weiss.core.utils.CollectionUtils;
import com.github.weiss.core.utils.FileUtils;
import com.github.weiss.core.utils.LogUtils;
import com.github.weiss.core.utils.SPUtils;
import com.github.weiss.core.utils.StringUtils;
import com.github.weiss.core.utils.TimeUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hiscene.flytech.C;
import com.hiscene.flytech.R;
import com.hiscene.flytech.entity.AttachFirstModel;
import com.hiscene.flytech.entity.AttachFourModel;
import com.hiscene.flytech.entity.AttachSecondModel;
import com.hiscene.flytech.entity.AttachThreeModel;
import com.hiscene.flytech.entity.ProcessModel;
import com.hiscene.flytech.entity.Result;
import com.hiscene.flytech.event.EventCenter;
import com.hiscene.flytech.util.GsonUtil;
import com.hiscene.flytech.util.POIUtil;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipFile;

import static com.hiscene.flytech.C.ASSETS_PATH;
import static com.hiscene.flytech.C.ATTACH_ONE_ROW_BEGIN;
import static com.hiscene.flytech.C.ATTACH_ONE_ROW_END;
import static com.hiscene.flytech.C.ATTACH_SECOND_ROW_BEGIN;
import static com.hiscene.flytech.C.ATTACH_SECONG_ROW_END;
import static com.hiscene.flytech.C.EXCEL_WRITE_ERROR;
import static com.hiscene.flytech.C.EXCEL_WRITE_SUCCESS;
import static com.hiscene.flytech.C.OUTPUT_PATH;
import static com.hiscene.flytech.C.PROCESS_ROW_BEGIN;
import static com.hiscene.flytech.C.PROCESS_ROW_END;
import static com.hiscene.flytech.C.START_TIME_BEGIN;
import static com.hiscene.flytech.ui.fragment.ExcelFragmentManager.START_TIME;

/**
 * @author Minamo
 * @e-mail kleinminamo@gmail.com
 * @time 2019/6/17
 * @des 工作过程表格
 */
public class ProcessExcel implements IExcel {

    public List<ProcessModel> processExcelList = new ArrayList<>();
    public List<AttachFirstModel> attachFirstModels = new ArrayList<>();
    public List<AttachSecondModel> attachSecondModelList = new ArrayList<>();
    public List<AttachThreeModel> attachThreeModelList = new ArrayList<>();
    public List<AttachFourModel> attachFourModelList = new ArrayList<>();



    @Override
    public void read() {
        try {

            // 延迟解析比率
//            ZipSecureFile.setMinInflateRatio(-1.0d);
            XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(new File(ASSETS_PATH + C.PROCESS_FILE)));
            // replace the dummy-content to show that we could write and read the cell-values
            Sheet sheet = wb.getSheetAt(0);
            //作业过程：行10-59  列
            for (int i = PROCESS_ROW_BEGIN-1; i <PROCESS_ROW_END; i++) {
                Row row = sheet.getRow(i);
                if(row==null){//防止空行导致获取到的总行数不正确
                    break;
                }
                if (row != null) {
                    row.getCell(0).setCellType(CellType.NUMERIC);
                    processExcelList.add(new ProcessModel((int) row.getCell(0).getNumericCellValue(),
                            row.getCell(1).getStringCellValue(), row.getCell(2).getStringCellValue()));
                }
            }
            LogUtils.d("sheet.getLastRowNum(): "+sheet.getLastRowNum());
            LogUtils.d("processExcelList: "+processExcelList.size());

            //附表1 行：73-75
            for(int i=ATTACH_ONE_ROW_BEGIN-1;i<ATTACH_ONE_ROW_END;i++){
                Row row=sheet.getRow(i);
                if(row!=null){
                    row.getCell(0).setCellType(CellType.NUMERIC);
                    attachFirstModels.add(new AttachFirstModel((int)row.getCell(0).getNumericCellValue(),row.getCell(1).getStringCellValue(),row.getCell(2).getStringCellValue()));
                }
            }

            //附表2
            for(int i=ATTACH_SECOND_ROW_BEGIN-1;i<ATTACH_SECONG_ROW_END;i++){
                Row row=sheet.getRow(i);
                if(row!=null){
                    row.getCell(0).setCellType(CellType.NUMERIC);
                    String name=row.getCell(1).getStringCellValue();
                    attachSecondModelList.add(new AttachSecondModel((int)row.getCell(0).getNumericCellValue(),name));
                    LogUtils.d("name:",attachSecondModelList.get(0).name);
                }
            }


            //附表3
            AttachThreeModel attachThreeModel;
            List<String> titles=CollectionUtils.arrayToList(BaseApp.getAppContext().getResources().getStringArray(R.array.third_excel_title));
            String item_name="",item_no="";
            int result_1,result_2;
            for(int i=0;i<18;i++){//主一裝置通道
                result_1=i/6;
                result_2=i%6;
                switch (result_1){
                    case 0:
                        item_name="主一装置通道";
                        item_no="项目1";
                        break;
                    case 1:
                        item_name="主二装置通道";
                        item_no="项目2";
                        break;
                    case 2:
                        item_name="光电转换装置通道";
                        item_no="项目3";
                        break;
                }
                attachThreeModel=new AttachThreeModel(item_name,item_no,titles.get(result_2),"","");
                attachThreeModelList.add(attachThreeModel);
            }
            wb.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //附表4
        attachFourModelList.add(new AttachFourModel("",""));
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
                    String path= OUTPUT_PATH+C.PROCESS+"-"+SPUtils.getString(START_TIME)+".xlsx";//OUTPUT_PATH+C.PROCESS_FILE
                    POIUtil.setCellValueAt(ASSETS_PATH + C.PROCESS_FILE,path,PROCESS_ROW_BEGIN,4,resultList);

                    //附表1
                    List<String> results=new ArrayList<>();
                    for(AttachFirstModel attachFirstModel:attachFirstModels){
                        results.add(attachFirstModel.result+" MΩ");
                    }
                    POIUtil.setCellValueAt(path,path,ATTACH_ONE_ROW_BEGIN,4,results);

                    //附表2 列3，4，5，6
                    POIUtil.setCellValueAtSecond(path,path, ATTACH_SECOND_ROW_BEGIN,attachSecondModelList);

                    //附表3
                    POIUtil.setCellValueAtThird(path,path,C.ATTACH_THIRD_ROW_BEGIN_1,attachThreeModelList);

                    //附表4
                    POIUtil.setCellValueAtFour(path,path,C.ATTACH_FOUR_ROW_BEGIN,attachFourModelList);

                    //基本信息 列D,F
                    int[] col_array=new int[]{4,6};
                    results.clear();
                    LogUtils.d(SPUtils.getString(START_TIME));
                    results.add(SPUtils.getString(START_TIME));//作业开始时间
                    results.add(TimeUtils.getNowTimeString());//作业结束时间
                    POIUtil.setCellValueAtMulCol(path,path,START_TIME_BEGIN,col_array,results);

                    //及时刷新文件
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.fromFile(new File((path)))); // 需要更新的文件路径
                    BaseApp.getAppContext().sendBroadcast(intent);


                    EventCenter.getInstance().post(new Result(EXCEL_WRITE_SUCCESS,""));
                    LogUtils.d("已成功修改表格内容");
                } catch (Exception e) {
                    Result result=new Result(EXCEL_WRITE_ERROR,e.getMessage());
                    EventCenter.getInstance().post(result);
                    LogUtils.d(e.getMessage());
                    e.printStackTrace();
                }
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

    @Override
    public void restore() {
        //从缓存文件中读取内容
        String read_content=FileUtils.readFile2String(C.TEMP_PROCESS_FILE,"utf-8");
        Type type = new TypeToken<List<ProcessModel>>(){}.getType();
        processExcelList=new Gson().fromJson(read_content,type);

        read_content=FileUtils.readFile2String(C.TEMP_ATTACH_FIRST_FILE,"utf-8");
        type=new TypeToken<List<AttachFirstModel>>(){}.getType();
        attachFirstModels=new Gson().fromJson(read_content,type);

        //附表2
        read_content=FileUtils.readFile2String(C.TEMP_ATTACH_SECOND_FILE,"utf-8");
        type=new TypeToken<List<AttachSecondModel>>(){}.getType();
        attachSecondModelList=new Gson().fromJson(read_content,type);

        //附表3
        read_content=FileUtils.readFile2String(C.TEMP_ATTACH_THREE_FILE,"utf-8");
        type=new TypeToken<List<AttachThreeModel>>(){}.getType();
        attachThreeModelList=new Gson().fromJson(read_content,type);

        //附表4
        read_content=FileUtils.readFile2String(C.TEMP_ATTACH_FOUR_FILE,"utf-8");
        type=new TypeToken<List<AttachFourModel>>(){}.getType();
        attachFourModelList=new Gson().fromJson(read_content,type);
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
        //附表2
        if(!CollectionUtils.isEmpty(attachSecondModelList)){
            String write_content=GsonUtil.gsonString(attachSecondModelList.toArray());
            if(!StringUtils.isEmpty(write_content)) {
                if (FileUtils.createFileByDeleteOldFile(C.TEMP_ATTACH_SECOND_FILE)) {
                    boolean result = FileUtils.writeFileFromString(C.TEMP_ATTACH_SECOND_FILE, write_content, true);
                    LogUtils.d("save 缓存:" + result);
                }
            }
        }
        //附表3
        if(!CollectionUtils.isEmpty(attachThreeModelList)){
            String write_content=GsonUtil.gsonString(attachThreeModelList.toArray());
            if(!StringUtils.isEmpty(write_content)) {
                if (FileUtils.createFileByDeleteOldFile(C.TEMP_ATTACH_THREE_FILE)) {
                    boolean result = FileUtils.writeFileFromString(C.TEMP_ATTACH_THREE_FILE, write_content, true);
                    LogUtils.d("save 缓存:" + result);
                }
            }
        }
        //附表4
        if(!CollectionUtils.isEmpty(attachFourModelList)){
            String write_content=GsonUtil.gsonString(attachFourModelList.toArray());
            if(!StringUtils.isEmpty(write_content)) {
                if (FileUtils.createFileByDeleteOldFile(C.TEMP_ATTACH_FOUR_FILE)) {
                    boolean result = FileUtils.writeFileFromString(C.TEMP_ATTACH_FOUR_FILE, write_content, true);
                    LogUtils.d("save 缓存:" + result);
                }
            }
        }
    }

}
