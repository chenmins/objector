package org.chenmin.open.objector.test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Date;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

@SuppressWarnings("rawtypes")
public class Test {
    public static void main(String[] args) {

        testReflectParamName();
        // 反射获取方法参数注解
        testReflectMethodParamAnno();
    }

    /**
     * 反射获取方法参数名称
     */
    public static void testReflectParamName() {
		Class clazz = MyClass.class;
        try {
            ClassPool pool = ClassPool.getDefault();
            CtClass cc = pool.get(clazz.getName());
            CtMethod cm = cc.getDeclaredMethod("concatString");

            // 使用javaassist的反射方法获取方法的参数名
            MethodInfo methodInfo = cm.getMethodInfo();
            CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
            LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute
                    .getAttribute(LocalVariableAttribute.tag);
            if (attr == null) {
                // exception
            }
            String[] paramNames = new String[cm.getParameterTypes().length];
            int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
            for (int i = 0; i < paramNames.length; i++)
                paramNames[i] = attr.variableName(i + pos);
            // paramNames即参数名
            for (int i = 0; i < paramNames.length; i++) {
                System.out.println("参数名" + i + ":" + paramNames[i]);
            }

        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 反射获取方法参数注解
     */
    @SuppressWarnings("unchecked")
	public static void testReflectMethodParamAnno() {
        Class clazz = MyClass.class;
        try {
            // 使用jdk原生的反射方法
            Method m = clazz.getDeclaredMethod("datefomat",
                    new Class[] { Date.class });
            Annotation[][] annotations = m.getParameterAnnotations();
            System.out.println("jdk获取方法参数anno:"+annotations[0][0]);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        try {
            ClassPool pool = ClassPool.getDefault();
            CtClass cc = pool.get(clazz.getName());
            CtMethod cm = cc.getDeclaredMethod("datefomat");

            // 使用javassist的反射方法可以获得参数标注值
            Object[][] annotations = cm.getParameterAnnotations();
            DateFormat myAnno = (DateFormat) annotations[0][0];
            System.out.println("参数注解："+myAnno.value());

        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}




