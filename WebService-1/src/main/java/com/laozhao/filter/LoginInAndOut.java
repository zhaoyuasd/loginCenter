package com.laozhao.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginInAndOut implements Filter {
    @Value("${cookie.loginUri:  /login}")
    private String  loginUri;

    @Value("${cookie.loginOutUri:  /loginOut}")
    private String  loginOut;

    @Value("${cookie.userName:  userName}")
    private String username ;

    @Value("${cookie.password:  password}")
    private String password;

    @Value("${cookie.token: token}")
    private  String   token;


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse resp=(HttpServletResponse)  response;
        HttpServletRequest req=(HttpServletRequest)  request;
        String uri=req.getRequestURI();
        if(loginUri.equalsIgnoreCase(uri)||loginOut.equalsIgnoreCase(uri)){
            chain.doFilter(request,response);
            return ;
        }
        StringBuffer url=req.getRequestURL();
        if(loginUri.equalsIgnoreCase(uri)){
            //1. 检查token是否存在
            String tokenstr=req.getParameter(token);
            if(tokenstr==null){
                String userName=req.getParameter(username);
                String passWord=req.getParameter(password);
                if(StringUtils.isEmpty(userName)||StringUtils.isEmpty(passWord)){
                    resp.getOutputStream().println("账号密码不为空");
                    return ;
                }

            }
        }

    }

    public static void main(String[] args) {
       /* String query="name=zy:password=zy|name=name,password=password";
        byte[] b=query.getBytes();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<b.length;i++){
            int j=b[i];
            if(j<10){
                sb.append("0"+j)  ;
            }else if(j<16){
                sb.append("0"+Integer.toHexString(j))  ;
            }else{
                sb.append(Integer.toHexString(j))  ;
            }
        }
        6e616d653d7a793a70617373776f72643d7a797c6e616d653d6e616d652c70617373776f72643d70617373776f7264
        System.out.println(sb.toString());*/

        StringBuffer sb=new StringBuffer();
        String raw="6e616d653d7a793a70617373776f72643d7a797c6e616d653d6e616d652c70617373776f72643d70617373776f7264";
        byte[] b=new byte[1024];
        int index=0;
        for(int i=0;i<raw.length();i+=2){
            String item=raw.substring(i,i+2);
            int hig=Integer.parseInt(item,16);
            //int low=Integer.parseInt(item.substring(1,2));
            b[index]=(byte)(hig);
            index++;
        }
        System.out.println(new String(b,0,index-1));
    }
}
