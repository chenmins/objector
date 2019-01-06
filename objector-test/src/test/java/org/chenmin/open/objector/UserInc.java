package org.chenmin.open.objector;

import java.io.Serializable;

import org.chenmin.open.objector.annotation.Column;
import org.chenmin.open.objector.annotation.Entity;
import org.chenmin.open.objector.annotation.EntityOption;
import org.chenmin.open.objector.annotation.Key;
@Entity
@EntityOption(maxVersions=3)
public class UserInc implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7117524444986097954L;
	@Key(index = true)
	private String openid;
	@Column(type=ColumnTypeObject.INTEGER)
	private Long readCount;
	@Column(type=ColumnTypeObject.INTEGER)
	private long writeCount;
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public Long getReadCount() {
		return readCount;
	}
	public void setReadCount(Long readCount) {
		this.readCount = readCount;
	}
	public long getWriteCount() {
		return writeCount;
	}
	public void setWriteCount(long writeCount) {
		this.writeCount = writeCount;
	}

}
