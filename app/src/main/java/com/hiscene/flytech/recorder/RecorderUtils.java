package com.hiscene.flytech.recorder;

import android.util.Log;

import com.hiscene.flytech.C;
import com.hiscene.flytech.entity.ExcelStep;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author Minamo
 * @e-mail kleinminamo@gmail.com
 * @time 2019/6/27
 * @des
 */
public class RecorderUtils {

    public static String getFilePath() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        String path = C.TEMP_PATH+"Recorder"+File.separator  + format.format(new Date()) + ".mp4";
        return path;
    }

    public static String getScreenRecorderFilePath() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        String path = C.TEMP_PATH+"Recorder"+File.separator  + format.format(new Date()) + ".mp4";
        return path;
    }
}
