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

    //临时目录下的缓存文件
    public static final String TEMP_FILE = TEMP_PATH + "芬莱作业过程.json";
}
