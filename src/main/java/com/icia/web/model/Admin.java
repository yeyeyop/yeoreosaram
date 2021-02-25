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
   
   
   
}