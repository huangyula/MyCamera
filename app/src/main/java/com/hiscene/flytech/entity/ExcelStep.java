package com.hiscene.flytech.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hiscene.flytech.util.GsonUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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
    public static List<ExcelStep> test() {
//        List<ExcelStep> excelSteps = new ArrayList<>();
//        int steps = 10;
//        for (int i = 0; i < steps; i++) {
//            excelSteps.add(new ExcelStep(ExcelStyle.PROCESS_EXCEL, i));
//        }

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
        System.out.println("执行步骤: "+excelSteps.size());
        //作业过程步骤
        for(int i=0;i<50;i++){
            excelSteps.add(new ExcelStep(ExcelStyle.PROCESS_EXCEL, i,0));
        }
        return excelSteps;
    }
}
