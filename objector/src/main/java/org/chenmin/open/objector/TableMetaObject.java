package org.chenmin.open.objector;

import java.util.LinkedList;
import java.util.List;

public class TableMetaObject implements IStoreTable{
	private String tablename;

	private List<PrimaryKeySchemaObject> primaryKey = new LinkedList<PrimaryKeySchemaObject>();

	public String getTablename() {
		return tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public List<PrimaryKeySchemaObject> getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(List<PrimaryKeySchemaObject> primaryKey) {
		this.primaryKey = primaryKey;
	}

}
