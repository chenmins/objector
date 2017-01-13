package org.chenmin.open.objector;

import java.io.Serializable;

public interface Objector {

	IStoreTableRow create(Class<? extends IStoreTableRow> c);

	<T extends Serializable> T createObject(Class<? extends Serializable> c);
}
