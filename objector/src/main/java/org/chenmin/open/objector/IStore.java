package org.chenmin.open.objector;

import java.io.Serializable;

public interface IStore {
	
	boolean save(Serializable t);

	boolean del(Serializable t);

	boolean get(Serializable t);

	boolean update(Serializable t);
	
}
