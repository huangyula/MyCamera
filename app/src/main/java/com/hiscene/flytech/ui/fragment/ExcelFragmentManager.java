package com.hiscene.flytech.ui.fragment;

import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.github.weiss.core.utils.CollectionUtils;
import com.github.weiss.core.utils.LogUtils;
import com.github.weiss.core.utils.SPUtils;
import com.github.weiss.core.utils.TimeUtils;
import com.hiscene.flytech.R;
import com.hiscene.flytech.entity.AttachSecondModel;
import com.hiscene.flytech.entity.ExcelStep;
import com.hiscene.flytech.entity.ExcelStyle;
import com.hiscene.flytech.entity.ProcessModel;
import com.hiscene.flytech.excel.ExecuteExcel;
import com.hiscene.flytech.excel.IExcel;
import com.hiscene.flytech.excel.ProcessExcel;
import com.hiscene.flytech.util.PositionUtil;

import java.util.List;

import static com.github.weiss.core.base.BaseApp.getAppContext;
import static com.hiscene.flytech.App.userManager;

/**
 * @author Minamo
 * @e-mail kleinminamo@gmail.com
 * @time 2019/6/17
 * @des ExcelFragment状态机切换管理类
 */
public class ExcelFragmentManager {

    public static final String RECOVERY = "Recovery";
    public static final String START_TIME = "START_TIME";//作业开始时间
    public static final String END_TIME = "END_TIME";//作业结束时间

    private FragmentManager manager;
    //填表格步骤
    private List<ExcelStep> excelSteps;
    //当前步骤，-1 没有小步骤 比如：2.3 大步骤2，小步骤3。
    public String pos = "0.0";
    public int CURRENT_FRAGMENT=-1;//0：作业过程,1:附表一,2:附表2,-1措施单

    private ProcessExcelFragment processExcelFragment;
    private ExecuteExcelFragment executeExcelFragment;
    private RecoverExcelFragment recoverExcelFragment;
    private AttachSecondExcelFragment attachSecondExcelFragment;
    private AttachThreeExcelFragment attachThreeExcelFragment;
    private AttachFourExcelFragment attachFourExcelFragment;
    private ProcessExcel processExcel;
    private ExecuteExcel executeExcel;
    //当前表格
    private IExcel currentExcel;
    //临界点判断
    private boolean laststep=false;
    private boolean firststep=false;

    public ExcelFragmentManager( FragmentManager fm ) {
        this.manager = fm;
        excelSteps = ExcelStep.test();
        processExcel = new ProcessExcel();
        executeExcel = new ExecuteExcel();

        if (!SPUtils.getBoolean(RECOVERY)) {//重新启动
            processExcel.read();
            executeExcel.read();
            processExcel.svae();
            executeExcel.svae();
        } else {//恢复启动
            processExcel.restore();
            executeExcel.restore();
            pos = SPUtils.getString(PositionUtil.POSITION);
        }
    }

    public void init() {
        showExcel(PositionUtil.pos2ExcelStep_ChildStep(pos, excelSteps));
    }


    /**
     * 上一步
     */
    public void previousStep() {
        if(laststep){
            recoverExcelFragment.nextStepBtn.setEnabled(true);
            laststep=false;
        }
        if (PositionUtil.isFirstStep(pos, excelSteps)) {
            LogUtils.d("已经是第一步了");
            firststep=true;
            executeExcelFragment.previousStepBtn.setEnabled(false);
            return;
        }
        pos = PositionUtil.previousStep(pos, excelSteps);
        showExcel(PositionUtil.pos2ExcelStep_ChildStep(pos, excelSteps));


    }

    /**
     * 下一步
     */
    public void nextStep() {
        if(firststep){
            executeExcelFragment.previousStepBtn.setEnabled(true);
            firststep=false;
        }
        //附表1的填写
        ExcelStep excelStep=PositionUtil.pos2ExcelStep_ChildStep(pos,excelSteps);
        if(excelStep.style==ExcelStyle.ATTACH_FIRST_EXCEL){
            String number= TextUtils.isEmpty(processExcelFragment.et_number.getText())?"":processExcelFragment.et_number.getText().toString().trim();
            processExcel.attachFirstModels.get(excelStep.step).result=number;
        }else if(excelStep.style==ExcelStyle.ATTACH_FOUR_EXCEL){//附表四的填写
            String time_1= TextUtils.isEmpty(attachFourExcelFragment.time_1.getText())?"":attachFourExcelFragment.time_1.getText().toString().trim();
            String time_2= TextUtils.isEmpty(attachFourExcelFragment.time_2.getText())?"":attachFourExcelFragment.time_2.getText().toString().trim();
            processExcel.attachFourModelList.get(excelStep.step).time_1=time_1;
            processExcel.attachFourModelList.get(excelStep.step).time_1=time_2;
        }


        currentExcel.svae();
        String index = PositionUtil.nextStep(pos, excelSteps);
        if (PositionUtil.islastStep(index, excelSteps)) {
            lastStep();
        } else {
            pos=PositionUtil.nextStep(pos, excelSteps);
            showExcel(PositionUtil.pos2ExcelStep_ChildStep(pos, excelSteps));
        }
    }

    /**
     * 最后一步,生成新的表格
     */
    public void lastStep() {
        laststep=true;
        recoverExcelFragment.nextStepBtn.setEnabled(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                executeExcel.write();
                processExcel.write();
            }
        }).start();
    }

    /**
     * 没填完表格退出
     */
    public void exit() {
        userManager.logout();
        SPUtils.put(RECOVERY, true);
        SPUtils.put(PositionUtil.POSITION, pos);
        currentExcel=null;
        CURRENT_FRAGMENT=-1;
    }

    /**
     * 根据ExcelStyle显示不同的
     *
     * @param excelStep
     */
    private void showExcel( ExcelStep excelStep ) {
        if (excelStep.style == ExcelStyle.PROCESS_EXCEL) {
            showProcessExcel(excelStep);
        } else if (excelStep.style == ExcelStyle.EXCUTE_EXCEL) {
            showExecuteExcel(excelStep);
        }else if(excelStep.style==ExcelStyle.RECOVER_EXCEL){
            showRecoverExcel(excelStep);
        } else if(excelStep.style==ExcelStyle.ATTACH_FIRST_EXCEL){
            showProcessExcel(excelStep);
        }else if(excelStep.style==ExcelStyle.ATTACH_SECOND_EXCEL){
            showAttachSecondExcel(excelStep);
        }else if(excelStep.style==ExcelStyle.ATTACH_THREE_EXCEL){
            showAttachThreeExcel(excelStep);
        }else if(excelStep.style==ExcelStyle.ATTACH_FOUR_EXCEL){
            showAttachFourExcel(excelStep);
        }
    }




    /**
     * 显示作业过程
     *
     * @param excelStep
     */
    private void showProcessExcel( ExcelStep excelStep ) {
        if (CollectionUtils.isEmpty(processExcel.processExcelList)) return;
        if (processExcelFragment == null) {
            processExcelFragment = ProcessExcelFragment.newInstance(this);
        }
        if (CURRENT_FRAGMENT != 0) {
            manager.beginTransaction().replace(R.id.fragment, processExcelFragment).commitNowAllowingStateLoss();
        }
        int[] posArr=PositionUtil.pos2posArr(pos);
        excelStep = excelSteps.get(posArr[0]);
        if (!CollectionUtils.isEmpty(excelStep.childSteps)) {//有子步骤
            processExcelFragment.setData2(processExcel.processExcelList.get(excelStep.step),processExcel.attachFirstModels,excelStep,posArr[1]);
        } else {//
            processExcelFragment.setData(processExcel.processExcelList.get(excelStep.step));
        }
//        processExcelFragment.setData(processExcel.processExcelList.get(excelStep.step));
        currentExcel = processExcel;
        CURRENT_FRAGMENT=0;

    }

    /**
     * 显示二次措施单--执行部分
     *
     * @param excelStep
     */
    private void showExecuteExcel( ExcelStep excelStep) {
        if (CollectionUtils.isEmpty(executeExcel.executeModelList)) return;
        if (executeExcelFragment == null) {
            executeExcelFragment = executeExcelFragment.newInstance(this);
        }
        if (executeExcel != currentExcel) {
            manager.beginTransaction().replace(R.id.fragment, executeExcelFragment).commit();
        }
        int[] posArr=PositionUtil.pos2posArr(pos);
        excelStep = excelSteps.get(posArr[0]);
        if (!CollectionUtils.isEmpty(excelStep.childSteps)) {//有子步骤
            executeExcelFragment.setData2(executeExcel.executeModelList,excelStep,posArr[1]);
        } else {//
            executeExcelFragment.setData(executeExcel.executeModelList.get(excelStep.step));
        }
        currentExcel = executeExcel;
        CURRENT_FRAGMENT=-1;
    }

    private void showRecoverExcel( ExcelStep excelStep) {
        if (CollectionUtils.isEmpty(executeExcel.executeModelList)) return;
        if (recoverExcelFragment == null) {
            recoverExcelFragment = recoverExcelFragment.newInstance(this);
        }
        if (executeExcel != currentExcel) {
            manager.beginTransaction().replace(R.id.fragment, recoverExcelFragment).commit();
        }
        int[] posArr=PositionUtil.pos2posArr(pos);
        excelStep = excelSteps.get(posArr[0]);
        if (!CollectionUtils.isEmpty(excelStep.childSteps)) {//有子步骤
            recoverExcelFragment.setData2(executeExcel.executeModelList,excelStep,posArr[1]);
        } else {//
            recoverExcelFragment.setData(executeExcel.executeModelList.get(excelStep.step));
        }
        currentExcel = executeExcel;
        CURRENT_FRAGMENT=5;
    }

    private void showAttachSecondExcel( ExcelStep excelStep ) {
        if (CollectionUtils.isEmpty(processExcel.processExcelList)) return;
        if ( attachSecondExcelFragment== null) {
            attachSecondExcelFragment = AttachSecondExcelFragment.newInstance(this);
        }
        if (CURRENT_FRAGMENT != 2) {
            manager.beginTransaction().replace(R.id.fragment, attachSecondExcelFragment).commit();
        }
        int[] posArr=PositionUtil.pos2posArr(pos);
        excelStep = excelSteps.get(posArr[0]);
        ProcessModel processModel=processExcel.processExcelList.get(excelStep.step);
        AttachSecondModel attachSecondModel=processExcel.attachSecondModelList.get(excelStep.childSteps.get(posArr[1]).step);
        attachSecondExcelFragment.setData(processModel,attachSecondModel,(posArr[1]+1)+"/"+excelStep.childSteps.size());

        currentExcel = processExcel;
        CURRENT_FRAGMENT=2;
    }

    private void showAttachThreeExcel( ExcelStep excelStep ) {
        if ( attachThreeExcelFragment== null) {
            attachThreeExcelFragment = AttachThreeExcelFragment.newInstance(this);
        }
        if (CURRENT_FRAGMENT != 3) {
            manager.beginTransaction().replace(R.id.fragment, attachThreeExcelFragment).commit();
        }
        int[] posArr=PositionUtil.pos2posArr(pos);
        excelStep = excelSteps.get(posArr[0]);
        ProcessModel processModel=processExcel.processExcelList.get(excelStep.step);
        attachThreeExcelFragment.setData(processModel,excelStep.childSteps.get(posArr[1]).step,(posArr[1]+1)+"/"+excelStep.childSteps.size());

        currentExcel = processExcel;
        CURRENT_FRAGMENT=3;
    }

    private void showAttachFourExcel( ExcelStep excelStep ) {
        if ( attachFourExcelFragment== null) {
            attachFourExcelFragment = AttachFourExcelFragment.newInstance(this);
        }
        if (CURRENT_FRAGMENT != 4) {
            manager.beginTransaction().replace(R.id.fragment, attachFourExcelFragment).commit();
        }
        int[] posArr=PositionUtil.pos2posArr(pos);
        excelStep = excelSteps.get(posArr[0]);
        ProcessModel processModel=processExcel.processExcelList.get(excelStep.step);
        attachFourExcelFragment.setData(processModel,excelStep.childSteps.get(posArr[1]).step,(posArr[1]+1)+"/"+excelStep.childSteps.size());

        currentExcel = processExcel;
        CURRENT_FRAGMENT=4;
    }


    protected void setResult( int result ) {
        int[] posArr = PositionUtil.pos2posArr(pos);
        ExcelStep excelStep = PositionUtil.pos2ExcelStep(pos, excelSteps);
        int index=0;
        if(!CollectionUtils.isEmpty(excelStep.childSteps)) {//有子步骤
            index=excelStep.childSteps.get(posArr[1]).step;
        }else {
            index=excelStep.step;
        }
        if (excelStep.style == ExcelStyle.EXCUTE_EXCEL) {
            executeExcel.executeModelList.get(index).excute_result = result;
            executeExcel.executeModelList.get(index).execute_date= TimeUtils.getNowTimeString();
        }else if(excelStep.style==ExcelStyle.RECOVER_EXCEL) {
            executeExcel.executeModelList.get(index).recover_result = result;
            executeExcel.executeModelList.get(index).recover_date = TimeUtils.getNowTimeString();
        } else if (excelStep.style == ExcelStyle.PROCESS_EXCEL) {
            processExcel.processExcelList.get(index).result = result;
        }
    }





}
