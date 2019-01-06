package org.chenmin.open.objector.test;

import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.chenmin.open.objector.IStore;
import org.chenmin.open.objector.StoreException;
import org.chenmin.open.objector.StoreFactory;
import org.chenmin.open.objector.UserBlog;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestUserBlogRange{
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
		UserBlog u = new UserBlog();
		store.deleteTable(u);
	}
	@Test
	public void test() throws StoreException {
		String openid="chenmins";
		String title="Hello";
		String body="Chenmins";
		for(int i=0;i<10;i++){
			UserBlog u = new UserBlog();
			u.setOpenid(openid);
			u.setTitle(title+":"+i);
			u.setBody(body);
//			assertTrue(store.save(u));
		}
		UserBlog start = new UserBlog();
//		start.setOpenid(openid);
		UserBlog end = new UserBlog();
//		end.setOpenid(openid);
		int limit=3;
		UserBlog next = null;
		while(true){
			List<Serializable> range= new ArrayList<Serializable>();
			next = (UserBlog) store.getRange(start, end , range,true, limit);
//			System.out.println(range);
			for(Serializable sa:range){
				UserBlog s = (UserBlog) sa;
				System.out.println("openid:"+s.getOpenid()+",id:"+s.getId());
			}
			if(next==null){
				break;
			}else{
				System.out.println("next:"+"openid:"+next.getOpenid()+",id:"+next.getId());
				start = next;
				next = new UserBlog();
			}
		}
	
//		 
	}
}
