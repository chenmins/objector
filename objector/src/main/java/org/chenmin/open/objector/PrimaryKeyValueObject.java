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

	public static PrimaryKeyValueObject fromString(String value) {
		return new PrimaryKeyValueObject(value, PrimaryKeyTypeObject.STRING);
	}

	public static PrimaryKeyValueObject fromLong(long value) {
		return new PrimaryKeyValueObject(Long.valueOf(value), PrimaryKeyTypeObject.INTEGER);
	}

	public static PrimaryKeyValueObject fromInt(int value) {
		return new PrimaryKeyValueObject(Long.valueOf(value), PrimaryKeyTypeObject.INTEGER);
	}

	public static PrimaryKeyValueObject fromBinary(byte[] value) {
		return new PrimaryKeyValueObject(value, PrimaryKeyTypeObject.BINARY);
	}

	public Object getValue() {
		return value;
	}

	public String asString() {
		if (this.type != PrimaryKeyTypeObject.STRING) {
			throw new IllegalStateException("The type of primary key is not STRING.");
		}
		return (String) this.value;
	}

	public long asLong() {
		if (this.type != PrimaryKeyTypeObject.INTEGER) {
			throw new IllegalStateException("The type of primary key is not INTEGER.");
		}
		return ((Long) this.value).longValue();
	}

	public int asInt() {
		if (this.type != PrimaryKeyTypeObject.INTEGER) {
			throw new IllegalStateException("The type of primary key is not INTEGER.");
		}
		return ((Long) this.value).intValue();
	}

	public byte[] asBinary() {
		if (this.type != PrimaryKeyTypeObject.BINARY) {
			throw new IllegalStateException("The type of primary key is not BINARY");
		}
		return (byte[]) this.value;
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
