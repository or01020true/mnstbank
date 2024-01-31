package org.example.hacking02_sk.controller;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GoController {

    @GetMapping("/go")
    public void redirectToUrl(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String url = request.getParameter("url");
        if (url != null && !url.isEmpty()) {
            response.sendRedirect(url);
        } else {
            // url 파라미터가 없을 경우 에러 처리
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "URL parameter is missing");
        }
    }
}