package com.moyou.activity.util.csv;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wyf 2018/11/6
 */
public class CsvImportResult<T> {

    //总记录数
    private int totalNum;

    //成功记录数
    private int successNum;

    //失败记录
    private List<T> failList = new ArrayList<>();

    //成功记录
    private List<T> successList = new ArrayList<>();

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public int getSuccessNum() {
        return successNum;
    }

    public void setSuccessNum(int successNum) {
        this.successNum = successNum;
    }

    public List<T> getFailList() {
        return failList;
    }

    public void setFailList(List<T> failList) {
        this.failList = failList;
    }

    public List<T> getSuccessList() {
        return successList;
    }

    public void setSuccessList(List<T> successList) {
        this.successList = successList;
    }
}
