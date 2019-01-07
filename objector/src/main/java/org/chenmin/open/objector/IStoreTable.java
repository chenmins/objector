package org.chenmin.open.objector;

import java.util.List;
/**
 * 表对象
 * @author Chenmin
 *
 */
public interface IStoreTable {
	/**
	 * 
	 * @return 表名
	 */
	String getTablename();
	
	/**
	 * 
	 * @return 主键列表
	 */
	List<PrimaryKeySchemaObject> getPrimaryKey();
	
	/**
	 * 
	 * @return 数据存活时间 单位：秒。
	 */
	int timeToLive();
	
	/**
	 * 
	 * @return 每个属性列保留的最大版本数	
	 */
	int maxVersions();
	
	/**
	 * 写入数据的时间戳与系统当前时间的偏差允许最大值 单位：秒。
	 * @return
	 */
	long maxTimeDeviation();
	
	/**
	 * 
	 * @return 读能力单元的值
	 */
	int readCapacityUnit();
	
	/**
	 * 
	 * @return 写能力单元的值
	 */
	int writeCapacityUnit();
	
	/**
	 * 
	 * @return 自增键名
	 */
	String autoPrimaryKey();
	
}
