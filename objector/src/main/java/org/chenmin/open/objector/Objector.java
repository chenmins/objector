package org.chenmin.open.objector;

import java.io.Serializable;

/**
 * 存储对象构造器
 * @author Chenmin
 *
 */
public interface Objector {

	/**
	 * 生成增强对象
	 * @param c
	 * @return
	 */
	<T extends Serializable> T createObject(Class<? extends Serializable> c);
	
}
