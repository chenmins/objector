package org.chenmin.open.objector;

import java.io.Serializable;

public interface Objector {

	<T extends Serializable> T createObject(Class<? extends Serializable> c);
	
}
