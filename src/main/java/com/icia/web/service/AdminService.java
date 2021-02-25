package com.icia.web.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.icia.web.dao.AdminDao;
import com.icia.web.model.Admin;

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
}