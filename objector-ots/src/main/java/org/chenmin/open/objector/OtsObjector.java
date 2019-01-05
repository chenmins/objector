package org.chenmin.open.objector;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.chenmin.open.objector.annotation.CapacityUnit;
import org.chenmin.open.objector.annotation.Column;
import org.chenmin.open.objector.annotation.Entity;
import org.chenmin.open.objector.annotation.EntityOption;
import org.chenmin.open.objector.annotation.Key;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
public class OtsObjector implements Objector {
	
	private static HashMap<String,CtClass > classMap = new HashMap<String,CtClass >();

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Serializable> T createObject(Class<? extends Serializable> c) {
		CtClass ctClass = null;
		Class<?> clazz = null;
		try {
			if(!classMap.containsKey(c.getName())){
				ctClass = createEntity(c);
				classMap.put(c.getName(), ctClass);
				clazz = ctClass.toClass();
			}else{
				ctClass = classMap.get(c.getName());
				clazz =Class.forName(ctClass.getName());
			}
			Object obj = clazz.newInstance();
			return (T) obj;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (CannotCompileException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	private CtClass createEntity(Class<? extends Serializable> c) {
		Entity entity = c.getAnnotation(Entity.class);
		EntityOption entityOption = c.getAnnotation(EntityOption.class);
		CapacityUnit capacityUnit = c.getAnnotation(CapacityUnit.class);
		Field[] fields = c.getDeclaredFields();
		LinkedList<Field> key_result = new LinkedList<Field>();
		List<Column> columns = new LinkedList<Column>();
		LinkedHashMap<Field, Key> refKey = new LinkedHashMap<Field, Key>();
		LinkedHashMap<Field, Column> refColumn = new LinkedHashMap<Field, Column>();
		LinkedHashMap<Key,Field> refField = new LinkedHashMap<Key,Field>();
		for (Field field : fields) {
			Key annotationKey = field.getAnnotation(Key.class);
			if (annotationKey != null) {
				if (annotationKey.index()) {
					key_result.add(0,field);
				} else {
					key_result.add(field);
				}
				refField.put(annotationKey,field);
				refKey.put(field, annotationKey);
			}
			Column annotationColumn = field.getAnnotation(Column.class);
			if (annotationColumn != null) {
				columns.add(annotationColumn);
				refColumn.put(field, annotationColumn);
			}
		}
		ClassPool pool = ClassPool.getDefault();
		pool.insertClassPath(new ClassClassPath((this.getClass())));
		// 创建一个类
		String entity_name = entity.value();
		String new_classname = null;
		if(entity_name.isEmpty())
			new_classname = c.getSimpleName();
		else
			new_classname =  c.getSimpleName()+"_"+entity_name;
		CtClass ctClass = pool.makeClass(c.getPackage().getName() + "." +new_classname + "_Gen");
		try {
			// parent
			CtClass ctParent = pool.get(c.getName());
			ctClass.setSuperclass(ctParent);
			CtClass ctClassRow = pool.get(IStoreTableRow.class.getName());
			CtClass[] interfacelist={ctClassRow};
			ctClass.setInterfaces(interfacelist);
			CtMethod[] mts = ctClassRow.getMethods();
			for (CtMethod method : mts) {
				if(method.getModifiers()==(Modifier.ABSTRACT+Modifier.PUBLIC)){
					CtClass returnType = method.getReturnType();
					CtClass[] parameterTypes = method.getParameterTypes();
					CtMethod method2 = new CtMethod(returnType, method.getName(), parameterTypes, ctClass);
					method2.setModifiers(Modifier.PUBLIC);
					if(method.getName().equals("getTablename")){
						//tablename
						if(entity_name.isEmpty())
							method2.setBody("{return \""+c.getSimpleName()+"\";}");
						else
							method2.setBody("{return \""+entity_name+"\";}");
					}else if(method.getName().equals("autoPrimaryKey")){
						//autoPrimaryKey
						String autoKey = null;
						for (Field field : key_result) {
							Key annotationKey = field.getAnnotation(Key.class);
							String name = annotationKey.value();
							boolean auto = annotationKey.auto_increment();
							if(auto){
								autoKey = name;
								if(name.isEmpty()){
									autoKey = field.getName();
								}
							}
						}
						if(autoKey==null){
							method2.setBody("{return null;}");
						}else{
							method2.setBody("{return \""+autoKey+"\";}");
						}
					}else if(method.getName().equals("readCapacityUnit")){
						if(capacityUnit == null)
							method2.setBody("{return 0;}");
						else
							method2.setBody("{return "+capacityUnit.readCapacityUnit()+";}");
					}else if(method.getName().equals("writeCapacityUnit")){
						if(capacityUnit == null)
							method2.setBody("{return 0;}");
						else
							method2.setBody("{return "+capacityUnit.writeCapacityUnit()+";}");
					}else if(method.getName().equals("timeToLive")){
						if(entityOption == null)
							method2.setBody("{return -1;}");
						else
							method2.setBody("{return "+entityOption.timeToLive()+";}");
					}else if(method.getName().equals("maxVersions")){
						if(entityOption == null)
							method2.setBody("{return 1;}");
						else
							method2.setBody("{return "+entityOption.maxVersions()+";}");
					}else if(method.getName().equals("maxTimeDeviation")){
						if(entityOption == null)
							method2.setBody("{return 0L;}");
						else
							method2.setBody("{return "+entityOption.maxTimeDeviation()+"L;}");
					}else if(method.getName().equals("getPrimaryKey")){
						//getPrimaryKey 
//						java.util.List<PrimaryKeySchemaObject> pk = new java.util.ArrayList<PrimaryKeySchemaObject>();
//						pk.add(new PrimaryKeySchemaObject("openid", PrimaryKeyTypeObject.STRING));
//						return pk;
						StringBuffer sb = new StringBuffer();
						sb.append("{");
						sb.append("java.util.List  pk = new java.util.ArrayList ();");
						int autoCount = 0;
						for (Field field : key_result) {
							Key annotationKey = field.getAnnotation(Key.class);
							String name = annotationKey.value();
							boolean auto = annotationKey.auto_increment();
							if(auto)
								autoCount++;
							if(autoCount>1)
								throw new  CannotCompileException("表格存储目前支持多个主键，第一个主键为分区键，分区键上不允许使用主键列自增功能");
							if(auto&&annotationKey.type()!=PrimaryKeyTypeObject.INTEGER)
								throw new  CannotCompileException("表格存储主键列自增功能,只能是INTEGER类型");
							if(name.isEmpty())
								name = field.getName();
							if(annotationKey.index()&&auto)
								throw new  CannotCompileException("对于每张表，目前只允许设置一个主键列为自增列");
							if(auto)
								sb.append("pk.add(new org.chenmin.open.objector.PrimaryKeySchemaObject(\""+name+"\", org.chenmin.open.objector.PrimaryKeyTypeObject."+annotationKey.type()+",org.chenmin.open.objector.PrimaryKeyOptionObject.AUTO_INCREMENT));");
							else
								sb.append("pk.add(new org.chenmin.open.objector.PrimaryKeySchemaObject(\""+name+"\", org.chenmin.open.objector.PrimaryKeyTypeObject."+annotationKey.type()+"));");
						}
						sb.append("return pk;");
						sb.append("}");
						method2.setBody(sb.toString());
					}else if(method.getName().equals("getPrimaryKeyValue")){
						//getPrimaryKeyValue 
//						Map<String, PrimaryKeyValueObject> m = new LinkedHashMap<String, PrimaryKeyValueObject>();
//						m.put("openid", new PrimaryKeyValueObject(this.openid, PrimaryKeyTypeObject.STRING));
//						return m;
						StringBuffer sb = new StringBuffer();
						sb.append("{");
						sb.append("java.util.LinkedHashMap  m = new java.util.LinkedHashMap();");
						//for echo refKey
						for(Field f:refKey.keySet()){
							Key a = refKey.get(f);
							String name = a.value();
							if(name.isEmpty())
								name = f.getName();
							CtMethod gets = null;
							for(CtMethod ctm:ctParent.getMethods()){
								String getname = ctm.getName().toLowerCase();
								if(getname.equals("get"+f.getName()))
									gets = ctm;
							}
							if(gets==null)
								throw new CannotCompileException(c.getName()+"."+f.getName()+" has no getter");
							//TODO 类型判断
							String ctype = null;
							PrimaryKeyTypeObject type = a.type();
							switch (type) {
							case INTEGER:
								ctype = "fromLong"; 
								break;
							case STRING:
								ctype = "fromString";
								break;
							case BINARY:
								ctype= "fromBinary";
								break;
							default:
								break;
							}
							if(ctype==null)
								throw new CannotCompileException(c.getName()+"."+f.getName()+" is Error in getPrimaryKeyValue");
//							if(f.getType().getName()=="int"){
//								sb.append("if(this."+gets.getName()+"()!=0)");
//							}else{
//								sb.append("if(this."+gets.getName()+"()!=null)");
//							}
							sb.append("m.put(\""+name+"\",org.chenmin.open.objector.PrimaryKeyValueObject."+ctype+"(this."+gets.getName()+"()));");
						}
						sb.append("return m;");
						sb.append("}");
//						System.out.println(sb.toString());
						method2.setBody(sb.toString());
					}else if(method.getName().equals("getColumnValue")){
						//getColumnValue 
//						Map<String, ColumnValueObject> m = new LinkedHashMap<String, ColumnValueObject>();
//						for (String key : attrs.keySet()) {
//							m.put(key, new ColumnValueObject(attrs.get(key), ColumnTypeObject.STRING));
//						}
//						return m;
						StringBuffer sb = new StringBuffer();
						sb.append("{");
						sb.append("java.util.LinkedHashMap  m = new java.util.LinkedHashMap();");
						//for echo refColumn
						for(Field f:refColumn.keySet()){
							Column a = refColumn.get(f);
							String name = a.value();
							if(name.isEmpty())
								name = f.getName();
							CtMethod gets = null;
							for(CtMethod ctm:ctParent.getMethods()){
								String getname = ctm.getName();
								if(getname.equalsIgnoreCase("get"+f.getName()))
									gets = ctm;
							}
							if(gets==null)
								throw new CannotCompileException(c.getName()+"."+f.getName()+" has no getter method");
							//TODO 多类型
							ColumnTypeObject type = a.type();
							String ctype = null;
							switch (type) {
							case INTEGER:
								ctype = "fromLong";//TODO 可以是asLong
								break;
							case STRING:
								ctype = "fromString";
								break;
							case BOOLEAN:
								ctype = "fromBoolean";
								break;
							case DOUBLE:
								ctype = "fromDouble";
								break;
							case BINARY:
								ctype= "fromBinary";
								break;
							default:
								break;
							}
							if(ctype==null)
								throw new CannotCompileException(c.getName()+"."+f.getName()+" is Error in getColumnValue");
							sb.append("m.put(\""+name+"\",org.chenmin.open.objector.ColumnValueObject."+ctype+"(this."+gets.getName()+"()));");
						}
						sb.append("return m;");
						sb.append("}");
						method2.setBody(sb.toString());
					}else if(method.getName().equals("setPrimaryKeyValue")){
						//setPrimaryKeyValue 
						// setFileid(((PrimaryKeyValueObject)paramMap.get("fileid")).getValue().toString());
						StringBuffer sb = new StringBuffer();
						sb.append("{");
						//for echo refKey
						for(Field f:refKey.keySet()){
							Key a = refKey.get(f);
							String name = a.value();
							if(name.isEmpty())
								name = f.getName();
							CtMethod sets = null;
							for(CtMethod ctm:ctParent.getMethods()){
								String getname = ctm.getName().toLowerCase();
								if(getname.equals("set"+f.getName()))
									sets = ctm;
							}
							if(sets==null)
								throw new CannotCompileException(c.getName()+"."+f.getName()+" has no setter");
							//TODO 此处只有String的实现，未增加其他类型
							String ctype = null;
							PrimaryKeyTypeObject type = a.type();
							switch (type) {
							case INTEGER:
								ctype = "asLong"; 
								break;
							case STRING:
								ctype = "asString";
								break;
							case BINARY:
								ctype= "asBinary";
								break;
							default:
								break;
							}
							if(ctype==null)
								throw new CannotCompileException(c.getName()+"."+f.getName()+" is Error in setPrimaryKeyValue");
							//参数非空判断
							//if($1.containsKey(\""+name+"\"))
							sb.append("if($1.containsKey(\""+name+"\"))");
							sb.append("this."+sets.getName()+"(((org.chenmin.open.objector.PrimaryKeyValueObject)$1.get(\""+name+"\"))."+ctype+"());");
							
						}
						sb.append("}");
//						System.out.println(sb);
						method2.setBody(sb.toString());
					}else if(method.getName().equals("setColumnValue")){
						//setColumnValue 
						// setFileid(((ColumnValueObject)paramMap.get("fileid")).getValue().toString());
						StringBuffer sb = new StringBuffer();
						sb.append("{");
						//for echo refKey
						for(Field f:refColumn.keySet()){
							Column a = refColumn.get(f);
							ColumnTypeObject type = a.type();
							String name = a.value();
							if(name.isEmpty())
								name = f.getName();
							CtMethod sets = null;
							for(CtMethod ctm:ctParent.getMethods()){
								String getname = ctm.getName();
								if(getname.equalsIgnoreCase("set"+f.getName()))
									sets = ctm;
							}
							if(sets==null)
								throw new CannotCompileException(c.getName()+"."+f.getName()+" has no setter");
							//TODO 此处只有String的实现，未增加其他类型
							//STRING, INTEGER, BOOLEAN, DOUBLE, BINARY
							String ctype = null;
							switch (type) {
							case INTEGER:
								ctype = "asLong";//TODO 可以是asLong
								break;
							case STRING:
								ctype = "asString";
								break;
							case BOOLEAN:
								ctype = "asBoolean";
								break;
							case DOUBLE:
								ctype = "asDouble";
								break;
							case BINARY:
								ctype= "asBinary";
								break;
							default:
								break;
							}
							if(ctype==null)
								throw new CannotCompileException(c.getName()+"."+f.getName()+" is Error in setColumnValue");
							//参数非空判断
							//if($1.containsKey(\""+name+"\"))
							sb.append("if($1.containsKey(\""+name+"\"))");
							sb.append("this."+sets.getName()+"(((org.chenmin.open.objector.ColumnValueObject)$1.get(\""+name+"\"))."+ctype+"());");
						}
						sb.append("}");
						method2.setBody(sb.toString());
					}else{
						//未实现 做空
						if(returnType==null){
							method2.setBody("{ }");
						}else{
							method2.setBody("{return null;}");
						}
					}
					ctClass.addMethod(method2);
//					System.out.println("------"+method);
				}
			}
//			// 为类设置方法
//			CtMethod method = new CtMethod(CtClass.voidType, "run", null, ctClass);
//			method.setModifiers(Modifier.PUBLIC);
//			method.setBody("{System.out.println(\"执行结果\");}");
//			ctClass.addMethod(method);
//			ctClass.writeFile("target/gen/");
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (CannotCompileException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ctClass;
	}

}
