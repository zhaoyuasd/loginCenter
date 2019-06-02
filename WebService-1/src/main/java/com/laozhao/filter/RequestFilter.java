package com.laozhao.filter;

import com.laozhao.common.KeyUtils;
import com.laozhao.common.RequestUtils;
import com.laozhao.common.ThreadCurrent;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(filterName = "tokenFliter",urlPatterns = "/*")
public class RequestFilter implements Filter{

	@Value("${cookie.salt:  salt}")
	private  String   salt;
	@Value("${cookie.token: token}")
	private  String   token;
	@Value("${cookie.jaas:  jaas}")
	private  String   jaas;
    @Value("${cookie.loginUri:  /login}")
	private String  loginUri;
	// 首先检查cookie cokie的name为 指定的ip+salt 求MD5  然后使用MD5进行 byte的^ 运算 然后转16进制的字符串 取14次
    //value的生成规则类似 不过是与 jaas+token 求MD5 然后byte ^运算 取18次16进制的值
    //
    // 检查cookie否过期 是否需要刷新时间
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
                String uri=req.getRequestURI();
        	    if(!method.toLowerCase().contains("get")) {
        	         if(loginUri.equals(uri)){

                     }else{
                         resp.sendRedirect(loginUri);
                     }
					return ;
				}else{
                    chain.doFilter(request,response);
                    return ;
				}
			} else{
               initCookieToResponse(resp,req,tokenstr);
               String originUrl=RequestUtils.getOriginUrl(req);
               resp.sendRedirect(originUrl);
               return ;
			}
		}
        ThreadCurrent.setCookieId(cookie);
        chain.doFilter(request,response);
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
