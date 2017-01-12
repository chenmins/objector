package org.chenmin.open.objector;

public interface IUserService extends IDao<IUserObject>{
	
	boolean hasUser(String openid) throws StoreException;
	
	boolean delUser(String openid) throws StoreException;

	IUserObject getUserByOpenid(String openid) throws StoreException;
	
	boolean check(String openid,String passwd) throws StoreException;
	
	boolean reg(IUserObject userObject) throws StoreException;
	
}
