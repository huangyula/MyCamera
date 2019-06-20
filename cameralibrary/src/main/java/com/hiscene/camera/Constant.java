package com.hiscene.camera;

import android.os.Build;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xujiangang
 */
public class Constant {

    public static List<String> G200_MODELS;
    public static boolean IS_G200;
    public static boolean IS_G100;
    static {
        G200_MODELS = new ArrayList<>();
        G200_MODELS.add("MSM8996 for arm64");
        G200_MODELS.add("G200");

        IS_G200 = G200_MODELS.contains(Build.MODEL);
        IS_G100 = Build.BRAND.equals("HiAR");
    }
}
