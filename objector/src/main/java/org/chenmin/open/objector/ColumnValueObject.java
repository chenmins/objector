package org.chenmin.open.objector;

public class ColumnValueObject {
	private Object value;
	private byte[] rawData;
	private ColumnTypeObject type;

	public ColumnValueObject(Object value, ColumnTypeObject type) {
		super();
		this.value = value;
		this.type = type;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public byte[] getRawData() {
		return rawData;
	}

	public void setRawData(byte[] rawData) {
		this.rawData = rawData;
	}

	public ColumnTypeObject getType() {
		return type;
	}

	public void setType(ColumnTypeObject type) {
		this.type = type;
	}
}
