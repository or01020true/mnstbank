<%@page import="java.util.Iterator"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.io.FileReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.File"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@page import="org.example.hacking02_sk.model.User"%>
<%@ page import="org.example.hacking02_sk.service.MyDBConnection" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%!
//	Connection connection;
	Statement statement;
	ResultSet resultSet;
%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>문의게시판 상세보기</title>
	
	<script charset="UTF-8" src="/js/MyLibrary.js"></script>
	<script type="text/javascript" src="/js/jquery-3.4.1.min.js"></script>

	<link rel="stylesheet" href="/css/bootstrap.css">
	
	<style type="text/css">
		.container {
			display: flex;

			height: auto;
			min-height: calc(100vh - 100px);

			margin: 0 auto;

			flex-direction: column;
		}
		.button_class{ 
			background-color : rgba(212,244,250,0.7);
			border: thin;
			color: green;
			font-size: 0.5cm;
			font-weight: 500;  
			text-shadow: gray;
		}
		font{   
			margin-left: 15%;
			font-weight: bold;        
			color: rgba(34,116,28,1.0);
		}
		
		
	</style> 
	
</head>
<body>
	<jsp:include page="header.jsp" />
	<%!
	String mysubject,myid,mydate,mycontent,readcount,mypriority,myfilepath="",mytext,cur_user_id;  
	int fileCount=0;
	%>
<%try{%>

	<script>
		<%
			HttpSession httpSession = request.getSession();
			
			if(httpSession.getAttribute("user") != null){
				User user = (User)httpSession.getAttribute("user");
				cur_user_id = user.getMyid();
			} 
		
		
			if(cur_user_id == null){
		%>							
				alert('로그인 후 이용해주세요. 로그인 페이지로 이동합니다.');
				location.href="/member/login"; 

		<%  }%>
		
		
		var cur_user_id = '<%=cur_user_id%>'
	
		window.param1 = {
			method: "POST",  
			url: "/NoticeBoard",      //protocol://hostname:port/pathname?name=value&searchparam#hash     
			setRequestHeader: '', //["요청-헤더","값"] setRequestHeader 는 for(var 키 in setRequestHeader) { xhr.setRequestHeader(키 , setRequestHeader[키]) } 
			async: "false",  //true<비동기>또는false<동기>  
			withCredentials: "true", //true<cors 일 ? 쿠키 보냄>또는false<안보냄> 
			//responseType : "text", //text또는document등..   
		}
		
		
			
		
		
		
	</script>




	<!-- /FileUploadServlet -->
		<%
		
			String parmmysubject = request.getParameter("mysubject");
			String parm_mypriority = request.getParameter("mypriority");
//			Class.forName("com.mysql.jdbc.Driver");
//			connection = DriverManager.getConnection(
//				"jdbc:mysql://localhost:3306/myhacking?verifyServerCertificate=false&useSSL=false&useUnicode=true&serverTimezone=Asia/Seoul&allowPublicKeyRetrieval=true&autoReconnect=true",
//				"myhack",
//				"1234"
//			);
			
			statement = MyDBConnection.getConnection().createStatement();
			/*
			if(parm_mypriority == null){
				System.out.println(parm_mypriority);
				out.println("<sciprt>alert('mypriority 값 null'); window.go(-1);</script>"); 
			} 
			else{
				out.println("<sciprt>alert('mypriority 값 "+parm_mypriority+"'); </script>");
				System.out.println(parm_mypriority);  
			}
			*/    
			resultSet = statement.executeQuery("select * from myboard where mypriority=" + parm_mypriority + ";"); // 대소문자 상관없음.
			String mydate=null ,mysubject=null,mypriority=null,myid=null,myfilepath=null,mycontent=null,mytext=null , myreadcount=null;
			while(resultSet.next()){
				mydate = resultSet.getString("mydate");
				mysubject = resultSet.getString("mysubject");
				mypriority = String.valueOf(resultSet.getInt("mypriority")); // 번째 행
				myid = resultSet.getString("myid");
				myfilepath = resultSet.getString("myfilepath");
				mycontent = resultSet.getString("mycontent");
				mytext = resultSet.getString("mytext");
				myreadcount = resultSet.getString("myreadcount");
				
			} 
			if(myfilepath == null) {
				myfilepath = ""; 
			}
		%>  
		
		
		 
			
	    <br/> 
	    <script type="text/javascript">
			window.param1.send_querystring='update=true&mypriority='+ '<%=mypriority%>' + '&myreadcount=' + (parseInt('<%=myreadcount%>') + 1)
			MyLibrary.Base.xmlHttpRequest( window.param1 )	 
	    </script>
	<div class="container">
		<h2 style="text-align:center;">게시글</h2>
		<div style="width:100%">
			<table class="table table-bordered">
				<tbody>
				<tr>
					<th style="background-color:#fafafa; width:10%">유형</th>
					<td style="width:40%"><%=mycontent%></td>

					<th style="background-color:#fafafa; width:10%">작성일</th>
					<td style="width:50%"><%=mydate%></td>
				</tr>
				<tr>
					<th style="background-color:#fafafa;">제목</th>
					<td><%=mysubject%></td>

					<th style="background-color:#fafafa;">조회</th>
					<td style="width:50%"><%=myreadcount%></td>
				</tr>
				<tr>
					<th style="background-color:#fafafa;">작성자</th>
					<td colspan="3"><%=myid%></td>
				</tr>
				<tr>
					<th style="background-color:#fafafa;">첨부파일</th>
					<% if(!myfilepath.equals("")) fileCount=myfilepath.split(";").length;  %>
					<td id="tdmyid_첨부파일" colspan="3" style="cursor: pointer;"><%=fileCount%> 개
						<span id="tdmyid_첨부파일_링크"></span>
					</td>

					<script type="text/javascript">
						var myfilepath = '<%=myfilepath%>'
						//alert(myfilepath)
						click_count=0

						tdmyid_첨부파일.addEventListener('click',(e)=>{
							myfilepath.split(';').forEach((v,i,arr)=>{
								if(v.match(/\/fileupload\//img)){
									파일이름_확장자 = v.split('/fileupload/')[1];
								}else{
									파일이름_확장자 = v;
								}
								if(파일이름_확장자.match(/^null/)){
									파일이름_확장자 = 파일이름_확장자.replace(/^null/,"")
								}


								if(click_count % 2 === 0){
									if(파일이름_확장자 != undefined){
										tdmyid_첨부파일_링크.innerHTML += '<br/>'
										a = document.createElement('a');
										a.id = 'amyid_' + 파일이름_확장자.split('.')[0]
										a.download = 파일이름_확장자;
										a.href = '/fileupload/' + 파일이름_확장자;
										a.innerText =파일이름_확장자;
										tdmyid_첨부파일_링크.append(a);
									}
								}else if(click_count % 2 === 1) {
									tdmyid_첨부파일.querySelectorAll('a').forEach((a)=>{
										a.remove();
										tdmyid_첨부파일_링크.innerHTML = '';

									})
								}
							})

							click_count++
						})
					</script>
				</tr>
				<tr>
					<th style="background-color:#fafafa;">내용</th>
					<td colspan="3"><%=mytext%></td>
				</tr>
				</tbody>
			</table>
			<div id="divmyid_수정목록" style="text-align:center;">
				<input id="inputmyid_수정" class="input_class_버튼 btn btn-secondary form-label" type="button" value="수정"
					   style="display:inline-block"/>
				<input id="inputmyid_삭제" class="input_class_버튼 btn btn-secondary form-label" type="button" value="삭제"
					   style="display:inline-block"/>
				<input id="inputmyid_목록" class="input_class_버튼 btn btn-secondary form-label" type="button" value="목록"
					   style="display:inline-block"/>
			</div>
		</div>
	</div>
	<script>
			inputmyid_삭제.addEventListener('click',(e)=>{
				window.param1.send_querystring = 'delete=true&mypriority=' + '<%=mypriority%>'  
				MyLibrary.Base.xmlHttpRequest( window.param1 )  
				location.href = '/jsp/___NoticeBoard_List'           
			})
		
		
		
			inputmyid_수정.addEventListener('click',(e)=>{
				//mysubject,mypriority,myid,myfilepath,mycontent,mytext, myreadcount;
				location.href = '/jsp/___NoticeBoard_Writer?'
				+ '_rewriter=true'
				+'&mysubject=' + encodeURI('<%=mysubject%>')
				+ '&mypriority=' + encodeURI('<%=mypriority%>')
				+'&myid=' + encodeURI('<%=myid%>')
				
				+'&myfilepath=' + encodeURI('<%=myfilepath%>')
				+'&mycontent=' + encodeURI('<%=mycontent%>')
				+'&mytext=' + encodeURI('<%=mytext%>')
				+'&myreadcount=' + encodeURI('<%=myreadcount%>')       
			})
			
			inputmyid_목록.addEventListener('click',(e)=>{
				location.href = '/jsp/___NoticeBoard_List'     
			})		
			
			
			window.addEventListener('load',(e)=>{
				if(typeof(cur_user_id) !== 'undefined' && cur_user_id.match(/(root|admin|manager[0-9]?|<%=myid%>)/)){
					
				}else{
					inputmyid_수정.remove() 
					inputmyid_삭제.remove()   
				}  
			})			
		</script>
	<%
		}catch (Exception e){
			out.println(e.getMessage());
			e.printStackTrace();
		}
	%>
	<jsp:include page="footer.jsp" />
</body>
</html>