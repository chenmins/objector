package org.chenmin.open.objector.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.NavigableMap;
import java.util.TreeMap;

import org.chenmin.open.objector.ColumnValueObject;
import org.chenmin.open.objector.IStore;
import org.chenmin.open.objector.StoreException;
import org.chenmin.open.objector.StoreFactory;
import org.chenmin.open.objector.UserAttr;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestUserAttr {
	private static IStore store;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		store =StoreFactory.create();
		UserAttr u = new UserAttr();
		if (!store.exsitTable(u)) {
			store.createTable(u);
		}else{
			store.deleteTable(u);
			store.createTable(u);
		}
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	@Test
	public void test() throws StoreException {

		UserAttr userObject = new UserAttr();
		String openid = "chenminUserAttr";
		String nackname = "Chenmin";
		String email = "admin@chenmin.org";
		String telphone = "13333333333";
		
		String telphone2 = "12";
		userObject.setOpenid(openid);
		userObject.setNackname(nackname);
		userObject.setEmail(email);
		userObject.setTelphone(telphone);
		assertTrue(store.save(userObject));
		UserAttr t = new UserAttr();
		t.setOpenid(openid);
		assertTrue(store.get(t));
		assertEquals(t.getTelphone(), telphone);
		UserAttr u = new UserAttr();
		u.setOpenid(openid);
		u.setTelphone(telphone2);
		assertTrue(store.update(u));
		//查询多版本
		t = new UserAttr();
		t.setOpenid(openid);
		NavigableMap<String,NavigableMap<Long,ColumnValueObject>> columnMap = new TreeMap<String,NavigableMap<Long,ColumnValueObject>>();
		store.getByMaxVersions(t, 2, columnMap);
		System.out.println(columnMap);
		
		t = new UserAttr();
		t.setOpenid(openid);
		assertTrue(store.get(t));
		assertEquals(t.getTelphone(), telphone2);
		t = new UserAttr();
		t.setOpenid(openid);
		assertTrue(store.del(t));
		assertEquals(t.getTelphone(),null);
	}

}
