# objector
an simple tablestore api
##Install for Maven
```xml
	<dependency>
			<groupId>org.chenmin.open</groupId>
			<artifactId>objector-ots</artifactId>
			<version>0.0.2</version>
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
	private static Objector objector;
	private static IStore store;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		injector = Guice.createInjector(new ServiceModule());
		objector = injector.getInstance(Objector.class);
		store = injector.getInstance(IStore.class);
		UserObject u = objector.createObject(UserObject.class);
		if (!store.exsitTable(u)) {
			store.createTable(u);
		}
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {

		UserObject userObject = objector.createObject(UserObject.class);;
		String openid = "chenmintest";
		String passwd = "12345678";
		String passwd2 = "12";
		userObject.setOpenid(openid);
		userObject.setPasswd(passwd);
		assertTrue(store.save(userObject));
		UserObject t = objector.createObject(UserObject.class);;
		t.setOpenid(openid);
		assertTrue(store.get(t));
		assertEquals(t.getPasswd(), passwd);
		UserObject u = objector.createObject(UserObject.class);;
		u.setOpenid(openid);
		u.setPasswd(passwd2);
		assertTrue(store.update(u));
		t = objector.createObject(UserObject.class);;
		t.setOpenid(openid);
		assertTrue(store.get(t));
		assertEquals(t.getPasswd(), passwd2);
		t = objector.createObject(UserObject.class);;
		t.setOpenid(openid);
		assertTrue(store.del(t));
		assertEquals(t.getPasswd(),null);
	}

}

```

