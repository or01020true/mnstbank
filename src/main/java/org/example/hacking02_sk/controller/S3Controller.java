package org.example.hacking02_sk.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.example.hacking02_sk.service.S3Uploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.example.hacking02_sk.model.User;
import org.example.hacking02_sk.model.UserDAO;
import org.example.hacking02_sk.service.JwtUtil;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("banner")
public class S3Controller {
    @Autowired
	private S3Uploader s3Uploader;

    @Autowired
    private JwtUtil jwtUtil;

	@Autowired
    private UserDAO userDAO;

    @GetMapping("preview")
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

 
    // 관리자
    @GetMapping("modify")
    public String modify(Model model, HttpServletRequest request) {
        /*
        HttpSession session = request.getSession();
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user.getMylevel().equals("0")) {
                model.addAttribute("msg", "관리자만 접근 가능합니다.");
                return "banking/alert";
            }
        }
        */
        String jwt = jwtUtil.getToken(request.getCookies());
        if (jwt == null || !jwtUtil.validateToken(jwt)) {
            model.addAttribute("msg", "JWT이 존재하지 않습니다.");
            return "banking/alert";
        } 
        model.addAttribute("name", userDAO.getName(jwtUtil.extractUserId(jwt)));
        model.addAttribute("level", userDAO.getLevel(jwtUtil.extractUserId(jwt)));
        return "member/modify";
    }
 	
    @PostMapping("modify2")
    @ResponseBody
    public String create(@RequestParam("multipartFile") MultipartFile multipartFile){
//    	System.out.println(multipartFile);
        String fileName = "";
        if(multipartFile != null){ // 파일 업로드한 경우에만
            try{// 파일 업로드
                fileName = s3Uploader.saveFile(multipartFile); // S3 버킷의 images 디렉토리 안에 저장됨
//                System.out.println("fileName = " + fileName);
            }catch (Exception e){
            	e.getMessage();
            }
        }
        return fileName;
    }
}
