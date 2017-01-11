package org.chenmin.open.objector;

public class PrimaryKeySchemaObject {
	private String name;
	private PrimaryKeyTypeObject type;
	private PrimaryKeyOptionObject option;

	public PrimaryKeySchemaObject(String name, PrimaryKeyTypeObject type) {
		super();
		this.name = name;
		this.type = type;
	}

	public PrimaryKeySchemaObject(String name, PrimaryKeyTypeObject type, PrimaryKeyOptionObject option) {
		super();
		this.name = name;
		this.type = type;
		this.option = option;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PrimaryKeyTypeObject getType() {
		return type;
	}

	public void setType(PrimaryKeyTypeObject type) {
		this.type = type;
	}

	public PrimaryKeyOptionObject getOption() {
		return option;
	}

	public void setOption(PrimaryKeyOptionObject option) {
		this.option = option;
	}

}
