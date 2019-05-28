package com.laozhao.controller;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.propertyeditors.URLEditor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
private final String cookieId="myCookie";
private final ConcurrentHashMap<String, String>  loginInfo=new ConcurrentHashMap(); 
private final ConcurrentHashMap<String, String>  userInfo=new ConcurrentHashMap();

@GetMapping("/tologin")
public void tologin(String url,HttpServletRequest req,HttpServletResponse  resp) {
	String uuid=UUID.randomUUID().toString();
	Cookie cook=new Cookie(cookieId,uuid);
	cook.setHttpOnly(true);
	cook.setPath("/");
	System.out.println(" set cookie "+uuid);
	resp.addCookie(cook);
    System.out.println("url:"+url);
    int index=url.lastIndexOf("/");
    String toUrl="";
    if(index>0)
    	toUrl=url.substring(0, index);
    else
    	toUrl=url;
    String token="/?token="+uuid;
    userInfo.put(uuid, toUrl);
    userInfo.put(toUrl, uuid);
    try {
    	System.out.println(toUrl+token);
    	//req.getRequestDispatcher("/redirectToOriginal?toUrl="+url+"&token="+uuid).forward(req, resp);  //不在web地址栏打印信息   转发
    	resp.sendRedirect("/redirectToOriginal?toUrl="+toUrl+"&token="+uuid);//在地址栏里打印信息 同时设置的cookie会回传给浏览器    重定向
	} catch (Exception e) {
		e.printStackTrace();
	}
}

@GetMapping("/redirectToOriginal")
public void redirectToOriginal(HttpServletRequest req,HttpServletResponse  resp) {
	Cookie[] cs=req.getCookies();
	String  url=req.getParameter("toUrl");
	String  token=req.getParameter("token");
	if(cs==null) {
		System.out.println(" null");
		return;
	}
	for(Cookie itm:cs) {
		System.out.println(itm.getName()+":"+itm.getValue());
	}
	if(url.indexOf("=")>0) {
		url=url+"&token="+token;
	}else if(url.indexOf("?")>0) {
		url=url+"token="+token;
	}else  {
		url=url+"?token="+token;
	}
	try {
		resp.sendRedirect(url);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}



@GetMapping("/tockeck")
public String tockeck(String token) {
	return userInfo.get(token);
}
}