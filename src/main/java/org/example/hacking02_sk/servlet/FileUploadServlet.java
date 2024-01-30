package org.example.hacking02_sk.servlet;



/* 파일 업로드 시 이미지일 경우 사진보여줌

 * 저장경로 파일이름등을 보여줌.
 * 
 * */
import java.io.IOException;
import org.example.hacking02_sk.*;



import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebListener;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javax.sql.DataSource;
import org.example.hacking02_sk.model.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;  
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;  

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.example.hacking02_sk.service.MyDBConnection;
import org.springframework.beans.factory.annotation.Autowired;

@WebServlet("/FileUploadServlet")
public class FileUploadServlet extends HttpServlet {
//    Connection connection;
    //PreparedStatement preparedStatement;
    //ResultSet resultSet;   
    Board board = new Board();
    int 중복카운트 = 0;
	//String 프젝이름 = "School_Project";
	//String 루트디렉 = "E:/C_drive_backup/tool/my_eclipse/eclipse-workstation/"+프젝이름+"/src/main/webapp";
    //String 빌드경로 = 루트디렉 + "/fileupload/";  
    String 파일업로드경로 = "";
    //String param_mypriority;
    ServletContext servletContext;

      
    public boolean f_check_valid(String string) {
    	if(string != null && !string.equals("null") && !string.equals("")) return true;  
    	return false;
    }
    
    @SuppressWarnings("all")
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("겟방식");
		String param_mypriority = "";
		//    multipart/form-data 는  post 로 보냄 (파람값은 못보냄. getParameter 불가.) 때문에 <form action=servlet?name=value> 로 보내줘야됨.
    	servletContext = getServletContext();
    	request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8"); 
		
		HttpSession httpSession = request.getSession();
		
		
		
		PrintWriter printWriter = response.getWriter();
		
		Enumeration<String> enumeration = request.getParameterNames();
		while(enumeration.hasMoreElements()) {
			String name속값 = enumeration.nextElement();
			System.out.println(name속값 + " = " + URLDecoder.decode(request.getParameter(name속값),"UTF-8"));
		}
		param_mypriority = request.getParameter("mypriority");
		
		if(f_check_valid(param_mypriority)) {  
			
			int int_mypriority = Integer.parseInt(  param_mypriority ) + 1;    // 
			
			servletContext.setAttribute("mypriority", String.format("%d", int_mypriority)  ); // 아이디 같을 시 update 때 게시판 중복쓰면 파일경로 업데이트 중첩됨. 			
		}  
		  
		try {
			 String myid = ((User)httpSession.getAttribute("user")).getMyid();
			 
			/*    2024-01-23     수정해야됨.   */
//			if(request.isRequestedSessionIdValid()) {	 
			 if(MyLibrary.f_check_valid(myid)) {
				board.setMyip( new String(request.getRemoteAddr().getBytes("utf-8")) );
				board.setMyid(   myid   ); 
				board.setMysubject(new String(URLDecoder.decode(request.getParameter("mysubject"),"UTF-8").getBytes("utf-8")));
				board.setMycontent(new String( URLDecoder.decode(request.getParameter("mycontent"),"UTF-8").getBytes("utf-8")));
				
				board.setMytext(new String( URLDecoder.decode(request.getParameter("mytext"),"UTF-8").getBytes("utf-8") ));
				 
				 
			    System.out.println("user 값 들어갔는지 확인 => "+board.getMyip()+board.getMyid()+board.getMysubject()+board.getMycontent()+board.getMytext());  
				Statement statement = MyDBConnection.getConnection().createStatement();
				String _rewriter = request.getParameter("_rewriter"); 
				  
				String updates="";
				
				if(f_check_valid(_rewriter) && !_rewriter.equals("true")) {   
					updates="insert into myboard(mydate,mypriority,myreadcount,mycontent,myip,myid,mysubject,myfilepath,mytext) values("
							+ "now()" +", " + 0 + "," + 0 + ",'" + board.getMycontent()+ "', '" + board.getMyip()+"', '"+board.getMyid() + "', '"+ board.getMysubject()+"', " + null+ ", '" + board.getMytext()+"'); ";
					if(statement.executeUpdate(updates) >= 1) {
						System.out.println("insert into 성공함!! => "+updates); 
						
					}else {  
						System.out.println(updates);
					}					
				}
				
				if(f_check_valid(_rewriter) && _rewriter.equals("true") && f_check_valid(param_mypriority)) { // 게시판 수정
					updates = "update myboard"
							+ " set mycontent=" + "'" + board.getMycontent()+"'"+ ", " + "myip=" + "'" + board.getMyip() + "'" + ", " + "mysubject=" + "'" + board.getMysubject() + "'" + ", " + "mytext=" + "'" + board.getMytext() + "'"
							+ " where mypriority="+param_mypriority+";";  
					if(statement.executeUpdate(updates) >= 1) {
						System.out.println("update 성공함!! => "+updates);   
						printWriter.println("true");
					}else {  
						System.out.println(updates);
						printWriter.println("false");
					}
				}				 
			 }else {
				 printWriter.println("alert('로그인 후 이용해주세요.');location.href='/';");
			 }
			 
				
//    		    connection.close();
//			} else {
//				response.getWriter().println("<script>");
//				response.getWriter().println("alert('로그인 후 이용해주세요.');"); 
//				// /School_Project/___ClientPage/loginWindow.jsp       
//				response.getWriter().println("location.href='/School_Project/___ClientPage/loginWindow.jsp';");
//				response.getWriter().println("</script>");
//				response.getWriter().flush();    
//				
//			} 
			
		} catch (Exception e) {
			System.out.println("예외 발생 코드 :: \n\t");
			e.printStackTrace();
			
			response.getWriter().println(e.getMessage()+e.toString()); 
		} 
    }
    
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, NullPointerException {
		//클라한테 요청올 시 get post 둘다 수행됨. 때믄에 user 에 담은것임.
    	servletContext = getServletContext(); 
    	request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8"); 
		HttpSession httpSession = request.getSession();
        Enumeration<String> enumeration = request.getParameterNames();
		while(enumeration.hasMoreElements()) {
			String name속값 = enumeration.nextElement();
			System.out.println(URLDecoder.decode(request.getParameter(name속값),"UTF-8"));
		}

        PrintWriter printWriter = response.getWriter();
        String myid = ((User)httpSession.getAttribute("user")).getMyid();
//      if(request.isRequestedSessionIdValid()) {
        if(MyLibrary.f_check_valid(myid)) {
        	 try {
        		 //param_mypriority = request.getParameter("mypriority");
//				 System.out.println("param_mypriority = " + param_mypriority);
        		 
        		 if(MyLibrary.os_name.startsWith("window")) {
             		 파일업로드경로 = request.getSession().getServletContext().getRealPath("/").replace("webapp\\", "") + "resources\\static\\fileupload\\";

					  
        		 }else if(MyLibrary.os_name.startsWith("linux")){
        			 ///usr/local/tomcat/webapps/ROOT/WEB-INF/classes/static/fileupload/
        			 파일업로드경로 = request.getSession().getServletContext().getRealPath("/") + "WEB-INF/classes/static/fileupload/" ;
        		 }
   
        		 
        		 
        		 System.out.println("파일업로드경로 : \n\n\t\t"+파일업로드경로);           		 

                 DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory(); //디스크 파일 아이템 펙토리 생성자 호출
                 diskFileItemFactory.setSizeThreshold(  (int)Runtime.getRuntime().freeMemory()  ); // 업로드시 사용할 임시 메모리 사이즈 지정
                 
                 File file_파일업로드경로 = new File(파일업로드경로) ;

				 if(!file_파일업로드경로.exists()) {
					 file_파일업로드경로.mkdirs();
					 file_파일업로드경로.setReadable(true,false); file_파일업로드경로.setWritable(true,false); file_파일업로드경로.setExecutable(true,false);
				 }

				 diskFileItemFactory.setRepository( // 셑 저장소를
                	file_파일업로드경로
                 ); //임시저장폴더
                 
                 //2. 업로드 요청을 처리하는 ServletFileUpload생성
                 ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);
                 servletFileUpload.setSizeMax(  file_파일업로드경로.getUsableSpace()  ); // 전체 최대 업로드 파일 크기 
                 
                 @SuppressWarnings("unchecked") // 경고무시
                 //3. 업로드 요청파싱해서 FileItem 목록구함​​
                 List<FileItem> list = servletFileUpload.parseRequest(request); 
                 
                 
                 
                 boolean is_file_upload = false;
                 String _rewriter = "";
                 int mypriority = -1;
                 List<FileItem> list_fileItem = new ArrayList<>();
                 
                 for(Iterator iterator=list.iterator(); iterator.hasNext();) {
                 	FileItem fileItem = (FileItem) iterator.next(); //아이템 얻기
                    if(fileItem.isFormField()){ //파일이 아닌경우
                    	System.out.println(" input_name = " + fileItem.getFieldName() + " \n input_value = " + fileItem.getString("UTF-8"));
                    	
                        if(fileItem.getFieldName().equals("_rewriter")) {
                        	_rewriter = fileItem.getString("UTF-8");
                        }
                        
                        /*
                        if(fileItem.getFieldName().equals("mypriority")) {
                        	mypriority = Integer.parseInt(fileItem.getString("UTF-8")) ;
                        	System.out.println("mypriority = " + mypriority);
                        }
                    	*/
                        
                        if(MyLibrary.f_check_valid(_rewriter) && _rewriter.equals("true")) {  // update
                        	mypriority = (Integer.parseInt(servletContext.getAttribute("mypriority").toString()) - 1); 
                        	
                        }else { // insert into 없지만 파일처리부분은 get에서 먼저 insert into 하므로 update로 무조건 _filepath 를 업데이트하는 개념임.
                        	mypriority = (Integer.parseInt(servletContext.getAttribute("mypriority").toString()));
                        }
                        
                		System.out.println("mypriority = " + mypriority);
//                        System.out.println("필드 = "+필드이름+"\n 파일 = "+파일이름+"\n 사이즈="+사이즈);
                        //file_upload(printWriter, fileItem, _rewriter ,mypriority, 빌드경로,request); 
                    
                    } else { //파일인 경우 파일개수만큼 블럭내용 실행됨.
                    	is_file_upload = true; 
                    	list_fileItem.add(fileItem);
                    }
                 }
     		     System.out.println("처리완료됨.");
     		     
     		     if(  MyLibrary.f_check_valid(is_file_upload) && MyLibrary.f_check_valid(_rewriter) && MyLibrary.f_check_valid(mypriority) ) {
     		        
     		    	Statement statement = MyDBConnection.getConnection().createStatement();
     		        
     		        if(_rewriter.equals("true")) { // 게시글수정 시 기존 저장되있는 _filepath="" 해줌.
     					String f_update = "update myboard set myfilepath=" + "''" + " where " + "mypriority=" +  mypriority + ";";
     					System.out.println("f_update = " + f_update);
     					if(statement.executeUpdate(f_update) >= 1) {
     						System.out.println("업데이트이므로 경로 초기화");    
     					};    
     		        }
     		        
     		    	for(FileItem fileItem : list_fileItem) { 
     		    		file_upload(printWriter, fileItem, _rewriter ,mypriority, 파일업로드경로,request); 
     		    	} 
     		     }else if(MyLibrary.f_check_valid(is_file_upload) && !MyLibrary.f_check_valid(_rewriter) && MyLibrary.f_check_valid(mypriority)) {
      		    	for(FileItem fileItem : list_fileItem) { 
     		    		file_upload(printWriter, fileItem, "" ,mypriority, 파일업로드경로,request); 
     		    	}
     		     }
     		     response.getWriter().println("<script>location.href='/jsp/___NoticeBoard_List';</script>");    
     		    
             } catch(Exception e) { // 무시 ㄱ      
             	 System.out.println(e.getMessage()); 
             	 response.getWriter().println(e.getMessage());   
             	 e.printStackTrace();
             }
//        }
        }else{
        	printWriter.println("alert('로그인 후 이용해주세요.');location.href='/';");
        }
    }
    

    //업로드한 정보가 파일인경우 처리. 파일다운로드 시 업로드가 다 되야 파일다운로드 가능함. 그 전까지는 파일 다운로드 불가.
    private void file_upload(PrintWriter printWriter, FileItem fileItem,String _rewriter ,int mypriority , String 파일업로드경로, HttpServletRequest request ) throws Exception {
        String 필드이름 = fileItem.getFieldName(); //파일의 필드 이름 얻기 즉 name속값임.
        String 파일이름 = fileItem.getName(); //파일명 얻기
        long 사이즈 = fileItem.getSize(); //파일의 크기 얻기
        ResultSet resultSet;
        Statement statement = MyDBConnection.getConnection().createStatement(); 
        HttpSession httpSession = request.getSession();

        
        board.setMyfilepath(파일업로드경로 + 파일이름);
        File file = new File(파일업로드경로 + 파일이름);
        if(file.exists()) { 
        	file = new File( 파일업로드경로 + 파일이름.replace(파일이름 , (this.중복카운트++) + "_" + 파일이름) );         
        }        
        
        if(!file.exists() || !file.canRead() || !file.canWrite() || !file.canExecute()) {
        	file.createNewFile(); 
        	file.setExecutable(true,false); file.setReadable(true,false); file.setWritable(true,false);
        	
        	fileItem.write(file); //(1)file 에 씀.        저장
        	
        }else {
        	System.out.println(" !file.exists() || !file.canRead() || !file.canWrite() || !file.canExecute() ");
        	System.out.println("!!!!!!!!!!!!!!!!! 파일없음 !!!!!!!!!!!!!!!!!");
        	
        	fileItem.write(file); //(1)file 에 씀.        저장 
        }
		

		System.out.println("resultset 실행 전 오류");
		System.out.println("select myfilepath from myboard where myid=" + "'"+board.getMyid()+"'" + " and " + "mypriority=" + servletContext.getAttribute("mypriority") + ";");
		resultSet = statement.executeQuery("select myfilepath from myboard where myid=" + "'"+board.getMyid()+"'" + " and " + "mypriority=" + mypriority + ";");
		System.out.println("resultset 실행 후 오류");
		String getmyfilepath = "",getmyfilepath2="";
		while(resultSet.next()) {  
			getmyfilepath=resultSet.getString("myfilepath"); 
		}
		if(getmyfilepath != null) {
			getmyfilepath2 += getmyfilepath; 
		}
		System.out.println(getmyfilepath2);


		String db_file_upload_path = "";
		if(파일이름 != null && !파일이름.equals("")) {   
			if(MyLibrary.os_name.startsWith("window")) {
				db_file_upload_path = request.getSession().getServletContext().getRealPath("/").toString().replaceAll("\\\\","/"); // eclipse에서 실행될때의 경로는 달라지므로 받아온 빌드경로로 x
				db_file_upload_path = db_file_upload_path.replace("/webapp","/resources/static/fileupload");
				System.out.println("db_file_upload_path = " + db_file_upload_path); 
			}else if(MyLibrary.os_name.startsWith("linux")){
				///usr/local/tomcat/webapps/ROOT/WEB-INF/classes/static/fileupload/
				db_file_upload_path = request.getSession().getServletContext().getRealPath("/") + "WEB-INF/classes/static/fileupload/";
				
			}
			System.out.println("db_file_upload_path = " + db_file_upload_path);

			System.out.println("this.db_file_upload_path = " + db_file_upload_path);
			db_file_upload_path +=  파일이름+";";
			System.out.println(db_file_upload_path + "DB File Upload 경로");
			String f_update = "update myboard set myfilepath=" + ("'" + getmyfilepath2 + db_file_upload_path + "'") + " where myid=" + "'" + board.getMyid() + "'" + " and " + "mypriority=" +   mypriority + ";";
			System.out.println("f_update = " + f_update);
			if(statement.executeUpdate(f_update) >= 1) {
				board.setMyfilepath(db_file_upload_path);
				System.out.println("디비 파일 업로드된 경로 = " + board.getMyfilepath());
			};     
		}else { // ?
			String f_update = "update myboard set myfilepath=" + "'"+ db_file_upload_path+"'" + " where " + "mypriority=" +  mypriority + ";";
			if(statement.executeUpdate(f_update) >= 1) {
				board.setMyfilepath(db_file_upload_path);
				System.out.println("디비 파일 업로드된 경로 = " + board.getMyfilepath());
			} 
		}
        System.out.println("myfilepath => "+파일이름);
        System.out.println("\n 업로드된 파일 = "+파일이름);
    }
    
}