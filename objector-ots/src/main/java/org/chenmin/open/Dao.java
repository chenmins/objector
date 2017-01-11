package org.chenmin.open;

import org.chenmin.open.objector.IStoreTableRow;

import com.google.inject.Inject;

public class Dao<T extends IStoreTableRow> implements IDao<T> {
	protected ITableStoreService tableStoreService;

	public ITableStoreService getTableStoreService() {
		return tableStoreService;
	}

	@Inject
	public void setTableStoreService(ITableStoreService tableStoreService) {
		this.tableStoreService = tableStoreService;
	}
	
	@Override
	public boolean save(T t) {
		return tableStoreService.putRow(t);
	}

	@Override
	public boolean del(T t) {
		return tableStoreService.deleteRow(t);
	}

	@Override
	public boolean get(T t) {
		return tableStoreService.getRow(t);
	}

	@Override
	public boolean update(T t) {
		return tableStoreService.updateRow(t);
	}

}
