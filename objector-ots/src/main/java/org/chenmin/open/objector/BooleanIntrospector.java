package org.chenmin.open.objector;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.BeanIntrospector;
import org.apache.commons.beanutils.IntrospectionContext;
import org.apache.commons.lang.WordUtils;

public class BooleanIntrospector implements BeanIntrospector{
    @Override
    public void introspect(IntrospectionContext icontext) throws IntrospectionException {
        for (Method m : icontext.getTargetClass().getMethods()) {
            if (m.getName().startsWith("is") && Boolean.class.equals(m.getReturnType())) {
                String propertyName = getPropertyName(m);
                PropertyDescriptor pd = icontext.getPropertyDescriptor(propertyName);

                if (pd == null)
                    icontext.addPropertyDescriptor(new PropertyDescriptor(propertyName, m, getWriteMethod(icontext.getTargetClass(), propertyName)));
                else if (pd.getReadMethod() == null)
                    pd.setReadMethod(m);

            }
        }
    }

    private String getPropertyName(Method m){
        return WordUtils.uncapitalize(m.getName().substring(2, m.getName().length()));
    }

    private Method getWriteMethod(Class<?> clazz, String propertyName){
        try {
            return clazz.getMethod("get" + WordUtils.capitalize(propertyName));
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}