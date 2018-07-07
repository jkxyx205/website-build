package com.rick.dev.plugin.syntable.service;


import com.rick.dev.plugin.syntable.model.DbColumn;
import com.rick.dev.plugin.syntable.model.DbSQL;
import com.rick.dev.plugin.syntable.model.DbTable;
import com.rick.dev.plugin.syntable.model.SyncType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.util.CollectionUtils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Rick.Xu on 2016/02/05.
 */
public class DbExecutionService {
	private final static Log LOGGER = LogFactory.getLog(DbExecutionService.class);
	
    private static final String COLUMN_NAME = "COLUMN_NAME";

    private static final String queryDeleteSQL = "SELECT KEY_ID FROM XX_POG_CHANGE WHERE STATUS = 4";

 
    private JdbcTemplate srcJdbcTemplate;

    private JdbcTemplate destJdbcTemplate;


    public JdbcTemplate getSrcJdbcTemplate() {
        return srcJdbcTemplate;
    }

    public void setSrcJdbcTemplate(JdbcTemplate srcJdbcTemplate) {
        this.srcJdbcTemplate = srcJdbcTemplate;
    }

    public JdbcTemplate getDestJdbcTemplate() {
        return destJdbcTemplate;
    }

    public void setdestJdbcTemplate(JdbcTemplate destJdbcTemplate) {
        this.destJdbcTemplate = destJdbcTemplate;
    }

    public void executeTask(JdbcTemplate srcJdbcTemplate, JdbcTemplate destJdbcTemplate,final DbTable tableMap, SyncType syncType,final ExecuteCallback executeCallback) throws Exception {
    	LOGGER.info("executeTask: " + tableMap.getSrcTableName() + " -> " + tableMap.getDestTableName());
        try {
            List<DbColumn> dbColumnSet = tableMap.getDbColumns();
            if (CollectionUtils.isEmpty(dbColumnSet)) { //the table are same structure
                tableMap.setDbColumns(getAllColumns(srcJdbcTemplate.getDataSource().getConnection(), tableMap.getDestTableName(), "xxxx"));
            }

            DbSQL dbSQL = DbFormatService.srcExtraSQL(tableMap);
            String insertSQL = dbSQL.getInsertSQL();
            if (syncType == SyncType.FULL)
                destJdbcTemplate.execute(dbSQL.getCleanDestSQL());
            else if(syncType == SyncType.MERAGE) {
                insertSQL = insertSQL.replace(tableMap.getDestTableName(), tableMap.getTempDestTableName());
                destJdbcTemplate.execute(dbSQL.getTruncateTmpSQL());
            }

            final int columnSize =  tableMap.getDbColumns().size();

            final List<Object[]> params = new ArrayList<Object[]>();

            executeCallback.executeBefore(srcJdbcTemplate, destJdbcTemplate);

            srcJdbcTemplate.query(dbSQL.getQuerySQL(), new RowCallbackHandler() {
                public void processRow(ResultSet rs) throws SQLException {
                    Object[] param = new Object[columnSize];
                    for (int i = 1; i <=columnSize; i++) {
                        param[i-1] = rs.getObject(i);
                    }
                    params.add(param);
                }
            });
            
            
            destJdbcTemplate.batchUpdate(insertSQL, params);
            if(syncType == SyncType.MERAGE) {
                destJdbcTemplate.execute(dbSQL.getMergeSQL());
            }
            executeCallback.executeAfter(srcJdbcTemplate, destJdbcTemplate);
        } catch (Exception e) {
            throw e;
        }
    }
    
	public void executeTask(final  DbTable tableMap, SyncType syncType,final ExecuteCallback executeCallback) throws Exception {
        executeTask(this.srcJdbcTemplate, this.destJdbcTemplate, tableMap, syncType, executeCallback);
    }

    public void executeTask(DbTable tableMap,ExecuteCallback executeCallback) throws Exception {
        executeTask(tableMap, SyncType.FULL,executeCallback);
    }
    public void executeTask(DbTable tableMap) throws Exception {
        executeTask(tableMap, SyncType.FULL,new ExecuteCallbackAdapter());
    }
    public static interface ExecuteCallback {
        public void executeBefore(JdbcTemplate srcJdbcTemplate, JdbcTemplate destJdbcTemplate);
        public void executeAfter(JdbcTemplate srcJdbcTemplate,JdbcTemplate destJdbcTemplate);
    }

    public class ExecuteCallbackAdapter implements ExecuteCallback{
        public void executeBefore(JdbcTemplate srcJdbcTemplate, JdbcTemplate destJdbcTemplate) {
        }
        public void executeAfter(JdbcTemplate srcJdbcTemplate, JdbcTemplate destJdbcTemplate) {
        }
    }

    /**
     * first delete,
     * then insert
     * @param targetTable
     * @param keyIds
     * @throws Exception
     */
    public void executeMerge(String targetTable, final String ... keyIds) throws Exception {
    	LOGGER.info("executeMerge table:" + targetTable);
    	List<DbColumn> dbColumns = getAllColumns(srcJdbcTemplate.getDataSource().getConnection(), targetTable, "");
        DbTable dbTable = new DbTable(targetTable, dbColumns);
        dbTable.getDestTableKeyColumn().addAll(Arrays.asList(keyIds));
        DbSQL dbSQL = DbFormatService.srcExtraSQL(dbTable);

        final List<Object[]> params = new ArrayList<Object[]>();
        destJdbcTemplate.query("SELECT :COL FROM :TARGET_TABLE".replaceAll(":TARGET_TABLE", targetTable).replaceAll(":COL",dbSQL.getKeyColumns()), new RowCallbackHandler() {
            private int columnCount = -1;
          
            public void processRow(ResultSet rs) throws SQLException {
                if (columnCount < 0)
                    columnCount = rs.getMetaData().getColumnCount();

                Object[] param = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    param[i-1] =  rs.getObject(i);
                }
                params.add(param);
            }
        });
        srcJdbcTemplate.batchUpdate(dbSQL.getDeleteByKeySQL(), params);
        //
        executeTask(this.srcJdbcTemplate, this.destJdbcTemplate, new DbTable(targetTable),SyncType.INSERT,new ExecuteCallbackAdapter());
    }
    
    /**
     * fist update row, affected row == 0
     *   then insert 
     * @param targetTable
     * @param keyIds
     * @throws Exception
     */
    public void executeMerge2(String targetTable, final String ... keyIds) throws Exception {
    	LOGGER.info("executeMerge table:" + targetTable);
        List<DbColumn> dbColumns = getAllColumns(srcJdbcTemplate.getDataSource().getConnection(), targetTable, "");
        DbTable dbTable = new DbTable(targetTable, dbColumns);
        dbTable.getDestTableKeyColumn().addAll(Arrays.asList(keyIds));
        final DbSQL dbSQL = DbFormatService.srcExtraSQL(dbTable);
        
        final int keyLen = keyIds.length;

        destJdbcTemplate.query("SELECT :COL FROM :TARGET_TABLE".replaceAll(":TARGET_TABLE", targetTable).replaceAll(":COL",dbSQL.getAllColumns()), new RowCallbackHandler() {
            private int columnCount = -1;
          
            public void processRow(ResultSet rs) throws SQLException {
                if (columnCount < 0)
                    columnCount = rs.getMetaData().getColumnCount();

                List<Object> param = new ArrayList<Object>(columnCount + keyLen);
                
                for (int i = 1; i <= columnCount; i++) {
                	param.add(rs.getObject(i));
                    
                }
                Object[] paramInsert = param.toArray();
                for (String keyId : keyIds) {
                	param.add(rs.getObject(keyId));
                }
                Object[] paramUpdate = param.toArray();
                
                int c = srcJdbcTemplate.update(dbSQL.getUpdateSQL(), paramUpdate);
                if (c == 0)
                	srcJdbcTemplate.update(dbSQL.getInsertSQL(), paramInsert);
            }
        });
    }

    /**
     * this is common function
     * @param targetTable
     * @throws SQLException
     */
    public void executeDeleteByKeyIds(String targetTable, final String ... keyIds) throws SQLException {
    	DbTable dbTable = new DbTable(targetTable);
    	dbTable.getDestTableKeyColumn().addAll(Arrays.asList(keyIds));
    	final DbSQL dbSQL = DbFormatService.srcExtraSQL(dbTable);
    	
    	
        final List<Object[]> batchArgs = new ArrayList<Object[]>();
        final int len = keyIds.length;

        destJdbcTemplate.query(queryDeleteSQL, new RowCallbackHandler() {
          
            public void processRow(ResultSet rs) throws SQLException {
            	Object[] param = new Object[len];
            	for (int i = 0; i < len; i++) {
            		param[i] = rs.getObject(keyIds[i]);
            	}
                batchArgs.add(param);
            }
        });
        if (batchArgs.size() > 0)
        	srcJdbcTemplate.batchUpdate(dbSQL.getDeleteByKeySQL(), batchArgs);
    }

    private List<DbColumn> getAllColumns(Connection conn, String tableName, String schemaName) throws SQLException {
        DatabaseMetaData md = conn.getMetaData();
        List<DbColumn> dbColumnSet = new ArrayList<DbColumn>();
        ResultSet rs = md.getColumns(null, schemaName, tableName,"%");
        while (rs.next()) {
            String columnName = rs.getString(COLUMN_NAME);//列名
            dbColumnSet.add(new DbColumn(columnName));
        }
        rs.close();
        conn.close();
        return dbColumnSet;
    }
    
}