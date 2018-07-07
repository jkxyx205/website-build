package com.rick.dev.plugin.syntable.model;

/**
 * Created by Rick.Xu on 2016/02/05.
 */
public class DbSQL {
    private String querySQL;

    private String insertSQL;

    private String updateSQL;

    private String cleanDestSQL;

    private String truncateTmpSQL;

    private String mergeSQL;

    private String keyColumns;

    private String allColumns;

    private String deleteByKeySQL;

	public String getQuerySQL() {
		return querySQL;
	}

	public void setQuerySQL(String querySQL) {
		this.querySQL = querySQL;
	}

	public String getInsertSQL() {
		return insertSQL;
	}

	public void setInsertSQL(String insertSQL) {
		this.insertSQL = insertSQL;
	}

	public String getUpdateSQL() {
		return updateSQL;
	}

	public void setUpdateSQL(String updateSQL) {
		this.updateSQL = updateSQL;
	}

	public String getCleanDestSQL() {
		return cleanDestSQL;
	}

	public void setCleanDestSQL(String cleanDestSQL) {
		this.cleanDestSQL = cleanDestSQL;
	}

	public String getTruncateTmpSQL() {
		return truncateTmpSQL;
	}

	public void setTruncateTmpSQL(String truncateTmpSQL) {
		this.truncateTmpSQL = truncateTmpSQL;
	}

	public String getMergeSQL() {
		return mergeSQL;
	}

	public void setMergeSQL(String mergeSQL) {
		this.mergeSQL = mergeSQL;
	}

	public String getKeyColumns() {
		return keyColumns;
	}

	public void setKeyColumns(String keyColumns) {
		this.keyColumns = keyColumns;
	}

	public String getAllColumns() {
		return allColumns;
	}

	public void setAllColumns(String allColumns) {
		this.allColumns = allColumns;
	}

	public String getDeleteByKeySQL() {
		return deleteByKeySQL;
	}

	public void setDeleteByKeySQL(String deleteByKeySQL) {
		this.deleteByKeySQL = deleteByKeySQL;
	}
}