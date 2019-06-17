package com.hiscene.flytech.excel;

/**
 * @author Minamo
 * @e-mail kleinminamo@gmail.com
 * @time 2019/6/17
 * @des 表格接口
 */
public interface IExcel {

    /**
     * 读取表格
     */
    void read();

    /**
     * 写入表格
     */
    void write();

    /**
     * 临时恢复（形式不限于表格，可用数据库，JSON）
     */
    void restore();

    /**
     * 临时保存（形式不限于表格，可用数据库，JSON）
     */
    void svae();
}