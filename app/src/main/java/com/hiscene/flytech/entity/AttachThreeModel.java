package com.hiscene.flytech.entity;

import com.github.weiss.core.entity.Entity;

/**
 * @time 2019/6/17
 * @des 附表3数据模型
 */
public class AttachThreeModel extends Entity {

    //主一裝置通道
    public String item_name;

    //項目序號
    public String item_no;

    //A通道
    public String a;

    //B通道
    public String b;

    public String title="本侧光发功率";

    public AttachThreeModel( String item_name, String item_no, String title ,String a, String b) {
        this.item_name = item_name;
        this.item_no = item_no;
        this.title = title;
        this.a = a;
        this.b = b;
    }
}
