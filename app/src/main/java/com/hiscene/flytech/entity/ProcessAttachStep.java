package com.hiscene.flytech.entity;

/**
 *作业过程表与附表关联
 */
public class ProcessAttachStep {

    public String row;
    public String attach;
    public String count;
    public String step;

    public ProcessAttachStep( String row, String attach, String count, String step ) {
        this.row = row;
        this.attach = attach;
        this.count = count;
        this.step = step;
    }
}