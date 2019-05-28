package com.laozhao.common;

import java.security.MessageDigest;

public class KeyUtils {

    public   static String get32MdString(String str){
        byte[] b=get32Md5Byte(str);
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<(b==null?0:b.length);i++){
            int j=b[i];
            if(j<0)
                j+=256;
            if(j<16)
                sb.append("0");
            sb.append(Integer.toHexString(j));
        }
        return sb.toString();
    }

    private  static byte[] get32Md5Byte(String str) {
        MessageDigest md = null;
        byte[] b = null;
        try {
            md = MessageDigest.getInstance("MD5");
            b = md.digest(str.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    public static String getKey(String str,int length,int interval ){
        byte[] b=get32Md5Byte(str);
        if(length<=12)
            length=12;
        if(interval >= 8)
            interval =8;
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int num=(b[i]^b[(b.length+i+interval)%b.length])&0xff;
            if(num<10){
                sb.append("0"+num);
            }else if(num<16){
                sb.append("0");
                sb.append(Integer.toHexString(num));
            }else
            {
                sb.append(Integer.toHexString(num));
            }
        }

        return sb.toString();
    }

}
