package com.laozhao.filter;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.laozhao.common.KeyUtils;
import com.laozhao.common.RequestUtils;
import com.laozhao.common.StoreInMermery;
import com.laozhao.common.ThreadCurrent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;

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
@Component
public class RequestFilter implements Filter{

	@Value("${cookie.loginUri:  /login}")
	private String  loginUri;

    @Value("${cookie.checktoken:  /tocheck}")
    private String  checktoken;

	@Value("${cookie.loginOutUri:  /loginOut}")
	private String  loginOut;

	@Value("${cookie.salt:  salt}")
	private  String   salt;
	@Value("${cookie.token: token}")
	private  String   token;
	@Value("${cookie.jaas:  jaas}")
	private  String   jaas;

    private final String getToken ="http://127.0.0.1:8080/getToken";

    private RestTemplate restTemplate=new RestTemplate() ;
	// 首先检查cookie cokie的name为 指定的ip+salt 求MD5  然后使用MD5进行 byte的^ 运算 然后转16进制的字符串 取14次
    //value的生成规则类似 不过是与 jaas+token 求MD5 然后byte ^运算 取18次16进制的值
    //
    // 检查cookie否过期 是否需要刷新时间
    @Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse resp=(HttpServletResponse)  response;
		HttpServletRequest req=(HttpServletRequest)  request;
        resp.setContentType("text/html;charset=utf-8");
        String cookie=findplatformCookie(req);
        String uri=req.getRequestURI();
        System.out.println(uri);
        if(cookie==null){
        	String tokenstr=req.getParameter(token);
            String method=req.getMethod();
        	if(tokenstr==null){
                if("/login".equalsIgnoreCase(uri)||loginOut.equalsIgnoreCase(uri)){
                    chain.doFilter(request,response);
                    return ;
                }
                String dir=req.getParameter("dir");
                if(dir!=null){  //这里是走了登陆中心
                    response.getOutputStream().print("not login");
                    return;
                }else{
                    String url=req.getRequestURL().toString();
                    resp.sendRedirect(getToken+"?url="+url);
                    return;
                }
        	   /* if(!method.toLowerCase().contains("get")) {
        	         if(loginUri.equals(uri)){
                         chain.doFilter(request,response);
                         return ;
                     }else{
                         resp.sendRedirect(loginUri);
                     }
					return ;
				}else{
                    chain.doFilter(request,response);
                    return ;
				}*/
			} else{
               JSONObject jb=tocheck(tokenstr);
               if(jb.getBoolean("flag")){
                   initCookieToResponse(resp,req,tokenstr);
                   StoreInMermery.addUserName(tokenstr,jb.getString("name"));
               }else{
                   response.getOutputStream().print("auth error");
                   return;
               }

                if(loginUri.equalsIgnoreCase(uri)||loginOut.equalsIgnoreCase(uri)) {
                    response.getOutputStream().print("login suce");
                    return;
                }
                chain.doFilter(request,response);
              /* initCookieToResponse(resp,req,tokenstr);
               String originUrl=RequestUtils.getOriginUrl(req);
               resp.sendRedirect(originUrl);*/
               return ;
			}
		}else{
            if(loginUri.equalsIgnoreCase(uri)||loginOut.equalsIgnoreCase(uri)) {
                response.getOutputStream().print("登陆成功");
                return;
            }

        }
        ThreadCurrent.setCookieId(cookie);
        chain.doFilter(request,response);
        return ;
	}

	private void initCookieToResponse(HttpServletResponse resp, HttpServletRequest req, String tokenstr) {
		//String jaasValue=getJaasHeaderValue(req);
		String cookieValue=tokenstr;//KeyUtils.getKey(tokenstr+jaasValue,18,5);
		String cookieName=getCookieName(req);
		Cookie cookie=new Cookie(cookieName,cookieValue);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		resp.addCookie(cookie);
	}

    public JSONObject tocheck(String token) {
        String url="http://127.0.0.1:8080/tockeck?token="+token;
        ResponseEntity<String> str=restTemplate.getForEntity(url, String.class, new HashMap());
        System.out.println(str.getBody());
        String  info=str.getBody();
        JSONObject.toJSON(info);
        return (JSONObject)JSONObject.toJSON(info);
    }


	private String findplatformCookie(HttpServletRequest req) {
        String cookieName=getCookieName(req);
		Cookie[] cs=req.getCookies();
		if(cs!=null)
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
        return "zy";
	  /* String ip= RequestUtils.getIpFromRequest(req);
	   return  KeyUtils.getKey(ip+salt,14,8);*/
   }
}
