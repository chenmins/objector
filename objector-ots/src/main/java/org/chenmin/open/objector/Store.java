package org.chenmin.open.objector;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class Store implements IStore {

	protected Objector objector;
	
	protected ITableStoreService tableStoreService;

	public Objector getObjector() {
		return objector;
	}

	@Inject
	public void setObjector(Objector objector) {
		this.objector = objector;
	}

	public ITableStoreService getTableStoreService() {
		return tableStoreService;
	}

	@Inject
	public void setTableStoreService(ITableStoreService tableStoreService) {
		this.tableStoreService = tableStoreService;
	}

	protected Serializable createObject(Serializable t) {
		Serializable object = objector.createObject(t.getClass());
		return object;
	}

	protected Serializable copyObject(Serializable user) {
		try {
			Serializable userObject = createObject(user);
			BeanUtils.copyProperties(userObject, user);
			return userObject;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean save(Serializable t) throws StoreException {
		return tableStoreService.putRow((IStoreTableRow) copyObject(t));
	}

	@Override
	public boolean del(Serializable t) throws StoreException {
		return tableStoreService.deleteRow((IStoreTableRow) copyObject(t));
	}

	@Override
	public boolean get(Serializable t) throws StoreException {
		IStoreTableRow copyObject = (IStoreTableRow) copyObject(t);
		boolean row = tableStoreService.getRow(copyObject);
		try {
			BeanUtils.copyProperties(t, copyObject);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return row;
	}

	@Override
	public boolean update(Serializable t) throws StoreException {
		return tableStoreService.updateRow((IStoreTableRow) copyObject(t));
	}

	@Override
	public boolean exsitTable(Serializable t) throws StoreException {
		return tableStoreService.exsit((IStoreTableRow) copyObject(t));
	}

	@Override
	public boolean createTable(Serializable t) throws StoreException {
		return tableStoreService.createTable((IStoreTableRow) copyObject(t));
	}

}
