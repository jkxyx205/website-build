package com.rick.dev.plugin.syntable.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rick.Xu on 2016/02/05.
 */
public class DbTable {

    public static final String TEMP_TABLE_PREFIX = "TMP_";

    private String srcTableName;

    private String destTableName;

    private String tempDestTableName;

    private List<DbColumn> dbColumns;

    private List<String> destTableKeyColumn = new ArrayList<String>();

    public DbTable(String tableName) {
        this(tableName, tableName);
    }

    public DbTable(String srcTableName, String desTableName) {
        this(srcTableName, desTableName,new ArrayList<DbColumn>());
    }

    public DbTable(String tableName,List<DbColumn> dbColumns) {
        this(tableName, tableName, dbColumns);
    }

    public DbTable(String srcTableName, String desTableName, List<DbColumn> dbColumns) {
        this.srcTableName = srcTableName;

        //check table Name
        if (!srcTableName.matches("\\S+")) {
            this.srcTableName = "(" + srcTableName + ") as " + desTableName;
        }

        this.destTableName = desTableName;
        this.dbColumns = dbColumns;
    }

    public List<String> getDestTableKeyColumn() {
        return destTableKeyColumn;
    }

    public String getSrcTableName() {
        return srcTableName;
    }

    public String getDestTableName() {
        return destTableName;
    }

    public List<DbColumn> getDbColumns() {
        return dbColumns;
    }

    public void setDestTableName(String destTableName) {
        this.destTableName = destTableName;
    }

    public void setTempDestTableName(String tempDestTableName) {
        this.tempDestTableName = tempDestTableName;
    }

    public String getTempDestTableName() {
        return tempDestTableName == null ? TEMP_TABLE_PREFIX + destTableName :tempDestTableName;
    }

    public void setDbColumns(List<DbColumn> dbColumns) {
        this.dbColumns = dbColumns;
    }
}
