
package com.icia.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icia.web.dao.UserDao2;
import com.icia.web.model.User2;


@Service("userService2")
public class UserService2
{
   private static Logger logger = LoggerFactory.getLogger(UserService2.class);
   
   @Autowired
   private UserDao2 userDao2;
   
   public int userInsert2(User2 user2)
   {
      int count = 0;
      
      try
      {
         count = userDao2.userInsert2(user2);
      }
      catch(Exception e)
      {
         logger.error("[UserService2] userInsert2 Exception", e);
      }
      
      return count;
   }

   public User2 userSelect2(String userId2)
   {
      User2 user2 = null;
      
      try
      {
         user2 = userDao2.userSelect2(userId2);
      }
      catch(Exception e)
      {
         logger.error("[UserService2] userSelect2 Exception", e);
      }
      
      return user2;
   }
   
   
   
   
   
   public int userUpdate2(User2 user2)
   {
      int count = 0;
      
      try
      {
         count = userDao2.userUpdate2(user2);
      }
      catch(Exception e)
      {
         logger.error("[UserService2] userUpdate2 Exception", e);
      }
      
      return count;
   }
   
   public int userStatusUpdate2(User2 user2)
   {
      int count = 0;
      
      try
      {
         count = userDao2.userStatusUpdate2(user2);
      }
      catch(Exception e)
      {
         logger.error("[UserService2] userStatusUpdate2 Exception", e);
      }
      
      return count;
   }
   
   
}