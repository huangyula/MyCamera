package com.hiscene.flytech.ui.dialog;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;

import com.github.weiss.core.base.BaseApp;
import com.github.weiss.core.utils.CollectionUtils;
import com.github.weiss.core.utils.FileUtils;
import com.github.weiss.core.utils.LogUtils;
import com.github.weiss.core.utils.SPUtils;
import com.github.weiss.core.utils.TimeUtils;
import com.github.weiss.core.utils.ToastUtils;
import com.hiscene.flytech.App;
import com.hiscene.flytech.C;
import com.hiscene.flytech.R;
import com.hiscene.flytech.entity.AttachSecondModel;
import com.hiscene.flytech.entity.AttachThreeModel;
import com.hiscene.flytech.entity.ExcelStep;
import com.hiscene.flytech.entity.ExcelStyle;
import com.hiscene.flytech.entity.ProcessAttachStep;
import com.hiscene.flytech.entity.ProcessModel;
import com.hiscene.flytech.entity.Result;
import com.hiscene.flytech.entity.Setting;
import com.hiscene.flytech.event.EventCenter;
import com.hiscene.flytech.excel.ExecuteExcel;
import com.hiscene.flytech.excel.IExcel;
import com.hiscene.flytech.excel.ProcessExcel;
import com.hiscene.flytech.ui.MainActivity;
import com.hiscene.flytech.util.PositionUtil;

import java.util.ArrayList;
import java.util.List;

import static com.github.weiss.core.base.BaseApp.getAppContext;
import static com.hiscene.flytech.App.userManager;
import static com.hiscene.flytech.C.OUTPUT_PATH;
import static com.hiscene.flytech.entity.ExcelStep.readProcessStep;
import static com.hiscene.flytech.entity.ExcelStep.readSetting;
import static com.hiscene.flytech.ui.fragment.ExcelFragmentManager.END_TIME;
import static com.hiscene.flytech.ui.fragment.ExcelFragmentManager.START_TIME;

/**
 * @author Minamo
 * @e-mail kleinminamo@gmail.com
 * @time 2019/6/17
 * @des ExcelFragment状态机切换管理类
 */
public class ExcelDialogManager {

    public static final String RECOVERY = "Recovery";
    public static final String START_TIME = "START_TIME";//作业开始时间
    public static final String END_TIME = "END_TIME";//作业结束时间
    public static final String EXCEL_END_TIME="EXCEL_END_TIME";//表单结束时间

    private FragmentManager manager;
    //填表格步骤
    private List<ExcelStep> excelSteps;
    //配置文件
    //读取配置文件
    List<Setting> settingList;
    List<ProcessAttachStep> processAttachStepList;
    //当前步骤，-1 没有小步骤 比如：2.3 大步骤2，小步骤3。
    public String pos = "0.0";
    public int CURRENT_DIALOG=-2;
    public final int EXECUTE_DIALOG=-1,PROCESS_DIALOG=0,ATTACH_FIRST_DIALOG=1,ATTACH_SECOND_DIALOG=2,ATTACH_THIRD_DIALOG=3,
            ATTACH_FOUR_DIALOG=4,RECOVER_DIALOG=5;
    private ProcessExcelDialog processExcelDialog;
    private ExecuteExcelDialog executeExcelDialog;
    private RecoverExcelDialog recoverExcelDialog;
    private AttachSecondExcelDialog attachSecondExcelDialog;
    private AttachThreeExcelDialog attachThreeExcelDialog;
    private AttachFourExcelDialog attachFourExcelDialog;
    private ProcessExcel processExcel;
    private ExecuteExcel executeExcel;

    //当前表格
    private IExcel currentExcel;
    //临界点判断
    public boolean laststep=false;
    public boolean firststep=true;

    private Context mContext;

    private BaseExcelDialog mCurrentDailog;

    public ExcelDialogManager( Context context ) {
        mContext=context;
        long time = System.currentTimeMillis();
        try {
            //读取配置文件
            settingList=readSetting();
            processAttachStepList=readProcessStep();
            excelSteps = ExcelStep.test(settingList,processAttachStepList);
        }catch (Exception e){
            Result result=new Result(C.SETTING_ERROR,"配置文件有错误："+e.getMessage());
            EventCenter.getInstance().post(result);
        }
        processExcel = new ProcessExcel();
        executeExcel = new ExecuteExcel();
        processExcel.setSettingList(settingList);
        executeExcel.setSettingList(settingList);


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
        //表格加载成功
        Result result=new Result(C.EXCEL_LOADED,"");
        EventCenter.getInstance().post(result);
        LogUtils.d("load time: "+(System.currentTimeMillis() - time));
    }


    public void init() {
        showExcel(PositionUtil.pos2ExcelStep_ChildStep(pos, excelSteps));
        if (PositionUtil.islastStep(pos, excelSteps)) {
            lastStep();
        }else {
            mCurrentDailog.nextStepBtn.setText(BaseApp.getAppResources().getString(R.string.next_operation));
        }
        if(SPUtils.getBoolean(RECOVERY)){
            new ComfirmDialog(mContext).show();
        }

    }


    /**
     * 上一步
     */
    public void previousStep() {
        LogUtils.d("上一步");
        if(laststep){
            mCurrentDailog.nextStepBtn.setText(BaseApp.getAppResources().getString(R.string.next_operation));
            laststep=false;
        }
        if (PositionUtil.isFirstStep(pos, excelSteps)) {
            ToastUtils.show("当前已经是第一步了");
            firststep=true;
            return;
        }
        pos = PositionUtil.previousStep(pos, excelSteps);
        SPUtils.put(PositionUtil.POSITION,pos);
        SPUtils.put(RECOVERY,true);
        showExcel(PositionUtil.pos2ExcelStep_ChildStep(pos, excelSteps));


    }

    /**
     * 下一步
     */
    public void nextStep() {
        //保存上一步数据
        LogUtils.d("下一步");
        if(firststep){
            firststep=false;
        }
        //附表1的填写
        ExcelStep excelStep=PositionUtil.pos2ExcelStep_ChildStep(pos,excelSteps);
        if(excelStep.style==ExcelStyle.ATTACH_FIRST_EXCEL){
            String number= TextUtils.isEmpty(processExcelDialog.et_number.getText())?"":processExcelDialog.et_number.getText().toString().trim();
            if(!CollectionUtils.isEmpty(processExcel.attachFirstModels)){
                processExcel.attachFirstModels.get(excelStep.step).result=number;
            }

        }else if(excelStep.style==ExcelStyle.ATTACH_SECOND_EXCEL){//附表二的填写
            String device_version= TextUtils.isEmpty(attachSecondExcelDialog.device_version.getText())?"":attachSecondExcelDialog.device_version.getText().toString();
            String device_number= TextUtils.isEmpty(attachSecondExcelDialog.device_number.getText())?"":attachSecondExcelDialog.device_number.getText().toString();
            String check_code= TextUtils.isEmpty(attachSecondExcelDialog.check_code.getText())?"":attachSecondExcelDialog.check_code.getText().toString();
            String factory= TextUtils.isEmpty(attachSecondExcelDialog.factory.getText())?"":attachSecondExcelDialog.factory.getText().toString();
            if(!CollectionUtils.isEmpty(processExcel.attachSecondModelList)){
                processExcel.attachSecondModelList.get(excelStep.step).verison=device_version;
                processExcel.attachSecondModelList.get(excelStep.step).number=device_number;
                processExcel.attachSecondModelList.get(excelStep.step).check_code=check_code;
                processExcel.attachSecondModelList.get(excelStep.step).factory=factory;
            }
        } else if(excelStep.style==ExcelStyle.ATTACH_THREE_EXCEL){//附表三的填写
            String a= TextUtils.isEmpty(attachThreeExcelDialog.et_A.getText())?"":attachThreeExcelDialog.et_A.getText().toString().trim();
            String b= TextUtils.isEmpty(attachThreeExcelDialog.et_B.getText())?"":attachThreeExcelDialog.et_B.getText().toString().trim();
            if(!CollectionUtils.isEmpty(processExcel.attachThreeModelList)){
                processExcel.attachThreeModelList.get(excelStep.step).a=a;
                processExcel.attachThreeModelList.get(excelStep.step).b=b;
            }

        } else if(excelStep.style==ExcelStyle.ATTACH_FOUR_EXCEL){//附表四的填写
            String time_1= TextUtils.isEmpty(attachFourExcelDialog.time_1.getText())?"":attachFourExcelDialog.time_1.getText().toString().trim();
            String time_2= TextUtils.isEmpty(attachFourExcelDialog.time_2.getText())?"":attachFourExcelDialog.time_2.getText().toString().trim();
            if(!CollectionUtils.isEmpty(processExcel.attachFourModelList)){
                processExcel.attachFourModelList.get(excelStep.step).time_1=time_1;
                processExcel.attachFourModelList.get(excelStep.step).time_2=time_2;
            }
        }
        currentExcel.svae();

        //显示下一步的内容,若是最后一步,则直接导出数据,无需再显示。
        if(laststep){
                LogUtils.d("导出表单");
                if(mCurrentDailog!=null&&mCurrentDailog.nextStepBtn.getText().equals(App.getAppResources().getString(R.string.export_excel))){
                    export();
                    return;
                }
                return;
        }
        pos=PositionUtil.nextStep(pos, excelSteps);
        SPUtils.put(PositionUtil.POSITION,pos);
        SPUtils.put(RECOVERY,true);
        showExcel(PositionUtil.pos2ExcelStep_ChildStep(pos, excelSteps));
        if (PositionUtil.islastStep(pos, excelSteps)) {
            lastStep();
        }else {
            mCurrentDailog.nextStepBtn.setText(BaseApp.getAppResources().getString(R.string.next_operation));
        }

    }

    /**
     * 最后一步,生成新的表格
     */
    public void  lastStep() {
        laststep=true;
        ToastUtils.show("当前已经是最后一步了");
        mCurrentDailog.nextStepBtn.setText(BaseApp.getAppResources().getString(R.string.export_excel));
    }

    private void export(){
        EventCenter.getInstance().post(C.LOADING);
        new Thread(new Runnable() {
            @Override
            public void run() {
                SPUtils.put(EXCEL_END_TIME,TimeUtils.getNowTimeString());
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
        dismissCurrentDialog();
        currentExcel=null;
//        CURRENT_DIALOG=-2;
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
        //记录作业开始时间
        if(TextUtils.isEmpty(SPUtils.getString(START_TIME))){
            SPUtils.put(START_TIME, TimeUtils.getNowTimeString());
        }
        //记录作业结束时间
        SPUtils.put(END_TIME,TimeUtils.getNowTimeString());
        if (CollectionUtils.isEmpty(processExcel.processExcelList)) return;
        if (processExcelDialog == null) {
            processExcelDialog = processExcelDialog.newInstance(mContext,this);
        }
        if (CURRENT_DIALOG != PROCESS_DIALOG) {
            processExcelDialog.show();
            dismissCurrentDialog();
        }
        int[] posArr=PositionUtil.pos2posArr(pos);
        excelStep = excelSteps.get(posArr[0]);
        if (!CollectionUtils.isEmpty(excelStep.childSteps)) {//有子步骤
            processExcelDialog.setData2(processExcel.processExcelList.get(excelStep.step),processExcel.attachFirstModels,excelStep,posArr[1]);
        } else {//
            processExcelDialog.setData(processExcel.processExcelList.get(excelStep.step));
        }
//        processExcelDialog.setData(processExcel.processExcelList.get(excelStep.step));
        currentExcel = processExcel;
        CURRENT_DIALOG=PROCESS_DIALOG;
        mCurrentDailog=processExcelDialog;

    }

    /**
     * 显示二次措施单--执行部分
     *
     * @param excelStep
     */
    private void showExecuteExcel( ExcelStep excelStep) {
        if (CollectionUtils.isEmpty(executeExcel.executeModelList)) return;
        if (executeExcelDialog == null) {
            executeExcelDialog = ExecuteExcelDialog.newInstance(mContext,this);
        }
        if (CURRENT_DIALOG != EXECUTE_DIALOG) {
            executeExcelDialog.show();
            dismissCurrentDialog();
        }
        int[] posArr=PositionUtil.pos2posArr(pos);
        excelStep = excelSteps.get(posArr[0]);
        if (!CollectionUtils.isEmpty(excelStep.childSteps)) {//有子步骤
            executeExcelDialog.setData2(executeExcel.executeModelList,excelStep,posArr[1]);
        } else {
            executeExcelDialog.setData(executeExcel.executeModelList.get(excelStep.step));
        }
        currentExcel = executeExcel;
        CURRENT_DIALOG=EXECUTE_DIALOG;
        mCurrentDailog=executeExcelDialog;
    }

    private void showRecoverExcel( ExcelStep excelStep) {
        if (CollectionUtils.isEmpty(executeExcel.executeModelList)) return;
        if (recoverExcelDialog == null) {
            recoverExcelDialog = recoverExcelDialog.newInstance(mContext,this);
        }
        if (CURRENT_DIALOG != RECOVER_DIALOG) {
            recoverExcelDialog.show();
            dismissCurrentDialog();
        }
        int[] posArr=PositionUtil.pos2posArr(pos);
        excelStep = excelSteps.get(posArr[0]);
        if (!CollectionUtils.isEmpty(excelStep.childSteps)) {//有子步骤
            recoverExcelDialog.setData2(executeExcel.executeModelList,excelStep,posArr[1]);
        } else {//
            recoverExcelDialog.setData(executeExcel.executeModelList.get(excelStep.step));
        }
        currentExcel = executeExcel;
        CURRENT_DIALOG=RECOVER_DIALOG;
        mCurrentDailog=recoverExcelDialog;
    }

    private void showAttachSecondExcel( ExcelStep excelStep ) {
        if (CollectionUtils.isEmpty(processExcel.processExcelList)) return;
        if (attachSecondExcelDialog== null) {
            attachSecondExcelDialog = attachSecondExcelDialog.newInstance(mContext,this);
        }
        if (CURRENT_DIALOG != ATTACH_SECOND_DIALOG) {
            attachSecondExcelDialog.show();
            dismissCurrentDialog();
        }
        int[] posArr=PositionUtil.pos2posArr(pos);
        excelStep = excelSteps.get(posArr[0]);
        ProcessModel processModel=processExcel.processExcelList.get(excelStep.step);
        AttachSecondModel attachSecondModel=processExcel.attachSecondModelList.get(excelStep.childSteps.get(posArr[1]).step);
        attachSecondExcelDialog.setData(processModel,attachSecondModel,(posArr[1]+1)+"/"+excelStep.childSteps.size());

        currentExcel = processExcel;
        CURRENT_DIALOG=ATTACH_SECOND_DIALOG;
        mCurrentDailog=attachSecondExcelDialog;
    }

    private void showAttachThreeExcel( ExcelStep excelStep ) {
        if ( attachThreeExcelDialog== null) {
            attachThreeExcelDialog = attachThreeExcelDialog.newInstance(mContext,this);
        }
        if (CURRENT_DIALOG != ATTACH_THIRD_DIALOG) {
            attachThreeExcelDialog.show();
            dismissCurrentDialog();
        }
        int[] posArr=PositionUtil.pos2posArr(pos);
        excelStep = excelSteps.get(posArr[0]);
        ProcessModel processModel=processExcel.processExcelList.get(excelStep.step);
        AttachThreeModel attachThreeModel=processExcel.attachThreeModelList.get(excelStep.childSteps.get(posArr[1]).step);
        attachThreeExcelDialog.setData(processModel,attachThreeModel,excelStep.childSteps.get(posArr[1]).step,(posArr[1]+1)+"/"+excelStep.childSteps.size());

        currentExcel = processExcel;
        CURRENT_DIALOG=ATTACH_THIRD_DIALOG;
        mCurrentDailog=attachThreeExcelDialog;
    }

    private void showAttachFourExcel( ExcelStep excelStep ) {
        if ( attachFourExcelDialog== null) {
            attachFourExcelDialog = attachFourExcelDialog.newInstance(mContext,this);
        }
        if (CURRENT_DIALOG != ATTACH_FOUR_DIALOG) {
            attachFourExcelDialog.show();
            dismissCurrentDialog();
        }
        int[] posArr=PositionUtil.pos2posArr(pos);
        excelStep = excelSteps.get(posArr[0]);
        ProcessModel processModel=processExcel.processExcelList.get(excelStep.step);
        attachFourExcelDialog.setData(processModel,excelStep.childSteps.get(posArr[1]).step,(posArr[1]+1)+"/"+excelStep.childSteps.size());

        currentExcel = processExcel;
        CURRENT_DIALOG=ATTACH_FOUR_DIALOG;
        mCurrentDailog=attachFourExcelDialog;
    }

    public void dismissCurrentDialog() {
        switch (CURRENT_DIALOG){
            case EXECUTE_DIALOG:
                if(executeExcelDialog!=null){
                    executeExcelDialog.dismiss();
                }
                break;
            case PROCESS_DIALOG:
                if(processExcelDialog!=null){
                    processExcelDialog.dismiss();
                }
                break;
            case ATTACH_SECOND_DIALOG:
                if(attachSecondExcelDialog!=null){
                    attachSecondExcelDialog.dismiss();
                }
                break;
            case ATTACH_THIRD_DIALOG:
                if(attachThreeExcelDialog!=null){
                    attachThreeExcelDialog.dismiss();
                }
                break;
            case ATTACH_FOUR_DIALOG:
                if(attachFourExcelDialog!=null){
                    attachFourExcelDialog.dismiss();
                }
                break;
            case RECOVER_DIALOG:
                if(recoverExcelDialog!=null){
                    recoverExcelDialog.dismiss();
                }
                break;
        }
        CURRENT_DIALOG=-2;
        mCurrentDailog=null;
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
            processExcel.processExcelList.get(excelStep.step).result = result;
        }
    }


    public void restart(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //读取配置文件
                    settingList=readSetting();
                    processAttachStepList=readProcessStep();
                    excelSteps = ExcelStep.test(settingList,processAttachStepList);
                }catch (Exception e){
                    Result result=new Result(C.SETTING_ERROR,"配置文件有错误："+e.getMessage());
                    EventCenter.getInstance().post(result);
                }
                processExcel = new ProcessExcel();
                executeExcel = new ExecuteExcel();

                processExcel.read();
                executeExcel.read();
                processExcel.svae();
                executeExcel.svae();

                processExcel.setSettingList(settingList);
                executeExcel.setSettingList(settingList);
                //表格加载成功
                Result result=new Result(C.RESTART_EXCEL,"");
                EventCenter.getInstance().post(result);
            }
        }).start();
    }

    public void reset(){ //重新初始化值
        pos="0.0";
        currentExcel=null;
        firststep=true;
        laststep=false;
        dismissCurrentDialog();
        SPUtils.put(PositionUtil.POSITION,"0.0");
        SPUtils.put(RECOVERY,false);
        SPUtils.put(START_TIME,"");
    }

    public Dialog getCurrentDailog() {
        return mCurrentDailog;
    }


    public void clearAllDialog() {
        dismissCurrentDialog();
    }
}
