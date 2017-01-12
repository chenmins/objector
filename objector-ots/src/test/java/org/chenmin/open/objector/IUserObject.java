package org.chenmin.open.objector;

import java.util.Map;

public interface IUserObject extends IStoreTableRow{

	String getOpenid();

	void setOpenid(String openid);

	String getPasswd();

	void setPasswd(String passwd);

	Map<String, Object> getAttrs();

	void setAttrs(Map<String, Object> attrs);

}