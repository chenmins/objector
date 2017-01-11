package org.chenmin.open.objector;

import java.util.Map;

public interface IStoreTableRow  extends IStoreTable {
	
	Map<String, PrimaryKeyValueObject> getPrimaryKeyValue();

	Map<String, ColumnValueObject> getColumnValue();
	
	void setPrimaryKeyValue(Map<String, PrimaryKeyValueObject> v);
	
	void setColumnValue(Map<String, ColumnValueObject> v);
}
