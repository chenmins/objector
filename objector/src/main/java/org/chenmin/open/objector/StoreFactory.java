package org.chenmin.open.objector;

public class StoreFactory {
	
	public static IStore create(){
		ITableStoreService ts = ClassFactory.create(ITableStoreService.class);
		Objector ob = ClassFactory.create(Objector.class);
		IStore is = ClassFactory.create(IStore.class);
		is.setObjector(ob);
		is.setTableStoreService(ts);
		return is;
	}

}
