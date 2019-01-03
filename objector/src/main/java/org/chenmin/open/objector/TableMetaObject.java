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

	@Override
	public int timeToLive() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int maxVersions() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long maxTimeDeviation() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int readCapacityUnit() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int writeCapacityUnit() {
		// TODO Auto-generated method stub
		return 0;
	}

}
