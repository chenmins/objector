package org.chenmin.open.objector.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.chenmin.open.objector.ColumnTypeObject;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
public @interface Column {
	/**
	 * Controls the actual kind name used in the datastore.
	 */
	String value() default "";
	
	ColumnTypeObject type() default ColumnTypeObject.STRING;
}
