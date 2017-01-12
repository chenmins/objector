package org.chenmin.open.objector;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.chenmin.open.objector.annotation.Entity;
@Entity(name="UserFileObject1")
public class UserFileObject implements IUserFileObject, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7427521444986097954L;

	@Override
	public Map<String, PrimaryKeyValueObject> getPrimaryKeyValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, ColumnValueObject> getColumnValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPrimaryKeyValue(Map<String, PrimaryKeyValueObject> v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setColumnValue(Map<String, ColumnValueObject> v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getTablename() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PrimaryKeySchemaObject> getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}
 

}
