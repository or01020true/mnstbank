package org.example.hacking02_sk.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GoController {

    @GetMapping("/go")
    public void redirectToUrl(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String url = request.getParameter("url");
        if (url != null && !url.isEmpty()) {
            try {
                URL targetUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) targetUrl.openConnection();
                connection.setRequestMethod("GET");

                // 응답 코드 확인
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // 응답 본문 읽기
                    InputStream inputStream = connection.getInputStream();
                    Scanner scanner = new Scanner(inputStream);
                    StringBuilder responseData = new StringBuilder();
                    while (scanner.hasNextLine()) {
                        responseData.append(scanner.nextLine());
                    }
                    scanner.close();
                    inputStream.close();
                    
                    // 클라이언트에 응답 전송
                    response.getWriter().write(responseData.toString());
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to retrieve URL content.");
                }
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
            }
        } else {
            // URL 매개변수가 없는 경우 에러 처리
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "URL parameter is missing");
        }
    }
}
