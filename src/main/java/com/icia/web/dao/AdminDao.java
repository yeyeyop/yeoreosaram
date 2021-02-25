package com.icia.web.dao;

import org.springframework.stereotype.Repository;
import com.icia.web.model.Admin;


@Repository("adminDao")
public interface AdminDao {
   
   //관리자 정보 조회
   public Admin adminSelect(String adminId);

}