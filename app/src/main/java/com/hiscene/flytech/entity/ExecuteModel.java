package com.hiscene.flytech.entity;

import com.github.weiss.core.entity.Entity;

import java.util.Date;

/**
 * @author huangyu
 * @des 措施单执行/恢复部分数据模型
 */
public class ExecuteModel extends Entity {
    //序号
    public String id;

    //安全技术措施内容
    public String content;

    //执行结果 false=0, true=1, null=-1
    public int excute_result;

    //执行时间
    public String execute_date;

    //恢复结果 false=0, true=1, null=-1
    public int recover_result;

    //恢复时间
    public String recover_date;

    public ExecuteModel(String id, String content) {
        this(id,content,-1,null,-1,null);
    }

    public ExecuteModel(String id, String content, int excute_result, String execute_date, int recover_result, String recover_date) {
        this.id = id;
        this.content = content;
        this.excute_result = excute_result;
        this.execute_date = execute_date;
        this.recover_result = recover_result;
        this.recover_date = recover_date;
    }

    public int getExcute_result() {
        return excute_result;
    }
}
