package org.example.hacking02_sk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("error")
public class ErrorController {
    @RequestMapping("404")
    public String errorPage404(HttpServletRequest request, HttpServletResponse response){
        return "error-page/error";
    }

    @RequestMapping("500")
    public String errorPage500(HttpServletRequest request, HttpServletResponse response){
        return "error-page/error";
    }
}
