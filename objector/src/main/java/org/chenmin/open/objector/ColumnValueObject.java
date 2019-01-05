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

	// STRING, INTEGER, BOOLEAN, DOUBLE, BINARY;
	public String asString() {
		if (this.type != ColumnTypeObject.STRING) {
			throw new IllegalStateException("The type of column is not STRING.");
		}
		return (String) this.value;
	}

	public long asLong() {
		if (this.type != ColumnTypeObject.INTEGER) {
			throw new IllegalStateException("The type of column is not INTEGER.");
		}
		return ((Long) this.value).longValue();
	}

	public int asInt() {
		if (this.type != ColumnTypeObject.INTEGER) {
			throw new IllegalStateException("The type of column is not INTEGER.");
		}
		return ((Long) this.value).intValue();
	}

	public double asDouble() {
		if (this.type != ColumnTypeObject.DOUBLE) {
			throw new IllegalStateException("The type of column is not DOUBLE.");
		}
		return ((Double) this.value).doubleValue();
	}

	public boolean asBoolean() {
		if (this.type != ColumnTypeObject.BOOLEAN) {
			throw new IllegalStateException("The type of column is not BOOLEAN.");
		}
		return ((Boolean) this.value).booleanValue();
	}

	public byte[] asBinary() {
		if (this.type != ColumnTypeObject.BINARY) {
			throw new IllegalStateException("The type of column is not BINARY.");
		}
		return (byte[]) this.value;
	}

	public static ColumnValueObject fromString(String value) {
		return new ColumnValueObject(value, ColumnTypeObject.STRING);
	}

	public static ColumnValueObject fromLong(long value) {
		return new ColumnValueObject(Long.valueOf(value), ColumnTypeObject.INTEGER);
	}

	public static ColumnValueObject fromInt(int value) {
		return new ColumnValueObject(Long.valueOf(value), ColumnTypeObject.INTEGER);
	}

	
	public static ColumnValueObject fromBinary(byte[] value) {
		return new ColumnValueObject(value, ColumnTypeObject.BINARY);
	}

	public static ColumnValueObject fromDouble(double value) {
		return new ColumnValueObject(Double.valueOf(value), ColumnTypeObject.DOUBLE);
	}

	public static ColumnValueObject fromBoolean(boolean value) {
		return new ColumnValueObject(Boolean.valueOf(value), ColumnTypeObject.BOOLEAN);
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
