package org.chenmin.open.objector;

import java.io.Serializable;

import org.chenmin.open.objector.annotation.Column;
import org.chenmin.open.objector.annotation.Entity;
import org.chenmin.open.objector.annotation.Key;
@Entity
public class UserBlog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7117521444986097954L;
	@Key(index = true)
	private String openid;
	@Key(auto_increment=true,type=PrimaryKeyTypeObject.INTEGER)
	private Long id;
	@Column
	private String title;
	@Column
	private String body;
	@Column(type=ColumnTypeObject.INTEGER)
	private long readCount;
	
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public long getReadCount() {
		return readCount;
	}
	public void setReadCount(long readCount) {
		this.readCount = readCount;
	}
	
	
}
