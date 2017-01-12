package org.chenmin.open.objector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UserObject implements IUserObject, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7427521444986097954L;

	private String openid;

	private String passwd;

	private Map<String, Object> attrs = new LinkedHashMap<String, Object>();

	/*
	 * （非 Javadoc）
	 * 
	 * @see com.fanbiji.lib.shared.IUserObject#getOpenid()
	 */
	@Override
	public String getOpenid() {
		return openid;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see com.fanbiji.lib.shared.IUserObject#setOpenid(java.lang.String)
	 */
	@Override
	public void setOpenid(String openid) {
		this.openid = openid;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see com.fanbiji.lib.shared.IUserObject#getPasswd()
	 */
	@Override
	public String getPasswd() {
		return passwd;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see com.fanbiji.lib.shared.IUserObject#setPasswd(java.lang.String)
	 */
	@Override
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see com.fanbiji.lib.shared.IUserObject#getAttrs()
	 */
	@Override
	public Map<String, Object> getAttrs() {
		return attrs;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see com.fanbiji.lib.shared.IUserObject#setAttrs(java.util.Map)
	 */
	@Override
	public void setAttrs(Map<String, Object> attrs) {
		this.attrs = attrs;
	}

	@Override
	public String getTablename() {
		return "userobject";
	}

	@Override
	public List<PrimaryKeySchemaObject> getPrimaryKey() {
		List<PrimaryKeySchemaObject> pk = new ArrayList<PrimaryKeySchemaObject>();
		pk.add(new PrimaryKeySchemaObject("openid", PrimaryKeyTypeObject.STRING));
		return pk;
	}

	@Override
	public Map<String, PrimaryKeyValueObject> getPrimaryKeyValue() {
		Map<String, PrimaryKeyValueObject> m = new LinkedHashMap<String, PrimaryKeyValueObject>();
		m.put("openid", new PrimaryKeyValueObject(this.openid, PrimaryKeyTypeObject.STRING));
		return m;
	}

	@Override
	public Map<String, ColumnValueObject> getColumnValue() {
		Map<String, ColumnValueObject> m = new LinkedHashMap<String, ColumnValueObject>();
		m.put("passwd", new ColumnValueObject(this.passwd, ColumnTypeObject.STRING));
		for (String key : attrs.keySet()) {
			m.put(key, new ColumnValueObject(attrs.get(key), ColumnTypeObject.STRING));
		}
		return m;
	}

	@Override
	public void setPrimaryKeyValue(Map<String, PrimaryKeyValueObject> v) {
		this.openid = v.get("openid").getValue().toString();
	}

	@Override
	public void setColumnValue(Map<String, ColumnValueObject> v) {
		this.passwd = v.get("passwd").getValue().toString();
		for (String key : v.keySet()) {
			attrs.put(key, v.get(key).getValue());
		}
	}

	@Override
	public String toString() {
		return "UserObject [openid=" + openid + ", passwd=" + passwd + ", attrs=" + attrs + "]";
	}

}
