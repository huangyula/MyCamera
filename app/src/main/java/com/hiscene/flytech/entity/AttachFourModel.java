package com.hiscene.flytech.entity;

import com.github.weiss.core.entity.Entity;

/**
 * @time 2019/6/17
 * @des 附表3数据模型
 */
public class AttachFourModel extends Entity {

    public String time_1;//第一组跳闸

    public String time_2;//第二组跳闸

    public AttachFourModel() {
    }

    public AttachFourModel( String time_1, String time_2 ) {
        this.time_1 = time_1;
        this.time_2 = time_2;
    }
}
