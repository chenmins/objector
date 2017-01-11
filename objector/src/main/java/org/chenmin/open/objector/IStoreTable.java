package org.chenmin.open.objector;

import java.util.List;

public interface IStoreTable {
	public String getTablename();
	
	public List<PrimaryKeySchemaObject> getPrimaryKey();
	
}
