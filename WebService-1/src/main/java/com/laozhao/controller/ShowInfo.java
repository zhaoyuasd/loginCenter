package com.laozhao.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShowInfo {

    @RequestMapping("/showInfo")
    public String showInfo(){
    	String str="app456 ,login";
        System.out.println(str);
        return str;
    };
}
