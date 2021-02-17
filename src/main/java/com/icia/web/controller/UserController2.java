package com.icia.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonObject;
import com.icia.common.util.StringUtil;
import com.icia.web.model.Response;
import com.icia.web.model.User;
import com.icia.web.model.User2;
import com.icia.web.service.UserService2;
import com.icia.web.util.CookieUtil;
import com.icia.web.util.HttpUtil;
import com.icia.web.util.JsonUtil;

/**
 * <pre>
 * 패키지명   : com.icia.web.controller
 * 파일명     : UserController.java
 * 작성일     : 2021. 1. 20.
 * 작성자     : daekk
 * 설명       : 사용자 컨트롤러
 * </pre>
 */

@Controller("userController2")
public class UserController2
{
   private static Logger logger = LoggerFactory.getLogger(UserController2.class);
   
   // 쿠키명
   @Value("#{env['auth.cookie.name']}")
   private String AUTH_COOKIE_NAME;
   
   @Autowired
   private UserService2 userService2;
   
   /**
    * <pre>
    * 메소드명   : idCheck
    * 작성일     : 2021. 1. 22.
    * 작성자     : daekk
    * 설명       : 아이디 중복 체크
    * </pre>
    * @param request  HttpServletRequest
    * @param response HttpServletResponse
    * @return Response<Object>
    */
   @RequestMapping(value="/user/idCheck2", method=RequestMethod.POST)
   @ResponseBody
   public Response<Object> idCheck2(HttpServletRequest request, HttpServletResponse response)
   {
      String userId2 = HttpUtil.get(request, "userId2");
      Response<Object> ajaxResponse = new Response<Object>();
      
      if(!StringUtil.isEmpty(userId2))
      {
         if(userService2.userSelect2(userId2) == null) //값이 없다.
         {
            ajaxResponse.setResponse(0, "Success"); // 사용가능 아이디
         }
         else
         {
            ajaxResponse.setResponse(100, "Duplicate ID"); // 중복된 아이디 (Duplicate ID)
         }
      }
      else
      {
         ajaxResponse.setResponse(400, "Bad Request"); // 파라미터가 올바르지 않음 (Bad Request)
      }
      
      if(logger.isDebugEnabled())
      {
         logger.debug("[UserController2] /user/idCheck2 response\n" + JsonUtil.toJsonPretty(ajaxResponse));
      }
      
      return ajaxResponse;
   }
   
   /**
    * <pre>
    * 메소드명   : regForm
    * 작성일     : 2021. 1. 22.
    * 작성자     : daekk
    * 설명       : 회원가입 폼
    * </pre>
    * @param request  HttpServletRequest
    * @param response HttpServletResponse
    * @return String
    */
   @RequestMapping(value="/user/regForm2", method=RequestMethod.GET)
   public String regForm2(HttpServletRequest request, HttpServletResponse response)
   {
      String cookieUserId = CookieUtil.getHexValue(request, AUTH_COOKIE_NAME);
      
      if(!StringUtil.isEmpty(cookieUserId))
      {
         CookieUtil.deleteCookie(request, response, AUTH_COOKIE_NAME);
            
         return "redirect:/";
      }
      else
      {
         return "/user/regForm2";
      }
   }
   
   /**
    * <pre>
    * 메소드명   : regProc
    * 작성일     : 2021. 1. 22.
    * 작성자     : daekk
    * 설명       : 회원 등록 (AJAX)
    * </pre>
    * @param request  HttpServletRequest
    * @param response HttpServletResponse
    * @return Response<Object>
    */
   @RequestMapping(value="/user/regProc2", method=RequestMethod.POST)
   @ResponseBody
   public Response<Object> regProc2(HttpServletRequest request, HttpServletResponse response)
   {
      String userId2 = HttpUtil.get(request, "userId2");
      String userPwd2 = HttpUtil.get(request, "userPwd2");
      String userName2 = HttpUtil.get(request, "userName2");
      String userEmail2 = HttpUtil.get(request, "userEmail2");
      //스테터스 ? 
      //String regDate2=HttpUtil.get(request, "regDate2");
      String gender2=HttpUtil.get(request, "gender2");
      String userBirth2=HttpUtil.get(request, "userBirth2");
      
      Response<Object> ajaxResponse = new Response<Object>();
      
      if(!StringUtil.isEmpty(userId2) && !StringUtil.isEmpty(userPwd2) && !StringUtil.isEmpty(userName2) && !StringUtil.isEmpty(userEmail2)
    		  && !StringUtil.isEmpty(gender2) && !StringUtil.isEmpty(userBirth2))
      {
         if(userService2.userSelect2(userId2) == null) //중복체크
         {
            User2 user2 = new User2();
            
            user2.setUserId2(userId2);
            user2.setUserPwd2(userPwd2);
            user2.setUserName2(userName2);
            user2.setUserEmail2(userEmail2);
            user2.setStatus2("Y");
            //user2.setRegDate2(regDate2);
            user2.setGender2(gender2);
            user2.setUserBirth2(userBirth2);
            
            if(userService2.userInsert2(user2) > 0)
            {
               ajaxResponse.setResponse(0, "Success"); // 회원가입 성공
            }
            else
            {
               ajaxResponse.setResponse(500, "Internal Server Error"); // 회원가입 실패 (Internal Server Error)
            }
         }
         else
         {
            ajaxResponse.setResponse(100, "Duplicate ID"); // 중복된 아이디 (Duplicate ID)
         }
      }
      else
      {
         ajaxResponse.setResponse(400, "Bad Request"); // 파라미터가 올바르지 않음 (Bad Request)
      }
      
      if(logger.isDebugEnabled())
      {
         logger.debug("[UserController2] /user/regProc2 response\n" + JsonUtil.toJsonPretty(ajaxResponse));
      }
      
      return ajaxResponse;
   }
   
   
   @RequestMapping(value="/user/login2", method=RequestMethod.POST)
   @ResponseBody
   public Response<Object> login2(HttpServletRequest request, HttpServletResponse response)
   {
      String userId2 = HttpUtil.get(request, "userId2");
      String userPwd2 = HttpUtil.get(request, "userPwd2");
      Response<Object> ajaxResponse = new Response<Object>();
      
      if(!StringUtil.isEmpty(userId2) && !StringUtil.isEmpty(userPwd2))
      {
         User2 user2 = userService2.userSelect2(userId2);
         
         if(user2 != null)
         {
            if(StringUtil.equals(user2.getUserPwd2(), userPwd2))
            {
               CookieUtil.addCookie(response, "/", -1, AUTH_COOKIE_NAME, CookieUtil.stringToHex(userId2));
               
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
         logger.debug("[UserController2] /user2/login2 response\n" + JsonUtil.toJsonPretty(ajaxResponse));
      }
      
      return ajaxResponse;
   }
   
   
   /**
    * <pre>
    * 메소드명   : loginOut
    * 작성일     : 2021. 1. 22.
    * 작성자     : daekk
    * 설명       : 로그 아웃
    * </pre>
    * @param request  HttpServletRequest
    * @param response HttpServletResponse
    * @return String
    */
   @RequestMapping(value="/user/loginOut2", method=RequestMethod.GET)
   public String loginOut2(HttpServletRequest request, HttpServletResponse response)
   {
      if(CookieUtil.getCookie(request, AUTH_COOKIE_NAME) != null)
      {
         CookieUtil.deleteCookie(request, response, "/", AUTH_COOKIE_NAME);
      }
      
      return "redirect:/";
   }
   
   /**
	 * <pre>
	 * 메소드명   : updateForm
	 * 작성일     : 2021. 1. 22.
	 * 작성자     : daekk
	 * 설명       : 회원 정보 수정 폼
	 * </pre>
	 * @param model    ModelMap
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 * @return String
	 */
	@RequestMapping(value="/user/updateForm2", method=RequestMethod.GET)
	public String updateForm2(ModelMap model, HttpServletRequest request, HttpServletResponse response)
	{
		String cookieUserId2 = CookieUtil.getHexValue(request, AUTH_COOKIE_NAME);
		
		User2 user2 = userService2.userSelect2(cookieUserId2);
		
		model.addAttribute("user2", user2);
		
		return "/user/updateForm2";
	}
	
	/**
	 * <pre>
	 * 메소드명   : updateProc
	 * 작성일     : 2021. 1. 22.
	 * 작성자     : daekk
	 * 설명       : 회원 정보 수정 (AJAX)
	 * </pre>
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 * @return Response<Object>
	 */
	@RequestMapping(value="/user/updateProc2", method=RequestMethod.POST)
	@ResponseBody
	public Response<Object> updateProc2(HttpServletRequest request, HttpServletResponse response)
	{
		String cookieUserId2 = CookieUtil.getHexValue(request, AUTH_COOKIE_NAME);
		String userPwd2 = HttpUtil.get(request, "userPwd2");
		String userName2 = HttpUtil.get(request, "userName2");
		String userEmail2 = HttpUtil.get(request, "userEmail2");
		
		Response<Object> ajaxResponse = new Response<Object>();
		
		if(!StringUtil.isEmpty(cookieUserId2))
		{
			User2 user2 = userService2.userSelect2(cookieUserId2);
			
			if(user2 != null)
			{
				if(StringUtil.equals(user2.getStatus2(), "Y"))
				{
					if(!StringUtil.isEmpty(userPwd2) && !StringUtil.isEmpty(userName2) && !StringUtil.isEmpty(userEmail2))
					{
						user2.setUserPwd2(userPwd2);
						user2.setUserName2(userName2);
						user2.setUserEmail2(userEmail2);
						
						if(userService2.userUpdate2(user2) > 0)
						{
							ajaxResponse.setResponse(0, "success"); // 회원정보 수정 성공
						}
						else
						{
							ajaxResponse.setResponse(500, "Internal Server Error"); // 회원정보 수정 실패 (Internal Server Error)
						}
					}
					else
					{
						// 파라미터가 올바르지 않음
						ajaxResponse.setResponse(400, "Bad Request"); // 파라미터가 올바르지 않음 (Bad Request)
					}
				}
				else
				{
					// 정지된 사용자 (쿠키 삭제)
					CookieUtil.deleteCookie(request, response, AUTH_COOKIE_NAME);
					ajaxResponse.setResponse(400, "Bad Request"); // 파라미터가 올바르지 않음 (Bad Request)
				}
			}
			else
			{
				// 사용자 정보 없음 (쿠키 삭제)
				CookieUtil.deleteCookie(request, response, AUTH_COOKIE_NAME);
				ajaxResponse.setResponse(404, "Not Found"); // 사용자 정보 없음 (Not Found)
			}
		}
		else
		{
			ajaxResponse.setResponse(400, "Bad Request"); // 파라미터가 올바르지 않음 (Bad Request)
		}
		
		if(logger.isDebugEnabled())
		{
			logger.debug("[UserController] /user/updateProc2 response\n" + JsonUtil.toJsonPretty(ajaxResponse));
		}
		
		return ajaxResponse;
	}
   
}