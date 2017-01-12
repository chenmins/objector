/**
 * 
 */
package org.chenmin.open.objector.test;

import static org.junit.Assert.*;

import org.chenmin.open.objector.ITableStoreService;
import org.chenmin.open.objector.IUserObject;
import org.chenmin.open.objector.IUserService;
import org.chenmin.open.objector.ServiceModule;
import org.chenmin.open.objector.UserObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * @author chenmin
 *
 */
public class TestUserService {

	private static Injector injector;
	private static ITableStoreService tss;
	private static IUserService userService;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		injector = Guice.createInjector(new ServiceModule());
		tss = injector.getInstance(ITableStoreService.class);
		IUserObject userObject = new UserObject();
		if (!tss.exsit(userObject)) {
			tss.createTable(userObject);
		}
		userService = injector.getInstance(IUserService.class);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		IUserObject userObject = new UserObject();
		String openid = "chenmintest";
		String passwd = "12345678";
		String passwd2 = "12";
		userObject.setOpenid(openid);
		userObject.setPasswd(passwd);
		assertTrue(userService.save(userObject));
		IUserObject t = new UserObject();
		t.setOpenid(openid);
		assertTrue(userService.get(t));
		assertEquals(t.getPasswd(), passwd);
		IUserObject u = new UserObject();
		u.setOpenid(openid);
		u.setPasswd(passwd2);
		assertTrue(userService.update(u));
		t = new UserObject();
		t.setOpenid(openid);
		assertTrue(userService.get(t));
		assertEquals(t.getPasswd(), passwd2);
		t = new UserObject();
		t.setOpenid(openid);
		assertTrue(userService.del(t));
		assertEquals(t.getColumnValue().size(), 0);

	}

}
