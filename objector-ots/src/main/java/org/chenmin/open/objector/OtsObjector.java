package org.chenmin.open.objector;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.chenmin.open.objector.annotation.Column;
import org.chenmin.open.objector.annotation.Entity;
import org.chenmin.open.objector.annotation.Key;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

public class OtsObjector implements Objector {
	
	private static HashMap<String,CtClass > classMap = new HashMap<String,CtClass >();

	@Override
	public IStoreTableRow create(Class<? extends IStoreTableRow> c) {
		System.out.println(c.getName());
		Annotation[] an = c.getDeclaredAnnotations();
		System.out.println("an.length:" + an.length);
		for (Annotation a : an) {
			System.out.println(a);
		}
		System.out.println("create");
		return null;
	}

	@Override
	public IStoreTableRow createObject(Class<? extends Serializable> c) {
//		System.out.println("createObject:"+c.getName());
		CtClass ctClass = null;
		Class<?> clazz = null;
		try {
			if(!classMap.containsKey(c.getName())){
				ctClass = createEntity(c);
				classMap.put(c.getName(), ctClass);
				clazz = ctClass.toClass();
			}else{
				ctClass = classMap.get(c.getName());
//				System.out.println("ctClass.getName():"+ctClass.getName());
//				ClassPool pool = ClassPool.getDefault();
//				ctClass = pool.get(ctClass.getName());
//				clazz = ctClass.getClass();
				clazz =Class.forName(ctClass.getName());
			}
			Object obj = clazz.newInstance();
			return (IStoreTableRow) obj;
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
		Field[] fields = c.getDeclaredFields();
		List<Field> result = new ArrayList<Field>();
		LinkedList<Key> keys = new LinkedList<Key>();
		List<Column> columns = new LinkedList<Column>();
		LinkedHashMap<Field, Key> refKey = new LinkedHashMap<Field, Key>();
		LinkedHashMap<Field, Column> refColumn = new LinkedHashMap<Field, Column>();
		LinkedHashMap<Key,Field> refField = new LinkedHashMap<Key,Field>();
		for (Field field : fields) {
			Key annotationKey = field.getAnnotation(Key.class);
			if (annotationKey != null) {
				result.add(field);
				if (annotationKey.index()) {
					keys.add(0, annotationKey);
				} else {
					keys.add(annotationKey);
				}
				refField.put(annotationKey,field);
				refKey.put(field, annotationKey);
			}
			Column annotationColumn = field.getAnnotation(Column.class);
			if (annotationColumn != null) {
				result.add(field);
				columns.add(annotationColumn);
				refColumn.put(field, annotationColumn);
			}
		}
		ClassPool pool = ClassPool.getDefault();

		// 创建一个类
		CtClass ctClass = pool.makeClass(c.getPackage().getName() + "." + entity.name() + "Gen");
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
						method2.setBody("{return \""+entity.name()+"\";}");
					}else if(method.getName().equals("getPrimaryKey")){
						//getPrimaryKey 
//						java.util.List<PrimaryKeySchemaObject> pk = new java.util.ArrayList<PrimaryKeySchemaObject>();
//						pk.add(new PrimaryKeySchemaObject("openid", PrimaryKeyTypeObject.STRING));
//						return pk;
						StringBuffer sb = new StringBuffer();
						sb.append("{");
						sb.append("java.util.List  pk = new java.util.ArrayList ();");
						for(Key annotationKey :keys){
							String name = annotationKey.name();
							Field f = refField.get(annotationKey);
							if(name.isEmpty())
								name = f.getName();
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
							String name = a.name();
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
							sb.append("m.put(\""+name+"\", new org.chenmin.open.objector.PrimaryKeyValueObject(this."+gets.getName()+"(), org.chenmin.open.objector.PrimaryKeyTypeObject."+a.type()+"));");
						}
						sb.append("return m;");
						sb.append("}");
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
							String name = a.name();
							if(name.isEmpty())
								name = f.getName();
							CtMethod gets = null;
							for(CtMethod ctm:ctParent.getMethods()){
								String getname = ctm.getName().toLowerCase();
								if(getname.equals("get"+f.getName()))
									gets = ctm;
							}
							if(gets==null)
								throw new CannotCompileException(c.getName()+"."+f.getName()+" has no getter method");
							sb.append("m.put(\""+name+"\", new org.chenmin.open.objector.ColumnValueObject(this."+gets.getName()+"(), org.chenmin.open.objector.ColumnTypeObject."+a.type()+"));");
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
							String name = a.name();
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
							sb.append("this."+sets.getName()+"(((org.chenmin.open.objector.PrimaryKeyValueObject)$1.get(\""+name+"\")).getValue().toString());");
						}
						sb.append("}");
						method2.setBody(sb.toString());
					}else if(method.getName().equals("setColumnValue")){
						//setColumnValue 
						// setFileid(((ColumnValueObject)paramMap.get("fileid")).getValue().toString());
						StringBuffer sb = new StringBuffer();
						sb.append("{");
						//for echo refKey
						for(Field f:refColumn.keySet()){
							Column a = refColumn.get(f);
							String name = a.name();
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
							sb.append("this."+sets.getName()+"(((org.chenmin.open.objector.ColumnValueObject)$1.get(\""+name+"\")).getValue().toString());");
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

//			CtClass cc = pool.get(c.getPackage().getName() + "." + entity.name());
			ctClass.writeFile("target/gen/");
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
