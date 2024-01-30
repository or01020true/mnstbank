package org.example.hacking02_sk.model;

import lombok.Data;
import org.springframework.stereotype.Repository;

@Repository
public class User {
	
	private String myname;
	private String myid;
	private String mypw;
	private String myemail;
	private String mylocation;
	private String myphone;
	private String mysid;
	private String myaccpw;
	
	public String getMyname() {
		return myname;
	}
	public void setMyname(String myname) {
		this.myname = myname;
	}
	public String getMyid() {
		return myid;
	}
	public void setMyid(String myid) {
		this.myid = myid;
	}
	public String getMypw() {
		return mypw;
	}
	public void setMypw(String mypw) {
		this.mypw = mypw;
	}
	public String getMyemail() {
		return myemail;
	}
	public void setMyemail(String myemail) {
		this.myemail = myemail;
	}
	public String getMylocation() {
		return mylocation;
	}
	public void setMylocation(String mylocation) {
		this.mylocation = mylocation;
	}
	public String getMyphone() {
		return myphone;
	}
	public void setMyphone(String myphone) {
		this.myphone = myphone;
	}
	public String getMysid() {
		return mysid;
	}
	public void setMysid(String mysid) {
		this.mysid = mysid;
	}
	public String getMyaccpw() {
		return myaccpw;
	}
	public void setMyaccpw(String myaccpw) {
		this.myaccpw = myaccpw;
	}
}
