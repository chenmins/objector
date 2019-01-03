package org.chenmin.open.objector;

import java.io.Serializable;

import org.chenmin.open.objector.annotation.CapacityUnit;
import org.chenmin.open.objector.annotation.Column;
import org.chenmin.open.objector.annotation.Entity;
import org.chenmin.open.objector.annotation.EntityOption;
import org.chenmin.open.objector.annotation.Key;

@Entity("UserAttr1")
@EntityOption(maxVersions=3)
@CapacityUnit(readCapacityUnit=0,writeCapacityUnit=0)
public class UserAttr implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7127521444186097954L;
	
	@Key
	private String openid;
	@Column
	private String nackname;
	@Column
	private String email;
	@Column
	private String telphone;
	
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getNackname() {
		return nackname;
	}
	public void setNackname(String nackname) {
		this.nackname = nackname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelphone() {
		return telphone;
	}
	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

}
