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
        // 요청에서 URL 매개변수 가져오기
        String url = request.getParameter("url");
        if (url != null && !url.isEmpty()) {
            try {
                // 대상 URL 생성
                URL targetUrl = new URL(url);
                // URL 연결 설정
                HttpURLConnection connection = (HttpURLConnection) targetUrl.openConnection();
                connection.setRequestMethod("GET");

                // 응답 코드 확인
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // 응답 본문 읽기
                    InputStream inputStream = connection.getInputStream();
                    // 클라이언트에 응답 본문 전송
                    // 바이트 스트림으로 전송하여 문자열로 변환하지 않음
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        response.getOutputStream().write(buffer, 0, bytesRead);
                    }
                    inputStream.close();
                } else {
                    // URL 내용을 가져오지 못한 경우 에러 처리
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to retrieve URL content.");
                }
            } catch (Exception e) {
                // 예외 발생 시 내부 서버 오류 처리
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
            }
        } else {
            // URL 매개변수가 없는 경우 에러 처리
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "URL parameter is missing");
        }
    }
}
