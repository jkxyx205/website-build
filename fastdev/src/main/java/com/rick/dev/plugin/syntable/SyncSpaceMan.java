package com.rick.dev.plugin.syntable;


import com.rick.dev.plugin.syntable.model.ChangeStatus;
import com.rick.dev.plugin.syntable.model.DbTable;
import com.rick.dev.plugin.syntable.service.DbExecutionService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rick.Xu on 2016/02/19.
 */
public class SyncSpaceMan {
	private final static Log LOGGER = LogFactory.getLog(SyncSpaceMan.class);
	
    private static final String[] ABOVE_TABLES = {
    											 "SC_FONT",
    											 "SC_PL_SCHEMATIC",
    											 "SC_PLANOGRAM",
											     "SC_NSW",
											     "SC_PRODUCT_LIST",
											     "SC_SECTION",
											     "SC_POSITION",
											     "SC_FIXEL"
											     };
    public DbExecutionService getDbExecutionService() {
        return dbExecutionService;
    }

    public void setDbExecutionService(DbExecutionService dbExecutionService) {
        this.dbExecutionService = dbExecutionService;
    }

    private DbExecutionService dbExecutionService;

    private static final String  XX_POG_CHANGE= "select KEY_ID, PLANOGRAM, STATUS, CREATE_DATETIME, LAST_UPDATE_DATETIME from XX_POG_CHANGE f1 WHERE EXISTS( "+
    											"select 1 from (SELECT key_id, planogram,max(create_datetime) create_datetime FROM XX_POG_CHANGE WHERE status IN (0,4)"+
												" GROUP BY key_id, planogram) f2 " +
												" where f1.key_id = f2.key_id and f1.planogram = f2.planogram and f1.create_datetime = f2.create_datetime)";

    private static final String BASE_TABLE_SQL = "SELECT *"+
									            " FROM :TARGET_TABLE T"+
									            " WHERE EXISTS("+
									            " SELECT 1"+
									            " FROM XX_POG_CHANGE T2"+
									            " WHERE "+
									            " T2.KEY_ID =T.KEY_ID AND T2.STATUS = 0"+
									            ")";
    
    private static final String PRODUCT_DETAIL_TABLE = "SELECT SC_PRODUCT.* " +
														" FROM SC_PRODUCT" +
														" WHERE EXISTS (SELECT 1" +
														"          FROM SC_PRODUCT_LIST LST" +
														"        INNER JOIN XX_POG_CHANGE CHANGE" +
														"          ON CHANGE.KEY_ID = LST.KEY_ID" +
														"      WHERE SC_PRODUCT.PRODUCT_ID = LST.PRODUCT_ID" +
													    "       AND CHANGE.STATUS = 0)";

    public static final String UPDATE_STATUS = "UPDATE XX_POG_CHANGE SET STATUS = ?, LAST_UPDATE_DATETIME = GETDATE() WHERE STATUS IN(0,4) AND KEY_ID IN(:PARAM)";
    
    public static final String LOCAL_CHANGE_COUNT = "SELECT COUNT(1) FROM XX_POG_CHANGE WHERE STATUS IN(0,4)";

    /**
     * 同步local到SpaceMaster临时表中
     * @throws Exception
     */
    private void syncLocal2Master() throws Exception {
		dbExecutionService.executeTask(new DbTable(BASE_TABLE_SQL.replace(":TARGET_TABLE", "SC_PLANO_KEY"), "SC_PLANO_KEY"));
		
        //sync tables from MY local to Space Master
        dbExecutionService.executeTask(new DbTable(XX_POG_CHANGE, "XX_POG_CHANGE"));
        dbExecutionService.executeTask(new DbTable(PRODUCT_DETAIL_TABLE, "SC_PRODUCT"));
        for (String targetTable : ABOVE_TABLES) {
            dbExecutionService.executeTask(new DbTable(BASE_TABLE_SQL.replace(":TARGET_TABLE", targetTable), targetTable));
        }
      
    }
    
    /**
     * SpaceMaster临时表同步到中心库中
     * @throws Exception
     */

    private void syncMaster2Center() throws Exception {
    	//INSERT/UPDATE
        dbExecutionService.executeMerge("XX_POG_CHANGE","KEY_ID");
	    //first update,if affected rows == 0  then insert
	    dbExecutionService.executeMerge2("SC_PLANO_KEY", "KEY_ID");
 
        dbExecutionService.executeMerge("SC_FIXEL", "KEY_ID");
        dbExecutionService.executeMerge("SC_NSW", "KEY_ID");
        dbExecutionService.executeMerge("SC_PRODUCT", "PRODUCT_ID");
        dbExecutionService.executeMerge("SC_PRODUCT_LIST", "KEY_ID");
        dbExecutionService.executeMerge("SC_SECTION", "KEY_ID");
	    dbExecutionService.executeMerge("SC_POSITION", "KEY_ID");
	    dbExecutionService.executeMerge("SC_PL_SCHEMATIC", "KEY_ID");
	    dbExecutionService.executeMerge("SC_PLANOGRAM", "KEY_ID");
	    dbExecutionService.executeMerge("SC_FONT", "KEY_ID");
        
        //DELETE
	    for (String table :ABOVE_TABLES)
	    	dbExecutionService.executeDeleteByKeyIds(table, "KEY_ID");
	    
        dbExecutionService.executeDeleteByKeyIds("SC_PLANO_KEY","KEY_ID");
        
    }

    /**
     * 同步loacl到中心门面
     * 	1. 设置jdbcTemplate
     * 	2. 同步local到SpaceMaster临时表中
     * 	3. SpaceMaster临时表同步到中心库中
     * @param jobLog
     * @throws Exception
     */
    public synchronized void syncMYLocal2Center() throws Exception {
    	/*if (dbExecutionService.getCenterJdbcTemplate() == null) {
    		SMBaseDao dao = (SMBaseDao)ApplicationContextHelper.getBean("SMBaseDao");
    		dbExecutionService.setSrcJdbcTemplate(dao.getSpacemanLocalJdbcTemplate());
    		dbExecutionService.setCenterJdbcTemplate(dao.getSpacemanJdbcTemplate());
    	}
    	
        long start = System.currentTimeMillis();
        
        if (checkIfNeedSync()) {
        	syncLocal2Master();
            syncMaster2Center();
        }
        
        long end = System.currentTimeMillis();
        LOGGER.info("Sync MYLocal to Center costs " + (end - start) + "ms");
        jobLog.setErrorDetail("Sync MYLocal to Center costs " + (end - start) + "ms");*/
    }
    
    /**
     * 检查local XX_POG_CHANGE是否需要同步操作
     * @return
     */
    private boolean checkIfNeedSync() {
    	JdbcTemplate localJdbcTemplate = dbExecutionService.getSrcJdbcTemplate();
    	int count = localJdbcTemplate.queryForObject(LOCAL_CHANGE_COUNT, Integer.class);
		return count > 0 ? true : false;
	}

/**
    * 同步完成之后，修改local XX_POG_CHANGE状态
    * @param status
    */
   public synchronized void syncMYlocalChangeTableStatus(final ChangeStatus status) {
	   final List<Object> params = new ArrayList<Object>(1024);
	   params.add(status.getStatus());
	   
	   final StringBuilder paramBuilder = new StringBuilder();
	   
	   JdbcTemplate masterJdbcTemplate = dbExecutionService.getDestJdbcTemplate();
	   
 	   masterJdbcTemplate.query("SELECT KEY_ID FROM XX_POG_CHANGE", new RowCallbackHandler() {
 		   
 		private int rowIndex = 1;
		public void processRow(ResultSet rs) throws SQLException {
			if (rowIndex % 1000 == 0) {
				changeStatusStep(params, paramBuilder);
		 	    params.clear();
		 	    params.add(status.getStatus());
		 	    paramBuilder.setLength(0);
			}
			
			params.add(rs.getInt(1));
			paramBuilder.append("?,");
			rowIndex++;
		}
		   
	   });
 	   
 	   if (paramBuilder.length() > 0) {
 		  changeStatusStep(params, paramBuilder);
 	   }
   }
   
   private void changeStatusStep(final List<Object> params,
			final StringBuilder paramBuilder) {
		paramBuilder.deleteCharAt(paramBuilder.length()-1);
		JdbcTemplate jdbcTemplate = dbExecutionService.getSrcJdbcTemplate();
		jdbcTemplate.update(UPDATE_STATUS.replaceAll(":PARAM", paramBuilder.toString()), params.toArray());
	}
}