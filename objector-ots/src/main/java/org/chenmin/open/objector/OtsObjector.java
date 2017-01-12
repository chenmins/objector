package org.chenmin.open.objector;

import java.lang.annotation.Annotation;

public class OtsObjector implements Objector {

	@Override
	public IStoreTableRow create(Class<? extends IStoreTableRow> c) {
		System.out.println(c.getName());
		Annotation[] an = c.getDeclaredAnnotations();
		System.out.println("an.length:"+an.length);
		for(Annotation a:an){
			System.out.println(a);
		}
		System.out.println("create");
		return null;
	}

}
