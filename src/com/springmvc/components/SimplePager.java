package com.springmvc.components;

/**
 * Created by vohidjon-linux on 1/28/16.
 */
public class SimplePager {
    private Integer page;
    private Integer rows;
    public SimplePager(Integer page, Integer rows){
        this.page = page;
        this.rows = rows;
    }
    public SimplePager(){}
    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }
}
