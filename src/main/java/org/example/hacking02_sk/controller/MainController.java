package org.example.hacking02_sk.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.example.hacking02_sk.model.User;

@Controller
public class MainController {
    @RequestMapping("/")
    String index(Model model, HttpServletRequest request){
        HttpSession session = request.getSession(false);
    	if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
    		    model.addAttribute("name", user.getMyname());
            }
        }
        return "index";
    }
}

