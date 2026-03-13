package org.chenmin.open.objector;
/**
 * 存储工厂构造器
 * @author Chenmin
 *
 */
public final class StoreFactory {

	private StoreFactory() {
	}
	
	/**
	 * 
	 * @return IStore实现的实例
	 */
	public static IStore create(){
		ITableStoreService ts = ClassFactory.create(ITableStoreService.class);
		Objector ob = ClassFactory.create(Objector.class);
		IStore is = ClassFactory.create(IStore.class);
		if (ts == null || ob == null || is == null) {
			throw new IllegalStateException("Failed to create store components from SPI");
		}
		is.setObjector(ob);
		is.setTableStoreService(ts);
		return is;
	}

}
