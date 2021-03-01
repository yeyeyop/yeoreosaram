package com.icia.web.controller;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.icia.common.util.StringUtil;
import com.icia.web.model.Response;
import com.icia.web.dao.AdminDao;
import com.icia.web.model.Admin;
import com.icia.web.model.HiBoard;
import com.icia.web.model.Paging;
import com.icia.web.service.AdminService;
import com.icia.web.service.HiBoardService;
import com.icia.web.util.CookieUtil;
import com.icia.web.util.HttpUtil;
import com.icia.web.util.JsonUtil;

/*관리자 컨트롤러*/
@Controller("adminController")
public class AdminController 
{
   
	private static final int LIST_COUNT = 10;
	private static final int PAGE_COUNT = 10;
	
   //로그
   private static Logger logger = LoggerFactory.getLogger(AdminController.class);
   
   // 쿠키명
   @Value("#{env['auth.cookie.name']}")
   private String AUTH_COOKIE_NAME;
   
   //서비스 호출   
   @Autowired
   private AdminService adminService;
   private HiBoardService hiBoardService;
   
 
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
   @RequestMapping(value="/admin/index", method=RequestMethod.GET)
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
   
   //회원 리스트
   @Inject
   AdminDao adminDao;
   
   @RequestMapping(value = "admin/adminList")//, method = RequestMethod.GET)
   public String user2List (Model model) {
      List<Admin> list = adminDao.adminList();
      model.addAttribute("list", list);
      
      return "/admin/adminList";
   }
  
   /*원본
   @RequestMapping(value="/admin/list", method=RequestMethod.GET)
   public String view(HttpServletRequest request, HttpServletResponse response)
   {
      if(CookieUtil.getCookie(request, AUTH_COOKIE_NAME) != null)
      {
          CookieUtil.deleteCookie(request, response, "/", AUTH_COOKIE_NAME);
      }
      return "/admin/list";
   }*/
   
   @RequestMapping(value="/admin/list")
   public String list(ModelMap model, HttpServletRequest request, HttpServletResponse response)
   {
      //if(CookieUtil.getCookie(request, AUTH_COOKIE_NAME) != null)
     // {
          //CookieUtil.deleteCookie(request, response, "/", AUTH_COOKIE_NAME);
        
        //게시물 리스트
          
	      Admin admin = new Admin();
	   
          List<Admin> list = adminDao.testList(admin);
          
           model.addAttribute("list", list);
           
     // }
      
      return "/admin/list";
   }
   /*
   @RequestMapping(value="/admin/view")
   public String view(ModelMap model, HttpServletRequest request, HttpServletResponse response)
   {
      //if(CookieUtil.getCookie(request, AUTH_COOKIE_NAME) != null)
     // {
          //CookieUtil.deleteCookie(request, response, "/", AUTH_COOKIE_NAME);
        
        //게시물 리스트
          
	      Admin admin = new Admin();
	      long hiBbsSeq = HttpUtil.get(request, "hiBbsSeq", (long)0);
	        
          
           if(hiBbsSeq > 0)
           {
        	   logger.debug("===== 게시물 번호 [" + hiBbsSeq + "]==============================");
             admin = adminService.adminView(hiBbsSeq);
             //System.out.println("게시물 아이디 는"+admin.getAdminId());
       
           }else {
        	   logger.debug("===== 게시물 보기 오류==============================");
           }
           
           
           
           model.addAttribute("hiBbsSeq", hiBbsSeq);
           
           
     // }
      
      return "/admin/view";
   }*/
   
 /*게시물 조회*/
   @RequestMapping(value="/admin/view")
   public String view(ModelMap model, HttpServletRequest request, HttpServletResponse response)
   {
      String cookieUserId = CookieUtil.getHexValue(request, AUTH_COOKIE_NAME);
      long hiBbsSeq = HttpUtil.get(request, "hiBbsSeq", (long)0);

      //본인글 확인 여부
      String boardMe = "N";
      
      Admin admin = null;
      
      logger.debug("1111111111111111===================================================");
      logger.debug("===== hiBbsSeq [" + hiBbsSeq + "]==============================");
      
      
      //if(hiBbsSeq > 0)
      //{
         admin = adminService.adminView(hiBbsSeq);
         System.out.println("게시물 번호" + admin.getHiBbsSeq());
         System.out.println("게시물 제목" + admin.getHiBbsTitle());
         System.out.println("게시물 내용" + admin.getHiBbsContent());
         System.out.println("게시물 작성자" + admin.getAdminId());
         
      //}
      model.addAttribute("hiBbsSeq", hiBbsSeq);
      model.addAttribute("admin", admin);

      
      long totalCount = 0;
      //게시물 리스트
      List<Admin> list = adminDao.testList(admin);
      
      //게시물 답글 리스트
      List<Admin> replylist = null;
      
      

        replylist = adminService.testReplyList(admin);

      
        model.addAttribute("replylist", replylist);
      
      return "/admin/view";
   }

   @RequestMapping(value="/admin/adminCustomerList")
   public String adminCustomerList(ModelMap model, HttpServletRequest request, HttpServletResponse response)
   {
      //if(CookieUtil.getCookie(request, AUTH_COOKIE_NAME) != null)
     // {
          //CookieUtil.deleteCookie(request, response, "/", AUTH_COOKIE_NAME);
        
        //게시물 리스트
          
	      Admin admin = new Admin();
	   
          List<Admin> adminCustomerList = adminDao.qList(admin);
          
           model.addAttribute("adminCustomerList", adminCustomerList);
           
     // }
      
      return "/admin/adminCustomerList";
   }
   ////////////////////////////////
   
   //회원 강제탈퇴, 강탈 페이지로 연결
   @RequestMapping("admin/adminDelete")
   public String adminDelete() {
      
      return "/admin/adminDelete";
   }
   
   //회원 강제탈퇴 버튼 누를 시
   @RequestMapping("admin/adminDeleteForm")
   public ModelAndView adminDelete(String userId2) throws Exception{
      //유저 아이디 강탈하기 위해 담음
      Admin admin = new Admin();
      admin.setUserId2(userId2);
      
      //회원탈퇴 체크를 하기 위한 메소드, 탈퇴시키려는 회원의 아이디가 있는지 검사 한 후 result에 저장
      adminDao.adminDelete(admin);
      
      ModelAndView mav = new ModelAndView();
      
      if(admin.getUserId2() != null) {   //회원 강탈 성공
         mav.setViewName("/admin/adminDelete");
         mav.addObject("message", "회원 강제탈퇴 완료");
      }
      
      else {
         //mav.setViewName("/admin/adminDelete");
         mav.addObject("message", "목록에 없는 회원입니다. 다시 확인 바랍니다.");
      }
      
      return mav;
   }

   
}