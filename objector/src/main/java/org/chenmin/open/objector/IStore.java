package org.chenmin.open.objector;

import java.io.Serializable;
import java.util.List;
import java.util.NavigableMap;
/**
 * 存储接口
 * @author Chenmin
 *
 */
public interface IStore {
	/**
	 * 表是否存在
	 * @param t 
	 * @return
	 * @throws StoreException
	 */
	boolean exsitTable(Serializable t) throws StoreException;

	/**
	 * 创建表
	 * @param t
	 * @return
	 * @throws StoreException
	 */
	boolean createTable(Serializable t) throws StoreException;
	/**
	 * 删除表
	 * @param t
	 * @return
	 * @throws StoreException
	 */
	boolean deleteTable(Serializable t) throws StoreException;

	/**
	 * 保存行
	 * @param t
	 * @return
	 * @throws StoreException
	 */
	boolean save(Serializable t) throws StoreException;

	/**
	 * 删除行
	 * @param t
	 * @return
	 * @throws StoreException
	 */
	boolean del(Serializable t) throws StoreException;

	/**
	 * 获得行
	 * @param t
	 * @return
	 * @throws StoreException
	 */
	boolean get(Serializable t) throws StoreException;

	/**
	 * 更新行
	 * @param t
	 * @return
	 * @throws StoreException
	 */
	boolean update(Serializable t) throws StoreException;
	
	/**
	 * 自增行
	 * @param t
	 * @return
	 * @throws StoreException
	 */
	boolean increment(Serializable t) throws StoreException;
	
	/**
	 * 范围查询
	 * @param start 开始对象
	 * @param end 结束对象
	 * @param range 返回结果集
	 * @param asc 是否升序
	 * @param limit 返回数量
	 * @return 是否有下一行
	 * @throws StoreException
	 */
	<T extends Serializable> T getRange(Serializable start,Serializable end,List<? extends Serializable> range,boolean asc,int limit) throws StoreException;
	
	/**
	 * 多版本查询
	 * @param t 查询对象
	 * @param max 最大版本
	 * @param columnMap 返回结果集
	 * @return
	 * @throws StoreException
	 */
	boolean getByMaxVersions(Serializable t,int max,NavigableMap<String,NavigableMap<Long,ColumnValueObject>> columnMap) throws StoreException;
	
	/**
	 * 
	 * @return 对象构造器
	 */
	Objector getObjector() ;

	/**
	 * 设置对象构造器
	 * @param objector 
	 */
	void setObjector(Objector objector) ;

	/**
	 * 表存储驱动
	 * @return
	 */
	ITableStoreService getTableStoreService();

	/**
	 * 设置表存储驱动
	 * @param tableStoreService 
	 */
	void setTableStoreService(ITableStoreService tableStoreService);

}
