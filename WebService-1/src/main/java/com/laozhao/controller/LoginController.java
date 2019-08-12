package com.laozhao.controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class LoginController {
 private final String loginCenter ="http://127.0.0.1:8080/tologin";
 
// @Autowired
 private RestTemplate restTemplate=new RestTemplate() ;
 
  @GetMapping({"/tologin","/login"})	
  public String toLogin(HttpServletRequest req,HttpServletResponse resp) {
	  String ulr=req.getRequestURL().toString();
	  try {
		  String userName=req.getParameter("username");
		  String passWord=req.getParameter("password");
		  if(StringUtils.isEmpty(userName)||StringUtils.isEmpty(passWord)){
			  return "账号密码不为空";
		  }
		  addHeader(resp);
		  System.out.println(ulr);
		  //req.getRequestDispatcher(loginCenter+"?url="+ulr).forward(req, resp);
		StringBuffer sb=new StringBuffer();
		sb.append(loginCenter).append("?username="+userName).append("&password="+passWord).append("&url="+ulr);
		//resp.sendRedirect(loginCenter+"?url="+ulr);
		  resp.sendRedirect(sb.toString());

	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 return "ok";
  }
  
  @RequestMapping("/tocheck")
  public void tocheck(String token) {
	  String url="http://127.0.0.1:8080/tockeck?token="+token;
      ResponseEntity<String> str=restTemplate.getForEntity(url, String.class, new HashMap());
	  System.out.println(str.getBody());
  }
  
  
    private  String getLocalIp(){
      InetAddress inetAddress=null;
	try {
		inetAddress = InetAddress.getLocalHost();
	} catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null;
	}
      String ip=inetAddress.getHostAddress().toString();//获得本机Ip
      return ip;
  }

  private void addHeader(HttpServletResponse resp) {
	  resp.addHeader("zy", "zy");
  }
}
