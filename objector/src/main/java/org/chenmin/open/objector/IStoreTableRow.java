package org.chenmin.open.objector;

import java.util.Map;
/**
 * 行对象接口
 * @author Chenmin
 *
 */
public interface IStoreTableRow  extends IStoreTable {
	/**
	 * 获得主键的值
	 * @return
	 */
	Map<String, PrimaryKeyValueObject> getPrimaryKeyValue();

	/**
	 * 获得列的值
	 * @return
	 */
	Map<String, ColumnValueObject> getColumnValue();
	
	/**
	 * 设置主键的值
	 * @param v
	 */
	void setPrimaryKeyValue(Map<String, PrimaryKeyValueObject> v);
	
	/**
	 * 设置列的值
	 * @param v
	 */
	void setColumnValue(Map<String, ColumnValueObject> v);
}
