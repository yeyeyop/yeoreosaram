package com.icia.web.model;

import java.io.Serializable;

public class Admin {
   
   private static final long serialVersionUID = 1L;
   
   private String adminId;
   
   private String adminPwd;
   
   public Admin()
   {
      adminId="";
      adminPwd="";
   }

   public String getAdminId() {
      return adminId;
   }

   public String getAdminPwd() {
      return adminPwd;
   }

   public void setAdminId(String adminId) {
      this.adminId = adminId;
   }

   public void setAdminPwd(String adminPwd) {
      this.adminPwd = adminPwd;
   }
   
   private String userId2;
   private String userPwd2;
   private String userName2;
   private String userEmail2;
   
   public String getUserId2() {
      return userId2;
   }
   public void setUserId2(String userId2) {
      this.userId2 = userId2;
   }
   public String getUserPwd2() {
      return userPwd2;
   }
   public void setUserPwd2(String userPwd2) {
      this.userPwd2 = userPwd2;
   }
   public String getUserName2() {
      return userName2;
   }
   public void setUserName2(String userName2) {
      this.userName2 = userName2;
   }
   public String getUserEmail2() {
      return userEmail2;
   }
   public void setUserEmail2(String userEmail2) {
      this.userEmail2 = userEmail2;
   }
   
}