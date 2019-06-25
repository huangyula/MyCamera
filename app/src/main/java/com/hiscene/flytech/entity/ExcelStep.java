package com.hiscene.flytech.entity;

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
    public List<ExcelStep> chilSteps;


    public ExcelStep(int style, int step) {
        this(style, step, null);
    }

    public ExcelStep(int style, int step, List<ExcelStep> chilSteps) {
        this.style = style;
        this.step = step;
        this.chilSteps = chilSteps;
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

        List<ExcelStep> excelSteps = new ArrayList<>();
        List<ExcelStep> childSteps=new ArrayList<>();
        ExcelStep excelStep1=new ExcelStep(ExcelStyle.EXCUTE_EXCEL,0);
        ExcelStep excelStep2=new ExcelStep(ExcelStyle.EXCUTE_EXCEL,1);
        ExcelStep excelStep3=new ExcelStep(ExcelStyle.EXCUTE_EXCEL,2);
        ExcelStep excelStep4=new ExcelStep(ExcelStyle.EXCUTE_EXCEL,3);
        ExcelStep excelStep5=new ExcelStep(ExcelStyle.EXCUTE_EXCEL,4);
        ExcelStep excelStep6=new ExcelStep(ExcelStyle.EXCUTE_EXCEL,5);
        childSteps.add(excelStep2);
        childSteps.add(excelStep3);
        childSteps.add(excelStep4);
        childSteps.add(excelStep5);
        excelStep1.chilSteps=childSteps;
        excelSteps.add(excelStep1);
//        excelSteps.add(excelStep2);
//        excelSteps.add(excelStep3);
//        excelSteps.add(excelStep4);
//        excelSteps.add(excelStep5);
        excelSteps.add(excelStep6);
//        int steps = 10;
//        for (int i = 0; i < steps; i++) {
//            excelSteps.add(new ExcelStep(ExcelStyle.EXCUTE_EXCEL, i));
//        }
        return excelSteps;
    }
}
