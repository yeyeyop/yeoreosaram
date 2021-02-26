package com.icia.web.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import com.icia.web.model.Admin;

@Repository("adminDao")
public interface AdminDao {
   
   //관리자 정보 조회
   public Admin adminSelect(String adminId);

   //유저 조회
   //public List<Admin> userList(String adminId);
   
	/////////////////////////////////////////////
	//회원 목록
	public List<Admin> adminList();
	
	//회원 입력
	public void insertAdmin(Admin admin);
	
	//회원 정보 상세보기
	public Admin viewAdmin();
	
	//회원 삭제
	public void deleteAdmin(String userId);

}
