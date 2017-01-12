package org.chenmin.open.objector;

import org.chenmin.open.objector.annotation.Entity;
import org.chenmin.open.objector.annotation.Id;

@Entity(name = "UserFileObject")
public interface IUserFileObject extends IStoreTableRow {
	@Id
	String openid = null;

}