/**
 * 
 */
package org.chenmin.open.objector.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.chenmin.open.objector.IStoreTableRow;
import org.chenmin.open.objector.ITableStoreService;
import org.chenmin.open.objector.Objector;
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
	private static Objector objector;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		injector = Guice.createInjector(new ServiceModule());
		tss = injector.getInstance(ITableStoreService.class);
		objector = injector.getInstance(Objector.class);
		IStoreTableRow u = objector.createObject(UserObject.class);
		if (!tss.exsit(u)) {
			tss.createTable(u);
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
	public void test() {
		IStoreTableRow row = objector.createObject(UserObject.class);
		UserObject userObject = (UserObject) row;
		String openid = "chenmintest";
		String passwd = "12345678";
		String passwd2 = "12";
		userObject.setOpenid(openid);
		userObject.setPasswd(passwd);
		assertTrue(tss.putRow(row));
		IStoreTableRow rowt = objector.createObject(UserObject.class);
		UserObject t = (UserObject) rowt;
		t.setOpenid(openid);
		assertTrue(tss.getRow(rowt));
		assertEquals(t.getPasswd(), passwd);
		rowt = objector.createObject(UserObject.class);
		UserObject u = (UserObject) rowt;
		u.setOpenid(openid);
		u.setPasswd(passwd2);
		assertTrue(tss.updateRow(rowt));
		rowt = objector.createObject(UserObject.class);
		t = (UserObject) rowt;
		t.setOpenid(openid);
		assertTrue(tss.getRow(rowt));
		assertEquals(t.getPasswd(), passwd2);
		rowt = objector.createObject(UserObject.class);
		t = (UserObject) rowt;
		t.setOpenid(openid);
		assertTrue(tss.deleteRow(rowt) );
		assertEquals(t.getPasswd(), null);
	}

}
