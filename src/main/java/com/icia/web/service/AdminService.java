package com.icia.web.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.icia.web.dao.AdminDao;
import com.icia.web.model.Admin;
import com.icia.web.model.HiBoard;

@Service("adminService")
public class AdminService 
{
      private static Logger logger = LoggerFactory.getLogger(AdminService.class);
      
      @Autowired
      private AdminDao adminDao;

   public Admin adminSelect(String adminId)
   {
      Admin admin=null;
      
      try
      {
         admin = adminDao.adminSelect(adminId);
      }
      catch(Exception e)
      {
         logger.error("[AdminService] adminSelect Exception 오류 확인!", e);
      }
      
      return admin;
   }
   
 //유저리스트
  /* public List<Admin> userList(String adminId)
   {
      List<Admin> userlist = null;
      
      try
      { 
         userlist = adminDao.userList(adminId);
      }
      catch(Exception e)
      {
         logger.error("[AdminService] userList Exception", e);
      }
      
      return  userlist;
   }*/
   
}