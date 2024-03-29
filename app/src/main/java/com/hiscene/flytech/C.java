package com.hiscene.flytech;

import com.github.weiss.core.base.BaseApp;
import com.github.weiss.core.utils.SDCardUtils;

import java.io.File;

/**
 * 常量
 * Created by Weiss on 2017/1/10.
 */

public class C {

    public static final int PAGE_COUNT = 10;

    //根目录
    public static final String BASE_PATH = SDCardUtils.getSDCardPath()+ BaseApp.getAppContext().getResources().getString(R.string.app_name) +File.separator;

    //assets原表格资源目录
    public static final String ASSETS_PATH = BASE_PATH + File.separator + "form" + File.separator;

    //临时目录
    public static final String TEMP_PATH = BASE_PATH+File.separator+ "temp" + File.separator;

    //输出目录
    public static final String OUTPUT_PATH = BASE_PATH + File.separator + "export" + File.separator;

    //作业过程表格文件名
    public static final String PROCESS_FILE = "作业过程.xlsx";
    public static final String PROCESS = "作业过程";

    //二次措施单表格文件名
    public static final String EXECUTE_READ_FILE="220kV砚后甲线二次措施单-数据读取.xlsx";
    public static final String EXECUTE_FILE="220kV砚后甲线二次措施单.xlsx";
    public static final String EXECUTE="220kV砚后甲线二次措施单";

   //临时目录下的缓存文件
    public static final String TEMP_PROCESS_FILE=TEMP_PATH+"作业过程.json";
    public static final String TEMP_EXCUTE_FILE=TEMP_PATH+"220kV砚后甲线二次措施单.json";
    public static final String TEMP_ATTACH_FIRST_FILE=TEMP_PATH+"附表1.json";
    public static final String TEMP_ATTACH_SECOND_FILE=TEMP_PATH+"附表2.json";
    public static final String TEMP_ATTACH_THREE_FILE=TEMP_PATH+"附表3.json";
    public static final String TEMP_ATTACH_FOUR_FILE=TEMP_PATH+"附表4.json";

    //配置文件
    public static final String SETTING_FILE=ASSETS_PATH+"配置文件_表步骤.txt";
    public static final String SETTING_PROCESS_FILE=ASSETS_PATH+"配置文件_作业过程与附表关联.txt";

    public static  int START_TIME_BEGIN=2;//作业开始时间的行数
    public static  int EXECUTE_BEGIN=6,EXECUTE_END=36;
    public static  int PROCESS_ROW_BEGIN=10,PROCESS_ROW_END=59;
    public static  int ATTACH_ONE_ROW_BEGIN=62,ATTACH_ONE_ROW_END=64;
    public static  int ATTACH_SECOND_ROW_BEGIN=68,ATTACH_SECONG_ROW_END=71;
    public static  int ATTACH_THIRD_ROW_BEGIN_1=76,ATTACH_THIRD_ROW_END_1=81;
    public static  int ATTACH_THIRD_ROW_BEGIN_2=84,ATTACH_THIRD_ROW_END_2=89;
    public static  int ATTACH_FOUR_ROW_BEGIN=93,ATTACH_FOUR_ROW_END=93;

    //表格写入成功判断
    public static final String EXCEL_WRITE_SUCCESS = "EXCEL_WRITE_SUCCESS";
    public static final String EXCEL_WRITE_ERROR="EXCEL_WRITE_ERROR";
    public static final String LOADING="LOADING";
    public static final String EXCEL_LOADED="EXCEL_LOADED";

    //表单是否重新填写
    public static final String RESTART_EDIT="RESTART_EDIT";
    public static final String CONTINUE_EDIT="CONTINUE_EDIT";
    public static final String RESTART_EXCEL="RESTART_EXCEL";//重新打开表单

    //配置文件出错
    public static final String SETTING_ERROR="SETTING_ERROR";

    //设备资料路径
    public static final String FILE_DEVICE_FILE=BASE_PATH+File.separator + "info" + File.separator;//设备资料

}
