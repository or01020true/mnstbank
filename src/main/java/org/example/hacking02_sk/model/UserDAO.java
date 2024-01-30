package org.example.hacking02_sk.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;
import org.example.hacking02_sk.service.MyDBConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAO {
	
	private Connection conn;
	private PreparedStatement pstmt;
	private PreparedStatement pstmt2;
	private ResultSet rs;

//	@Autowired
//	public UserDAO(DataSource dataSource) {
//		try {
////			String dbURL = "jdbc:mysql://localhost:3306/myhacking";
////			String dbID = "myhack";
////			String dbPassword = "1234";
////			Class.forName("com.mysql.cj.jdbc.Driver");
//			conn = dataSource.getConnection();
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	//로그인
	public int login(String myid, String mypw) {
		String SQL = "SELECT mypw FROM myuser WHERE myid = ?";
		try {
			pstmt = MyDBConnection.getConnection().prepareStatement(SQL);
			pstmt.setString(1, myid);
			rs = pstmt.executeQuery(); // 결과 담는 객체
			if (rs.next()) {
				if(rs.getString(1).equals(mypw)) {
					return 1; // 로그인 성공
				}
				else {
					return 0; // 비밀번호 불일치
				}	
			}
			return -1; // 아이디가 없음
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -2; // DB 오류
	}
	
	//회원가입
	public int signup(User user) {
        String query1 = "INSERT INTO myuser (myname, myid, mypw, myemail, mylocation, myphone, mysid) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?);";
        String query2 = "INSERT INTO myacc (myacc, myid, mymoney, mybank, myaccpw) " +
        "VALUES (?, ?, ?, ?, ?);";
		
		int num = 0;
		
		try {
			// myuser table
			pstmt = MyDBConnection.getConnection().prepareStatement(query1);
			pstmt.setString(1, user.getMyname());
			pstmt.setString(2, user.getMyid());
			pstmt.setString(3, user.getMypw());
			pstmt.setString(4, user.getMyemail());
			pstmt.setString(5, user.getMylocation());
			pstmt.setString(6, "010" + user.getMyphone());
			pstmt.setString(7, user.getMysid());
			
			pstmt.executeUpdate();
			pstmt.close();
			
			// myacc table
			pstmt2 = MyDBConnection.getConnection().prepareStatement(query2);
			pstmt2.setInt(1, Integer.parseInt("010" + user.getMyphone()));
			pstmt2.setString(2, user.getMyid());
			pstmt2.setInt(3, 1000000);	// 초기 잔액
			pstmt2.setString(4, "MNST");
			pstmt2.setInt(5, Integer.parseInt(user.getMyaccpw()));
			
			num = pstmt2.executeUpdate();
			return num;
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	//아이디 중복 체크
    public int userCheck(String myid) {
    	String SQL = "SELECT myid FROM myuser WHERE myid = ?";
		
//    	System.out.println("넘어오는 myid 값 : " + myid);
    	
    	if (myid.equals("")) {
    		return 2;
    	}
    	
    	try {		
			pstmt = MyDBConnection.getConnection().prepareStatement(SQL);
			pstmt.setString(1, myid);
//			System.out.println(pstmt);
			rs = pstmt.executeQuery(); // 결과 담는 객체

			if(!rs.next()) {
//				System.out.println("없는 아이디! 회원가입 가능");
				return 1; // 회원가입 가능
			} else {
//				System.out.println("이미 존재하는 ID");
				return 0; // 아이디 이미 존재
			}

    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	return -1; // Error
    }
    
	//전화번호 중복 체크
    public int phoneCheck(String myphone) {
    	String SQL = "SELECT myphone FROM myuser WHERE myphone = ?";
		
//    	System.out.println("넘어오는 myphone 값 : " + "010" + myphone);

    	try {		
			pstmt = MyDBConnection.getConnection().prepareStatement(SQL);
			pstmt.setString(1, "010" + myphone);
//			System.out.println(pstmt);
			rs = pstmt.executeQuery(); // 결과 담는 객체

			if(!rs.next()) {
				System.out.println("없는 폰번호! 회원가입 가능");
				return 1; // 회원가입 가능
			} else {
				System.out.println("이미 존재하는 폰번호");
				return 0; // 핸드폰 번호 이미 존재
			}
			
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	return -1; // Error
    }
    
	public User getUser(String myid, String mypw) {
		User user = null;
		String SQL = "SELECT myname, myid, mypw, myemail, mylocation, myphone, mysid FROM myuser WHERE myid = ? AND mypw = ?";
		try {
			pstmt = MyDBConnection.getConnection().prepareStatement(SQL);
			pstmt.setString(1, myid);
			pstmt.setString(2, mypw);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				user = new User();
				user.setMyname(rs.getString("myname"));
				user.setMyid(rs.getString("myid"));
				user.setMypw(rs.getString("mypw"));
				user.setMyemail(rs.getString("myemail"));
				user.setMylocation(rs.getString("mylocation"));
				user.setMyphone(rs.getString("myphone"));
				user.setMysid(rs.getString("mysid"));
			}

			return user;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return user;
	}
    
    //주소 찾기
    public List<Location> findLocation(String keyword) throws SQLException {
    	String SQL = "SELECT * FROM myaddr WHERE myaddr LIKE '%" + keyword + "%'";

    	if (keyword == null) {
    		return new ArrayList<Location>();
    	}
    	
		pstmt = MyDBConnection.getConnection().prepareStatement(SQL);
//		System.out.println(pstmt);
		rs = pstmt.executeQuery(); // 결과 담는 객체
		
		List<Location> list = new ArrayList<>(); // DB 정보 담을 List
		
		while(rs.next()) {
			Location loc = new Location();
			loc.setMyaddr(rs.getString("myaddr"));
			loc.setMyzip(rs.getString("myzip"));
			list.add(loc);
		}

		//System.out.println("DB 리스트 : " + list);
		return list;
    }
}
