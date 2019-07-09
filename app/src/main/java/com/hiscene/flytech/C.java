package com.hiscene.flytech;

import java.io.File;

/**
 * 常量
 * Created by Weiss on 2017/1/10.
 */

public class C {

    public static final int PAGE_COUNT = 10;

    //根目录
    public static final String BASE_PATH = App.getAppContext().getExternalCacheDir().getPath();

    //assets原表格资源目录
    public static final String ASSETS_PATH = BASE_PATH + File.separator + "assets" + File.separator + "res" + File.separator;

    //临时目录
    public static final String TEMP_PATH = BASE_PATH + File.separator + "temp" + File.separator;

    //输出目录
    public static final String OUTPUT_PATH = BASE_PATH + File.separator + "output" + File.separator;

    //作业过程表格文件名
    public static final String PROCESS_FILE = "芬莱作业过程.xlsx";
    public static final String PROCESS = "芬莱作业过程";

    //二次措施单表格文件名
    public static final String EXECUTE_READ_FILE="220kV砚后甲线二次措施单-数据读取.xlsx";
    public static final String EXECUTE_FILE="220kV砚后甲线二次措施单.xlsx";
    public static final String EXECUTE="220kV砚后甲线二次措施单";

   //临时目录下的缓存文件
    public static final String TEMP_PROCESS_FILE=TEMP_PATH+"芬莱作业过程.json";
    public static final String TEMP_EXCUTE_FILE=TEMP_PATH+"220kV砚后甲线二次措施单.json";
    public static final String TEMP_ATTACH_FIRST_FILE=TEMP_PATH+"附表1.json";
    public static final String TEMP_ATTACH_SECOND_FILE=TEMP_PATH+"附表2.json";
    public static final String TEMP_ATTACH_THREE_FILE=TEMP_PATH+"附表3.json";
    public static final String TEMP_ATTACH_FOUR_FILE=TEMP_PATH+"附表4.json";
    //测试路径

    public static final int START_TIME_BEGIN=2;
    public static final int EXECUTE_BEGIN=6;
    public static final int PROCESS_ROW_BEGIN=10,PROCESS_ROW_END=59;
    public static final int ATTACH_ONE_ROW_BEGIN=62,ATTACH_ONE_ROW_END=64;
    public static final int ATTACH_SECOND_ROW_BEGIN=68,ATTACH_SECONG_ROW_END=71;
    public static final int ATTACH_THIRD_ROW_BEGIN_1=76,ATTACH_THIRD_ROW_END_1=81;
    public static final int ATTACH_THIRD_ROW_BEGIN_2=84,ATTACH_THIRD_ROW_END_2=89;
    public static final int ATTACH_FOUR_ROW_BEGIN=93,ATTACH_FOUR_ROW_END=93;

    //表格写入成功判断
    public static final String EXCEL_WRITE_SUCCESS = "EXCEL_WRITE_SUCCESS";
    public static final String EXCEL_WRITE_ERROR="EXCEL_WRITE_ERROR";
    public static final String LOADING="LOADING";
    public static final String EXCEL_LOADED="EXCEL_LOADED";

    //设备资料路径
    public static final String FILE_DEVICE_FILE=BASE_PATH+File.separator + "device" + File.separator;//设备资料

}
