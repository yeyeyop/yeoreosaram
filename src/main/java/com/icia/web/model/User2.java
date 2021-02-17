/**
 * <pre>
 * 프로젝트명 : BasicBoard
 * 패키지명   : com.icia.web.model
 * 파일명     : User.java
 * 작성일     : 2021. 1. 12.
 * 작성자     : daekk
 * </pre>
 */
package com.icia.web.model;

import java.io.Serializable;

public class User2 implements Serializable
{
   private static final long serialVersionUID = 8638989512396268543L;
   
   private String userId2;          // 사용자 아이디
   private String userPwd2;       // 비밀번호
   private String userName2;     // 사용자 명
   private String userEmail2;     // 사용자 이메일 
   private String status2;          // 상태 ("Y":사용, "N":정지)
   private String regDate2;
   private String gender2;      	  //성별
   private String userBirth2;       //나이
   
//   USER_ID2" VARCHAR2(20 BYTE) NOT NULL ENABLE, 
//	"USER_PWD2" VARCHAR2(20 BYTE), 
//	"USER_NAME2" VARCHAR2(30 BYTE), 
//	"USER_EMAIL2" VARCHAR2(50 BYTE), 
//	"STATUS2" CHAR(1 BYTE), 
//	"REG_DATE2" DATE, 
//	"GENDER2" CHAR(1 BYTE), 
//	"USER_BIRTH2" CHAR(8 BYTE), 
//	"USER_AGE2" NUMBER(6,0)
   
   /**
    * 생성자 
    */
   public User2()
   {
      userId2 = "";
      userPwd2 = "";
      userName2 = "";
      userEmail2 = "";
      status2 = "";
      regDate2 = "";
      gender2="";
      userBirth2="";
   }

public String getUserId2() {
	return userId2;
}

public String getUserPwd2() {
	return userPwd2;
}

public String getUserName2() {
	return userName2;
}

public String getUserEmail2() {
	return userEmail2;
}

public String getStatus2() {
	return status2;
}

public String getRegDate2() {
	return regDate2;
}

public String getGender2() {
	return gender2;
}

public String getUserBirth2() {
	return userBirth2;
}

public void setUserId2(String userId2) {
	this.userId2 = userId2;
}

public void setUserPwd2(String userPwd2) {
	this.userPwd2 = userPwd2;
}

public void setUserName2(String userName2) {
	this.userName2 = userName2;
}

public void setUserEmail2(String userEmail2) {
	this.userEmail2 = userEmail2;
}

public void setStatus2(String status2) {
	this.status2 = status2;
}

public void setRegDate2(String regDate2) {
	this.regDate2 = regDate2;
}

public void setGender2(String gender2) {
	this.gender2 = gender2;
}

public void setUserBirth2(String userBirth2) {
	this.userBirth2 = userBirth2;
}





}