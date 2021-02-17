/**
 * <pre>
 * 프로젝트명 : HiBoard
 * 패키지명   : com.icia.web.controller
 * 파일명     : UserController.java
 * 작성일     : 2021. 1. 20.
 * 작성자     : daekk
 * </pre>
 */
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
import com.icia.web.service.UserService;
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
@Controller("userController")
public class UserController
{
   private static Logger logger = LoggerFactory.getLogger(UserController.class);
   
   // 쿠키명
   @Value("#{env['auth.cookie.name']}")
   private String AUTH_COOKIE_NAME;
   
   @Autowired
   private UserService userService;
   
   /**
    * <pre>
    * 메소드명   : login
    * 작성일     : 2021. 1. 21.
    * 작성자     : daekk
    * 설명       : 로그인 
    * </pre>
    * @param request  HttpServletRequest
    * @param response HttpServletResponse
    * @return Response<Object>
    */
   @RequestMapping(value="/user/login", method=RequestMethod.POST)
   @ResponseBody
   public Response<Object> login(HttpServletRequest request, HttpServletResponse response)
   {
      String userId = HttpUtil.get(request, "userId");
      String userPwd = HttpUtil.get(request, "userPwd");
      Response<Object> ajaxResponse = new Response<Object>();
      
      if(!StringUtil.isEmpty(userId) && !StringUtil.isEmpty(userPwd))
      {
         User user = userService.userSelect(userId);
         
         if(user != null)
         {
            if(StringUtil.equals(user.getUserPwd(), userPwd))
            {
               CookieUtil.addCookie(response, "/", -1, AUTH_COOKIE_NAME, CookieUtil.stringToHex(userId));
               
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
         logger.debug("[UserController] /user/login response\n" + JsonUtil.toJsonPretty(ajaxResponse));
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
   @RequestMapping(value="/user/loginOut", method=RequestMethod.GET)
   public String loginOut(HttpServletRequest request, HttpServletResponse response)
   {
      if(CookieUtil.getCookie(request, AUTH_COOKIE_NAME) != null)
      {
         CookieUtil.deleteCookie(request, response, "/", AUTH_COOKIE_NAME);
      }
      
      return "redirect:/";
   }
   
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
   @RequestMapping(value="/user/idCheck", method=RequestMethod.POST)
   @ResponseBody
   public Response<Object> idCheck(HttpServletRequest request, HttpServletResponse response)
   {
      String userId = HttpUtil.get(request, "userId");
      Response<Object> ajaxResponse = new Response<Object>();
      
      if(!StringUtil.isEmpty(userId))//아이디값이 있을 때
      {
         if(userService.userSelect(userId) == null) //값이 없다
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
         logger.debug("[UserController] /user/idCheck response\n" + JsonUtil.toJsonPretty(ajaxResponse));
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
   @RequestMapping(value="/user/regForm", method=RequestMethod.GET)
   public String regForm(HttpServletRequest request, HttpServletResponse response)
   {
      String cookieUserId = CookieUtil.getHexValue(request, AUTH_COOKIE_NAME);
      
      if(!StringUtil.isEmpty(cookieUserId))//쿠키이름이 있다면
      {
         CookieUtil.deleteCookie(request, response, AUTH_COOKIE_NAME);
         //일단 지우고 index.jsp로 다시 돌아가 !   
         return "redirect:/";
      }
      else//공백이면
      { 
         return "/user/regForm";  //얘는 DispatcherServlet이 받는다 servlet-context.jsp가 jsp로 변환시캬준다
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
   @RequestMapping(value="/user/regProc", method=RequestMethod.POST)
   @ResponseBody
   public Response<Object> regProc(HttpServletRequest request, HttpServletResponse response)
   {
      String userId = HttpUtil.get(request, "userId");
      String userPwd = HttpUtil.get(request, "userPwd");
      String userName = HttpUtil.get(request, "userName");
      String userEmail = HttpUtil.get(request, "userEmail");
      
      Response<Object> ajaxResponse = new Response<Object>();
      
      if(!StringUtil.isEmpty(userId) && !StringUtil.isEmpty(userPwd) && !StringUtil.isEmpty(userName) && !StringUtil.isEmpty(userEmail))
      {
         if(userService.userSelect(userId) == null) //유저 테이블에서 셀렉트해왔을때 유저 아이디가 없으면(중복체크)
         {
            User user = new User();
            
            user.setUserId(userId);
            user.setUserPwd(userPwd);
            user.setUserName(userName);
            user.setUserEmail(userEmail);
            user.setStatus("Y");
            
            if(userService.userInsert(user) > 0)
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
         logger.debug("[UserController] /user/regProc response\n" + JsonUtil.toJsonPretty(ajaxResponse));
      }
      
      return ajaxResponse;
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
   @RequestMapping(value="/user/updateForm", method=RequestMethod.GET)
   public String updateForm(ModelMap model, HttpServletRequest request, HttpServletResponse response)
   {
      String cookieUserId = CookieUtil.getHexValue(request, AUTH_COOKIE_NAME);
      
      User user = userService.userSelect(cookieUserId);
      
      model.addAttribute("user", user);
      
      return "/user/updateForm";
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
   @RequestMapping(value="/user/updateProc", method=RequestMethod.POST)
   @ResponseBody
   public Response<Object> updateProc(HttpServletRequest request, HttpServletResponse response)
   {
      String cookieUserId = CookieUtil.getHexValue(request, AUTH_COOKIE_NAME);
      String userPwd = HttpUtil.get(request, "userPwd");
      String userName = HttpUtil.get(request, "userName");
      String userEmail = HttpUtil.get(request, "userEmail");
      
      Response<Object> ajaxResponse = new Response<Object>();
      
      if(!StringUtil.isEmpty(cookieUserId))
      {
         User user = userService.userSelect(cookieUserId);
         
         if(user != null)
         {
            if(StringUtil.equals(user.getStatus(), "Y"))
            {
               if(!StringUtil.isEmpty(userPwd) && !StringUtil.isEmpty(userName) && !StringUtil.isEmpty(userEmail))
               {
                  user.setUserPwd(userPwd);
                  user.setUserName(userName);
                  user.setUserEmail(userEmail);
                  
                  if(userService.userUpdate(user) > 0) //한 건 이상이 업데이트 되었을 때
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
         logger.debug("[UserController] /user/updateProc response\n" + JsonUtil.toJsonPretty(ajaxResponse));
      }
      
      return ajaxResponse;
   }
}
