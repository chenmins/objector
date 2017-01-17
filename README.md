# objector
an simple tablestore api
Document is 
[http://objector.open.chenmin.org](http://objector.open.chenmin.org)
##Install for Maven
```xml
	<dependency>
			<groupId>org.chenmin.open</groupId>
			<artifactId>objector-ots</artifactId>
			<version>0.0.3</version>
	</dependency>
	<repositories>
		<repository>
			<id>dpm</id>
			<name>Team dpm Repository</name>
			<url>http://vpn.dpm.im:8081/nexus/content/repositories/releases/</url>
		</repository>
	</repositories>
```
##Useage 
###Config for Aliyun.com OpenTableStore
objector.properties in classpath
```
#Sign up in https://www.aliyun.com/product/ots
ALIYUN_ACCESS_KEY=XXXXXX
ALIYUN_SECRET_KEY=XXXXXX
TS_ENDPOINT=http://YYYY.cn-beijing.ots.aliyuncs.com
TS_INSTANCENAME=YYYY
```
### Pojo with Annotation
```java
@Entity
public class UserObject implements Serializable {
	
	@Key(index = true)
	private String openid;
	
	@Column
	private String passwd;

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

}
```

###with Guice Example @Inject

```java
public class ServiceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ITableStoreService.class).to(TableStoreService.class);
		bind(IStore.class).to(Store.class);
		bind(Objector.class).to(OtsObjector.class);
	}

}
```

###CRUD for junit test 

```java
public class TestUserService {

	private static Injector injector;
	private static IStore store;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		injector = Guice.createInjector(new ServiceModule());
		store = injector.getInstance(IStore.class);
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

ocean test
