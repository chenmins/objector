---
layout: default
---

# [](#objector-1)Objector

Objector 是一个简单的对象创建器。

主要支持普通的Pojo对象通过@@annotation方式进行字节码增强，将对象直接持久化的微型框架。

目前采用阿里云的**表格存储**实现，详情见
[https://www.aliyun.com/product/ots](https://www.aliyun.com/product/ots)
待此版本稳定后，将提供其他实现方式如**MongoDB**
 

## [](#maven-install)安装方式Install for Maven

来自maven中心库，加入以下依赖。

```xml
<dependency>
		<groupId>org.chenmin.open</groupId>
		<artifactId>objector-ots</artifactId>
		<version>0.2.2</version>
</dependency>
```

## [](#Useage)使用方式Useage 

### 阿里云配置Config for Aliyun.com OpenTableStore

请再classpath中加入配置文件根目录objector.properties

objector.properties in classpath 

```
#Sign up in https://www.aliyun.com/product/ots
ALIYUN_ACCESS_KEY=XXXXXX
ALIYUN_SECRET_KEY=XXXXXX
TS_ENDPOINT=http://YYYY.cn-beijing.ots.aliyuncs.com
TS_INSTANCENAME=YYYY
```

### Pojo with Annotation

[详细源码 点此进入](https://github.com/chenmins/objector/blob/master/objector-test/src/test/java/org/chenmin/open/objector/UserObject.java)

```java
@Entity//表示此对象为持久化实体
public class UserObject implements Serializable {
	
	@Key(index = true)//主键，和索引
	private String openid;
	
	@Column//列名称
	private String passwd;

	//some getter and setter ......

}
```
 

### CRUD for junit test 

[详细源码 点此进入](https://github.com/chenmins/objector/blob/master/objector-test/src/test/java/org/chenmin/open/objector/test/TestUserService.java)


```java
public class TestUserService {

	private static IStore store;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		//初始化Guice容器
		store =StoreFactory.create();
		UserObject u = new UserObject();
		if (!store.exsitTable(u)) {
			store.createTable(u);
		} 
	}

	@Test
	public void test() {
		
		UserObject userObject = new UserObject();
		String openid = "chenmintest";
		String passwd = "12345678";
		String passwd2 = "12";
		userObject.setOpenid(openid);
		userObject.setPasswd(passwd);
		assertTrue(store.save(userObject));
		UserObject t = new UserObject();
		t.setOpenid(openid);
		assertTrue(store.get(t));
		assertEquals(t.getPasswd(), passwd);
		UserObject u = new UserObject();
		u.setOpenid(openid);
		u.setPasswd(passwd2);
		assertTrue(store.update(u));
		t = new UserObject();
		t.setOpenid(openid);
		assertTrue(store.get(t));
		assertEquals(t.getPasswd(), passwd2);
		t = new UserObject();
		t.setOpenid(openid);
		assertTrue(store.del(t));
		assertEquals(t.getPasswd(),null);
	}
}

```


