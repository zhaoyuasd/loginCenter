package com.laozhao.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShowInfo {

    @RequestMapping("/showInfo")
    public String showInfo(){
        System.out.println("app1 ,login");
        return "app1 ,login";
    };
}
