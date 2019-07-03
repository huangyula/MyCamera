package com.hiscene.flytech.entity;

import com.github.weiss.core.entity.Entity;

/**
 * @time 2019/6/17
 * @des 附表2数据模型
 */
public class AttachSecondModel extends Entity {

    //序号
    public int id;

    //装置名称
    public String name;

    //装置型号
    public String number;

    //校验码
    public String check_code;

    //装置版本号
    public String verison;
    //生产厂家
    public String factory;


    public AttachSecondModel() {
    }

    public AttachSecondModel( int id, String name ) {
        this.id = id;
        this.name = name;
    }

    public AttachSecondModel( int id, String name, String number, String check_code, String verison, String factory) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.check_code = check_code;
        this.verison = verison;
        this.factory = factory;
    }
}
