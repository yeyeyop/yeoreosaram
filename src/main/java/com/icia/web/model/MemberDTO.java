package com.icia.web.model;

import java.util.Date;

public class MemberDTO {
    
    private String user_id;    //아이디
    private String member_pass;    //비밀번호
    private String e_mail;    //이메일
    private Date join_date;    //가입일자
    
    
    
    public String getUser_id() {
        return user_id;
    }
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public String getMember_pass() {
        return member_pass;
    }
    public void setMember_pass(String member_pass) {
        this.member_pass = member_pass;
    }
    public String getE_mail() {
        return e_mail;
    }
    public void setE_mail(String e_mail) {
        this.e_mail = e_mail;
    }
    public Date getJoin_date() {
        return join_date;
    }
    public void setJoin_date(Date join_date) {
        this.join_date = join_date;
    }
    
    
    @Override
    public String toString() {
        return "MemberDTO [user_id=" + user_id + ", member_pass=" + member_pass + ", e_mail=" + e_mail + ", join_date="
                + join_date + "]";
    }
    
 
}
