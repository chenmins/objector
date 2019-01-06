package org.chenmin.open.objector;

import java.util.NavigableMap;

/**
 * 表格存储服务
 * 
 * @author Chenmin
 *
 */
public interface ITableStoreService {

	boolean exsit(IStoreTable table);

	boolean createTable(IStoreTable table);
	
	boolean deleteTable(IStoreTable table);

	boolean init();

	boolean putRow(IStoreTableRow row);
	
	boolean getRow(IStoreTableRow row);
	
	boolean deleteRow(IStoreTableRow row);
	
	boolean updateRow(IStoreTableRow row);
	
	boolean getByMaxVersions(IStoreTableRow t,int max,NavigableMap<String,NavigableMap<Long,ColumnValueObject>> columnMap) ;
	
	boolean increment(IStoreTableRow t);
}
