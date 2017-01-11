package org.chenmin.open.objector;

public class PrimaryKeyValueObject {
	private Object value;
	private byte[] rawData; // raw bytes for utf-8 string
	private PrimaryKeyTypeObject type;

	public PrimaryKeyValueObject(Object value, PrimaryKeyTypeObject type) {
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

	public PrimaryKeyTypeObject getType() {
		return type;
	}

	public void setType(PrimaryKeyTypeObject type) {
		this.type = type;
	}
}
