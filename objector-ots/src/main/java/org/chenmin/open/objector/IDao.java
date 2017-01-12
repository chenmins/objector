package org.chenmin.open.objector;

import org.chenmin.open.objector.IStoreTableRow;

public interface IDao<T extends IStoreTableRow> {
	
	boolean save(T t);

	boolean del(T t);

	boolean get(T t);

	boolean update(T t);
}
