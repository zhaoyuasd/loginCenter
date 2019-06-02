package com.laozhao.common;

import java.util.HashMap;
import java.util.Map;

public class ThreadCurrent {
   private static  ThreadLocal<Map>  threadLocal=new ThreadLocal<>();
   private static final String cookieName="user:cookie";

   public static void setCookieId(String cookie){
      Map map= threadLocal.get();
      if(map==null){
          map=new HashMap();
      }
       map.put(cookieName,cookie);
       threadLocal.set(map);
   }

   public static String getCookieId(){
       Map map= threadLocal.get();
       if(map==null){
          return null;
       }
       return map.get(cookieName).toString();
   }
}
