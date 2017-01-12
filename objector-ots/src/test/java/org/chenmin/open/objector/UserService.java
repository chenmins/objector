package org.chenmin.open.objector;

public class UserService extends Dao<IUserObject> implements IUserService {

	private IUserObject createUserObjectByOpenid(String openid) {
		IUserObject userObject = new UserObject();
		userObject.setOpenid(openid);
		return userObject;
	} 

	@Override
	public boolean check(String openid, String passwd) throws StoreException {
		IUserObject userObject = createUserObjectByOpenid(openid);
		IUserObject u = getUserByOpenid(userObject.getOpenid());
		if (u.getPasswd() == null)
			throw new StoreException("openid is not Exsit!");
		return u.getPasswd().equals(passwd);
	}

	@Override
	public boolean hasUser(String openid) throws StoreException {
		IUserObject userObject = createUserObjectByOpenid(openid);
		IUserObject u = getUserByOpenid(userObject.getOpenid());
		return u.getPasswd() != null;
	}

	@Override
	public boolean delUser(String openid) throws StoreException {
		IUserObject userObject = createUserObjectByOpenid(openid);
		return this.del(userObject);
	}

	@Override
	public IUserObject getUserByOpenid(String openid) throws StoreException {
		IUserObject userObject = createUserObjectByOpenid(openid);
		this.get(userObject);
		return userObject;
	}

	@Override
	public boolean reg(IUserObject userObject) throws StoreException {
		if (this.hasUser(userObject.getOpenid()))
			throw new StoreException("帐号" + userObject.getOpenid() + "已存在");
		return this.save(userObject);
	}

}
