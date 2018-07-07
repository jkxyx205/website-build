package com.rick.dev.plugin.syntable.service;


import com.rick.dev.plugin.syntable.model.DbColumn;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Rick.Xu on 2016/02/18.
 */
public final class DbColumnUtils {

    /**
     * if the table between different datasource has the same structure(same column name)
     * @return columnsLables
     */
    public static Set<DbColumn> getSameMappingColumnsSet(String ... columnsLabels) {
        Set<DbColumn> dbColumns = new HashSet<DbColumn>();
        for (String columnsLabel : columnsLabels) {
            DbColumn column = new DbColumn(columnsLabel);
            dbColumns.add(column);
        }
        return dbColumns;
    }
}
