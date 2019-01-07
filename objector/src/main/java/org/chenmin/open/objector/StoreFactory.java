package org.chenmin.open.objector;
/**
 * 存储工厂构造器
 * @author Chenmin
 *
 */
public class StoreFactory {
	
	/**
	 * 
	 * @return IStore实现的实例
	 */
	public static IStore create(){
		ITableStoreService ts = ClassFactory.create(ITableStoreService.class);
		Objector ob = ClassFactory.create(Objector.class);
		IStore is = ClassFactory.create(IStore.class);
		is.setObjector(ob);
		is.setTableStoreService(ts);
		return is;
	}

}
