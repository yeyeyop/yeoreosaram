package com.icia.web.service;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
 
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
 
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icia.web.dao.MemberDAO;
import com.icia.web.model.MemberDTO;
 

 
@Service //서비스 빈 선언 
public class MemberSerivceImpl implements MemberService {
 
    
    @Inject    
    MemberDAO memberdao; //dao를 사용하기 위해 의존성을 주입
    private JavaMailSender mailSender;
 
    
    
    @Override    //회원가입 메소드, Map과 dto를 갖이 넘김
    public void join(Map<String, Object>map,MemberDTO dto) {
        memberdao.join(map,dto);
 
    }
 
 
    @Override    //로그인 관련 메소드 (세션에 아이디와 비밀번호를 저장)
    public boolean loginCheck(MemberDTO dto, HttpSession session) {
        
        boolean result = memberdao.loginCheck(dto);
        if(result) {    //로그인 성공
            session.setAttribute("user_id", dto.getUser_id());
            session.setAttribute("member_pass", dto.getMember_pass());
            System.out.println(session.getAttribute("user_id"));
            System.out.println(session.getAttribute("member_pass"));
        }
        
        return result;
    }
 
    //아이디 찾기
    @Override
    public String find_idCheck(MemberDTO dto) {
        String id = memberdao.find_idCheck(dto);
        
        return id;
    }
 
    //비밀번호 찾기
    @Override
    public String find_passCheck(MemberDTO dto) {
        String pass = memberdao.find_passCheck(dto);
        return pass;
    }
 
 
    @Override
    public void authentication(MemberDTO dto) {
        
        memberdao.authentication(dto);
    }
 
 
    @Override
    public void pass_change(Map<String, Object> map, MemberDTO dto) throws Exception {
        
        
        memberdao.pass_change(map,dto);
    }
 
 
    //이메일 중복 확인
    @Override
    public boolean email_check(String e_mail) throws Exception{
        
        boolean result = memberdao.email_check(e_mail);
        
        return result;
        
    }
 
    //아이디 중복 확인
    @Override
    public boolean join_id_check(String user_id) throws Exception {
    
        boolean result = memberdao.join_id_check(user_id);
        
        return result;
    }
 
 
    //자신의 프로필을 볼 수 있게 하는 메소드
    @Override
    public List<MemberDTO> member_profile(String user_id) throws Exception{
        
        return memberdao.member_profile(user_id);
    }
    
 
    
}
