/**
 * 
 */
package org.chenmin.open.objector.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.chenmin.open.objector.IStore;
import org.chenmin.open.objector.ServiceModule;
import org.chenmin.open.objector.StoreException;
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
	private static IStore store;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		injector = Guice.createInjector(new ServiceModule());
		store = injector.getInstance(IStore.class);
		UserObject u = new UserObject();
		if (!store.exsitTable(u)) {
			store.createTable(u);
		}
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
	public void test() throws StoreException {

		UserObject userObject = new UserObject();
		String openid = "chenmintest";
		String passwd = "12345678";
		String passwd2 = "12";
		userObject.setOpenid(openid);
		userObject.setPasswd(passwd);
		assertTrue(store.save(userObject));
		UserObject t = new UserObject();
		t.setOpenid(openid);
		assertTrue(store.get(t));
		assertEquals(t.getPasswd(), passwd);
		UserObject u = new UserObject();
		u.setOpenid(openid);
		u.setPasswd(passwd2);
		assertTrue(store.update(u));
		t = new UserObject();
		t.setOpenid(openid);
		assertTrue(store.get(t));
		assertEquals(t.getPasswd(), passwd2);
		t = new UserObject();
		t.setOpenid(openid);
		assertTrue(store.del(t));
		assertEquals(t.getPasswd(),null);
	}

}
