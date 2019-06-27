package com.hiscene.flytech.util;

import com.github.weiss.core.utils.CollectionUtils;
import com.hiscene.flytech.entity.ExcelStep;

import java.util.List;

/**
 * @author Minamo
 * @e-mail kleinminamo@gmail.com
 * @time 2019/6/17
 * @des 当前位置管理工具类
 */
public class PositionUtil {

    public static final String POSITION = "Position";

    /**
     * 上一步
     */
    public static String previousStep(String pos, List<ExcelStep> excelSteps) {
        //pos执行上一步
//        String[] posStrArr = pos.split("\\.");
//        int[] posArr = new int[posStrArr.length];
//        for (int i = 0; i < posArr.length; i++) {
//            posArr[i] = Integer.parseInt(posStrArr[i]);
//        }
//        ExcelStep excelStep = excelSteps.get(posArr[0]);
//        if (posArr.length > 1 && -1 != posArr[1]) {//如果有小步骤
//            if (posArr[1]>=1) {
//                posArr[1]--;
//            } else {
//                posArr[0]--;
//                posArr[1] = -1;
//            }
//        } else {
//            posArr[0]--;
//            posArr[1] = -1;
//        }
//        pos = posArr[0] + "." + posArr[1];
//        return pos;

        String[] posStrArr = pos.split("\\.");
        int[] posArr = new int[posStrArr.length];
        for (int i = 0; i < posArr.length; i++) {
            posArr[i] = Integer.parseInt(posStrArr[i]);
        }
        ExcelStep excelStep = excelSteps.get(posArr[0]);
        if (excelStep.childSteps!=null&&excelStep.childSteps.size()>0) {//如果有小步骤
            if (posArr[1]>=1) {
                posArr[1]--;
            } else {
                posArr[0]--;
                if(posArr[0]>=0){
                    posArr[1] =excelSteps.get(posArr[0]).childSteps.size()-1;
                }else {
                    posArr[1]=0;
                }

            }
        } else {
            posArr[0]--;
            posArr[1] = 0;
        }
        pos = posArr[0] + "." + posArr[1];
        return pos;

    }

    /**
     * 下一步
     */
    public static String nextStep(String pos, List<ExcelStep> excelSteps) {
        //pos指向下一步
//        String[] posStrArr = pos.split("\\.");
//        int[] posArr = new int[posStrArr.length];
//        for (int i = 0; i < posArr.length; i++) {
//            posArr[i] = Integer.parseInt(posStrArr[i]);
//        }
//        ExcelStep excelStep = excelSteps.get(posArr[0]);
//        if (posArr.length > 1 && -1 != posArr[1]) {//如果有小步骤
//            if (excelStep.chilSteps.size() > posArr[1] + 1) {
//                posArr[1]++;
//            } else {
//                posArr[0]++;
//                posArr[1] = -1;
//            }
//        } else {
//            posArr[0]++;
//            posArr[1] = -1;
//        }
//        pos = posArr[0] + "." + posArr[1];
//        return pos;

        String[] posStrArr = pos.split("\\.");
        int[] posArr = new int[posStrArr.length];
        for (int i = 0; i < posArr.length; i++) {
            posArr[i] = Integer.parseInt(posStrArr[i]);
        }
        ExcelStep excelStep = excelSteps.get(posArr[0]);
        if (excelStep.childSteps!=null&&excelStep.childSteps.size()>0) {//如果有小步骤
            if (excelStep.childSteps.size() > posArr[1] + 1) {
                posArr[1]++;
            } else {
                posArr[0]++;
                posArr[1] = 0;
            }
        } else {
            posArr[0]++;
            posArr[1] = 0;
        }
        pos = posArr[0] + "." + posArr[1];
        return pos;
    }

    public static boolean islastStep(String pos, List<ExcelStep> excelSteps) {
        //TODO
        String[] posStrArr = pos.split("\\.");
        int[] posArr = new int[posStrArr.length];
        for (int i = 0; i < posArr.length; i++) {
            posArr[i] = Integer.parseInt(posStrArr[i]);
        }

//        ExcelStep excelStep = excelSteps.get(posArr[0]);
//        if (posArr.length > 1 && -1 != posArr[1]) {//如果有小步骤
//            if(excelSteps.size()==posArr[0]&&excelStep.chilSteps.size() == posArr[1]){
//                return true;
//            }
//
//        } else {
//            if(excelSteps.size()==posArr[0]){
//                return true;
//            }
//        }

        if(posArr[0]>=excelSteps.size()) {
            System.out.println("posArr[0],excelSteps.size() "+posArr[0]+" "+excelSteps.size());
            System.out.println("已经是最后一步了");
            return true;
//            if (CollectionUtils.isEmpty(excelSteps.get(excelSteps.size() - 1).chilSteps)) {
//                    return true;
//            }
//            if (posArr[1] >= excelSteps.get(excelSteps.size() - 1).chilSteps.size()) {
//
//                return true;
//            }
        }

        return false;
    }


    public static boolean isFirstStep(String pos, List<ExcelStep> excelSteps) {
        //TODO
        String[] posStrArr = pos.split("\\.");
        int[] posArr = new int[posStrArr.length];
        for (int i = 0; i < posArr.length; i++) {
            posArr[i] = Integer.parseInt(posStrArr[i]);
        }

        if(posArr[0]<=0&&posArr[1]<=0) {
            return true;
        }

        return false;
    }

    /**
     * 根据当前步骤从excelSteps中获取到ExcelStep
     *
     * @return ExcelStep
     */
    public static ExcelStep pos2ExcelStep(String pos, List<ExcelStep> excelSteps) {
//        String[] posArr = pos.split("\\.");
//        ExcelStep excelStep = excelSteps.get(Integer.parseInt(posArr[0]));
//        if (posArr.length > 1 && !"-1".equals(posArr[1])) {
//            excelStep = excelSteps.get(Integer.parseInt(posArr[0])).chilSteps.get(Integer.parseInt(posArr[1]));
//        }
//        return excelStep;

        String[] posArr = pos.split("\\.");
        ExcelStep excelStep = excelSteps.get(Integer.parseInt(posArr[0]));
//        if (excelStep.chilSteps!=null  && !"-1".equals(posArr[1])) {
//            excelStep = excelSteps.get(Integer.parseInt(posArr[0])).chilSteps.get(Integer.parseInt(posArr[1]));
//        }
        return excelStep;
    }
}
