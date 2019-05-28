package com.laozhao.filter;

import com.laozhao.common.KeyUtils;
import com.laozhao.common.RequestUtils;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestFilter implements Filter{

	@Value("${cookie.salt:  salt}")
	private  String   salt;
	@Value("${cookie.token: token}")
	private  String   token;
	@Value("${cookie.jaas:  jaas}")
	private  String   jaas;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse resp=(HttpServletResponse)  response;
		HttpServletRequest req=(HttpServletRequest)  request;
        String cookie=findplatformCookie(req);
        if(cookie==null){
        	String tokenstr=req.getParameter(token);
        	if(token==null){
        	    String method=req.getMethod();
        	    if(!method.toLowerCase().contains("get")) {
					resp.sendRedirect("/login");
					return ;
				}else{

				}
			} else{
               initCookieToResponse(resp,req,tokenstr);
               String originUrl=RequestUtils.getOriginUrl(req);
               resp.sendRedirect(originUrl);
               return ;
			}
		}
	}

	private void initCookieToResponse(HttpServletResponse resp, HttpServletRequest req, String tokenstr) {
		String jaasValue=getJaasHeaderValue(req);
		String cookieValue=KeyUtils.getKey(tokenstr+jaasValue,18,5);
		String cookieName=getCookieName(req);
		Cookie cookie=new Cookie(cookieName,cookieValue);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		resp.addCookie(cookie);
	}

	private String findplatformCookie(HttpServletRequest req) {
        String cookieName=getCookieName(req);
		Cookie[] cs=req.getCookies();
		for(Cookie itm:cs ){
			if(cookieName.equals(itm.getName())){
				return itm.getValue();
			}
		}
		return null;
	}

   private String getJaasHeaderValue(HttpServletRequest req){
	   String ip= RequestUtils.getIpFromRequest(req);
	   return  KeyUtils.get32MdString(ip+salt);
   }

   private String  getCookieName(HttpServletRequest req){
	   String ip= RequestUtils.getIpFromRequest(req);
	   return  KeyUtils.getKey(ip+salt,14,8);
   }
}
