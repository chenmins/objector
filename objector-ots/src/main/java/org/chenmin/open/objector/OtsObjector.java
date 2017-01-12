package org.chenmin.open.objector;

import java.io.IOException;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
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
		Entity entity = c.getAnnotation(Entity.class);
		System.out.println(c.getPackage().getName());
		System.out.println(c.getCanonicalName());
		System.out.println(c.getSimpleName());
		System.out.println(entity);
		Field[] fields = c.getDeclaredFields();
		List<Field> result = new ArrayList<Field>();
		LinkedList<Key> keys = new LinkedList<Key>();
		List<Column> columns = new LinkedList<Column>();
		LinkedHashMap<Field, Annotation> ref = new LinkedHashMap<Field, Annotation>();
		for (Field field : fields) {
			Key annotationKey = field.getAnnotation(Key.class);
			if (annotationKey != null) {
				result.add(field);
				if (annotationKey.index()) {
					keys.add(0, annotationKey);
				} else {
					keys.add(annotationKey);
				}
				ref.put(field, annotationKey);
			}
			Column annotationColumn = field.getAnnotation(Column.class);
			if (annotationColumn != null) {
				result.add(field);
				columns.add(annotationColumn);
				ref.put(field, annotationColumn);
			}
		}
		for (Field f : ref.keySet()) {
			Annotation a = ref.get(f);
			if (a instanceof Key) {
				Key ks = (Key) a;
				System.out.println(f.getType().getName() + " " + f.getName() + ":" + ks);
			} else if (a instanceof Column) {
				Column cs = (Column) a;
				System.out.println(f.getName() + ":" + cs);
			}
		}
		ClassPool pool = ClassPool.getDefault();

		try {
			// 创建一个类
			CtClass ctClass = pool.makeClass(c.getPackage().getName() + "." + entity.name() + "1");
			// parent
			CtClass ctParent = pool.get(StoreTableRow.class.getName());
			ctClass.setSuperclass(ctParent);

			CtMethod[] mts = ctParent.getMethods();
			for (CtMethod method : mts) {
				if(method.getName().equals("getTablename")){
					//tablename
					CtMethod method2 = new CtMethod(method.getReturnType(), method.getName(), method.getParameterTypes(), ctClass);
					method2.setModifiers(Modifier.PUBLIC);
					method2.setBody("{return \""+entity.name()+"\";}");
					ctClass.addMethod(method2);
				}
			}
//			// 为类设置方法
//			CtMethod method = new CtMethod(CtClass.voidType, "run", null, ctClass);
//			method.setModifiers(Modifier.PUBLIC);
//			method.setBody("{System.out.println(\"执行结果\");}");
//			ctClass.addMethod(method);

//			CtClass cc = pool.get(c.getPackage().getName() + "." + entity.name());
			Class<?> clazz = ctClass.toClass();
			Object obj = clazz.newInstance();
			ctClass.writeFile("e:\\tw");
			return (IStoreTableRow) obj;
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (CannotCompileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
