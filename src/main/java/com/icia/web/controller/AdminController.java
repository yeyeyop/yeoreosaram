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
   
   ////////////////////////////////
   //회원정보 상세조회
   /*@RequestMapping("admin/admin2View")
   public String adminUpdate(@RequestParam String userId2, Model model) {
      model.addAttribute("dto?", adminDao.viewAdmin2(userId2));
      
      logger.info("클릭한 아이디 : " + userId2);
      return "/admin/admin2View";
   } */
   
   //회원 강제탈퇴
   @RequestMapping("admin/delete")
   public String adminDelete(@RequestParam String userId2, @RequestParam String userPwd2, Model model) {
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
   public String view(ModelMap model, HttpServletRequest request, HttpServletResponse response)
   {
      if(CookieUtil.getCookie(request, AUTH_COOKIE_NAME) != null)
      {
          CookieUtil.deleteCookie(request, response, "/", AUTH_COOKIE_NAME);
          
        //조회항목(1:작성자조회, 2:제목조회, 3:내용조회)
          // String searchType = HttpUtil.get(request, "searchType");
           String searchType = "";
           //조회값
           //String searchValue = HttpUtil.get(request, "searchValue");
           String searchValue = "";
           //현재 페이지
           long curPage = HttpUtil.get(request, "curPage", (long)1);
           //총 게시물 수
           long totalCount = 0;
           //게시물 리스트
           List<HiBoard> list = null;
           //페이징 객체
           Paging paging = null;
           //조회객체
           HiBoard search = new HiBoard();
           
           /*
           if(!StringUtil.isEmpty(searchType) && !StringUtil.isEmpty(searchValue))
           {
              search.setSearchType(searchType);
              search.setSearchValue(searchValue);
           }
           else
           {
              searchType = "";
              searchValue = "";
           }*/
           
           totalCount = hiBoardService.boardListCount2(search);
           
           logger.debug("totalCount : " + totalCount);
           logger.debug("curPage : " + curPage);
           
           if(totalCount > 0)
           {
              paging = new Paging("/admin/list", totalCount, LIST_COUNT, PAGE_COUNT, curPage, "curPage");
              
              paging.addParam("searchType", searchType);
              paging.addParam("searchValue", searchValue);
              paging.addParam("curPage", curPage);
              
              search.setStartRow(paging.getStartRow());
              search.setEndRow(paging.getEndRow());
              
              list = hiBoardService.boardList(search);
           }
           
           model.addAttribute("list", list);
           model.addAttribute("searchType", searchType);
           model.addAttribute("searchValue", searchValue);
           model.addAttribute("curPage", curPage);
           model.addAttribute("paging", paging);
          
      }
      
    
      return "/admin/list";
   }
   
 /*관리자용 게시물 조회
   @RequestMapping(value="/board/view")
   public String view(ModelMap model, HttpServletRequest request, HttpServletResponse response)
   {
      String cookieUserId = CookieUtil.getHexValue(request, AUTH_COOKIE_NAME);
      long hiBbsSeq = HttpUtil.get(request, "hiBbsSeq", (long)0);
      String searchType = HttpUtil.get(request, "searchType", "");
      String searchValue = HttpUtil.get(request, "searchValue", "");
      long curPage = HttpUtil.get(request, "curPage", (long)1);
      //본인글 확인 여부
      String boardMe = "N";
      
      HiBoard hiBoard = null;
      
      logger.debug("1111111111111111===================================================");
      logger.debug("===== hiBbsSeq [" + hiBbsSeq + "]==============================");
      
      
      if(hiBbsSeq > 0)
      {
         hiBoard = hiBoardService.boardView(hiBbsSeq);
         
         if(hiBoard != null && StringUtil.equals(hiBoard.getUserId(), cookieUserId))
         {
            boardMe = "Y";   //본인글인 경우
         }
      }
      model.addAttribute("hiBbsSeq", hiBbsSeq);
      model.addAttribute("hiBoard", hiBoard);
      model.addAttribute("boardMe", boardMe);
      model.addAttribute("searchType", searchType);
      model.addAttribute("searchValue", searchValue);
      model.addAttribute("curPage", curPage);
      
      long totalCount = 0;
      //게시물 리스트
      List<HiBoard> list = null;
      
      //게시물 답글 리스트
      List<HiBoard> replylist = null;
      
      //페이징 객체
      Paging paging = null;
      
      //조회객체
      HiBoard search = new HiBoard();
      
      if(!StringUtil.isEmpty(searchType) && !StringUtil.isEmpty(searchValue))
      {
         search.setSearchType(searchType);
         search.setSearchValue(searchValue);
      }
      else
      {
         searchType = "";
         searchValue = "";
      }
      
      totalCount = hiBoardService.boardListCount(search);
      
      logger.debug("totalCount : " + totalCount);
      logger.debug("curPage : " + curPage);
      
      if(totalCount > 0)
      {
         paging = new Paging("/board/view", totalCount, LIST_COUNT, PAGE_COUNT, curPage, "curPage");
         
         paging.addParam("searchType", searchType);
         paging.addParam("searchValue", searchValue);
         paging.addParam("curPage", curPage);
         
         search.setStartRow(paging.getStartRow());
         search.setEndRow(paging.getEndRow());
         
         list = hiBoardService.boardList(search);
         
         /////
         logger.debug("===================================================");
         logger.debug("===================== hiBoard bbs_seq [" + hiBoard.getHiBbsSeq() + "]======================================");
         replylist = hiBoardService.boardReplyList(hiBoard);
      }
      
      model.addAttribute("list", list);
      model.addAttribute("paging", paging);
      model.addAttribute("replylist", replylist);
      
      return "/board/view";
   }*/
   
   
}