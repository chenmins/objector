package org.chenmin.open.objector;

public interface Objector {

	IStoreTableRow create(Class<? extends IStoreTableRow> c);
	
}
