package com.icia.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.icia.common.util.StringUtil;
import com.icia.web.model.Response;
import com.icia.web.model.Admin;
import com.icia.web.service.AdminService;
import com.icia.web.util.CookieUtil;
import com.icia.web.util.HttpUtil;
import com.icia.web.util.JsonUtil;

/*관리자 컨트롤러*/
@Controller("adminController")
public class AdminController 
{
   
   //로그
   private static Logger logger = LoggerFactory.getLogger(AdminController.class);
   
   // 쿠키명
   @Value("#{env['auth.cookie.name']}")
   private String AUTH_COOKIE_NAME;
   
   //서비스 호출   
   @Autowired
   private AdminService adminService;
   
   //로그인
   @RequestMapping(value="/admin/login2", method=RequestMethod.POST)
    @ResponseBody
      public Response<Object> login2(HttpServletRequest request, HttpServletResponse response)
      {
         String adminId = HttpUtil.get(request, "adminId");
         String adminPwd = HttpUtil.get(request, "adminPwd");
         
         Response<Object> ajaxResponse = new Response<Object>();
         
         if(!StringUtil.isEmpty(adminId) && !StringUtil.isEmpty(adminPwd))
         {
            Admin admin = adminService.adminSelect(adminId);
            
            if(admin != null)
            {
               if(StringUtil.equals(admin.getAdminPwd(), adminPwd))
               {
                  CookieUtil.addCookie(response, "/", -1, AUTH_COOKIE_NAME, CookieUtil.stringToHex(adminId));
                  
                  ajaxResponse.setResponse(0, "Success"); // 로그인 성공
               }
               else
               {
                  ajaxResponse.setResponse(-1, "Passwords do not match"); // 비밀번호 불일치
               }
            }
            else
            {
               ajaxResponse.setResponse(404, "Not Found"); // 사용자 정보 없음 (Not Found)
            }
         }
         else
         {
            ajaxResponse.setResponse(400, "Bad Request"); // 파라미터가 올바르지 않음 (Bad Request)
         }
         
         if(logger.isDebugEnabled())
         {
            logger.debug("[AdminController] /admin/login2 response 오류! \n" + JsonUtil.toJsonPretty(ajaxResponse));
         }
         
         return ajaxResponse;
         
      }//logIn2
   
   
   //로그아웃
   @RequestMapping(value="admin/logOut2", method=RequestMethod.GET)
   public String logOut2(HttpServletRequest request, HttpServletResponse response)
   {
      if(CookieUtil.getCookie(request, AUTH_COOKIE_NAME) != null)
      {
          CookieUtil.deleteCookie(request, response, "/", AUTH_COOKIE_NAME);
      }
      return "redirect:/";
   }//logOut
   
   //admin/index 이 페이지로 가기 위한 메서드
   @RequestMapping(value="admin/index", method=RequestMethod.GET)
   public String index(HttpServletRequest request, HttpServletResponse response)
   {
      if(CookieUtil.getCookie(request, AUTH_COOKIE_NAME) != null)
      {
          CookieUtil.deleteCookie(request, response, "/", AUTH_COOKIE_NAME);
      }
      return "/admin/index";
   }
   
	
		//AdminHome
		@RequestMapping(value = "/admin/adminHome", method=RequestMethod.GET)
		public String mainHome(HttpServletRequest request, HttpServletResponse response)
		{
			return "/admin/adminHome";
		}

   
}