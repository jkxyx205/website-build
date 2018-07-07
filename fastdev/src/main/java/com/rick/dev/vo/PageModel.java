package com.rick.dev.vo;

import java.util.Map;

/**
 * Created by Rick.Xu on 2016/03/22.
 */
public class PageModel {
    private int page; //当前页
    private int rows = -1;    //每页显示行数,rows == -1 一次性全部加载出来,不再分页
    private String sidx;
    private String sord;
    private String queryName;

    public PageModel() {
    }

    public PageModel(String queryName) {
        this.queryName = queryName;
    }

    private Map<String,String> dicMap;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getSidx() {
        return sidx;
    }

    public void setSidx(String sidx) {
        this.sidx = sidx;
    }

    public String getSord() {
        return sord;
    }

    public void setSord(String sord) {
        this.sord = sord;
    }

    public String getQueryName() {
        return queryName;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    public Map<String, String> getDicMap() {
        return dicMap;
    }

    public void setDicMap(Map<String, String> dicMap) {
        this.dicMap = dicMap;
    }
}
