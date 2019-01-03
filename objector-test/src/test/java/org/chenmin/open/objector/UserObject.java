package org.chenmin.open.objector;

import java.io.Serializable;

import org.chenmin.open.objector.annotation.CapacityUnit;
import org.chenmin.open.objector.annotation.Column;
import org.chenmin.open.objector.annotation.Entity;
import org.chenmin.open.objector.annotation.EntityOption;
import org.chenmin.open.objector.annotation.Key;

@Entity("USR24")
@EntityOption(maxVersions=2)
@CapacityUnit(readCapacityUnit=0,writeCapacityUnit=0)
public class UserObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7127521444986097954L;
	
	@Key(index = true)
	private String openid;
	
	@Column
	private String passwd;

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

}
