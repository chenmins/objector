package org.chenmin.open.objector.test;

import org.apache.commons.beanutils.BeanIntrospector;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.WrapDynaBean;
import org.chenmin.open.objector.BooleanIntrospector;

public class Custom {
	
	public static class Test {
		private Integer a;
		private Boolean b;
		private Boolean c;
		public Integer getA() {
			return a;
		}
		public void setA(Integer a) {
			this.a = a;
		}
		public Boolean getB() {
			return b;
		}
		public void setB(Boolean b) {
			this.b = b;
		}
		public Boolean isC() {
			return c;
		}
		public void setC(Boolean c) {
			this.c = c;
		}
		
	}
	
	public static void main(String[] args) {
		try {
			BeanIntrospector a = new BooleanIntrospector();
			BeanUtilsBean.getInstance().getPropertyUtils().addBeanIntrospector(a );
			Test object = new Test();
			object.setA(100);
			object.setB(true);
			object.setC(true);
			Test userObject = new Test();
			BeanUtils.copyProperties(userObject, object);
			DynaBean dynaBean = new WrapDynaBean(object);
			for (DynaProperty propDesc : dynaBean.getDynaClass().getDynaProperties()) {
			    System.out.println("name = " + propDesc.getName());
			    System.out.println("class = " + propDesc.getType().getName());
			    System.out.println("value = " + dynaBean.get(propDesc.getName()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}