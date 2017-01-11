package org.chenmin.open.objector;

public class ColumnObject {
	private String name;
	private byte[] nameRawData;
	private ColumnValueObject value;

	public ColumnObject(String name, ColumnValueObject value) {
		super();
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getNameRawData() {
		return nameRawData;
	}

	public void setNameRawData(byte[] nameRawData) {
		this.nameRawData = nameRawData;
	}

	public ColumnValueObject getValue() {
		return value;
	}

	public void setValue(ColumnValueObject value) {
		this.value = value;
	}

}
 