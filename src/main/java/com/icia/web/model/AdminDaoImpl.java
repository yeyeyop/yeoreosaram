package com.icia.web.model;

import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import com.icia.web.dao.AdminDao;

@Service
public class AdminDaoImpl implements AdminDao{
   
   @Inject
   AdminImpl admin;
   AdminDao adminDao;
   SqlSession sqlSession;

   
   @Override
   public List<Admin> adminList(){
      return admin.adminList();
   }
  
   @Override
   public void insertAdmin(Admin admin) {
      
   }
   
   @Override
   public void adminDelete(Admin admin) throws Exception {
      // TODO Auto-generated method stub
      sqlSession.delete("admin.adminDelete", admin);
   }

   
	@Override
	public Admin adminSelect(String adminId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Admin> testList(Admin admin) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<Admin> qList(Admin admin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Admin testSelect(long hiBbsSeq) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Admin adminView(long hiBbsSeq) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int adminListDelete(long hiBbsSeq) {
		// TODO Auto-generated method stub
		return 0;
	}

}