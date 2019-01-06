package org.chenmin.open.objector;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;

import org.apache.commons.beanutils.BeanUtils;

public class Store implements IStore {

	protected Objector objector;
	
	protected ITableStoreService tableStoreService;

	public Objector getObjector() {
		return objector;
	}

	public void setObjector(Objector objector) {
		this.objector = objector;
	}

	public ITableStoreService getTableStoreService() {
		return tableStoreService;
	}

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
		IStoreTableRow copyObject = (IStoreTableRow) copyObject(t);
		boolean row = tableStoreService.putRow(copyObject);
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

	@Override
	public boolean deleteTable(Serializable t) throws StoreException {
		return tableStoreService.deleteTable((IStoreTableRow) copyObject(t));
	}

	@Override
	public boolean getByMaxVersions(Serializable t, int max,NavigableMap<String,NavigableMap<Long,ColumnValueObject>> columnMap) throws StoreException {
		return tableStoreService.getByMaxVersions((IStoreTableRow) copyObject(t), max, columnMap);
	}

	@Override
	public boolean increment(Serializable t) throws StoreException {
		IStoreTableRow copyObject = (IStoreTableRow) copyObject(t);
		boolean row = tableStoreService.increment(copyObject);
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
	public Serializable getRange(Serializable start, Serializable end,List<Serializable> range,boolean asc,int limit)
			throws StoreException {
		IStoreTableRow s = (IStoreTableRow) copyObject(start);
		IStoreTableRow e = (IStoreTableRow) copyObject(end);
		List<IStoreTableRow> r = new ArrayList<IStoreTableRow>();
		IStoreTableRow n = tableStoreService.getRange(s, e, r,asc,limit);
		Serializable next = null;
		if(n==null){
			next = null;
		}else{
			try {
				next = start.getClass().newInstance();
				BeanUtils.copyProperties(next, n);
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			} catch (InvocationTargetException e1) {
				e1.printStackTrace();
			} catch (InstantiationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		try {
			for(IStoreTableRow rr:r){
				Serializable r1 = start.getClass().newInstance();
				BeanUtils.copyProperties(r1, rr);
				range.add(r1);
			}
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			e1.printStackTrace();
		}
		return next;
	}

}
