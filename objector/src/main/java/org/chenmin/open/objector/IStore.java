package org.chenmin.open.objector;

import java.io.Serializable;

public interface IStore {

	boolean exsitTable(Serializable t) throws StoreException;

	boolean createTable(Serializable t) throws StoreException;

	boolean save(Serializable t) throws StoreException;

	boolean del(Serializable t) throws StoreException;

	boolean get(Serializable t) throws StoreException;

	boolean update(Serializable t) throws StoreException;

}
