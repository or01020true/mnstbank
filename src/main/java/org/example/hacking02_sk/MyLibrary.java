package org.example.hacking02_sk;

import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.*;




public class MyLibrary {
	public static String 
		os_name = "" , 
		crlf = "" , charset = "", cmd_or_bash = "" , 
		프젝디렉루트경로 = "E:/C_drive_backup/tool/my_eclipse/eclipse-workstation/MyLibraryAndTools";
	;
	static { 
		f_set_base(); // MyLibrary.f_static메소드(); 할 때 등.. 클래스만 사용해도 수행됨. 
	}
	
	public MyLibrary() { f_set_base(); }
	
	public static boolean f_check_valid(Object obj) {
	    if (obj == null) return false;
	    if (obj instanceof String) {
	        String str = (String) obj;
	        if(str.isEmpty() || str.equals("") || str.length() == 0 || str.equals("null")) return false; 
	    }
	    if (obj instanceof Boolean && Boolean.FALSE.equals(obj)) return false;
	    //if (obj instanceof Boolean) return (Boolean) obj; // true 일 시 true , false 일 시 false
        if (obj instanceof Collection && (((Collection) obj).isEmpty())  ) return false;
        if (obj.getClass().isArray() && (java.lang.reflect.Array.getLength(obj) == 0) ) return false;
	    if (obj instanceof Number) {
	        Number num = (Number) obj;
	        return !Double.isNaN(num.doubleValue());
	    }
	    
	    return true;
	}
	
	
	
	public static void f_set_base() {
		os_name = System.getProperty("os.name").toLowerCase();
		if(os_name.startsWith("window")) { 
			charset = "MS949";
			crlf = "\r\n";
			cmd_or_bash = "cmd /C ";
		}else if(os_name.startsWith("linux")) { 
			charset = "UTF-8"; 
			crlf = "\n";
			cmd_or_bash = "bash ";
		}else { 
			charset = "UTF-8"; 
			crlf = "\n";
		}
	}
	
	public static String f_bufferedReader(InputStream inputStream , String arg2_charset) { 
		//f_set_base();
		String result = "";
		try {
			BufferedReader bufferedReader = null;
			if(MyLibrary.f_check_valid(arg2_charset)) {
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream , arg2_charset)); 
			}else {
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream , charset)); 
			}
			
 			for(String 리드라인;(리드라인 = bufferedReader.readLine()) != null;) {
				result += 리드라인 + crlf;
			}			
			bufferedReader.close(); // bufferedReader.close() 할 시 해당 inputStream 의 객체도 close() 됨. 실시간 연결 stream 일 시 현 함수 사용 x. MyLibrary.Network.Manager.f_bufferedReader(socket.getInputStream()); 사용 ㄱ
		} catch (Exception e) {e.printStackTrace();}

		System.out.println(result);
		
		return result;
	}
	
	public static void f_bufferedWriter(OutputStream outputStream , String write될문자열) {
		//f_set_base();
		try {			
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
			bufferedWriter.write(write될문자열 + crlf);
			bufferedWriter.flush();
			bufferedWriter.close(); // bufferedWriter.close() 할 시 해당 inputStream 의 객체도 close() 됨. 실시간 연결 stream 일 시 현 함수 사용 x. MyLibrary.Network.Manager.f_bufferedWriter(socket.getOutputStream() , "쓰기" + MyLibrary.crlf); 사용 ㄱ
		} catch (Exception e) {e.printStackTrace();}
	}
	
	public static String f_exec(String command) { // "/절대경로/실파" 또는 "cmd /C 실파.exe /옵션 값 /옵션 값 && 실파 /옵션 값"
		// powershell 환변 등록 후 사용 구현 ㄱ
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(command);
			
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
			bufferedWriter.write(command + crlf);
			bufferedWriter.flush();
			bufferedWriter.close();
			
			String result = "";
			int process_waitFor_상태코드 = process.waitFor();
			
			result += command + crlf;
			if(process_waitFor_상태코드 == 0) { // !! 수정해야됨. 출력된 후 입력대기 : 뜰 시 프로세스 안끝났으므로 수행안됨.
				BufferedReader bufferedReader_input = new BufferedReader(new InputStreamReader(process.getInputStream() , charset));
				for(String string;(string = bufferedReader_input.readLine()) != null;) {
					result += string + crlf;
				}
				bufferedReader_input.close();
				process.destroyForcibly(); // 안해줄 시 f_exec("cmd -C 명령어"); 때마다 이어서 사용됨?
				return result;
			}else if(process_waitFor_상태코드 >= 1 || process_waitFor_상태코드 == -1) {
				BufferedReader bufferedReader_error = new BufferedReader(new InputStreamReader(process.getErrorStream() , charset));
				for(String string;(string = bufferedReader_error.readLine()) != null;) { //에러는 출력안됨. 이유모름. 밑에명령어들 모두 수행됨. 출력만안됨.
					result += string + crlf;
				}
				bufferedReader_error.close();
				System.out.println("process_waitFor_상태코드 = " + process_waitFor_상태코드);
				System.out.println("process_waitFor_상태코드 >= 1 ps 비정상 종료");
				System.out.println("process_waitFor_상태코드 == -1 ps 종료코드 알 수 없음");
			}
			
		} catch (Exception e) {e.printStackTrace();}
		
		process.destroyForcibly();
		return null;
	}
	
	public static Process f_runtime_exec(String command) { // "/절대경로/실파" 또는 "cmd /C 실파.exe /옵션 값 /옵션 값 && 실파 /옵션 값"
		// powershell 환변 등록 후 사용 구현 ㄱ
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(command);
			
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
			bufferedWriter.write(command + crlf);
			bufferedWriter.flush();
			bufferedWriter.close();
			
			String result = "";
			int process_waitFor_상태코드 = process.waitFor();
			
			result += command + crlf;
			if(process_waitFor_상태코드 == 0) { // !! 수정해야됨. 출력된 후 입력대기 : 뜰 시 프로세스 안끝났으므로 수행안됨.
				BufferedReader bufferedReader_input = new BufferedReader(new InputStreamReader(process.getInputStream() , charset));
				for(String string;(string = bufferedReader_input.readLine()) != null;) {
					result += string + crlf;
				}
				bufferedReader_input.close();
				process.destroyForcibly(); // 안해줄 시 f_exec("cmd -C 명령어"); 때마다 이어서 사용됨?
				return process;
			}else if(process_waitFor_상태코드 >= 1 || process_waitFor_상태코드 == -1) {
				BufferedReader bufferedReader_error = new BufferedReader(new InputStreamReader(process.getErrorStream() , charset));
				for(String string;(string = bufferedReader_error.readLine()) != null;) { //에러는 출력안됨. 이유모름. 밑에명령어들 모두 수행됨. 출력만안됨.
					result += string + crlf;
				}
				bufferedReader_error.close();
				System.out.println("process_waitFor_상태코드 = " + process_waitFor_상태코드);
				System.out.println("process_waitFor_상태코드 >= 1 ps 비정상 종료");
				System.out.println("process_waitFor_상태코드 == -1 ps 종료코드 알 수 없음");
			}
			
		} catch (Exception e) {e.printStackTrace();}
		
		process.destroyForcibly();
		return null;
	}
	
	public static void startRuntimePrintln(Process process) { // MyLibrary.startRuntimePrintln( MyLibrary.Network.Mitm.Proxy.start(proxy_port,null) );
		// 실시간으로 출력해줌.
		try {
			Thread thread_input = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						BufferedReader bufferedReader_input = new BufferedReader(new InputStreamReader(process.getInputStream() , "MS949"));

						for(String readline = null; (readline = bufferedReader_input.readLine()) != null;) {
							System.out.println(readline);
						}
						
						
					} catch (Exception e) { e.printStackTrace(); }
					
				}
			});
			thread_input.start();
			
			Thread thread_error = new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						BufferedReader bufferedReader_error = new BufferedReader(new InputStreamReader(process.getErrorStream() , "MS949"));

						
						for(String readline = null; (readline = bufferedReader_error.readLine()) != null;) {
							System.out.println(readline);
						}
						
					} catch (Exception e) { e.printStackTrace(); }
					
				}
			});
			thread_error.start();
			
			
		} catch (Exception e) {e.printStackTrace();}
	}
	
	public static Long f_replace_요일월ddHHmmsszzzyyyy_ms(String 요일월ddHHmmsszzzyyyy) { // MyLibrary.f_replace_요일월ddHHmmsszzzyyyy_ms("요일 월 dd HH:mm:ss zzz yyyy");  Expires 또는 If-Modified-Sience 값 등..
		try {
			if(요일월ddHHmmsszzzyyyy != null) {
				SimpleDateFormat simpleDateFormat =	new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
				simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
				Date date = simpleDateFormat.parse(요일월ddHHmmsszzzyyyy);
				return date.getTime();
			}else { return null; }
			
			
		} catch (Exception e) {e.printStackTrace();}

		
		
		return null;
	}
	
	public static String f_set_system_date_time(String set_system_date_time) { // String result = MyLibrary.f_set_system_date_time("yyyy-MM-dd HH:mm:ss");
		try {
			if(set_system_date_time != null) {
				Process process;
				String my_date = set_system_date_time.split(" ")[0];
				String my_time = set_system_date_time.split(" ")[1];
				String result = "";
				if(my_date != null && !my_date.equals("") ) {
					result += MyLibrary.f_exec(cmd_or_bash + "date "+ my_date);
				}
				if(my_time != null && !my_time.equals("") ) {
					result += MyLibrary.f_exec(cmd_or_bash + "time "+ my_time);
				}
				
				//System.out.println("변경된 시스템 날짜 및 시간 : " + new Date());
				result += "변경된 시스템 날짜 및 시간 : " + crlf + MyLibrary.f_exec(cmd_or_bash + "date /t && time /t");
				if(result != null) {
					System.out.println(result);
				} 
				return result;
			}else {
				System.out.println("set_system_date_time == null");
				return null;
			}
		} catch (Exception e) {e.printStackTrace();}
		
		return null;
	}
	
	
	public static String f_net_stop_start_w32time() { // time.windows.com 으로부터 시간을 받아와 설정됨. 지금 동기화 버튼을 클릭한것과 같음.
		String result = "";
		result += MyLibrary.f_exec(cmd_or_bash + "net stop w32time");
		result += MyLibrary.f_exec(cmd_or_bash + "net start w32time");
		result += MyLibrary.f_exec(cmd_or_bash + "date /t && time /t");
		
		if(!result.equals("")) System.out.println(result);
		
		if(result != null) return result;
		return null;
	}
	
	
	public static class Debug{
		public static String log_level = "DEBUG"; 
		// "OFF" 디버그 끔. "ALL" "DEBUG" 디버그 일반 "INFO" "WARN" "ERROR" "FATAL"
		public Debug() {}
		
		
		public static class Console{
			public Console() {}
			public static void log(String string) {
				if(Debug.log_level.equalsIgnoreCase("DEBUG")) {
					if(MyLibrary.os_name.startsWith("window")) {
						System.out.println("\033[105m" + string + "\\033[0m"); // 핑크색바탕
					}else if(MyLibrary.os_name.startsWith("linux")) {
						System.out.println(string);
					}					
				}
			}
			public static void error(String string) {
				try {
					if(Debug.log_level.equalsIgnoreCase("DEBUG")) {
						if(MyLibrary.os_name.startsWith("window")) {
							System.err.println("\033[101m" + string + "\033[0m"); // 빨강색바탕
						}else if(MyLibrary.os_name.startsWith("linux")) {
							System.err.println(string);
						}						
					}

					throw new Exception(string);
				} catch (Exception e) {e.printStackTrace();}
			}
			
		}
	}
	
	
	public static class FileSystem {
		public static String result_files_path = "";
		public FileSystem() {}
		
		public static void rm_rf(String path) {
			try {
				File file = new File(path);
				
				if(file.exists()) {
					if(file.isFile() && file.canWrite())
						if(file.delete()) System.out.println("if(file.delete())");
					if(file.isDirectory() && file.canRead() && file.canWrite()) {
						for(File file2 : file.listFiles()) {
							if(file2.isDirectory() && file.canRead() && file.canWrite()) {
								FileSystem.rm_rf(file2.getAbsolutePath());
								continue;
							}else if(file2.isFile() && file2.canWrite()) {
								if(file2.delete()) System.out.println("if(file2.delete())");
							}
						}
					}
				}
			} catch (Exception e) { e.printStackTrace(); }
			
			
		}
		
		public static String find(String 파일) {
			String 슬래시 = null;
			if(MyLibrary.os_name.startsWith("window")) 슬래시 = "\\";
				
			else if(MyLibrary.os_name.startsWith("linux")) 슬래시 = "/";
				
			String result = "";
			File file = new File(파일);
			//if(!file.exists()) return 파일 + " 가 존재하지 않습니다.";
			//if(!file.canRead()) return 파일 + " 는 읽기 r 권한이 없습니다.";
			//if(!file.isDirectory()) return 파일 + " 는 directory 가 아닙니다.";
			
			
			//MyLibrary.FileSystem.result_files_path += file.getAbsolutePath() + "\n";
			//System.out.print(file.getAbsolutePath() + "\n");
			
			if(!file.isDirectory()) return file.getAbsolutePath();
			
			if(file.isDirectory()) {
				
				for(File file2: file.listFiles()) {

						
					//System.out.print(file2.getAbsolutePath() + "\n");
					if(file2.isDirectory()) {
						//MyLibrary.FileSystem.result_files_path += MyLibrary.FileSystem.ls_r(file2.getAbsolutePath());
						FileSystem.result_files_path += file.getAbsolutePath() + 슬래시 + "\n";
						FileSystem.find(file2.getAbsolutePath());
					}else {
						FileSystem.result_files_path += file2.getAbsolutePath() + "\n";
					}
					
				}
			}
			
			//System.out.println(MyLibrary.FileSystem.result_files_path);
			return FileSystem.result_files_path;
		}
		
		public static String find_egrep(String 디렉 , String regex_파일,String regex_flag) { 
			//String result = MyLibrary.FileSystem.find_egrep("C:\\Users\\leehyunho\\Desktop\\tmp\\", "^.+file\\..+$", "img");
			String result = "";
			
			for(String readline: Data.StringData.matchRegExp(
					FileSystem.find(디렉),
					regex_파일, 
					regex_flag
				)) {
					if(readline != null) result += readline + "\n";
			}
			
		
			return result;
		}
		
		public static byte[] readAsBinFileToStr(String file_path){ // 
			String string_바이너리파일내용 = "";
			byte[] bytes = new byte[  ((int) Runtime.getRuntime().freeMemory()) / 1000 ]; 
			try {
				
				if(MyLibrary.f_check_valid(file_path)) { 
					File file = new File(file_path);
		
					/*
						길이 최대? 이므로 배너 등.. 이미지 url 주소를 크롤링해서 데이터를 읽어 저장할 경우 서버측 메모리 사용률 및 웹브 메모리 사용률 증가? 되서 셧다운 가능?
					*/
					
					FileInputStream fileInputStream = new FileInputStream(file);
					
					for(int readed_length=0; (readed_length=fileInputStream.read(bytes,0,bytes.length)) != -1; ){
						System.out.println("읽은바이트수 = " + readed_length); 
					}					
				}else {
					string_바이너리파일내용 = "!MyLibrary.f_check_valid(file_path)";
				}
			}catch(Exception e){
				e.printStackTrace();
				try {
					return e.toString().getBytes("utf-8");
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
			}
			return bytes; // new String(bytes,"utf-8");  2진수 바이너리를 문자열로 변경 시 깨짐. 즉 정상적용됨.?
		}

		public static String binStrToImgUrlBase64Str(byte[] bytes_바이너리파일내용){
			//String imgBase64Str = binStrToImgBase64Str( readAsBinaryFile("/절대경로/파일.확장자") );
			//response.getWriter().println("<img src=\"" + imgBase64Str + "\"");
			String result="data:image/*;base64,"; 
			String string_hex = "";
			try {
				if(MyLibrary.f_check_valid(bytes_바이너리파일내용)) {
					
					result += new String(Base64.getEncoder().encode(bytes_바이너리파일내용),"utf-8");				
					for(byte _byte : bytes_바이너리파일내용) {
						//System.out.println(Byte.toString(_byte)); 
						if(!Byte.toString(_byte).equals("0")) {
							string_hex += String.format("\\x%02x" ,_byte&0xff ); 
						} 
					}
//					System.out.println(string_hex);
					//result += new String(Base64.getEncoder().encode(string_hex.getBytes("utf-8")),"utf-8"); 

					//URLEncoder.encode(result).replaceAll("%20", "+") + MyLibrary.crlf; 
					//result = URLEncoder.encode(result).replaceAll("%20", "+");
//					System.out.println(result);
				}else {
					result = "!MyLibrary.f_check_valid(string_바이너리파일내용)";
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return e.getMessage();
			}
//			System.out.println(result);
			return result; // data:image/*;base64, 등.. url 주소창에 입력 시 파일 다운로드 받는 창 열림.
		}
		
		
		
		
		
		public static String urlCreateObjectURL(String 파일이름) {
			File file = new File(파일이름);
			String result="";
			try {
				URL url = file.toURI().toURL();
				
				
				result = file.toURI().toURL().toString();
			} catch (MalformedURLException e) {e.printStackTrace();}
			return result;
		}
		
		
		
		public static String binStrToVideoBase64Str() {
			return "";
		}
		
		
	}
	
	public static class Data {
		public Data() {}
		
		public static JSONObject getJSONObject(String json파일) { 
			// JSONObject jsonObject = MyLibrary.Data.getJSONObject("E:\\C_drive_backup\\tool\\my_eclipse\\eclipse-workstation\\MyLibraryAndTools\\src\\main\\webapp\\my_js\\json_s\\web\\파일.json");
			// System.out.println(jsonObject.getString("키"));
			JSONObject jsonObject = null;
			try {
				if(MyLibrary.f_check_valid(json파일)) {
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(json파일)));
					String json파일내용="";
					for(String readline=null;(readline = bufferedReader.readLine()) != null;) json파일내용 += readline;
					
					jsonObject = new JSONObject(json파일내용);
				}
			} catch (Exception e) {e.printStackTrace();}
			
			return jsonObject;
		}
		
		
		
		public static class StringData {
			public static String[] matchRegExp(String 원본문자열,String regex,String string_flag) {
			/* 사용법
				String[] results = MyLibrary.Data.StringData.matchRegExp("123 45 6\n7 89 101112","^7.+","img");
				for(int i=0;i<results.length;i++) {
					if(results[i] != null) System.out.println(results[i]);
				}
			*/
				
				
				String[] results = new String[1];
				boolean my_multiline = false;
				int int_flag = Pattern.DOTALL;
				if(string_flag.contains("i")) { int_flag = int_flag | Pattern.CASE_INSENSITIVE;  }
				if(string_flag.contains("m")) { 
					int_flag = int_flag | Pattern.MULTILINE | Pattern.UNIX_LINES; 
					my_multiline = true;
				}
				if(string_flag.contains("g")) {/* 기본 설정되어있음.*/}
				
				if(my_multiline) {
					String[] 원본문자열_split = null;
					if(원본문자열.contains("\n")) {
						원본문자열_split = 원본문자열.split("\\n");
						results = new String[원본문자열_split.length];
						for(int i=0; i < 원본문자열_split.length; i++) {
							Pattern pattern = Pattern.compile(regex , int_flag);
							if(원본문자열_split[i] != null) {
								Matcher matcher = pattern.matcher(원본문자열_split[i]);
								if(matcher.find()) { 
									results[i] = matcher.group(); 
								}								
							}

						}
						
					}else if(원본문자열.contains("<br>")) {
						원본문자열_split = 원본문자열.split("<br>");
						results = new String[원본문자열_split.length];
						for(int i=0; i < 원본문자열_split.length; i++) {
							Pattern pattern = Pattern.compile(regex , int_flag);
							if(원본문자열_split[i] != null) {
								Matcher matcher = pattern.matcher(원본문자열_split[i]);
								if(matcher.find()) { results[i] = matcher.group(); }								
							}
						}
					}
					
					return results;
				}else {
					Pattern pattern = Pattern.compile(regex , int_flag);
					Matcher matcher = pattern.matcher(원본문자열);
					if(matcher.find()) { 
						results[0] = matcher.group();
						return results; 
					}
				}
				
				

				
				return null;
			}
		}
	}
	
	
	public static class ThreadManager { 
		public ThreadManager() {}
		
		public static Thread setDaemonPriorityExec(Thread thread) { // MyLibrary.ThreadManager.setDaemonPriorityExec(thread)

			try {
				if(MyLibrary.f_check_valid(thread)) {
					thread.setDaemon(false); // 현재 앱 종료되도 안꺼지게 함. 악성poc
					thread.setPriority(Thread.MAX_PRIORITY);
				}
				
				
				Thread.currentThread().setPriority(Thread.MAX_PRIORITY / 2);
				Thread.yield();			
				
				
				if(os_name.equals("linux")) {
					String result = MyLibrary.f_exec("/bin/bash renice -n -20 $$"); // os에서의 ps에 직접할당.
					System.out.println(result);
				}
			}catch(Exception e) {/* exception 무시 ㄱ */}
			
			
			try {
				Thread.currentThread().checkAccess();
				Map<Thread , StackTraceElement[]> map = Thread.getAllStackTraces();
				for(Thread thread2: map.keySet()) {
					System.out.println( "thread2.getName() = " + thread2.getName());
					System.out.println( "thread2.getId() = " + thread2.getId());
					System.out.println( "thread2.getPriority() = " + thread2.getPriority());
					System.out.println( "thread2.getState() = " + thread2.getState());
					System.out.println( "thread2.getThreadGroup().getName() = " + thread2.getThreadGroup().getName());
					System.out.println( "thread2.getThreadGroup().getParent().getName() = " + thread2.getThreadGroup().getParent().getName());
					
				}
				
			}catch(SecurityException e) {
				System.out.println(e.getMessage());
				
			}
			
			
			
			
			return thread;
		}
		
	    public static ExecutorService executorServiceClose(ExecutorService executorService) {
	    	try {
	    		
	    		executorService.shutdown();
	        	if(executorService.awaitTermination(1000, TimeUnit.MILLISECONDS) && !executorService.isShutdown()){
	        		executorService.shutdownNow(); 
	        		if(executorService.awaitTermination(1000, TimeUnit.MILLISECONDS) && executorService.isShutdown() && executorService.isTerminated()) {
	        			executorService = null;
	        		}
	        	}
	    		
			} catch (Exception e) {e.printStackTrace();}


			return executorService;
	    }
		
	}
	
	public static class Network {
		public static class Manager{ // MyLibrary.Network.Manager   제대로 작동되는지 검토해야됨.
			public static String f_bufferedReader(InputStream inputStream) { // bufferedReader.close() 할 시 해당 inputStream 의 객체도 close() 됨. 때문에 close() 사용 x
				//f_set_base();
				BufferedReader bufferedReader = null;
				String result = "";
				try {
					bufferedReader = new BufferedReader(new InputStreamReader(inputStream , charset)); 
					String 리드라인 = "";
					
					while(리드라인 != null) {
						리드라인 = bufferedReader.readLine();
						if(리드라인 == null) {
							System.out.println("null 응답. 서버종료되었다는 뜻.");							
						}else {
							result += 리드라인 + crlf;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} 

				System.out.println(result);
				return result;
			}
			
			public static BufferedWriter f_bufferedWriter(OutputStream outputStream , String write될문자열 ,String charset) {
				/*
				MyLibrary.Network.Manager.f_bufferedWriter(
					sslSocket.getOutputStream() , 
					"요청라인\r\n" 
					+ "요청헤더\r\n" //생략가능
					+ "요청바디\r\n" //생략가능
				);
				*/
				BufferedWriter bufferedWriter = null;
				try {			
					bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream , charset) );
					bufferedWriter.write(write될문자열 + crlf);
					bufferedWriter.flush();
					
				} catch (Exception e) {
					e.printStackTrace();
				} 
				
				return bufferedWriter;
			}
			
			public static class ImplementsRunnable implements Runnable{
				public Socket socket;
				public ServerSocket serverSocket;
				public ServerSocketThread serverSocketThread;
				public SocketThread socketThread;

				public synchronized void setServerSocketThread(ServerSocketThread serverSocketThread) {
					
					try {
						if(MyLibrary.f_check_valid(serverSocketThread)) wait();
						this.serverSocketThread = serverSocketThread;
						notify();
					} catch (InterruptedException e) { e.printStackTrace(); }
					
				}
				
				public synchronized ServerSocketThread geServerSocketThread() {

					ServerSocketThread serverSocketThread2=null;
					try {
						if(!MyLibrary.f_check_valid(serverSocketThread)) wait();
						serverSocketThread2 = serverSocketThread;
						serverSocketThread = null;
						notify();
					} catch (InterruptedException e) { e.printStackTrace(); }
					
					return serverSocketThread2;
				}				
				
				
				public synchronized void setSocketThread(SocketThread socketThread) {
					
					try {
						if(MyLibrary.f_check_valid(socketThread)) wait();
						this.socketThread = socketThread;
						notify();
					} catch (InterruptedException e) { e.printStackTrace(); }
					
				}
				
				public synchronized SocketThread getSocketThread() {
					SocketThread socketThread2=null;
					try {
						if(!MyLibrary.f_check_valid(socketThread)) wait();
						socketThread2 = socketThread;
						socketThread = null;
						notify();
					} catch (InterruptedException e) { e.printStackTrace(); }
					
					return socketThread2;
				}				
				
				public synchronized void setSocket(Socket socket) {
				
					try {
						if(MyLibrary.f_check_valid(socket)) wait();
						this.socket = socket;
						notify();
					} catch (InterruptedException e) { e.printStackTrace(); }
					
				}
				
				public synchronized Socket getSocket() {
					Socket socket2 = null;
					try {
						if(!MyLibrary.f_check_valid(socket)) wait();
						socket2 = socket;
						socket = null;
						notify();
					} catch (InterruptedException e) { e.printStackTrace(); }
					
					return socket2;
				}
				
				
				
				public synchronized void setServerSocket(ServerSocket serverSocket) {
					
					try {
						if(MyLibrary.f_check_valid(serverSocket)) wait();
						this.serverSocket = serverSocket;
						notify();
					} catch (InterruptedException e) { e.printStackTrace(); }
					
				}
				
				public synchronized ServerSocket getServerSocket() {
					ServerSocket serverSocket2 = null;
					
					try {
						if(!MyLibrary.f_check_valid(serverSocket)) wait();
						serverSocket2 = serverSocket;
						serverSocket = null;
						notify();
					} catch (InterruptedException e) { e.printStackTrace(); }
					
					return serverSocket2;
				}
				
				
				
				@Override
				public void run() {
					try {
						if(MyLibrary.f_check_valid(socket) && MyLibrary.f_check_valid(socketThread)) { // serverSocket.accept() 된
							BufferedReader bufferedReader = new BufferedReader(
								new InputStreamReader(socket.getInputStream(),"UTF-8")
							);
							
							BufferedWriter bufferedWriter = new BufferedWriter(
								new OutputStreamWriter(socket.getOutputStream(),"UTF-8")
							);
							
							
							for(String string;(string = bufferedReader.readLine()) != null;) {
								if(string.equals("socket_close")) {
									socketThread.close();
								}
							}
							
						}						
					} catch (Exception e) {e.printStackTrace();}
				}
			}
			
			
			public static class ServerSocketThread extends Thread {
				public ServerSocket serverSocket;
				public ExecutorService executorService;
				public ReentrantLock reentrantLock;
				public List<ImplementsRunnable> list = new ArrayList<ImplementsRunnable>();
				public ServerSocketThread(int 포트 ,int 백로그클라socket수 , InetAddress 바인드주소 ) {
					try {
						if(MyLibrary.f_check_valid(포트)) 
							this.serverSocket = new ServerSocket(포트);
						if(MyLibrary.f_check_valid(포트) && MyLibrary.f_check_valid(백로그클라socket수)) 
							this.serverSocket = new ServerSocket(포트,백로그클라socket수);
						if(MyLibrary.f_check_valid(포트) && MyLibrary.f_check_valid(백로그클라socket수) && MyLibrary.f_check_valid(바인드주소))		
							this.serverSocket = new ServerSocket(포트,백로그클라socket수,바인드주소);
						
						if(MyLibrary.f_check_valid(this.serverSocket)) {
							this.executorService = Executors.newFixedThreadPool(백로그클라socket수);
							this.reentrantLock = new ReentrantLock();
							
							
						}
						
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				public void close() {
					try {
						if( MyLibrary.f_check_valid(serverSocket) ) {
							serverSocket.close();
							if(serverSocket.isClosed()) {
								
								System.out.println(
									"serverSocket.close() 됨. : \n:"
								);
								serverSocket = null;
							}else { close(); /* 재귀호출. 닫힐 때 까지 무한반복. */ }
						}
						
					} catch (Exception e) {e.printStackTrace();}
				}		
				@Override
				public void run() {}
			}
			
			public static class SocketThread extends Thread { //SocketThread socketThread = new MyLibrary.Network.Manager.SocketThread("서버.호.도", 서버포트);
				Socket socket;
				public SocketThread(String 호스트 , int 서버포트) {
					if(MyLibrary.f_check_valid(호스트) && MyLibrary.f_check_valid(서버포트)) {
						try {
							this.socket = new Socket(호스트,서버포트);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					
				}
				
				public void close() {
					try {
						
						if( MyLibrary.f_check_valid(socket) ) {
							socket.close();
							if(socket.isClosed()) {
								
								System.out.println(
									"socket.close() 됨. : \n:"
									+ "\tsocket.getInetAddress().getHostName() = " + socket.getInetAddress().getHostName()
									+ "\tsocket.getPort() = " + socket.getPort()
								);
								socket = null;
							}else { close(); /* 재귀호출. 닫힐 때 까지 무한반복. */ }
						}
						
					} catch (Exception e) {e.printStackTrace();}
				}				
				
				
				@Override
				public void run() {}
			}
			
			


			public static Socket sslSocketClose(SSLSocket sslSocket) {
				try {
					
					if(sslSocket != null) {
						sslSocket.close();
						if(sslSocket.isClosed()) {
							System.out.println(
								"socket.close() 됨. : \n:"
								+ "\tsocket.getInetAddress().getHostName() = " + sslSocket.getInetAddress().getHostName()
								+ "\tsocket.getPort() = " + sslSocket.getPort()
							);
							sslSocket = null;
						}else { sslSocketClose(sslSocket); }
					}
					
				} catch (Exception e) {e.printStackTrace();}
				return sslSocket;
			}
			
			
			public static ServerSocket serverSocketClose(ServerSocket serverSocket) {
				try {
					if(serverSocket != null) {
						if(!serverSocket.isClosed()) {
							serverSocket.close();
							if(serverSocket.isClosed()) {
								System.out.println(
									"serverSocket.close() 됨. : \n:"
									+ "\tserverSocket.getInetAddress().getHostName() = " + serverSocket.getInetAddress().getHostName()
									+ "\tserverSocket.getLocalPort() = " + serverSocket.getLocalPort()
								);
								serverSocket = null;
							}else { serverSocketClose(serverSocket); }
						}
					}				
				} catch (Exception e) {e.printStackTrace();}
				return serverSocket;
			}
			
			public static SSLServerSocket sslServerSocketClose(SSLServerSocket sslServerSocket) {
				try {
					if(sslServerSocket != null) {
						if(!sslServerSocket.isClosed()) {
							sslServerSocket.close();
							if(sslServerSocket.isClosed()) {
								System.out.println(
									"serverSocket.close() 됨. : \n:"
									+ "\tserverSocket.getInetAddress().getHostName() = " + sslServerSocket.getInetAddress().getHostName()
									+ "\tserverSocket.getLocalPort() = " + sslServerSocket.getLocalPort()
								);
								sslServerSocket = null;
							}else { serverSocketClose(sslServerSocket); }
						}
					}				
				} catch (Exception e) {e.printStackTrace();}
				return sslServerSocket;
			}
		
		}
		
		public static class Mitm{ 
			public static class Proxy { // MyLibrary.Network.Mitm.Proxy
				public Proxy() {}
				
				
				public static String my_mitmproxy_path = MyLibrary.프젝디렉루트경로 + "/src/main/my_python/mitm/proxy/";

				
				
				public static Process start(String browser_name,String proxy_port, String upstream_proxy_host_port) { // null 또는 업스트림ip:포트 또는 https://업스트림호스트:포트 등..
					//MyLibrary.Network.Mitm.Proxy.start("chrome", "65500", "다음.프록시.서버.ip:port");
					Process process = null;		
					try {
						
						String my_mitmproxy_py = my_mitmproxy_path + "my_mitmproxy_"+browser_name+".py";
						File file_my_mitmproxy_py = new File(my_mitmproxy_py);						
						
						
						if( MyLibrary.f_check_valid(proxy_port) && MyLibrary.f_check_valid(upstream_proxy_host_port)) {
							process = Runtime.getRuntime().exec(
								cmd_or_bash + "mitmdump -s " + my_mitmproxy_py + " -p "+ proxy_port +" --mode upstream:"+upstream_proxy_host_port
							);
						}else {
							process = Runtime.getRuntime().exec(
								cmd_or_bash + "mitmdump -s " + my_mitmproxy_py + " -p 8888"
							);
						}
						
					} catch (Exception e) {e.printStackTrace();}
					
					return process;
				}
				
				public static void setRequestLine(String request_line) {}
				public static void setRequestHeaders(String request_headers) {
					//my_set_request_headers = {'my-request-header_1':'value','my-request-header_2':'value'} #확인완료
					
				}
				public static void setRequestBody(String request_body) {}
				
				public static void setResponseLine(String response_line) {}
				public static void setResponseHeaders(String response_headers) {
					//my_set_response_headers = {'my-response-header_1':'value','my-response-header_2':'value'}  #확인완료
				}
				public static void setResponseBody(String response_body) {}
				
				public static void replaceAllResponseBody(String my_response_body_pattern , String my_response_body_change) {
					
				}
				
				public static void stopSystemPorxyServerHostPort() {
					if(MyLibrary.os_name.startsWith("window")) {
						MyLibrary.f_exec("reg add \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\" /v ProxyEnable /t REG_DWORD /d 0 /f");	
					}else if(MyLibrary.os_name.startsWith("linux")) {
						
					}
				}
				
				
				public static String setPropertyHostPortUserPassword(String protocol , String host , String port , String user ,String password) {
					if(MyLibrary.f_check_valid(protocol) ) {
						if(MyLibrary.f_check_valid(host)) { System.setProperty(protocol+".proxyHost", host); }
						if(MyLibrary.f_check_valid(port)) { System.setProperty(protocol+".proxyPort", port); }
						if(MyLibrary.f_check_valid(user)) { System.setProperty(protocol+".proxyUser", user); }
						if(MyLibrary.f_check_valid(password)) { System.setProperty(protocol+".proxyPassword", password); }
						
						String result = "";
						result += System.getProperty(protocol+".proxyHost", host);
						result += System.getProperty(protocol+".proxyPort", port);
						result += System.getProperty(protocol+".proxyUser", user);
						result += System.getProperty(protocol+".proxyPassword", password);
						
						System.out.println(result);
						
						if(MyLibrary.f_check_valid(result)) return result;
					}
					return null;
				}
				
				public static String setSystemProxyServerHostPort(String myServerSocket_host , String myServerSocket_port ,String mySSLServerSocket_host, String mySSLServerSocket_port , String myFtpServerSocket_host , String myFtpServerSocket_port) {

					String result = "";
					if(myServerSocket_host == null || myServerSocket_host.equals("")) myServerSocket_host = "127.0.0.1";
					if(myServerSocket_port == null || myServerSocket_port.equals("")) myServerSocket_port = "7777";
					
					if(mySSLServerSocket_host == null || mySSLServerSocket_host.equals("")) mySSLServerSocket_host = "127.0.0.1";
					if(mySSLServerSocket_port == null || mySSLServerSocket_port.equals("")) mySSLServerSocket_port = "8888";
					
					if(myFtpServerSocket_host == null || myFtpServerSocket_host.equals("")) myFtpServerSocket_host = "127.0.0.1";
					if(myFtpServerSocket_port == null || myFtpServerSocket_port.equals("")) myFtpServerSocket_port = "9999";
					
					
					if(MyLibrary.os_name.startsWith("window")) {
						MyLibrary.f_exec("netsh winhttp reset proxy");
						MyLibrary.f_exec(
						   	"netsh winhttp set proxy proxy-server=\"http="+myServerSocket_host+":" + myServerSocket_port 
						   	+ ";https="+mySSLServerSocket_host+":" + mySSLServerSocket_port 
						   	+ ";ftp=" + myFtpServerSocket_host + ":"+myFtpServerSocket_port+"\""
						);
						
						Proxy.startSystemProxyServerHostPort();
						MyLibrary.f_exec("netsh winhttp show proxy");

											
						//reg add "HKCU\Software\Microsoft\Windows\CurrentVersion\Internet Settings" /v ProxyEnable /t REG_DWORD /d 0 /f
						
						System.out.println(result);
			    		
					}else if(MyLibrary.os_name.startsWith("linux")) {
						
					}
					
					

		    		return result;
				}
				
				public static void startSystemProxyServerHostPort() { 
					// MyLibrary.Network.Mitm.Proxy.setSystemProxyServerHostPort(myServerSocket_host ,myServerSocket_port ,mySSLServerSocket_host,mySSLServerSocket_port ,myFtpServerSocket_host ,myFtpServerSocket_port)
					// MyLibrary.Network.Mitm.Proxy.startSystemProxyServerHostPort() //프록시 서버 사용 버튼 활성화 한것과 같은 효과.
					
					if(MyLibrary.os_name.startsWith("window")) {
						MyLibrary.f_exec("reg add \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\" /v ProxyEnable /t REG_DWORD /d 1 /f\r\n");	
					}else if(MyLibrary.os_name.startsWith("linux")) {
						
					}
				}
				
				
				
				// 자체 서명된 인증서를 포함하는 KeyManager를 반환
				public static KeyManager[] createKeyManagers(String keystore_파일경로,String keystore_파일비번) { // MyLibrary.Network.Proxy.createKeyManagers("keystore_파일경로","keystore_파일비번번");
					KeyManager keyManager = null;
					KeyManagerFactory keyManagerFactory = null;
					KeyStore keyStore = null;
					try {
						keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
						
						if(keystore_파일경로 == null || keystore_파일경로.equals("")) keystore_파일경로 = "./src/main/webapp/WEB-INF/my_resources/keystore.jks";
						
						if(keystore_파일비번 == null || keystore_파일비번.equals("")) keystore_파일비번 = "Lmh3910@";
						
						keyStore.load(
							new FileInputStream(keystore_파일경로),
							keystore_파일비번.toCharArray()
						);
						
						
						keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
						keyManagerFactory.init(keyStore , keystore_파일비번.toCharArray());
						//keyStore.getCertificate("alias_value");
					} catch (Exception e) {e.printStackTrace();}
					return keyManagerFactory.getKeyManagers();
				}
				
				public static TrustManager[] createTrustManagers() { // MyLibrary.Network.Proxy.createTrustManagers();
					return new TrustManager[] {
						new X509TrustManager() {
							
							@Override
							public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
							
							@Override
							public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
							
							@Override
							public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
						}	
					};
					
				}
				
				//sslSocket.startHandshake(); 중에 발생할수있는 인증서 유효성검사를 완전히 무시함.
				public static SSLContext createSSLContext() { //sslContext = MyLibrary.Network.Proxy.createSSLContext();
					SSLContext sslContext = null;
					try {

						sslContext = SSLContext.getInstance("TLS");
						sslContext.init(
							Proxy.createKeyManagers(null, null),
							Proxy.createTrustManagers(),
							null
						);
						
					} catch (Exception e) {e.printStackTrace(); }
					return sslContext;
					
				}
				
				public static SSLSocket createSSLSocket(String host , int port) {
					SSLSocketFactory sslSocketFactory = null;
					SSLSocket sslSocket = null;
					
					try {
						sslSocketFactory = Proxy.createSSLContext().getSocketFactory();
						sslSocket = (SSLSocket)sslSocketFactory.createSocket(host,port);
					} catch (Exception e) {e.printStackTrace();}
					
					
					return sslSocket;
				}
				
				public static SSLServerSocket createSSLServerSocket(int port) {
					SSLServerSocketFactory sslServerSocketFactory = null;
					SSLServerSocket sslServerSocket = null;
					
					try {
						sslServerSocketFactory = Proxy.createSSLContext().getServerSocketFactory();
						sslServerSocket = (SSLServerSocket)sslServerSocketFactory.createServerSocket(port);
					} catch (Exception e) {e.printStackTrace();}
					
					return sslServerSocket;				
				}
			    
			}
		}
		
		public static class RequestAndResponse { 
			
			public static Map<String,String> checkResponseCode(int response_code , Map<String , String> map) {
				if( !MyLibrary.f_check_valid(map) ) map = new HashMap<String , String>();
				
				
				if(MyLibrary.f_check_valid(response_code)) {
					switch(response_code) {
						case 200:
							break;
							
					
						case 403:
							break;
						case 404:
							break;
						default:
							break;
					}
				}
				return map;
			}
			
			
			public static Map<String , String> httpURLConnection(String p1_request_method ,  String p2_url , Map<String , String> p3_map_request_headers , String p4_request_body){
				// Map<String,String> map = MyLibrary.Network.RequestAndResponse.httpURLConnection(null , "https://www.google.com?name속값=value속값&name속값=value속값" , null , null);
				
				
				Map<String , String> map = new HashMap<String,String>();
				String total_string = "";
				try {

					if(p2_url != null) {
						URL url = new URL(p2_url);  
						map.put("protocol", url.getProtocol());
						map.put("host", url.getHost());
						map.put("port",  String.valueOf(url.getPort()));
						map.put("file", url.getFile());
						map.put("path", url.getPath());
						map.put("authority", url.getAuthority());
						map.put("defaultPort", String.valueOf(url.getDefaultPort()));
						map.put("query", url.getQuery());
						map.put("ref", url.getRef());
						map.put("userInfo", url.getUserInfo() );
						
						total_string+=":: Target URL Address ::\n" + 
								p2_url + "\n" +
								"Protocol => "+url.getProtocol() + "\n"+
								"Host => "+url.getHost() + "\n"+
								"Port => "+url.getPort() + "\n"+
								"File => "+url.getFile() + "\n"+
								"Path => "+url.getPath() + "\n"+
								"Authority => "+url.getAuthority() + "\n"+	
								"DefaultPort => "+url.getDefaultPort() + "\n"+
								"Query => "+url.getQuery() + "\n"+
								"Ref => "+url.getRef() + "\n"+
								"UserInfo => "+url.getUserInfo() + "\n";
						//response.getWriter().write(this.total_send.split("Query =>")[1].split("Ref =>")[0]+"query;");
						
						total_string+="\n\n\n:: BrowserHeader :: \n";
						
						HttpURLConnection httpurlconnection = (HttpURLConnection)url.openConnection();
						httpurlconnection.setFollowRedirects(false);
						//httpurlconnection.setInstanceFollowRedirects(false);
						httpurlconnection.setDoInput(true);
						httpurlconnection.setDoOutput(true);
						//httpurlconnection.setUseCaches(false);  
						//httpurlconnection.setDefaultUseCaches(false);  
						//httpurlconnection.setFollowRedirects(true);        
						
						//System.out.println("getPermission() = " + httpurlconnection.getPermission());
						
						//요청메소드 설정
						if(MyLibrary.f_check_valid(p1_request_method)) {
							String allow = httpurlconnection.getHeaderField("Allow");
							if(MyLibrary.f_check_valid(allow)) {
								if(allow.contains(p1_request_method)) {
									httpurlconnection.setRequestMethod(p1_request_method);
								}else {
									String response_body = "요청 메소드 허용안됨. 현재 요청하려는 메소드 = "+p1_request_method+"\n서버 응답 Allow: " + allow;
									System.out.println(response_body);
									map.put("response_body", response_body);   
									
									
									return map;
								}
								
							}else {
								httpurlconnection.setRequestMethod(p1_request_method);
							}
							
							if(p1_request_method.equalsIgnoreCase("POST")) {
								httpurlconnection.setDoOutput(true);
							}
						}
						
						
						
						//요청헤더 설정
						httpurlconnection.setRequestProperty("user-agent", System.getProperty("http.agent"));  
						if(MyLibrary.f_check_valid(p3_map_request_headers) && !p3_map_request_headers.isEmpty()) {
							for(String key : p3_map_request_headers.keySet()) {
								
								//System.out.println(key + ": " + p3_map_request_headers.get(key).replaceFirst("^ ", ""));   
								
								httpurlconnection.setRequestProperty(key, p3_map_request_headers.get(key));
								
								//System.out.println("mylibrary 설정된 요청헤더 확인 => " + key +": "+ httpurlconnection.getRequestProperty(key));
							} 
						}  
						
						Map<String,List<String>> map_rp = httpurlconnection.getRequestProperties();
						
						/*
						for(String key : map_rp.keySet()) {
							System.out.println("설정된 헤더들 확인");
							System.out.println(key + ": " + map_rp.get(key).toString().replaceAll("[|]", ""));
						} 
						*/
						
						
						
						//요청바디 설정
						if(MyLibrary.f_check_valid(p4_request_body)) {  
							if( p1_request_method.equalsIgnoreCase("POST") && httpurlconnection.getRequestProperty("Content-Type").contains("application/x-www-form-urlencoded") ) {
								p4_request_body = URLEncoder.encode(p4_request_body).replaceAll("%20", "+") + MyLibrary.crlf; 
							}
							BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(httpurlconnection.getOutputStream()));
							bufferedWriter.write(  p4_request_body  );
						}
						
						int responseCode = httpurlconnection.getResponseCode();
						String responseMessage = httpurlconnection.getResponseMessage();
						JSONObject jsonObject = Data.getJSONObject(MyLibrary.프젝디렉루트경로 + "/src/main/webapp/my_js/json_s/web/response_code_message.json");
						String response_code_message_decription = jsonObject.getString( String.valueOf( responseCode ) );
						System.out.println(response_code_message_decription);
						
						map.put("response_code_message_decription",response_code_message_decription); // 프로토콜 추가할것
						
						String response_code_message_protocol = responseCode + " " + responseMessage; // protocol 얻는 메소드 없음.
						
						
						
						
						
						// 3XX 응답코드 처리
						String location = httpurlconnection.getHeaderField("Location");
						String content_location = httpurlconnection.getHeaderField("Content-Location");
						if( (responseCode == 301 || responseCode == 302) && MyLibrary.f_check_valid(location) ) {
							System.out.println("Location: " + location);
							RequestAndResponse.httpURLConnection("GET" ,  location , p3_map_request_headers , p4_request_body);
						
						}else if( (responseCode == 307 || responseCode == 308) && MyLibrary.f_check_valid(location) ) {
							System.out.println("Location: " + location);
							RequestAndResponse.httpURLConnection(p1_request_method ,  location , p3_map_request_headers , p4_request_body);
						}else if( (responseCode == 302 || responseCode == 307) && MyLibrary.f_check_valid(content_location)  ) {
							System.out.println("Content-Location: " + content_location);
							RequestAndResponse.httpURLConnection("GET" ,  content_location , p3_map_request_headers , p4_request_body);
						}
						
						// 4XX 응답코드 처리
						if(responseCode == 400) {
							
						}else if(responseCode == 401) {
							
						}else if(responseCode == 403) {
							// 403 Forbidden 
							/*  
							 조회금지. 
							 	ip기반 다수접근시도하여 차단함. 
							 		upstream-proxy 로 우회 ㄱ.
							 		일정시간 지난 후 마다 요청(시간늘려야됨|횟수줄여야됨) 
							 	서버에서 고의적으로 차단. (방화벽 | In Ex ternal 구분 ..)
							 */
							
						}else if(responseCode == 404) {
							
						}else if(responseCode == 405) {
							
						}else if(responseCode == 406) {
							
						}else if(responseCode == 408) {
							// 408 Request Timeout
							/*  
							 서버에서 설정한 timeout 초과. 많은부하받은 상태일 가능성 높음. 일정시간 지난 후 마다 요청(시간늘려야됨|횟수줄여야됨.)
							 */							
						}else if(responseCode == 410) {
							
						}
						
						
						
						//urlconnection.setRequestProperty("Method", "GET"); //헤더를 검색방식과 맞춰줌. 로그인 할 때도 맞춰야됨.  
						
						//new url("주소").getInputStream() 은 원본 파일 그 자체임. 나중에 테스트 해볼것.
						// URLConnection 은 기본 설정 헤더값이 내 브라우저와 틀리게 되어있음. 때문에 갖게 맞춰야됨.
						// URLConnection.setRequestProperty( request.getHeaderNames()로 얻은 이름 ,request.getHeader(request.getHaderNames()로 얻은 이름) )
						
						//httpurlconnection.setRequestProperty("user-agent", httpurlconnection.getRequestProperty("user-agent"));
						
						httpurlconnection.connect();
						
						map.put("defaultUseCaches", String.valueOf(httpurlconnection.getDefaultUseCaches()) );
						map.put("fileNameMap", String.valueOf(httpurlconnection.getFileNameMap()) );
						map.put("defaultAllowUserInteraction", String.valueOf(httpurlconnection.getDefaultAllowUserInteraction()) );
						map.put("permission",  String.valueOf(httpurlconnection.getPermission()) );
						//map.put("content",  "" + httpurlconnection.getContent() ); 
						map.put("date", String.valueOf( httpurlconnection.getDate() ) );
						map.put("connectTimeout", String.valueOf( httpurlconnection.getConnectTimeout() ) );
						map.put("readTimeout", String.valueOf( httpurlconnection.getReadTimeout() ) );
						map.put("contentEncoding", String.valueOf( httpurlconnection.getContentEncoding() ) );
						map.put("contentLength", String.valueOf( httpurlconnection.getContentLength() ) );
						map.put("contentType", String.valueOf( httpurlconnection.getContentType() ) );
						map.put("expiration", String.valueOf( httpurlconnection.getExpiration() ) );
						map.put("headerFields", String.valueOf( httpurlconnection.getHeaderFields().toString().replaceAll(",", "\n") ) );
						map.put("lastModified", String.valueOf( httpurlconnection.getLastModified() ) );
						
						
						total_string+="\n\n\n:: urlConnectionInfo :: \n "+
								//"RequestProperties => "+httpurlconnection.getRequestProperties() + "\n"+ 
								"DefaultUseCaches => "+httpurlconnection.getDefaultUseCaches() + "\n"+
								"FileNameMap => "+httpurlconnection.getFileNameMap() + "\n"+
								"DefaultAllowUserInteraction => "+httpurlconnection.getDefaultAllowUserInteraction() + "\n"+  
								"Permission => "+httpurlconnection.getPermission() + "\n"+
//								"Content => " + httpurlconnection.getContent() + "\n"+ 
								"Date: "+httpurlconnection.getDate() + "\n"+
								"ConnectTimeout => " + httpurlconnection.getConnectTimeout() + "\n"+
								"ReadTimeout => "+httpurlconnection.getReadTimeout() + "\n"+
								"Content-Encoding: "+httpurlconnection.getContentEncoding() + "\n"+
								"Content-Length: "+httpurlconnection.getContentLength() + "\n"+
								"Content-Type: "+httpurlconnection.getContentType() + "\n"+
								
								"Expiration => "+httpurlconnection.getExpiration() + "\n"+ //expiration 만료
								"HeaderFields => "+httpurlconnection.getHeaderFields().toString().replaceAll(",", "\n") + "\n"+
								"LastModified => "+httpurlconnection.getLastModified() + "\n";
						
						
						total_string+="urlinfo;";
						
						String response_body = "";
						BufferedReader bufferedReader = new BufferedReader( // 응답 받은 데이터? 
							new InputStreamReader(
								httpurlconnection.getInputStream() , "UTF-8"   
							)
						); 
						
						for(String readLine=null;(readLine = bufferedReader.readLine()) != null;) {
							total_string += readLine + "\n";
							response_body += readLine + "\n";
						}

						
						// Refresh 헤더 
						String refresh = httpurlconnection.getHeaderField("Refresh");
						if(MyLibrary.f_check_valid(refresh)) {
							if(refresh.matches("URL.*=.*")) {
								String string = refresh.split("URL.*=.*")[1];
								System.out.println("response header 에서 파싱한 redirect url 주소 = " + string);
								RequestAndResponse.httpURLConnection("GET" ,  string , p3_map_request_headers , p4_request_body);
							}
						}else {
							for(String string: Data.StringData.matchRegExp(response_body, "<meta.*http-equive.*=.*refresh.*>" , "g")) {
								if(MyLibrary.f_check_valid(string)) {
									if(string.matches("URL.*=.*")) {
										string = string.split("URL.*=.*")[1];
										System.out.println("response body 에서 파싱한 Location: " + string);
										RequestAndResponse.httpURLConnection("GET" ,  string , p3_map_request_headers , p4_request_body);
									}
									
								}
							}							
						}
						
						for(String string: Data.StringData.matchRegExp(response_body, "<link.*rel.*=.*canonical.*>" , "g")) {
							if(MyLibrary.f_check_valid(string)) {
								if(string.matches("href.*=.*")) {
									string = string.split("href.*=.*")[1];
									System.out.println("response body 에서 파싱한 Content-Location: " + string);
									RequestAndResponse.httpURLConnection("GET" ,  string , p3_map_request_headers , p4_request_body);
								}
								
							}
						}


						
						
						map.put("total_string", total_string);
						map.put("response_body", response_body);   
						httpurlconnection.disconnect();  
						bufferedReader.close(); 
						httpurlconnection.disconnect();
						
						return map;
					}
					
				} catch (Exception e) {e.printStackTrace();}
				
				
				return map;
			}
			
			
			
			
			public static Map<String , String> httpsURLConnection(String url) { //해야됨
				Map<String , String> map = new HashMap<>();
				try {
					String response_message = "";
					
					SSLContext sslContext = Mitm.Proxy.createSSLContext();
					
					HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
					HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
						@Override
						public boolean verify(String hostname, SSLSession session) { return true; }
					});
					
					HttpsURLConnection httpsURLConnection = (HttpsURLConnection) new URL(url).openConnection();
					httpsURLConnection.connect();
					httpsURLConnection.setDoOutput(true);
					
					MyLibrary.f_bufferedWriter(httpsURLConnection.getOutputStream(), response_message);
					
					//해야됨
					
					
					httpsURLConnection.setDoInput(true);
					response_message += MyLibrary.f_bufferedReader(httpsURLConnection.getInputStream() , null);
					httpsURLConnection.disconnect();
					
					map.put("total_string", response_message);
					
					if(MyLibrary.f_check_valid(response_message)) return map;
										
				} catch (Exception e) { e.printStackTrace();}

				return null;
			}
			
		}
	}
	
	
	public static class API{ // MyLibrary.API
		public API() {}
		
		public static class AI { // MyLibrary.API.AI
			public AI() {}
			
			public static class OCR { // MyLibrary.API.AI.OCR
				
				static String SECRET_KEY = "T3l6UUxiQlVxUEpDY0RHYWFid3pKSm1jVEZ5d1pPdFk="; /*Secret Key 값*/
				static String APIGW_INVOKE_URL = "https://6a7a4hzejg.apigw.ntruss.com/custom/v1/24457/a8806d6dab7e274eb7f53affd2f6e21c1515f6d56ca2bbab245f9401470a298a/general"; /*APIGW Invoke URL 값*/
				public OCR() {}
				
				public static String antiCaptcha_TextImageToText(String my_id_getParameter ,  String image_type , String data_image_type_base64) { /* data:image/jpeg;base64, 부분을 지운 값 */
					try {
						//인터파크 텍스트 캡차 확인함.
						
						if(!MyLibrary.f_check_valid(my_id_getParameter) ) my_id_getParameter = "images_name";
						if( !MyLibrary.f_check_valid(image_type)) return null;
						if( !MyLibrary.f_check_valid(data_image_type_base64)) return null;
						
						String request_body = "{\r\n"
							+ "    \"version\": \"V2\",\r\n"
							+ "    \"requestId\": \"" + UUID.randomUUID().toString() + "\",\r\n"
							+ "    \"timestamp\": " + System.currentTimeMillis() + ",\r\n"
							+ "    \"lang\": \"ko\",\r\n"
							+ "\r\n"
							+ "    \"images\": [\r\n"
							+ "      {\r\n"
							+ "        \"format\": \"" + image_type + "\",\r\n"
							+ "        \"name\": \"" + my_id_getParameter + "_" + System.currentTimeMillis() + "\",\r\n"
							
							/* data:image/jpeg;base64, 부분을 지운 값 */
							+ "        \"data\": \"" + data_image_type_base64 + "\"\r\n"
							+ "      }\r\n"
							+ "    ],\r\n"
							+ "    \"enableTableDetection\": false\r\n"
							+ "}"
						;
						System.out.println(request_body);
						
						
						HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(OCR.APIGW_INVOKE_URL).openConnection();
						
						
						httpURLConnection.setDoOutput(true);
						httpURLConnection.setRequestMethod("POST");
						httpURLConnection.setRequestProperty("Content-Type", "application/json");
						httpURLConnection.setRequestProperty("X-OCR-SECRET",  OCR.SECRET_KEY);
						
						BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream()));
						bufferedWriter.write(request_body);
						bufferedWriter.flush();
						bufferedWriter.close();

						
						
						
						
						//httpURLConnection.setDoInput(true);
						BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
						String response_body = "";
						for(String readline = null;(readline = bufferedReader.readLine()) != null;) { response_body += readline;}
						bufferedReader.close();
						
						//System.out.println(response_body);	
						
						
						
						JSONObject jsonObject = new JSONObject(response_body);
						
						switch(httpURLConnection.getResponseCode()) {
							case 200:
								JSONArray jsonArray = jsonObject.getJSONArray("images");
								
								JSONObject jsonObject_jsonArray_0 = new JSONObject(jsonArray.get(0).toString()); 
								// images 배열의 0번째 객체
								
								
								//System.out.println(jsonObject_jsonArray_0.get("inferResult")); // "SUCCESS" 여야 됨.
								//System.out.println(jsonObject_jsonArray_0.get("message")); // "SUCCESS" 여야 됨.
								if(jsonObject_jsonArray_0.get("inferResult").equals("SUCCESS") && jsonObject_jsonArray_0.get("message").equals("SUCCESS")) {
									String 결과 = "";
									JSONArray jsonArray_fields = jsonObject_jsonArray_0.getJSONArray("fields");
									// 한번에 나올때도 있고 인덱 0 1 두개에 나눠 나올때도 있음. 글자수 합친 후 6글자 일 때 ㄱ
									for(int i=0; i< jsonArray_fields.length(); i++) {
										JSONObject jsonObject_jsonArray_fields_i = new JSONObject(jsonArray_fields.get(i).toString());
										결과 += jsonObject_jsonArray_fields_i.get("inferText");
									}
									결과 = 결과.replaceAll(" ", "").toUpperCase();
									System.out.println("결과 = "+결과);
									if(결과.length() == 6) {
										return "\"" + 결과 + "\""; //ocr 정상복호화값
									} 
									
								}else {
									throw new Exception("!!! 이미지 인식 실패. !(jsonObject_jsonArray_0.get(\"inferResult\").equals(\"SUCCESS\") && jsonObject_jsonArray_0.get(\"message\").equals(\"SUCCESS\"))");
								}

								break;
							default:
								System.out.println(jsonObject.toString());
								throw new Exception("!!! 이미지 인식 실패. " + jsonObject.toString() + "\n" + httpURLConnection.getResponseCode() + " " + httpURLConnection.getResponseMessage() + "\n0022 등.. 문서참조 ㄱ Request domain invalid 등.. 문서참조 ㄱ");
						}
						

						
						
						
						//images fields
						//httpURLConnection.connect();
						//httpURLConnection.disconnect();
					} catch (Exception e) {e.printStackTrace(); }
					
					return null;
				}
			}
		}
		
		
	}
	
	public static class Database {
		public static class MySQL {// MyLibrary.Database.MySql
			
			// MyLibrary.Database.MySql.connection("localhost","3306","target_db","root","Lmh3910@");
			public static Connection connection(String ip,String port , String database_name , String user , String password) {
				Connection connection = null;
				try {
					Class.forName("com.mysql.jdbc.Driver");
					connection = DriverManager.getConnection(
							"jdbc:mysql://"+ ip +":" + port + "/" + database_name,
							user,  
							password
					);
					System.out.println("db연결 성공");
				} catch (Exception e) { e.printStackTrace(); }
				
				return connection;
				
			}
		}
	}
	
	public static class WebWasServer { //MyLibrary.WebWasServer
		public static class Tomcat{ //MyLibrary.WebWasServer.Tomcat
			public static class Servlet { //MyLibrary.WebWasServer.Tomcat.Servlet
				
				public static HttpServletResponse setBasicResponseHeader(HttpServletRequest request ,HttpServletResponse response ) {
					
					response.setHeader("Access-Control-Allow-Methods", request.getHeader("Access-Control-Request-Method"));
					System.out.println("request_method="+request.getMethod());
					
					response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
					System.out.println("request_origin="+request.getHeader("Origin") );
					
					response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
					for(Enumeration<String> enumeration =  request.getHeaderNames();enumeration.hasMoreElements();) {
						String request_header_name = enumeration.nextElement();
						System.out.println("request_header=" + request_header_name + ":" + request.getHeader(request_header_name));
					}
					
					response.setHeader("Access-Control-Allow-Credentials", "true"); 
					response.setHeader("Access-Control-Max-Age", "3600");
					response.setHeader("Access-Control-Expose-Headers", "*"); 
					
					return response;
				}
				
				public static HttpServletResponse setAndResponse(HttpServletResponse httpServletResponse,String responseHeaders  ,String contentType , String responseBody) {
					//MyLibrary.WebWasServer.Tomcat.Servlet.setAndResponse(httpServletResponse,"Content-Length: 0; Content-Language: ko-kr;" ,"json","{\"키\":값}");  //일시 설정만
					
					try {
						if(MyLibrary.f_check_valid(httpServletResponse)) {
							httpServletResponse.setCharacterEncoding("UTF-8");							
							PrintWriter printWriter = httpServletResponse.getWriter();
							
							//응답헤더
							if(MyLibrary.f_check_valid(responseHeaders)) {
								for(String key_value:responseHeaders.split(";")) {
									String key = key_value.split(":")[0].replaceAll(" ","");
									String value = key_value.split(":")[1]; // 띄어쓰기로 구분되는 등.. 때문에 replaceAll 하면 안됨.
									httpServletResponse.setHeader(key, value);
								}
								
								
								
							}
							
							
							
							//응답바디 
							if(MyLibrary.f_check_valid(contentType)) {
								if(contentType.equalsIgnoreCase("json")) {
									httpServletResponse.setContentType("application/json");	
									if(MyLibrary.f_check_valid(responseBody)) {
										printWriter.println(new JSONObject(responseBody).toString());
									}
								} 
								else if(contentType.equalsIgnoreCase("plain")) {
									httpServletResponse.setContentType("text/plain");
									if(MyLibrary.f_check_valid(responseBody)) {
										printWriter.println(responseBody);
									}
								}
								else if(contentType.equalsIgnoreCase("form-data")) {
									httpServletResponse.setContentType("multipart/form-data");
									if(MyLibrary.f_check_valid(responseBody)) {
										printWriter.println();
									}
								} 
								else if(contentType.equalsIgnoreCase("x-www-form-urlencoded")) {
									httpServletResponse.setContentType("application/x-www-form-urlencoded");
									if(MyLibrary.f_check_valid(responseBody)) {
										printWriter.println(responseBody);
									}
								} 
								
								else { // 기본값 설정하지 말것. 위에 setHeader 했던 값들이 덮어써지므로.
									//httpServletResponse.setContentType("text/palin");
									
								}
							
							}
							
							printWriter.flush();
							printWriter.close();
							
							
							
							if(MyLibrary.f_check_valid(responseBody)) {
								
								
								
							}
						
						}	
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					return httpServletResponse;
				}
			}
		}
	}
	
	
	
	public static class Os { //하나도 테스트 안함.
		public Os() {}
		public static class Installer {
			public Installer() {}
			public static String base_install() {
				String result = "";
				//윈도우일 때
				if(MyLibrary.os_name.startsWith("window")) {
					result += MyLibrary.f_exec(MyLibrary.cmd_or_bash + "powershell Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'))");
					result += MyLibrary.f_exec(MyLibrary.cmd_or_bash + "echo \"y`n\" | choco install curl");
				}else if(MyLibrary.os_name.startsWith("linux")) {
					
				}

				
				return result;
			}
		}
		
		
		public static String setEnv(String key , String value) {
			String result = "";
			if(os_name.startsWith("window")) {
				result += MyLibrary.f_exec(MyLibrary.cmd_or_bash + "set " + key + "=" + "\""+value+"\"");
				result += MyLibrary.f_exec(MyLibrary.cmd_or_bash + "setx " + key + " " + "\""+value+"\"" + " /M");
				
				result += MyLibrary.f_exec(MyLibrary.cmd_or_bash + "refreshenv");
				
				
			}else if(os_name.startsWith("linux")) {
				
			}
			
			
			
			return result;
		}	
		
		public static String getEnv(String key) {
			String result = "";
			if(os_name.startsWith("window")) {
				result += MyLibrary.f_exec(MyLibrary.cmd_or_bash + "echo " + "%" + key + "%");
			}else if(os_name.startsWith("linux")) {
				
			}
			
			return result;
		}	

		
		public static String getAllEnv() {
			String result = "";
			if(os_name.startsWith("window")) {
				result = MyLibrary.f_exec(MyLibrary.cmd_or_bash + "set");
			}else if(os_name.startsWith("linux")) {
				
			}
			
			return result;
		}		
	}
}