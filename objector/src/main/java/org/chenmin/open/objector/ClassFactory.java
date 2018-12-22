package org.chenmin.open.objector;

import java.util.Iterator;
import java.util.ServiceLoader;

public class ClassFactory {

	public static <T> T create(Class<? extends T> c) {
		@SuppressWarnings("unchecked")
		ServiceLoader<T> serviceLoader = (ServiceLoader<T>) ServiceLoader.load(c);
		Iterator<T> it = serviceLoader.iterator();
		if (it.hasNext())
			return it.next();
		return null;
	}

}
