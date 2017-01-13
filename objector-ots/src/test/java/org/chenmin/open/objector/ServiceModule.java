package org.chenmin.open.objector;

import com.google.inject.AbstractModule;

public class ServiceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ITableStoreService.class).to(TableStoreService.class);
		bind(Objector.class).to(OtsObjector.class);
	}

}
