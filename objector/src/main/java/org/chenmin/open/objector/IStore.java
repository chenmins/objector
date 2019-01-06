package org.chenmin.open.objector;

import java.io.Serializable;
import java.util.List;
import java.util.NavigableMap;

public interface IStore {

	boolean exsitTable(Serializable t) throws StoreException;

	boolean createTable(Serializable t) throws StoreException;
	
	boolean deleteTable(Serializable t) throws StoreException;

	boolean save(Serializable t) throws StoreException;

	boolean del(Serializable t) throws StoreException;

	boolean get(Serializable t) throws StoreException;

	boolean update(Serializable t) throws StoreException;
	
	boolean increment(Serializable t) throws StoreException;
	
	Serializable getRange(Serializable start,Serializable end,List<Serializable> range,boolean asc,int limit) throws StoreException;
	
	boolean getByMaxVersions(Serializable t,int max,NavigableMap<String,NavigableMap<Long,ColumnValueObject>> columnMap) throws StoreException;
	
	Objector getObjector() ;

	void setObjector(Objector objector) ;

	ITableStoreService getTableStoreService();

	void setTableStoreService(ITableStoreService tableStoreService);

}
