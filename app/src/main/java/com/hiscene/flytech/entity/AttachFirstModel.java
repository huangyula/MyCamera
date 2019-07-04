package com.hiscene.flytech.entity;

import com.github.weiss.core.entity.Entity;

/**
 * @time 2019/6/17
 * @des 附表1数据模型
 */
public class AttachFirstModel extends Entity {

    //序号
    public int id;

    //内容
    public String content;

    //标准
    public String standard;

    //电阻值 false=0, true=1, null=-1
    public String result;

    public AttachFirstModel() {
    }

    public AttachFirstModel(int id, String content, String standard) {
        this(id, content, standard, "");
    }

    public AttachFirstModel(int id, String content, String standard, String result) {
        this.id = id;
        this.content = content;
        this.standard = standard;
        this.result = result;
    }

}
