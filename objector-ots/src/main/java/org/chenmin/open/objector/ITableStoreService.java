package org.chenmin.open.objector;

import org.chenmin.open.objector.IStoreTable;
import org.chenmin.open.objector.IStoreTableRow;

/**
 * 表格存储服务
 * 
 * @author Chenmin
 *
 */
public interface ITableStoreService {

	boolean exsit(IStoreTable table);

	boolean createTable(IStoreTable table);

	boolean init();

	boolean putRow(IStoreTableRow row);
	
	boolean getRow(IStoreTableRow row);
	
	boolean deleteRow(IStoreTableRow row);
	
	boolean updateRow(IStoreTableRow row);
	
}
