package com.rick.dev.plugin.syntable.model;

/**
 * Created by Rick.Xu on 2016/02/05.
 */
public class DbColumn {
    private String srcColumnName;

    private String descColumnName;

    public DbColumn(String columnName) {
      this(columnName, columnName);
    }

    public DbColumn(String srcColumnName, String descColumnName) {
        this.srcColumnName = srcColumnName;
        this.descColumnName = descColumnName;
    }

    public String getSrcColumnName() {
        return srcColumnName;
    }

    public String getDescColumnName() {
        return descColumnName;
    }
}
