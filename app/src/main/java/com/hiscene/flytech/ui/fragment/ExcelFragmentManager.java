package com.hiscene.flytech.ui.fragment;

import android.support.v4.app.FragmentManager;
import com.github.weiss.core.utils.CollectionUtils;
import com.hiscene.flytech.R;
import com.hiscene.flytech.entity.ExcelStep;
import com.hiscene.flytech.entity.ExcelStyle;
import com.hiscene.flytech.excel.IExcel;
import com.hiscene.flytech.excel.ProcessExcel;

import java.util.List;

/**
 * @author Minamo
 * @e-mail kleinminamo@gmail.com
 * @time 2019/6/17
 * @des
 */
public class ExcelFragmentManager {

    private FragmentManager manager;
    private ProcessExcelFragment processExcelFragment;
    private List<ExcelStep> excelSteps;
    //当前步骤 比如：2.2
    private String pos = "0.-1";

    private ProcessExcel processExcel;
    //当前表格
    private IExcel currentExcel;

    public ExcelFragmentManager(FragmentManager fm) {
        this.manager = fm;
        init();
    }


    protected void init() {
        excelSteps = ExcelStep.test();
        processExcel = new ProcessExcel();
        processExcel.read();
        showExcel(pos2ExcelStep());
    }

    public void previousStep() {

    }

    public void nextStep() {
        currentExcel.svae();

        String[] posStrArr = pos.split("\\.");
        int[] posArr = new int[posStrArr.length];
        for (int i = 0; i < posArr.length; i++) {
            posArr[i] = Integer.parseInt(posStrArr[i]);
        }
        ExcelStep excelStep = excelSteps.get(posArr[0]);
        if (posArr.length > 1 && -1 != posArr[1]) {
            if (excelStep.chilSteps.size() > posArr[1] + 1) {
                posArr[1]++;
            } else {
                posArr[0]++;
                posArr[1] = -1;
            }
        } else {
            posArr[0]++;
            posArr[1] = -1;
        }
        pos = posArr[0] + "." + posArr[1];
        showExcel(pos2ExcelStep());
    }

    /**
     * 根据当前步骤从excelSteps中获取到ExcelStep
     *
     * @return ExcelStep
     */
    private ExcelStep pos2ExcelStep() {
        String[] posArr = pos.split("\\.");
        ExcelStep excelStep = excelSteps.get(Integer.parseInt(posArr[0]));
        if (posArr.length > 1 && !"-1".equals(posArr[1])) {
            excelStep = excelSteps.get(Integer.parseInt(posArr[0])).chilSteps.get(Integer.parseInt(posArr[1]));
        }
        return excelStep;
    }

    /**
     * 根据ExcelStyle显示不同的
     *
     * @param excelStep
     */
    private void showExcel(ExcelStep excelStep) {
        if (excelStep.style == ExcelStyle.PROCESS_EXCEL) {
            showProcessExcel(excelStep);
        } else {

        }
    }


    private void showProcessExcel(ExcelStep excelStep) {
        if (CollectionUtils.isEmpty(processExcel.processExcelList)) return;
        if (processExcelFragment == null) {
            processExcelFragment = ProcessExcelFragment.newInstance(this);
        }
        if(processExcel != currentExcel) {
            manager.beginTransaction().replace(R.id.fragment, processExcelFragment).commit();
        }
        processExcelFragment.setData(processExcel.processExcelList.get(excelStep.step));
        currentExcel = processExcel;
    }


}
