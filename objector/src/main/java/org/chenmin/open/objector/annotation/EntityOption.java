package org.chenmin.open.objector.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表的配置选项，用于配置TTL、MaxVersions. TTL: TimeToLive的缩写, TableStore支持数据自动过期,
 * TimeToLive即为数据的存活时间.
 * 
 * 服务端根据当前时间, 每列每个版本的版本号, 表的TTL设置决定该列该版本是否过期, 过期的数据会自动清理. MaxVersions:
 * TableStore每行每列中, 最多保存的版本数. 当写入的版本超过MaxVersions时,
 * TableStore只保留版本号最大的MaxVersions个版本.
 * 
 * MaxTimeDeviation:
 * TableStore写入数据所指定的版本与系统时间的偏差允许的最大值，不允许写入与系统偏差大于MaxTimeDeviation的数据。
 * 
 * @author chenmin(admin@chenmin.org)
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Inherited
public @interface EntityOption {

	/**
	 * 数据存活时间 单位：秒。
	 *  如果期望数据永不过期，TTL 可设置为 -1。 数据是否过期是根据数据的时间戳、当前时间、表的
	 * TTL三者进行判断的。当前时间 - 数据的时间戳 > 表的 TTL时，数据会过期并被清理。
	 * 
	 * @return
	 */
	int timeToLive() default -1;

	/**
	 * 每个属性列保留的最大版本数	
	 * 如果写入的版本数超过 MaxVersions，服务端只会保留 MaxVersions 中指定的最大的版本。
	 * @return
	 */
	int maxVersions() default 1;

	/**
	 * 写入数据的时间戳与系统当前时间的偏差允许最大值
	 * 单位：秒。
	 * 默认情况下系统会为新写入的数据生成一个时间戳，数据自动过期功能需要根据这个时间戳判断数据是否过期。
	 * @return
	 */
	long maxTimeDeviation() default 0;

}
