package org.chenmin.open.objector.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)  
@Suite.SuiteClasses({   
	TestUserService.class,
	TestUserAttr.class,
	TestUserBlog.class,
	TestUserInc.class,
})  
public class TestSuite {

}
