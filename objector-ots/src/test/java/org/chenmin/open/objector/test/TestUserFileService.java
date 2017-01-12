package org.chenmin.open.objector.test;

import org.chenmin.open.objector.IUserFileObject;
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
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		injector = Guice.createInjector(new ServiceModule());
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
		IUserFileObject  u = (IUserFileObject) objector.createObject(UserFile.class);
		System.out.println("table:"+u.getTablename());
//		fail("Not yet implemented");
	}

}
