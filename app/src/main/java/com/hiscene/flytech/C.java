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

    //二次措施单表格文件名
    public static final String EXECUTE_READ_FILE="220kV砚后甲线二次措施单-数据读取.xlsx";
    public static final String EXECUTE_FILE="220kV砚后甲线二次措施单.xlsx";

   //临时目录下的缓存文件
    public static final String TEMP_PROCESS_FILE=TEMP_PATH+"芬莱作业过程.json";
    public static final String TEMP_EXCUTE_FILE=TEMP_PATH+"220kV砚后甲线二次措施单.json";
    public static final String TEMP_ATTACH_FIRST_FILE=TEMP_PATH+"附表1.json";
    public static final String TEMP_ATTACH_SECOND_FILE=TEMP_PATH+"附表2.json";
    public static final String TEMP_ATTACH_THREE_FILE=TEMP_PATH+"附表3.json";
    public static final String TEMP_ATTACH_FOUR_FILE=TEMP_PATH+"附表4.json";
    //测试路径

    public static final int START_TIME_BEGIN=2;
    public static final int PROCESS_ROW_BEGIN=10,PROCESS_ROW_END=59;
    public static final int ATTACH_ONE_ROW_BEGIN=73,ATTACH_ONE_ROW_END=75;
    public static final int ATTACH_SECOND_ROW_BEGIN=79,ATTACH_SECONG_ROW_END=82;
}
