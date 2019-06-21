package com.hiscene.flytech.entity;

import com.github.weiss.core.entity.Entity;

/**
 * @author Minamo
 * @e-mail kleinminamo@gmail.com
 * @time 2019/6/17
 * @des 工作过程数据模型
 */
public class ProcessModel extends Entity {

    //序号
    public int id;

    //作业内容
    public String content;

    //作业标准
    public String standard;

    //作业结果 false=0, true=1, null=-1
    public int result;

    public ProcessModel() {
    }

    public ProcessModel(int id, String content, String standard) {
        this(id, content, standard, -1);
    }

    public ProcessModel(int id, String content, String standard, int result) {
        this.id = id;
        this.content = content;
        this.standard = standard;
        this.result = result;
    }

}
