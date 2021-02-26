package com.icia.web.model;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.icia.web.dao.AdminDao;

@Service
public class AdminDaoImpl implements AdminDao{
   
   @Inject
   AdminImpl admin;
   
   @Override
   public List<Admin> adminList(){
      return admin.adminList();
   }
  
   @Override
   public void insertAdmin(Admin admin) {
      
   }
   
   @Override
   public Admin viewAdmin() {
      return null;
   }
   
   @Override
   public void deleteAdmin(String userId) {
      
   }
@Override
public Admin adminSelect(String adminId) {
	// TODO Auto-generated method stub
	return null;
}

}