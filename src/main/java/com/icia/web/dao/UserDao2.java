/**
 * <pre>
 * 프로젝트명 : HiBoard
 * 패키지명   : com.icia.web.dao
 * 파일명     : UserDao.java
 * 작성일     : 2021. 1. 19.
 * 작성자     : daekk
 * </pre>
 */
package com.icia.web.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.icia.web.model.User2;

@Repository("userDao2")
public interface UserDao2
{
   public int userInsert2(User2 user2);
   
   public int userUpdate2(User2 user2);

   public int userStatusUpdate2(User2 user2);
   
   public User2 userSelect2(String userId2);

}