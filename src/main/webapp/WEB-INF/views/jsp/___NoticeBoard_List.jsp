<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="org.example.hacking02_sk.model.User"%>
<%@page import="org.example.hacking02_sk.service.MyDBConnection"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%!
//Connection connection;
Statement statement;
ResultSet resultSet;
String cur_usermyid="";
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width", initial-scale="1">
<title></title>
<link rel="stylesheet" href="/css/bootstrap.css">
<script type="text/javascript" src="/js/jquery-3.4.1.min.js"></script>
<script charset="UTF-8"
	src="/js/MyLibrary.js"></script>

<style type="text/css">
.container {
	display: flex;
	justify-content: center;
	align-items: center;
	flex-direction: column;

	height: auto;
	min-height: calc(100vh - 100px);
	padding-bottom: 100px; /*footer 높이*/

	padding-top: 3%;
}
.search_keyword {
	width: 100%;
}
table td {
	text-align: center;
}
table input {
	text-align: center;
	width: 100%;
}
table th:first-child {
	width: 5%;
}
table th:nth-child(2) {
	width: 7%;
}
table th:nth-child(4) {
	width: 10%;
}
table th:last-child {
	width: 5%;
}

</style>
</head>
<body style="overflow-x: auto;">
<%try{%>
	<script>
		<%if( session.getAttribute("user") == null ){%>
			alert('로그인 후 이용해주세요. 로그인 페이지로 이동합니다.');
			location.href='/member/login';
		<%}%>


		window.param1 = {
			method: "POST",
			url: "/NoticeBoard",      //protocol://hostname:port/pathname?name=value&searchparam#hash
			setRequestHeader: '', //["요청-헤더","값"] setRequestHeader 는 for(var 키 in setRequestHeader) { xhr.setRequestHeader(키 , setRequestHeader[키]) }
			async: "false",  //true<비동기>또는false<동기>
			withCredentials: "true", //true<cors 일 ? 쿠키 보냄>또는false<안보냄>
			//responseType : "text", //text또는document등..
		}
	</script>
	
	<script type="text/javascript">
			var list_count = 1 ; var current_index = 0 ; var page_count = 1;  
			var result=""; 
			
			
			
			function remove_관리자(){
				document.querySelectorAll('.input_class_관리자').forEach((input)=>{
					if( typeof(cur_usermyid) !== 'undefined' && cur_usermyid !== '' ){  
						if( cur_usermyid.match(/(root|admin|manager.*)/)){  
							//input.style.cssText = input.style.cssText.replace('display: none;','display: block;') 
							click_count = 0
							input.addEventListener('click',(e)=>{
								if(click_count % 2 === 0){
									//alert()   
								}else{
									//alert('false')       
								}
								click_count++ 
							})
							
							
						}else{
							//input.style.cssText = input.style.cssText.replace('display: block;','display: none;')  
							//input.style.cssText = input.style.cssText.replace('display: block;','display: hidden;')   
							input.remove();         
						}      					
					}else{
						input.remove(); 
					}
				})
			}
	</script>
	
	<% System.out.println(request.getParameter("mypriority")); %> 
	
	<jsp:include page="header.jsp" />
	<div class="container">
		<h3>문의게시판</h3>
		<div class="search_keyword">
			<input id="inputmyid_조회" class="btn btn-secondary form-label" type="button"
				   style="display:inline-block; width:5%; float:right; margin-left: 5px;" value="검색" />
			<input id="inputmyid_입력해주세요" class="form-control" type="text" placeholder="입력해주세요."
				   style="display:inline; width:23%; float:right; margin-left: 5px;" required />
			<select id="selectmyid_검색유형" class="form-select"
					style="display:inline; width:8%; float:right;">
				<option id="optionmyid_전체" label="전체" value="전체" selected />
				<option id="optionmyid_작성자" label="작성자" value="작성자" />
			</select>
		</div>
		<div id="div_게시판myid">
			<!-- 게시판 목록 -->
			<table id="tablemyid" class="table">
				<tr>
					<th>
						<input name="_select" id="선택" class="input_class_관리자"
							   style="border:0; cursor:not-allowed;" type="text" readonly value="" />
					</th>
					<th>
						<input name="count" id="번호" type="text" value="번호"
							   style="border:0; cursor:not-allowed;" disabled/>
					</th>
					<th>
						<input name="title" id="게시판_제목" type="text" value="제목"
							   style="border:0; cursor:not-allowed;" disabled/>
					</th>
					<th>
						<input name="writer" id="작성자" type="text" value="작성자"
							   style="border:0; cursor:not-allowed;" disabled/>
					</th>
					<th>
						<input name="write_time" id="작성시간" type="text" value="작성일시"
							   style="border:0; cursor:not-allowed;" disabled/>
					</th>
					<th>
						<input name="_type" id="유형" type="text" value="유형"
							   style="border:0; cursor:not-allowed;" disabled/>
					</th>
					<th>
						<input name="view_count" id="조회수" type="text" value="조회수"
							   style="border:0; cursor:not-allowed;" disabled/>
					</th>
				</tr>
			</table>
			<div>
				<input id="inputmyid_삭제" class="input_class_관리자 btn btn-secondary form-label" type="button"
					   style="display:inline-block; width:5%; float:right; margin-left: 5px;" value="삭제" />
				<input id="inputmyid_게시글_작성" class="btn btn-secondary form-label" type="button"
					   style="display:inline-block; width:5%; float:right;" value="작성" />
				<script>
					inputmyid_삭제.addEventListener('click',(e)=>{
						document.querySelectorAll('input[class="input_class_관리자"][type="checkbox"]').forEach((input)=>{
							if(input.checked){
								window.param1.send_querystring = 'delete=true&mypriority=' + input.parentElement.nextSibling.innerText
								MyLibrary.Base.xmlHttpRequest( window.param1 )
							}
						})
						location.reload(true)
					})




					function 검색(){
						//alert(optionmyid_게시판제목.value + inputmyid_입력해주세요.value)
						//location.href = '/NoticeBoard?name='+encodeURI(inputmyid_입력해주세요.value);
						window.param1.send_querystring = 'mycontent=' + selectmyid_검색유형.options[selectmyid_검색유형.selectedIndex].value + '&input_name='+inputmyid_입력해주세요.value ;  //name=value&param=value
						window.param1.done_2xx_callback = function() {
							//alert(window.xmlHttpRequest.response.split("\n\n")[0])
							//alert( window.xmlHttpRequest.response.split("\n\n")[1] )
							document.querySelectorAll('.tbody_class').forEach((e)=>{
								e.remove()
							})
							document.querySelectorAll('.list_button_class').forEach((e)=>{
								e.remove()
							})

							console.log(window.xmlHttpRequest.response.split("\n\n")[0])
							console.log(window.xmlHttpRequest.response.split("\n\n")[1])

							result=window.xmlHttpRequest.response.split("\n\n")[0]

							new Function(window.xmlHttpRequest.response.split("\n\n")[1])(); //list_count=11;page_count=2;



							var page = result.split(";");
							var tablemyid = document.querySelector("#tablemyid");
							tablemyid.innerHTML += page[current_index]; // 첫 페이지부터 출력함.

							default_events();

							if(page_count == 1)	{
								var input = document.createElement("input")
								input.type="button";
								input.style.border="thin";
								input.value = 1;

							} else if(page_count > 1){
								for(var i = current_index+1; i <= page_count; i++){
									if(i % 11 == 0){

									} else {
										// debugger
										lt.outerHTML += '<input type=button id=list_buttonmyid_'+i+' class="list_button_class"  value='+i+' onclick="javascript: page_change('+ i +');"/>'

									}
								}
							}
							$('#list_buttonmyid_1').css('background-color','rgba(189,189,189,0.7)');
						}

						MyLibrary.Base.xmlHttpRequest( window.param1 )
					}
					window.addEventListener('load',(e)=>{
						input = document.querySelector('#inputmyid_조회')
						input.addEventListener('click',(e)=>{
							검색()
						})

						input = document.querySelector('#inputmyid_입력해주세요')
						input.addEventListener('keyup',(e)=>{
							if(e.keyCode === 13){
								검색()
							}
						})
					})

				</script>
			</div>
		</div>
		<div id="div_ltgt" style=""></div>
	</div>

	<script type="text/javascript">
	<%
		 
		HttpSession httpSession = request.getSession();
		if(httpSession.getAttribute("user") != null){
			cur_usermyid = ((User)httpSession.getAttribute("user")).getMyid(); 
		} 
	
	
		if(cur_usermyid == null){  
	%>
		$('#inputmyid_게시글_작성').on('click',function(click){
			alert('로그인 후 이용해주세요. 로그인 페이지로 이동합니다.');
			location.href="/member/login.html";
		});
	<%} else {%>
		var cur_usermyid='<%=cur_usermyid%>';
		
		$('#inputmyid_게시글_작성').on('click',function(click){ 
			tr = document.querySelector('.tr_list_class')
			
			if( tr != null ){
				location.href="/jsp/___NoticeBoard_Writer?mypriority="+tr.childNodes[1].innerText;
			} else {  
				location.href="/jsp/___NoticeBoard_Writer?mypriority=" + 0;
			} 
		});
		
	<%}%>

	</script>
	<%
		 
//		Class.forName("com.mysql.jdbc.Driver");
//		connection = DriverManager.getConnection(
//				"jdbc:mysql://localhost:3306/myhacking?verifyServerCertificate=false&useSSL=false&useUnicode=true&serverTimezone=Asia/Seoul&allowPublicKeyRetrieval=true&autoReconnect=true",
//				"myhack",
//				"1234"
//		);
		//statement = connection.createStatement();
		statement = MyDBConnection.getConnection().createStatement();
		resultSet = statement.executeQuery("select * from myboard where myid="+"'"+ cur_usermyid +"'"+";");

	%>
	<script>``
		<%if(resultSet.next() && !cur_usermyid.equals("")){%>
			// 아래 수행안됨.          
			mypriority = '<%=resultSet.getString("mypriority")%>'
			mysubject = '<%=resultSet.getString("mysubject")%>'
			myid = '<%=resultSet.getString("myid")%>'
			
			mydate = '<%=resultSet.getString("mydate")%>'
			mycontent= '<%=resultSet.getString("mycontent")%>'  
			myreadcount = '<%=resultSet.getString("myreadcount")%>' 		
		<%}%>	
		</script>


	<script type="text/javascript">  
		<%	
			resultSet = statement.executeQuery("select mypriority,mysubject,myid,mydate,mycontent,myreadcount from myboard order by mypriority desc;");
			while(resultSet.next()){
		%>
			//tr.innerHTML		
			result += '<tbody class="tbody_class">'    
			result += '<tr class=tr_list_class>'    
				result += "<td class = 'td_class' align='center'><input class='input_class_관리자' type='checkbox'></td>"    
				result += "<td class = 'td_class' id=tdmyid_" + '<%=resultSet.getString("mypriority")%>' + ">" + '<%=resultSet.getString("mypriority")%>' +"</td>";        
				result += "<td class = 'td_classmysubject'><a id=amyid_"+ '<%=resultSet.getString("myid")%>'   +   " class=a_class_"+list_count+ ">"   +   '<%=resultSet.getString("mysubject")%>' + "</a> </td>" ;
				result += "<td class = 'td_class' id=tdmyid_userid_"+  '<%=resultSet.getString("myid")%>'  +">"  +  '<%=resultSet.getString("myid")%>' +"</td>" ;
				result += "<td class = 'td_class'>"+'<%=resultSet.getString("mydate")%>' +"</td>" ;
				result += "<td class = 'td_class'>"+'<%=resultSet.getString("mycontent")%>' +"</td>" ;
				result += "<td class = 'td_class'>"+'<%=resultSet.getInt("myreadcount")%>' +"</td>" ;    	 
			result += "</tr>"; 
			
			result += '</tbody>'      
			
			if(list_count % 10 == 0) {  
				result += ";";
				page_count++;    
			}
			list_count++; 
				
		<%} %>
		</script>






	<script type="text/javascript">
		var page = result.split(";");
		var tablemyid = document.querySelector("#tablemyid");
		tablemyid.innerHTML += page[current_index]; // 첫 페이지부터 출력함.
		    
		//var td_class_usermyid = document.querySelector('#td_class_userid_');
		function default_events(){
			//tdmyid_userid_
			var userid = document.querySelectorAll('#tdmyid_userid_'+cur_usermyid)
			for(var i=0;i<userid.length;i++){
				userid[i].style.color='black';
				userid[i].backgroundColor='white';  
			}
			var subject = document.querySelectorAll('.td_classmysubject');
			for(var i=0; i < subject.length; i++){
				subject[i].onclick=function(e){ 
					var mypriority = e.currentTarget.firstChild.className.split('_')[1];  
					//alert(mypriority);    
					window.location.href = '/jsp/___NoticeBoard_Writer_check?mysubject=' + e.currentTarget.innerText + '&mypriority=' + e.currentTarget.previousSibling.innerText
				}   
				  
				subject[i].onmouseover =  function(e){
					// alert('over');     
					e.currentTarget.style.backgroundColor='white';
				}
				
				subject[i].onmouseout = function(e){
					e.currentTarget.style.backgroundColor='#ffffff';
				}
			}




		}         



		
		default_events();	
	</script>

	<script type="text/javascript">
		div_ltgt = document.querySelector('#div_ltgt');
		div_ltgt.innerHTML+=""
			+'<input id="ltlt" class="ltgt btn btn-secondary form-label" type="button" value="&lt&lt"  />'
			+'<input id="lt" class="ltgt btn btn-secondary form-label" type="button" value="&lt" />';
		
		// 안됨 수정해야됨.
		     
	    if(page_count == 1)	{
	    	var input = document.createElement("input")
			input.type="button";
			input.style.border="thin";
			input.value = 1;
			
		} else if(page_count > 1){
			for(var i = current_index+1; i <= page_count; i++){
				if(i % 11 == 0){	 
					     
				} else {
					
					/* 
					var input = document.createElement("input")
					input.type="button";
					input.style.border="thin"
					input.value=i; 
					input.style.width="1.5cm";  
					input.style.height="1.5cm"; 
					 
					input.className="list_button";  
					input.id="list_buttonmyid_" + i; 
					div_ltgt.appendChild(input); */  
					
					div_ltgt.innerHTML += '<input type=button id=list_buttonmyid_'+i+' class="list_button_class btn btn-secondary form-label"  value='+i+' onclick="javascript: page_change('+ i +');"/>'
					
				}
			}  
		}         
	    $('#list_buttonmyid_1').css('background-color','rgba(189,189,189,0.7)');
		
		function page_change(cur_index,pre_index){


			current_index=cur_index;  
			var tr_list_class=document.querySelectorAll(".tr_list_class");
			$('#list_buttonmyid_'+current_index).css('background-color', 'rgba(189,189,189,0.7)')
			
			for(var j=0;j < tr_list_class.length; j++){
				var parentElement = tr_list_class[j].parentElement;
				parentElement.removeChild(tr_list_class[j]);
			}    
			
			$('#list_buttonmyid_'+pre_index).css('background-color', 'white'); 
			 
			parentElement.innerHTML+=page[current_index-1] 
			default_events();
			
			//current_index=i-1; 
			remove_관리자()   
		}
		  
		function back_white_cur_index(){
			for(var i=0;i<page_count;i++){
				//list_button_class_1
				var list_button = document.querySelector('#list_button_class_'+i);
				list_button.style.backgroundColor='white';
				
			}
		}
		
		div_ltgt.innerHTML +='<input id="gt" class="ltgt btn btn-secondary form-label" type="button" value="&gt" />';
		div_ltgt.innerHTML +='<input id="gtgt" class="ltgt btn btn-secondary form-label" type="button" value="&gt&gt" />';
			
		$('#ltlt').on('click',function(click){
			if(page_count <= 10) page_change(1);
			else	page_change(page_count%90)
		});
		$('#lt').on('click',function(click){
			var pre_index=current_index;
			if(current_index != 1){
				current_index--;
				page_change(current_index);  
				
			}
			
			
		});
		$('#gtgt').on('click',function(click){
			if(page_count <= 10) page_change(page_count);
			else	page_change(page_count%10)
		});
		$('#gt').on('click',function(click){
			var pre_index=current_index;
			if(current_index == 0) current_index+=2;	
			else if(current_index > 0 && current_index <= page_count) current_index+=1;
			else {
				alert('마지막 페이지입니다.');
				return false;	
			} 
			page_change(current_index,pre_index);     
		});
		   
		
	
	</script>

	<script>

		function change_color_subject(){
			document.querySelectorAll('a[class*=a_class]').forEach((a)=>{
				if(a.style.cssText.match(/color.*#0076BE;/)){
					a.style.cssText = a.style.cssText.replace('color: #0076BE;','color: black;')
				}
				a.addEventListener('mouseover',(e)=>{
					e.currentTarget.style.cssText += 'color: #0076BE; font-weight: bold;'
				})

				a.addEventListener('mouseout',(e)=>{
					e.currentTarget.style.cssText += 'color: black;';
				})
			})
		}

		window.addEventListener('load',(e)=>{
			document.querySelectorAll('a[id*=amyid_]').forEach((a)=>{
				a.style.cssText='text-decoration: none; color: black; cursor: pointer;';
			})
			remove_관리자()

			change_color_subject()


			document.querySelectorAll(".list_button_class").forEach((input)=>{
				input.addEventListener("click",(e)=>{
					change_color_subject()


				})
			})
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
