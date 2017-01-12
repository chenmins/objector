package org.chenmin.open.objector;

import java.io.Serializable;

import org.chenmin.open.objector.annotation.Column;
import org.chenmin.open.objector.annotation.Entity;
import org.chenmin.open.objector.annotation.Key;

@Entity(name = "UserFileObject")
public class UserFile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7427521444986097954L;

	@Key(name = "fileid")
	private String fileid;
	
	@Key(name = "openid", index = true)
	private String openid;
	
	@Key(name = "filetype")
	private String filetype;
	
	@Key(name = "filepid")
	private String filepid;
	
	@Column(name = "filename")
	private String filename;

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getFileid() {
		return fileid;
	}

	public void setFileid(String fileid) {
		this.fileid = fileid;
	}

	public String getFiletype() {
		return filetype;
	}

	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}

	public String getFilepid() {
		return filepid;
	}

	public void setFilepid(String filepid) {
		this.filepid = filepid;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	@Override
	public String toString() {
		return "UserFileObject [openid=" + openid + ", fileid=" + fileid + ", filetype=" + filetype + ", filepid="
				+ filepid + ", filename=" + filename + "]";
	}
}
