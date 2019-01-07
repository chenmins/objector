package org.chenmin.open.objector.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.chenmin.open.objector.IStore;
import org.chenmin.open.objector.StoreException;
import org.chenmin.open.objector.StoreFactory;
import org.chenmin.open.objector.UserInc;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestUserInc {
	private static IStore store;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		store =StoreFactory.create();
		UserInc u = new UserInc();
		if (!store.exsitTable(u)) {
			store.createTable(u);
		} 
		//避免[ErrorCode]:OTSPartitionUnavailable, [Message]:The partition is not available., 
				Thread.sleep(2000);
	}
	

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		UserInc u = new UserInc();
		store.deleteTable(u);
	}
	@Test
	public void test() throws StoreException {
		UserInc u = new UserInc();
		String openid = "chenmins";
		u.setOpenid(openid);
		assertTrue(store.save(u));
		assertTrue(store.get(u));
		assertEquals(u.getReadCount(), null);
		assertEquals(u.getWriteCount(), 0);
		u.setReadCount(10L);
		u.setWriteCount(5);
		assertTrue(store.increment(u));
		assertEquals(u.getReadCount().longValue(), 10);
		assertEquals(u.getWriteCount(), 5);
		u.setReadCount(10L);
		u.setWriteCount(-15);
		assertTrue(store.increment(u));
		assertEquals(u.getReadCount().longValue(), 20);
		assertEquals(u.getWriteCount(), -10);
		u.setReadCount(null);
		u.setWriteCount(20);
		assertTrue(store.increment(u));
		assertEquals(u.getWriteCount(), 10);
		
	}
}
