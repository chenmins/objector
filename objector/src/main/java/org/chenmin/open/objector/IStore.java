package org.chenmin.open.objector;

import java.io.Serializable;

public interface IStore {
	boolean exsitTable(Serializable t);

	boolean createTable(Serializable t);
	
	boolean save(Serializable t);

	boolean del(Serializable t);

	boolean get(Serializable t);

	boolean update(Serializable t);
	
}
