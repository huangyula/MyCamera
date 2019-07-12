package com.hiscene.flytech;

import com.github.weiss.core.utils.FileUtils;
import com.github.weiss.core.utils.LogUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hiscene.flytech.entity.ExcelStep;
import com.hiscene.flytech.entity.ExcelStyle;
import com.hiscene.flytech.entity.ExecuteModel;
import com.hiscene.flytech.entity.ProcessModel;
import com.hiscene.flytech.util.GsonUtil;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args){
        testStep_1();
    }

    public static  void testStep(){
        String json="[\n" +
                "{\"style\":\"0\",\"step\":\"0\",\"childSteps\":null,\"childCount\":\"4\"},\n" +
                "{\"style\":\"0\",\"step\":\"1\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
                "{\"style\":\"0\",\"step\":\"2\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
                "{\"style\":\"0\",\"step\":\"3\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
                "{\"style\":\"0\",\"step\":\"4\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
                "{\"style\":\"0\",\"step\":\"5\",\"childSteps\":null,\"childCount\":\"4\"},\n" +
                "{\"style\":\"0\",\"step\":\"6\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
                "{\"style\":\"0\",\"step\":\"7\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
                "{\"style\":\"0\",\"step\":\"8\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
                "{\"style\":\"0\",\"step\":\"9\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
                "{\"style\":\"0\",\"step\":\"10\",\"childSteps\":null,\"childCount\":\"1\"},\n" +
                "{\"style\":\"0\",\"step\":\"11\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
                "{\"style\":\"0\",\"step\":\"12\",\"childSteps\":null,\"childCount\":\"3\"},\n" +
                "{\"style\":\"0\",\"step\":\"13\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
                "{\"style\":\"0\",\"step\":\"14\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
                "{\"style\":\"0\",\"step\":\"15\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
                "{\"style\":\"0\",\"step\":\"16\",\"childSteps\":null,\"childCount\":\"3\"},\n" +
                "{\"style\":\"0\",\"step\":\"17\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
                "{\"style\":\"0\",\"step\":\"18\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
                "{\"style\":\"0\",\"step\":\"19\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
                "{\"style\":\"0\",\"step\":\"20\",\"childSteps\":null,\"childCount\":\"2\"},\n" +
                "{\"style\":\"0\",\"step\":\"21\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
                "{\"style\":\"0\",\"step\":\"22\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
                "{\"style\":\"0\",\"step\":\"23\",\"childSteps\":null,\"childCount\":\"2\"},\n" +
                "{\"style\":\"0\",\"step\":\"24\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
                "{\"style\":\"0\",\"step\":\"25\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
                "{\"style\":\"0\",\"step\":\"26\",\"childSteps\":null,\"childCount\":\"3\"},\n" +
                "{\"style\":\"0\",\"step\":\"27\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
                "{\"style\":\"0\",\"step\":\"28\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
                "{\"style\":\"0\",\"step\":\"29\",\"childSteps\":null,\"childCount\":\"0\"}" +
                "]";


        Type type = new TypeToken<List<ExcelStep>>(){}.getType();
        List<ExcelStep> excelStepListTemp=new Gson().fromJson(json,type);
        LogUtils.d("excelStepListTemp: "+excelStepListTemp);
        List<ExcelStep> excelSteps= new ArrayList<>();
        ExcelStep excelStep;
        for(int i=0;i<excelStepListTemp.size();i++){
            excelStep=excelStepListTemp.get(i);
            if(excelStep.childCount!=0){//是大步骤
                excelStep.childSteps=excelStepListTemp.subList(i+1,i+excelStep.childCount);
                excelSteps.add(excelStep);
                i=i+excelStep.childCount;
            }

        }
        System.out.println(excelSteps.toString());
    }

    private static void  testStep_1(){
        List<ExcelStep> excelSteps= new ArrayList<>();
        List<ExcelStep> childSteps= new ArrayList<>();
        ExcelStep excelStep;
        int step=0;
        int childCount=0;

        String execute_setting="4.4.1.3.3.2.2.3";
        String[] bigStep=execute_setting.split("\\.");

        for(int i=0;i<bigStep.length;i++){
            childCount=Integer.valueOf(bigStep[i]);
            excelStep=new ExcelStep(ExcelStyle.EXCUTE_EXCEL,step,childCount);
            childSteps=new ArrayList<>();
            for(int j=0;j<childCount;j++){
                childSteps.add(new ExcelStep(ExcelStyle.EXCUTE_EXCEL,++step,0));
            }
            excelStep.childSteps=childSteps;
            excelSteps.add(excelStep);
            step++;
        }
        System.out.println(excelSteps.size()+"");
    }





}
