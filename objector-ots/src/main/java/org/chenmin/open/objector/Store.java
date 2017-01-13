package org.chenmin.open.objector;

import java.io.Serializable;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class Store implements IStore {
	protected ITableStoreService tableStoreService;

	public ITableStoreService getTableStoreService() {
		return tableStoreService;
	}

	@Inject
	public void setTableStoreService(ITableStoreService tableStoreService) {
		this.tableStoreService = tableStoreService;
	}

	@Override
	public boolean save(Serializable t) {
		return tableStoreService.putRow((IStoreTableRow) t);
	}

	@Override
	public boolean del(Serializable t) {
		return tableStoreService.deleteRow((IStoreTableRow) t);
	}

	@Override
	public boolean get(Serializable t) {
		return tableStoreService.getRow((IStoreTableRow) t);
	}

	@Override
	public boolean update(Serializable t) {
		return tableStoreService.updateRow((IStoreTableRow) t);
	}

	@Override
	public boolean exsitTable(Serializable t) {
		return tableStoreService.exsit((IStoreTableRow) t);
	}

	@Override
	public boolean createTable(Serializable t) {
		return tableStoreService.createTable((IStoreTableRow) t);
	}

}
