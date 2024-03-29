package com.hiscene.flytech.entity;

import android.graphics.Paint;

import com.github.weiss.core.utils.CollectionUtils;
import com.github.weiss.core.utils.FileUtils;
import com.github.weiss.core.utils.LogUtils;
import com.github.weiss.core.utils.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hiscene.flytech.C;
import com.hiscene.flytech.util.GsonUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.hiscene.flytech.C.PROCESS_ROW_BEGIN;
import static com.hiscene.flytech.C.PROCESS_ROW_END;

/**
 * @author Minamo
 * @e-mail kleinminamo@gmail.com
 * @time 2019/6/17
 * @des Excel的表格步驟
 */
public class ExcelStep {

    /**
     * 表格类型
     */
    public int style;

    /**
     * 所指表格的第几项
     */
    public int step;

    /**
     * 子步骤
     */
    public List<ExcelStep> childSteps;

    public int childCount;


    public ExcelStep(int style, int step,int childCount) {
        this(style, step, childCount,null);
    }

    public ExcelStep(int style, int step, int childCount,List<ExcelStep> chilSteps) {
        this.style = style;
        this.step = step;
        this.childSteps = chilSteps;
        this.childCount=childCount;
    }

    /**
     * 测试数据
     *
     * @return
     */
    public static List<ExcelStep> test( List<Setting> settingList, List<ProcessAttachStep> processAttachStepList) {


//        String json="[\n" +
//                "{\"style\":\"0\",\"step\":\"0\",\"childSteps\":null,\"childCount\":\"4\"},\n" +
//                "{\"style\":\"0\",\"step\":\"1\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
//                "{\"style\":\"0\",\"step\":\"2\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
//                "{\"style\":\"0\",\"step\":\"3\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
//                "{\"style\":\"0\",\"step\":\"4\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
//                "{\"style\":\"0\",\"step\":\"5\",\"childSteps\":null,\"childCount\":\"4\"},\n" +
//                "{\"style\":\"0\",\"step\":\"6\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
//                "{\"style\":\"0\",\"step\":\"7\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
//                "{\"style\":\"0\",\"step\":\"8\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
//                "{\"style\":\"0\",\"step\":\"9\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
//                "{\"style\":\"0\",\"step\":\"10\",\"childSteps\":null,\"childCount\":\"1\"},\n" +
//                "{\"style\":\"0\",\"step\":\"11\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
//                "{\"style\":\"0\",\"step\":\"12\",\"childSteps\":null,\"childCount\":\"3\"},\n" +
//                "{\"style\":\"0\",\"step\":\"13\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
//                "{\"style\":\"0\",\"step\":\"14\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
//                "{\"style\":\"0\",\"step\":\"15\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
//                "{\"style\":\"0\",\"step\":\"16\",\"childSteps\":null,\"childCount\":\"3\"},\n" +
//                "{\"style\":\"0\",\"step\":\"17\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
//                "{\"style\":\"0\",\"step\":\"18\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
//                "{\"style\":\"0\",\"step\":\"19\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
//                "{\"style\":\"0\",\"step\":\"20\",\"childSteps\":null,\"childCount\":\"2\"},\n" +
//                "{\"style\":\"0\",\"step\":\"21\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
//                "{\"style\":\"0\",\"step\":\"22\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
//                "{\"style\":\"0\",\"step\":\"23\",\"childSteps\":null,\"childCount\":\"2\"},\n" +
//                "{\"style\":\"0\",\"step\":\"24\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
//                "{\"style\":\"0\",\"step\":\"25\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
//                "{\"style\":\"0\",\"step\":\"26\",\"childSteps\":null,\"childCount\":\"3\"},\n" +
//                "{\"style\":\"0\",\"step\":\"27\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
//                "{\"style\":\"0\",\"step\":\"28\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
//                "{\"style\":\"0\",\"step\":\"29\",\"childSteps\":null,\"childCount\":\"0\"}" +
//                "]";
//
//        String json_recover="[\n" +
//                "{\"style\":\"6\",\"step\":\"0\",\"childSteps\":null,\"childCount\":\"4\"},\n" +
//                "{\"style\":\"6\",\"step\":\"1\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
//                "{\"style\":\"6\",\"step\":\"2\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
//                "{\"style\":\"6\",\"step\":\"3\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
//                "{\"style\":\"6\",\"step\":\"4\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
//                "{\"style\":\"6\",\"step\":\"5\",\"childSteps\":null,\"childCount\":\"4\"},\n" +
//                "{\"style\":\"6\",\"step\":\"6\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
//                "{\"style\":\"6\",\"step\":\"7\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
//                "{\"style\":\"6\",\"step\":\"8\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
//                "{\"style\":\"6\",\"step\":\"9\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
//                "{\"style\":\"6\",\"step\":\"10\",\"childSteps\":null,\"childCount\":\"1\"},\n" +
//                "{\"style\":\"6\",\"step\":\"11\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
//                "{\"style\":\"6\",\"step\":\"12\",\"childSteps\":null,\"childCount\":\"3\"},\n" +
//                "{\"style\":\"6\",\"step\":\"13\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
//                "{\"style\":\"6\",\"step\":\"14\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
//                "{\"style\":\"6\",\"step\":\"15\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
//                "{\"style\":\"6\",\"step\":\"16\",\"childSteps\":null,\"childCount\":\"3\"},\n" +
//                "{\"style\":\"6\",\"step\":\"17\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
//                "{\"style\":\"6\",\"step\":\"18\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
//                "{\"style\":\"6\",\"step\":\"19\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
//                "{\"style\":\"6\",\"step\":\"20\",\"childSteps\":null,\"childCount\":\"2\"},\n" +
//                "{\"style\":\"6\",\"step\":\"21\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
//                "{\"style\":\"6\",\"step\":\"22\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
//                "{\"style\":\"6\",\"step\":\"23\",\"childSteps\":null,\"childCount\":\"2\"},\n" +
//                "{\"style\":\"6\",\"step\":\"24\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
//                "{\"style\":\"6\",\"step\":\"25\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
//                "{\"style\":\"6\",\"step\":\"26\",\"childSteps\":null,\"childCount\":\"3\"},\n" +
//                "{\"style\":\"6\",\"step\":\"27\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
//                "{\"style\":\"6\",\"step\":\"28\",\"childSteps\":null,\"childCount\":\"0\"},\n" +
//                "{\"style\":\"6\",\"step\":\"29\",\"childSteps\":null,\"childCount\":\"0\"}" +
//                "]";
//
//        List<ExcelStep> excelSteps=jsonToList(json);
//        List<ExcelStep> excelSteps_recover=jsonToList(json_recover);

//        LogUtils.d("执行步骤: "+excelSteps.size());



        //作业过程步骤
//        ExcelStep excelStep;
//        for(int i=0;i<=PROCESS_ROW_END-PROCESS_ROW_BEGIN;i++){
//            excelStep= new ExcelStep(ExcelStyle.PROCESS_EXCEL, i,0);
//            if(i==3){//附表一
//                excelStep.childCount=3;
//                List<ExcelStep> stepList=new ArrayList<>();
//                stepList.add(new ExcelStep(ExcelStyle.ATTACH_FIRST_EXCEL,0,0));
//                stepList.add(new ExcelStep(ExcelStyle.ATTACH_FIRST_EXCEL,1,0));
//                stepList.add(new ExcelStep(ExcelStyle.ATTACH_FIRST_EXCEL,2,0));
//                excelStep.childSteps=stepList;
//            }
//            if(i==7){//附表二第一项
//                excelStep.childCount=1;
//                List<ExcelStep> stepList=new ArrayList<>();
//                stepList.add(new ExcelStep(ExcelStyle.ATTACH_SECOND_EXCEL,0,0));
//                excelStep.childSteps=stepList;
//            }
//            if(i==18){//附表二第二项
//                excelStep.childCount=1;
//                List<ExcelStep> stepList=new ArrayList<>();
//                stepList.add(new ExcelStep(ExcelStyle.ATTACH_SECOND_EXCEL,1,0));
//                excelStep.childSteps=stepList;
//            }
//            if(i==29){//附表二第三项
//                excelStep.childCount=1;
//                List<ExcelStep> stepList=new ArrayList<>();
//                stepList.add(new ExcelStep(ExcelStyle.ATTACH_SECOND_EXCEL,2,0));
//                excelStep.childSteps=stepList;
//            }
//            if(i==40){//附表三第一项
//                excelStep.childCount=12;
//                List<ExcelStep> stepList=new ArrayList<>();
//                //主一装置通道,主二裝置通道
//                for(int j=0;j<12;j++){
//                    stepList.add(new ExcelStep(ExcelStyle.ATTACH_THREE_EXCEL,j,0));
//                }
//                excelStep.childSteps=stepList;
//            }
//            if(i==41){//附表二第四项
//                excelStep.childCount=1;
//                List<ExcelStep> stepList=new ArrayList<>();
//                stepList.add(new ExcelStep(ExcelStyle.ATTACH_SECOND_EXCEL,3,0));
//                excelStep.childSteps=stepList;
//            }
//            if(i==42){//附表三第二项
//                excelStep.childCount=6;
//                List<ExcelStep> stepList=new ArrayList<>();
//                for(int k=12;k<18;k++){
//                    //光电转换装置通道
//                    stepList.add(new ExcelStep(ExcelStyle.ATTACH_THREE_EXCEL,k,0));
//                }
//                excelStep.childSteps=stepList;
//            }
//
//            if(i==43){//附表四
//                excelStep.childCount=1;
//                List<ExcelStep> stepList=new ArrayList<>();
//                stepList.add(new ExcelStep(ExcelStyle.ATTACH_FOUR_EXCEL,0,0));
//                excelStep.childSteps=stepList;
//            }
//            excelSteps.add(excelStep);
//        }

        //表单常量字段配置
        PROCESS_ROW_BEGIN= StringUtils.strArrayToIntArray(settingList.get(1).start_end.split("\\."))[0];
        PROCESS_ROW_END=StringUtils.strArrayToIntArray(settingList.get(1).start_end.split("\\."))[1];

        //二次措施单执行步骤
        List<ExcelStep> excelSteps=new ArrayList<>();
        excelSteps.addAll(getExcuteSteps(settingList.get(0).step,ExcelStyle.EXCUTE_EXCEL));
        //作业过程以及附表
        ExcelStep excelStep;

        int step=0;
        for(int i=PROCESS_ROW_BEGIN;i<=PROCESS_ROW_END;i++) {
            excelStep = new ExcelStep(ExcelStyle.PROCESS_EXCEL, step, 0);
            for(ProcessAttachStep processAttachStep:processAttachStepList){
                if(i==Integer.parseInt(processAttachStep.row)){
                    excelStep.childCount=Integer.valueOf(processAttachStep.count);
                    List<ExcelStep> stepList = new ArrayList<>();
                    List<String> attach_step_list=new ArrayList<>();
                    if(processAttachStep.step.length()>0){
                        if(processAttachStep.step.contains(".")){
                            attach_step_list=CollectionUtils.arrayToList(processAttachStep.step.split("\\."));
                        }else {
                            attach_step_list.add(processAttachStep.step);
                        }
                    }
                    for(int j=0;j<excelStep.childCount;j++){
                        stepList.add(new ExcelStep(Integer.valueOf(processAttachStep.attach), Integer.valueOf(attach_step_list.get(j))-1, 0));
                    }
                    excelStep.childSteps=stepList;
                    break;
                }

            }
            excelSteps.add(excelStep);
            step++;
        }

        //恢复部分
        excelSteps.addAll(getExcuteSteps(settingList.get(0).step,ExcelStyle.RECOVER_EXCEL));
        return excelSteps;
    }

    private static List<ExcelStep> jsonToList( String json){
        Type type = new TypeToken<List<ExcelStep>>(){}.getType();
        List<ExcelStep> excelStepListTemp=new Gson().fromJson(json,type);
        List<ExcelStep> excelSteps= new ArrayList<>();
        ExcelStep excelStep;
        for(int i=0;i<excelStepListTemp.size();i++){
            excelStep=excelStepListTemp.get(i);
            if(excelStep.childCount!=0){//是大步骤
                excelStep.childSteps=excelStepListTemp.subList(i+1,i+1+excelStep.childCount);
                excelSteps.add(excelStep);
                i=i+excelStep.childCount;
            }

        }
        return excelSteps;
    }

    private static List<ExcelStep>  getExcuteSteps(String execute_setting,int style){
        List<ExcelStep> excelSteps= new ArrayList<>();
        List<ExcelStep> childSteps= new ArrayList<>();
        ExcelStep excelStep;
        int step=0;
        int childCount=0;

        String[] bigStep=execute_setting.split("\\.");

        for(int i=0;i<bigStep.length;i++){
            childCount=Integer.valueOf(bigStep[i]);
            excelStep=new ExcelStep(style,step,childCount);
            childSteps=new ArrayList<>();
            for(int j=0;j<childCount;j++){
                childSteps.add(new ExcelStep(style,++step,0));
            }
            excelStep.childSteps=childSteps;
            excelSteps.add(excelStep);
            step++;
        }
        return excelSteps;
    }
    public static List<Setting> readSetting() {
        List<Setting> settingList=new ArrayList<>();
        String read_content=FileUtils.readFile2String(C.SETTING_FILE ,"utf-8");
        Type type = new TypeToken<List<Setting>>(){}.getType();
        settingList=new Gson().fromJson(read_content,type);
        return settingList;
    }

    public static List<ProcessAttachStep> readProcessStep(){
        List<ProcessAttachStep> processAttachSteps=new ArrayList<>();
        String read_content=FileUtils.readFile2String(C.SETTING_PROCESS_FILE ,"utf-8");
        Type type = new TypeToken<List<ProcessAttachStep>>(){}.getType();
        processAttachSteps=new Gson().fromJson(read_content,type);
        return processAttachSteps;
    }



}
