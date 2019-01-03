package org.chenmin.open.objector.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 构造CapacityUnit对象，并指定读能力单元的值和写能力单元的值
 * @author chenmin(admin@chenmin.org)
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Inherited
public @interface CapacityUnit {
	
	/**
	 * 
	 * @return 读能力单元的值
	 */
	int readCapacityUnit() default 0;
	
	/**
	 * 
	 * @return 写能力单元的值
	 */
	int writeCapacityUnit() default 0;
}
