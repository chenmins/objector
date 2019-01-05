package org.chenmin.open.objector.test;

import org.chenmin.open.objector.IStore;
import org.chenmin.open.objector.StoreException;
import org.chenmin.open.objector.StoreFactory;
import org.chenmin.open.objector.UserBlog;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestUserBlog {
	private static IStore store;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		store =StoreFactory.create();
		UserBlog u = new UserBlog();
		if (!store.exsitTable(u)) {
			store.createTable(u);
		} 
	}
	

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
//		UserBlog u = new UserBlog();
//		store.deleteTable(u);
	}
	@Test
	public void test() throws StoreException {
		UserBlog u = new UserBlog();
		String openid="chenmins";
		String title="Hello";
		String body="Chenmins";
		u.setOpenid(openid);
//		u.setTitle(title);
		u.setBody(body);
//		u.setReadCount(0);
//		System.out.println("::"+u.getId());
		assertTrue(store.save(u));
	}
}
