package org.chenmin.open.objector.test;

import org.chenmin.open.objector.IStoreTableRow;
import org.chenmin.open.objector.ITableStoreService;
import org.chenmin.open.objector.Objector;
import org.chenmin.open.objector.ServiceModule;
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
	private static Objector objector;
	private static ITableStoreService tss;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		injector = Guice.createInjector(new ServiceModule());
		tss = injector.getInstance(ITableStoreService.class);
		objector = injector.getInstance(Objector.class);
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
	public void test() {
		IStoreTableRow  u =  objector.createObject(UserFile.class);
		System.out.println("table:"+u.getTablename());
		if(!tss.exsit(u))
			tss.createTable(u);
//		fail("Not yet implemented");
	}

}
