package org.chenmin.open.objector;

import java.util.Iterator;
import java.util.ServiceLoader;
/**
 * Spi 工厂构造器
 * @author Chenmin
 *
 */
public final class ClassFactory {

	private ClassFactory() {
	}

	/**
	 * 创建接口的实现
	 * @param c
	 * @return
	 */
	public static <T> T create(Class<? extends T> c) {
		@SuppressWarnings("unchecked")
		ServiceLoader<T> serviceLoader = (ServiceLoader<T>) ServiceLoader.load(c);
		Iterator<T> it = serviceLoader.iterator();
		if (it.hasNext()) {
			return it.next();
		}
		throw new IllegalStateException("No SPI implementation found for " + c.getName());
	}

}
