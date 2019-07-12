package com.hiscene.flytech.util;

import com.github.weiss.core.utils.CollectionUtils;
import com.github.weiss.core.utils.LogUtils;
import com.hiscene.flytech.entity.ExcelStep;
import com.hiscene.flytech.entity.ExcelStyle;

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
        int[] posArr=pos2posArr(pos);
        ExcelStep excelStep = excelSteps.get(posArr[0]);
        if(!CollectionUtils.isEmpty(excelStep.childSteps)){//如果有小步骤
            if (posArr[1]>=1) {
                posArr[1]--;
            } else {
                posArr[0]--;
                if(posArr[0]>=0&&!CollectionUtils.isEmpty(excelSteps.get(posArr[0]).childSteps)){
                    posArr[1] =excelSteps.get(posArr[0]).childSteps.size()-1;
                }else {
                    posArr[1]=0;
                }

            }
        } else {
            posArr[0]--;
            if(posArr[0]>=0&&!CollectionUtils.isEmpty(excelSteps.get(posArr[0]).childSteps)){
                posArr[1] =excelSteps.get(posArr[0]).childSteps.size()-1;
            }else {
                posArr[1]=0;
            }
        }
        pos = posArr[0] + "." + posArr[1];
        return pos;

    }

    /**
     * 下一步
     */
    public static String nextStep(String pos, List<ExcelStep> excelSteps) {
        //pos指向下一步
        int[] posArr=pos2posArr(pos);
        ExcelStep excelStep = excelSteps.get(excelSteps.size()-1);
        if (!CollectionUtils.isEmpty(excelStep.childSteps)) {//如果有小步骤
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
        //判断最后一步
        //两种情况：1.最后一步是是大步骤,没有小步骤 2.最后一步是小步骤
        int[] posArr=pos2posArr(pos);
        ExcelStep excelStep=excelSteps.get(excelSteps.size()-1);//最后一个大步骤
        if(!CollectionUtils.isEmpty(excelStep.childSteps)){//最后大步骤有小步骤
            if(posArr[0]>=excelSteps.size()-1 && posArr[1]>=excelStep.childSteps.size()-1){
                LogUtils.d("已经是最后一步了");
                return true;
            }
        }else {
            if(posArr[0]>=excelSteps.size()-1){
                LogUtils.d("已经是最后一步了");
                return true;
            }
        }
        return false;
    }


    public static boolean isFirstStep(String pos, List<ExcelStep> excelSteps) {
        int[] posArr=pos2posArr(pos);

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
        String[] posArr = pos.split("\\.");
        ExcelStep excelStep = excelSteps.get(Integer.parseInt(posArr[0]));
        return excelStep;
    }

    public static ExcelStep pos2ExcelStep_ChildStep(String pos, List<ExcelStep> excelSteps) {
        int[] posArr = pos2posArr(pos);
        ExcelStep excelStep = excelSteps.get(posArr[0]);
        if(!CollectionUtils.isEmpty(excelStep.childSteps)){
            excelStep=excelStep.childSteps.get(posArr[1]);
        }
        return excelStep;
    }

    public static int[] pos2posArr( String pos){
        String[] posStrArr = pos.split("\\.");
        int[] posArr = new int[posStrArr.length];
        for (int i = 0; i < posArr.length; i++) {
            posArr[i] = Integer.parseInt(posStrArr[i]);
        }
        return posArr;
    }
}
