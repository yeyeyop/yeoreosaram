package com.icia.web.model;

import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.icia.web.dao.AdminDao;

@Repository
public class AdminImpl implements AdminDao{
   
   @Inject
   SqlSession sqlSession;
   
   //회원목록
   @Override
   public List<Admin> adminList(){
      return sqlSession.selectList("adminDao.adminList");
   }
   
   @Override
   public void insertAdmin(Admin admin) {
      
   }
   
   @Override
   public Admin viewAdmin() {
      return null;
   }
   
   @Override
   public int userDelete(String userId2) {
      
   }

   @Override
   public Admin adminSelect(String adminId) {
   // TODO Auto-generated method stub
   return null;
   }
    
}