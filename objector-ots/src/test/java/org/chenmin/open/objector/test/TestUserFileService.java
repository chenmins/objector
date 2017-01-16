package org.chenmin.open.objector.test;

import org.chenmin.open.objector.IStore;
import org.chenmin.open.objector.ServiceModule;
import org.chenmin.open.objector.StoreException;
import org.chenmin.open.objector.UserFile;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class TestUserFileService {
	private static Injector injector;
	private static IStore store;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		injector = Guice.createInjector(new ServiceModule());
		store = injector.getInstance(IStore.class);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws StoreException {
		UserFile u = new UserFile();
		if (!store.exsitTable(u)) {
			store.createTable(u);
		}
		// fail("Not yet implemented");
	}

}
