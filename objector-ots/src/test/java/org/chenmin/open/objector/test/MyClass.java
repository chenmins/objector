package org.chenmin.open.objector.test;

import java.util.Date;

public class MyClass {
    public String concatString(String param1, String param2) {
        return param1 + param2;
    }

    public String datefomat(@DateFormat("yyyy-MM-dd HH")
    Date date1) {
        return date1.toString();
    }
}