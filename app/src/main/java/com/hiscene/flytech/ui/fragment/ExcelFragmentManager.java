package com.hiscene.flytech.ui.fragment;

import android.support.v4.app.FragmentManager;

import com.github.weiss.core.utils.CollectionUtils;
import com.github.weiss.core.utils.SPUtils;
import com.hiscene.flytech.R;
import com.hiscene.flytech.entity.ExcelStep;
import com.hiscene.flytech.entity.ExcelStyle;
import com.hiscene.flytech.excel.ExecuteExcel;
import com.hiscene.flytech.excel.IExcel;
import com.hiscene.flytech.excel.ProcessExcel;
import com.hiscene.flytech.util.PositionUtil;

import org.json.JSONArray;

import java.util.List;

/**
 * @author Minamo
 * @e-mail kleinminamo@gmail.com
 * @time 2019/6/17
 * @des ExcelFragment状态机切换管理类
 */
public class ExcelFragmentManager {

    public static final String RECOVERY = "Recovery";

    private FragmentManager manager;
    //填表格步骤
    private List<ExcelStep> excelSteps;
    //当前步骤，-1 没有小步骤 比如：2.3 大步骤2，小步骤3。
//    private String pos = "0.-1";
    private String pos = "0.0";

    private ProcessExcelFragment processExcelFragment;
    private ExecuteExcelFragment executeExcelFragment;
    private ProcessExcel processExcel;
    private ExecuteExcel executeExcel;
    //当前表格
    private IExcel currentExcel;

    public ExcelFragmentManager(FragmentManager fm) {
        this.manager = fm;
        init();
    }

    protected void init() {
        excelSteps = ExcelStep.test();
        processExcel = new ProcessExcel();
        executeExcel = new ExecuteExcel();

        if (!SPUtils.getBoolean(RECOVERY)) {//重新启动
            processExcel.read();
            executeExcel.read();
        } else {//恢复启动
            processExcel.restore();
            executeExcel.restore();
            pos = SPUtils.getString(PositionUtil.POSITION);
        }
        showExcel(PositionUtil.pos2ExcelStep(pos, excelSteps));
    }


    /**
     * 上一步
     */
    public void previousStep() {

        if(PositionUtil.isFirstStep(pos,excelSteps)){
            System.out.println("已经是第一步了");
            return;

        }
        pos=PositionUtil.previousStep(pos,excelSteps);
        showExcel(PositionUtil.pos2ExcelStep(pos, excelSteps));


    }

    /**
     * 下一步
     */
    public void nextStep() {
        currentExcel.svae();
        pos = PositionUtil.nextStep(pos, excelSteps);
        if (PositionUtil.islastStep(pos, excelSteps)) {
            lastStep();
        } else {
            showExcel(PositionUtil.pos2ExcelStep(pos, excelSteps));
        }
    }

    /**
     * 最后一步,生成新的表格
     */
    public void lastStep() {
        processExcel.write();
        SPUtils.put(RECOVERY, false);
    }

    /**
     * 没填完表格退出
     */
    public void exit() {
        SPUtils.put(RECOVERY, true);
        SPUtils.put(PositionUtil.POSITION, pos);
    }

    /**
     * 根据ExcelStyle显示不同的
     *
     * @param excelStep
     */
    private void showExcel(ExcelStep excelStep) {
        if (excelStep.style == ExcelStyle.PROCESS_EXCEL) {
            showProcessExcel(excelStep);
        } else if(excelStep.style==ExcelStyle.EXCUTE_EXCEL){
                showExecuteExcel(excelStep);
        }
    }

    /**
     * 显示作业过程
     *
     * @param excelStep
     */
    private void showProcessExcel(ExcelStep excelStep) {
        if (CollectionUtils.isEmpty(processExcel.processExcelList)) return;
        if (processExcelFragment == null) {
            processExcelFragment = ProcessExcelFragment.newInstance(this);
        }
        if (processExcel != currentExcel) {
            manager.beginTransaction().replace(R.id.fragment, processExcelFragment).commit();
        }
        processExcelFragment.setData(processExcel.processExcelList.get(excelStep.step));
        currentExcel = processExcel;
    }

    /**
     * 显示二次措施单
     *
     * @param excelStep
     */
    private void showExecuteExcel(ExcelStep excelStep) {
        if (CollectionUtils.isEmpty(executeExcel.executeModelList)) return;
        if (executeExcelFragment == null) {
            executeExcelFragment = executeExcelFragment.newInstance(this);
        }
        if (executeExcel != currentExcel) {
            manager.beginTransaction().replace(R.id.fragment, executeExcelFragment).commit();
        }
        String[] posArr = pos.split("\\.");
        excelStep = excelSteps.get(Integer.parseInt(posArr[0]));
        if(excelStep.childSteps!=null&&excelStep.childSteps.size()>0){//有子步骤
            executeExcelFragment.setData2(executeExcel.executeModelList,excelStep, Integer.valueOf(posArr[1]));
        }else {//
            executeExcelFragment.setData(executeExcel.executeModelList.get(Integer.parseInt(posArr[0])));
        }
        currentExcel = executeExcel;
    }



}
