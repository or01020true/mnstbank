package org.example.hacking02_sk.servlet;

import java.io.IOException;


import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.example.hacking02_sk.service.MyDBConnection;
import org.springframework.beans.factory.annotation.Autowired;


@WebServlet("/NoticeBoard")
public class NoticeBoard extends HttpServlet {   
//	Connection connection;
	Statement statement;
	ResultSet resultSet;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");   
		PrintWriter printWriter = response.getWriter();
		//String name = new String(URLDecoder.decode(request.getParameter("name"),"UTF-8").getBytes("utf-8"));  
		try {  
			String mydate,mypriority,myreadcount,mycontent,input_name,myip,myid,mysubject,myfilepath,mytext , result=""; 
			int list_count=1, page_count=1 , current_index = 0 ;
//			Class.forName("com.mysql.jdbc.Driver");
//			connection = DriverManager.getConnection(
//				"jdbc:mysql://localhost:3306/myhacking?verifyServerCertificate=false&useSSL=false&useUnicode=true&serverTimezone=Asia/Seoul&allowPublicKeyRetrieval=true&autoReconnect=true",
//				"myhack",
//				"1234"
//			);
			statement = MyDBConnection.getConnection().createStatement();;
			
			
			
			String update = request.getParameter("update");
			mypriority = request.getParameter("mypriority"); 
			myreadcount = request.getParameter("myreadcount");
			
			if(update != null && update.equals("true") && mypriority != null && myreadcount != null) {
				String update_sql = "update myboard set myreadcount="+myreadcount + " where mypriority=" + mypriority + ";" ;
				if(statement.executeUpdate( update_sql ) >= 1) {   
					System.out.println(update_sql);
				}else { 
					System.out.println( "update 안됨" );
				}				
			}			
			
			
			
			
			String delete = request.getParameter("delete"); 
			mypriority = request.getParameter("mypriority");
			if(delete != null && delete.equals("true") && mypriority != null ) {
				String delete_sql = "delete from myboard where mypriority="+mypriority + ";" ;
				if(statement.executeUpdate( delete_sql ) >= 1) {  
					System.out.println(delete_sql);
				}
			} 			
			
			mycontent = request.getParameter("mycontent");  
			input_name = request.getParameter("input_name");       
			System.out.println(mycontent);  
			if(mycontent!=null){      
				//System.out.println(전체);
				//printWriter.println( mycontent + ";" + input_name);      
				
				if(mycontent.equals("전체")) {
					
					String query = "select * from myboard "
						+ "where mysubject regexp "+"'.*"+ input_name +".*'"+ " or " + "myid regexp " + "'.*" + input_name + ".*'" + " or " + "mytext regexp " + "'.*" + input_name + ".*'" 
						+ " order by mypriority desc" + ";" ;
					System.out.println(query);
					resultSet = statement.executeQuery(query);       
					 
					while(resultSet.next()) {  
						//(mydate,mypriority,myreadcount,mycontent,myip,myid,mysubject,myfilepath,mytext)

						
						//tr.innerHTML 		 
						result += "<tbody class='tbody_class' >";
						result += "<tr id='trmyid' class=tr_list_class>";    
							result += "<td class = 'td_class' align='center'><input class='input_class_관리자' type='checkbox'></td>";    
							result += "<td class = 'td_class' id='tdmyid_" + resultSet.getString("mypriority") + "'" + ">" + resultSet.getString("mypriority") +"</td>";        
							result += "<td class = 'td_classmysubject'><a id='amyid_"+ resultSet.getString("myid") + "'" + " class='a_class_리스트_카운트'>" + resultSet.getString("mysubject") + "</a> </td>" ;
							result += "<td class = 'td_class' id='tdmyid_userid_"+  resultSet.getString("myid") + "'"  +">"  +  resultSet.getString("myid") +"</td>" ;
							result += "<td class = 'td_class'>"+resultSet.getString("mydate") +"</td>" ;
							result += "<td class = 'td_class'>"+resultSet.getString("mycontent") +"</td>" ;
							result += "<td class = 'td_class'>"+resultSet.getInt("myreadcount") +"</td>" ;    	 
						result += "</tr>";    
						result += "</tbody>";         
						
						
						System.out.println(result);  
						
						if(list_count % 10 == 0) {  
							result += ";";
							page_count++;    
						}
						list_count++;				  
					}
					
					//System.out.println(jsonObject.toString()); // 로 할 시 마지막에 덮어써진 값만 출력됨.   
					
				}else if(mycontent.equals("작성자")) {  
					
				}else if(false) {
					
				}
				
				result += "\n\n";
				result+="list_count="+list_count + ";"; 
				result+="page_count="+page_count + ";";    
				result+="current_index="+current_index + ";";    
				
				System.out.println(result);
				
				printWriter.println(result);  
				
			}else{
				printWriter.println("false");      
			}
   			
		}catch(Exception e) {
			printWriter.println(e.getMessage());   
			e.printStackTrace(); 
		}finally {
			printWriter.flush();  
			printWriter.close();  
			response.flushBuffer();
		}

	}
	
	
	

}
