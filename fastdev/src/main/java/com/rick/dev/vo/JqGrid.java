package com.rick.dev.vo;

import java.util.List;

/**
 * Created by Rick.Xu on 2016/03/22.
 */
public class JqGrid<T> {
//    private List<Map<String,Object>> rows;
    private List<T> rows;
    private long records;
    private long total;
    private int page;
    private int pageRows;
//    public List<Map<String, Object>> getRows() {
//        return rows;
//    }
//    public void setRows(List<Map<String, Object>> rows) {
//        this.rows = rows;
//    }


    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public long getRecords() {
        return records;
    }
    public void setRecords(long records) {
        this.records = records;
    }
    public long getTotal() {
        return total;
    }
    public void setTotal(long total) {
        this.total = total;
    }
    public int getPage() {
        return page;
    }
    public void setPage(int page) {
        this.page = page;
    }

    public int getPageRows() {
        return pageRows;
    }

    public void setPageRows(int pageRows) {
        this.pageRows = pageRows;
    }
}
