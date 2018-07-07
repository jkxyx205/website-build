package com.rick.dev.plugin.syntable.service;

import com.rick.dev.plugin.syntable.model.DbColumn;
import com.rick.dev.plugin.syntable.model.DbSQL;
import com.rick.dev.plugin.syntable.model.DbTable;
import org.springframework.util.CollectionUtils;

/**
 * Created by Rick.Xu on 2016/02/05.
 */
public final class DbFormatService {
    static DbSQL srcExtraSQL(DbTable tableMap) {
        int columnSize = tableMap.getDbColumns().size();
        if (columnSize == 0) {
        	//WARNING
        	//throw new RuntimeException(tableMap.getDestTableName() + " has no column.");
        }

        StringBuilder querySQL = new StringBuilder("SELECT ");
        StringBuilder insertSQL = new StringBuilder("INSERT INTO ");
        StringBuilder updateSQL = new StringBuilder("UPDATE ");

        StringBuilder srcSQLColumn = new StringBuilder(" ");
        StringBuilder descSQLColumn = new StringBuilder(" ");

        StringBuilder keyColumnIds = new StringBuilder(" ");

        StringBuilder deleteSQL = new StringBuilder("DELETE FROM ").append(tableMap.getDestTableName()).append(" WHERE ");


        updateSQL.append(tableMap.getDestTableName()).append(" SET ");

        for (DbColumn columnMap : tableMap.getDbColumns()) {
            srcSQLColumn.append(columnMap.getSrcColumnName()).append(",");
            descSQLColumn.append(columnMap.getDescColumnName()).append(",");
            updateSQL.append(columnMap.getDescColumnName()).append("= ?,");
        }
        srcSQLColumn.deleteCharAt(srcSQLColumn.length()-1);
        descSQLColumn.deleteCharAt(descSQLColumn.length()-1);
        updateSQL.deleteCharAt(updateSQL.length()-1);

        querySQL.append(srcSQLColumn).append(" FROM ").append(tableMap.getSrcTableName());

        insertSQL.append(tableMap.getDestTableName()).append("(").append(descSQLColumn.toString()).append(") ").append("VALUES(");
        updateSQL.append(" WHERE ");


        for (int i = 0; i < columnSize; i++)
            insertSQL.append("?,");

        insertSQL.deleteCharAt(insertSQL.length()-1);
        insertSQL.append(")");

        //mergeSQL it's Oracle syntax
        StringBuilder mergeSQL =  new StringBuilder();
        if (!CollectionUtils.isEmpty(tableMap.getDestTableKeyColumn())) {
            String[] keyColumns = tableMap.getDestTableKeyColumn().toArray(new String[]{});
            StringBuilder condition = new StringBuilder("(");
            for (String keyColumn : keyColumns) {
                condition.append("T1.").append(keyColumn).append(" = ").append("T2.").append(keyColumn).append(" AND ");
                updateSQL.append(keyColumn).append(" = ?").append(" AND ");
                deleteSQL.append(keyColumn).append(" = ?").append(" AND ");
                keyColumnIds.append(keyColumn).append(",");
            }
            condition.delete(condition.length() - 4,condition.length()).append(")");
            updateSQL.delete(updateSQL.length() - 4, updateSQL.length());
            deleteSQL.delete(deleteSQL.length() - 4,deleteSQL.length());
            keyColumnIds.delete(keyColumnIds.length() - 1,keyColumnIds.length());

            StringBuilder update = new StringBuilder();
            StringBuilder tmpColumn = new StringBuilder();
            for (String columns : descSQLColumn.toString().split(",")) {
                tmpColumn.append("T2.").append(columns).append(",");
                if (tableMap.getDestTableKeyColumn().contains(columns))
                    continue;

                update.append("T1.").append(columns).append(" = ").append("T2.").append(columns).append(",");
            }
            tmpColumn.deleteCharAt(tmpColumn.length() - 1);
            update.deleteCharAt(update.length() - 1);

            mergeSQL.append("MERGE INTO ")
                    .append(tableMap.getDestTableName())
                    .append(" T1 USING ")
                    .append(tableMap.getTempDestTableName()).append(" T2 ON").append(condition)
                    .append(" WHEN MATCHED THEN UPDATE SET ").append(update)
                    .append(" WHEN NOT MATCHED THEN ").append("INSERT(").append(descSQLColumn).append(") VALUES (").append(tmpColumn).append(")");
        }
        //cleanSQL
        StringBuilder cleanDescSQL = new StringBuilder("DELETE FROM ");
        cleanDescSQL.append(tableMap.getDestTableName());

        DbSQL dbSQL = new DbSQL();
        dbSQL.setInsertSQL(insertSQL.toString());
        dbSQL.setQuerySQL(querySQL.toString());
        dbSQL.setMergeSQL(mergeSQL.toString());
        dbSQL.setCleanDestSQL(cleanDescSQL.toString());
        dbSQL.setTruncateTmpSQL("DELETE TABLE " + tableMap.getTempDestTableName());
        dbSQL.setAllColumns(descSQLColumn.toString());
        dbSQL.setUpdateSQL(updateSQL.toString());
        dbSQL.setDeleteByKeySQL(deleteSQL.toString());
        dbSQL.setKeyColumns(keyColumnIds.toString());
        return dbSQL;
    }
}

