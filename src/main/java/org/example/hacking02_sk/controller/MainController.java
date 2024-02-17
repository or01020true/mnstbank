package org.example.hacking02_sk.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;

import org.example.hacking02_sk.model.User;
import org.example.hacking02_sk.model.UserDAO;
import org.example.hacking02_sk.service.JwtUtil;

@Controller
public class MainController {

    @Autowired
    private UserDAO userDAO;

    @Autowired
 	private JwtUtil jwtUtil;

    @RequestMapping("/")
    String index(Model model, HttpServletRequest request){
        String jwt = jwtUtil.getToken(request.getCookies());
    	if (jwt != null && jwtUtil.validateToken(jwt)) {
            if (jwtUtil.extractUserId(jwt) != null) {
    		    model.addAttribute("name", userDAO.getName(jwtUtil.extractUserId(jwt)));
                model.addAttribute("level", userDAO.getLevel(jwtUtil.extractUserId(jwt)));
            }
        }
        return "index";
    }

    /** session
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
    */
}

