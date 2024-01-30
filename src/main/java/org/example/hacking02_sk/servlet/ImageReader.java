package org.example.hacking02_sk.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.example.hacking02_sk.MyLibrary;



@WebServlet("/ImageReader")
public class ImageReader extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		PrintWriter printWriter = response.getWriter();
		HttpSession httpSession = request.getSession();
		String IMAGES_경로 = "";
		String name = request.getParameter("name");
		if(MyLibrary.f_check_valid(name)) {
			if(MyLibrary.os_name.startsWith("window")) {
				IMAGES_경로 = request.getSession().getServletContext().getRealPath("/").toString().replaceAll("\\\\","/").replace("/webapp","/resources/static/images");
			}else if(MyLibrary.os_name.startsWith("linux")) {
				IMAGES_경로 = httpSession.getServletContext().getRealPath("/") + "WEB-INF/classes/static/images/";
			}
			
//			System.out.println( IMAGES_경로 );
			File file = new File(IMAGES_경로);
			if(file.exists()) {
				String imgBase64Str = MyLibrary.FileSystem.binStrToImgUrlBase64Str(MyLibrary.FileSystem.readAsBinFileToStr(IMAGES_경로 + name));
//				System.out.println(imgBase64Str);
				response.getWriter().println(imgBase64Str);
			}else {
//				System.out.println("파일이 없거나 읽을 수 없음.");
				response.getWriter().println("파일이 없거나 읽을 수 없음."); 
			}
		}
		
		printWriter.flush();
		response.flushBuffer();   
	}

}
