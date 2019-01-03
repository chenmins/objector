package org.chenmin.open.objector;

import java.util.List;

public interface IStoreTable {
	
	public String getTablename();
	
	public List<PrimaryKeySchemaObject> getPrimaryKey();
	
	int timeToLive();
	
	int maxVersions();
	
	long maxTimeDeviation();
	
	int readCapacityUnit();
	
	int writeCapacityUnit();
	
}
