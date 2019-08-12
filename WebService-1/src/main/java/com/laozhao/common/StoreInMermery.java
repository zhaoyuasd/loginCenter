package com.laozhao.common;

import java.util.HashMap;
import java.util.Map;

public class StoreInMermery {
    private static Map<String,String> userInfo=new HashMap();

    public static String getUserName(String cookie){
        return userInfo.get(cookie);
    }

    public static void addUserName(String cookie,String userName){
        userInfo.put(cookie,userName);
    }
}
