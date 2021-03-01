package com.icia.web.controller;


import java.util.List;





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
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.icia.common.model.FileData;
import com.icia.common.util.StringUtil;
import com.icia.web.model.HiBoard;
import com.icia.web.model.HiBoardFile;
import com.icia.web.model.Paging;
import com.icia.web.model.Response;

import com.icia.web.model.User2;
import com.icia.web.service.HiBoardService;

import com.icia.web.service.UserService2;
import com.icia.web.util.CookieUtil;
import com.icia.web.util.HttpUtil;
import com.icia.web.util.JsonUtil;

@Controller("hiBoardController")
public class HiBoardController 
{
   private static Logger logger = LoggerFactory.getLogger(HiBoardController.class);
   
   //쿠키명
   @Value("#{env['auth.cookie.name']}")
   private String AUTH_COOKIE_NAME;
   
   //파일경로
   @Value("#{env['upload.save.dir']}")
   private String UPLOAD_SAVE_DIR;
   
   @Autowired
   private HiBoardService hiBoardService;
   
   
   @Autowired
   private UserService2 userService2;
   
   private static final int LIST_COUNT = 10;
   private static final int PAGE_COUNT = 10;
   
   //게시물 수정
   @RequestMapping(value="/board/updateProc", method=RequestMethod.POST)
   @ResponseBody
   public Response<Object> updateProc(MultipartHttpServletRequest request, HttpServletResponse response)
   {
      String cookieUserId = CookieUtil.getHexValue(request, AUTH_COOKIE_NAME);
      
      long hiBbsSeq = HttpUtil.get(request, "hiBbsSeq", (long)0);
       String hiBbsTitle = HttpUtil.get(request, "hiBbsTitle", "");
       String hiBbsContent = HttpUtil.get(request, "hiBbsContent", "");
       FileData fileData = HttpUtil.getFile(request, "hiBbsFile", UPLOAD_SAVE_DIR);   //이전 페이지의 파일 정보를 요청해서 받는다
       
       Response<Object> ajaxResponse = new Response<Object>();
       
       if(hiBbsSeq > 0 && !StringUtil.isEmpty(hiBbsTitle) && ! StringUtil.isEmpty(hiBbsContent))
       {
          HiBoard hiBoard = hiBoardService.boardSelect(hiBbsSeq);
          
          if(hiBoard != null)
          {
             if(StringUtil.equals(hiBoard.getUserId(), cookieUserId))
             {
                hiBoard.setHiBbsSeq(hiBbsSeq);
                hiBoard.setHiBbsTitle(hiBbsTitle);
                hiBoard.setHiBbsContent(hiBbsContent);
                
                if(fileData != null && fileData.getFileSize() > 0)
                {
                   HiBoardFile hiBoardFile = new HiBoardFile();
                   
                   hiBoardFile.setFileName(fileData.getFileName());      //요청해서 받은 파일 정보를 hiBoardFile에 넣어준다
                   hiBoardFile.setFileOrgName(fileData.getFileOrgName());
                   hiBoardFile.setFileExt(fileData.getFileExt());
                   hiBoardFile.setFileSize(fileData.getFileSize());
                   
                   hiBoard.setHiBoardFile(hiBoardFile);
                }
                
                try
                {
                   if(hiBoardService.boardUpdate(hiBoard) > 0)
                   {
                      ajaxResponse.setResponse(0, "Success");
                   }
                   else
                   {
                      ajaxResponse.setResponse(500, "Inernal Server Error");
                   }
                }
                catch(Exception e)
                {
                   logger.error("[HiBoardController] /board/updateProc Exception", e);
                   ajaxResponse.setResponse(500, "Inernal Server Error");
                }
             }
             else
             {
                ajaxResponse.setResponse(404, "Not Found");
             }
          }
          else
          {
             ajaxResponse.setResponse(404, "Not Found");
          }
       }
       else
       {
          ajaxResponse.setResponse(400, "Bad Request");
       }
       
       if(logger.isDebugEnabled())
       {
          logger.debug("[HiBoardController] /board/updateProc response\n" + JsonUtil.toJsonPretty(ajaxResponse));
       }
       
       return ajaxResponse;
   }
   
   //게시물 수정 폼
   @RequestMapping(value="/board/updateForm")
   public String updateForm(ModelMap model, HttpServletRequest request, HttpServletResponse response)
   {
      String cookieUserId = CookieUtil.getHexValue(request, AUTH_COOKIE_NAME);
      
      long hiBbsSeq = HttpUtil.get(request, "hiBbsSeq", (long)0);
      String searchType = HttpUtil.get(request, "searchType", "");
      String searchValue = HttpUtil.get(request, "searchValue", "");
      long curPage = HttpUtil.get(request, "curPage", (long)1);
      
      HiBoard hiBoard = null;
      User2 user2 = null;
      
      if(hiBbsSeq > 0)
      {
         hiBoard = hiBoardService.boardView(hiBbsSeq);
         
         if(hiBoard != null)
         {
            if(StringUtil.equals(hiBoard.getUserId(), cookieUserId))
            {
               user2 = userService2.userSelect2(cookieUserId);
            }
            else
            {
               hiBoard = null;
            }
         }
      }
         model.addAttribute("searchType", searchType);
         model.addAttribute("searchValue", searchValue);
         model.addAttribute("curPage", curPage);
         model.addAttribute("hiBoard", hiBoard);
         model.addAttribute("user2", user2);
      
      return "/board/updateForm";
   }
   
   //게시물 답변
   @RequestMapping(value="/board/replyProc", method=RequestMethod.POST)
   @ResponseBody
   public Response<Object> replyProc(MultipartHttpServletRequest request, HttpServletResponse response)
   {
      String cookieUserId = CookieUtil.getHexValue(request, AUTH_COOKIE_NAME);
      
      long hiBbsSeq = HttpUtil.get(request, "hiBbsSeq", (long)0);
      //String hiBbsTitle = HttpUtil.get(request, "hiBbsTitle", "");
      String hiBbsContent = HttpUtil.get(request, "hiBbsContent", "");
      
      FileData fileData = HttpUtil.getFile(request, "hiBbsFile", UPLOAD_SAVE_DIR);
      Response<Object> ajaxResponse = new Response<Object>();
      
      if(hiBbsSeq > 0 /*&& !StringUtil.isEmpty(hiBbsTitle)*/ && !StringUtil.isEmpty(hiBbsContent))
      {
         HiBoard parentHiBoard = hiBoardService.boardSelect(hiBbsSeq);      //댓글은 부모밑에 있기때문에 parent라고 써줌
         
         if(parentHiBoard != null)
         {
            HiBoard hiBoard = new HiBoard();
            
            hiBoard.setUserId(cookieUserId);
           // hiBoard.setHiBbsTitle(hiBbsTitle);
            hiBoard.setHiBbsContent(hiBbsContent);
            hiBoard.setHiBbsGroup(parentHiBoard.getHiBbsGroup());
            hiBoard.setHiBbsOrder(parentHiBoard.getHiBbsOrder() + 1);
            hiBoard.setHiBbsIndent(parentHiBoard.getHiBbsIndent() + 1);
            hiBoard.setHiBbsParent(hiBbsSeq);
            
            if(fileData != null && fileData.getFileSize() > 0)
            {
               HiBoardFile hiBoardFile = new HiBoardFile();
               
               hiBoardFile.setFileName(fileData.getFileName());
               hiBoardFile.setFileOrgName(fileData.getFileOrgName());
               hiBoardFile.setFileExt(fileData.getFileExt());
               hiBoardFile.setFileSize(fileData.getFileSize());
               
               hiBoard.setHiBoardFile(hiBoardFile);
            }
            
            try
            {
               if(hiBoardService.boardReplyInsert(hiBoard) > 0)
               {
                  ajaxResponse.setResponse(0, "Success", hiBoard);
               }
               else
               {
                  ajaxResponse.setResponse(500, "Internal Server Error");
               }
            }
            catch(Exception e)
            {
               logger.error("[HiBoardController] /board.replyProc Exception", e);
               ajaxResponse.setResponse(500, "Internal Server Error");
            }         
         }
         else
         {
            ajaxResponse.setResponse(404, "Not Found");
         }
         
      }
      else
      {
         ajaxResponse.setResponse(400, "Bad Request");
      }
      
      if(logger.isDebugEnabled())
         {
            logger.debug("[HiBoardController] /board/replyProc response\n" + JsonUtil.toJsonPretty(ajaxResponse));
         }
            
      return ajaxResponse;
   }
   
   
   //view테이블 안에 댓글달기
   @RequestMapping(value="/board/replyApply", method=RequestMethod.POST)
   @ResponseBody
   public Response<Object> replyApply(MultipartHttpServletRequest request, HttpServletResponse response)
   {
      String cookieUserId = CookieUtil.getHexValue(request, AUTH_COOKIE_NAME);
      
      long hiBbsSeq = HttpUtil.get(request, "hiBbsSeq", (long)0);
      String hiBbsContent = HttpUtil.get(request, "hiBbsContent", "");
      
      FileData fileData = HttpUtil.getFile(request, "hiBbsFile", UPLOAD_SAVE_DIR);
      Response<Object> ajaxResponse = new Response<Object>();
      
      if(hiBbsSeq > 0 && !StringUtil.isEmpty(hiBbsContent))
      {
         HiBoard parentHiBoard = hiBoardService.boardSelect(hiBbsSeq);      //댓글은 부모밑에 있기때문에 parent라고 써줌
         
         if(parentHiBoard != null)
         {
            HiBoard hiBoard = new HiBoard();
            
            hiBoard.setUserId(cookieUserId);
            hiBoard.setHiBbsContent(hiBbsContent);
            hiBoard.setHiBbsGroup(parentHiBoard.getHiBbsGroup());
            hiBoard.setHiBbsOrder(parentHiBoard.getHiBbsOrder() + 1);
            hiBoard.setHiBbsIndent(parentHiBoard.getHiBbsIndent() + 1);
            hiBoard.setHiBbsParent(hiBbsSeq);
            
            if(fileData != null && fileData.getFileSize() > 0)
            {
               HiBoardFile hiBoardFile = new HiBoardFile();
               
               hiBoardFile.setFileName(fileData.getFileName());
               hiBoardFile.setFileOrgName(fileData.getFileOrgName());
               hiBoardFile.setFileExt(fileData.getFileExt());
               hiBoardFile.setFileSize(fileData.getFileSize());
               
               hiBoard.setHiBoardFile(hiBoardFile);
            }
            
            try
            {
               if(hiBoardService.boardReplyInsert(hiBoard) > 0)
               {
                  ajaxResponse.setResponse(0, "Success");
               }
               else
               {
                  ajaxResponse.setResponse(500, "Internal Server Error");
               }
            }
            catch(Exception e)
            {
               logger.error("[HiBoardController] /board.replyProc Exception", e);
               ajaxResponse.setResponse(500, "Internal Server Error");
            }         
         }
         else
         {
            ajaxResponse.setResponse(404, "Not Found");
         }
         
      }
      else
      {
         ajaxResponse.setResponse(400, "Bad Request");
      }
      
      if(logger.isDebugEnabled())
         {
            logger.debug("[HiBoardController] /board/replyProc response\n" + JsonUtil.toJsonPretty(ajaxResponse));
         }
            
      return ajaxResponse;
   }
   
   
   
   
   //게시물 답변 폼
   @RequestMapping(value="/board/replyForm", method=RequestMethod.POST)
   public String replyForm(ModelMap model, HttpServletRequest request, HttpServletResponse response)
   {
      String cookieUserId = CookieUtil.getHexValue(request, AUTH_COOKIE_NAME);
      
      long hiBbsSeq = HttpUtil.get(request, "hiBbsSeq", (long)0);
      
      String searchType = HttpUtil.get(request, "searchType");
      String searchValue = HttpUtil.get(request, "searchValue");
      long curPage = HttpUtil.get(request, "curPage", (long)1);
      
      HiBoard hiBoard = null;
      User2 user2 = null;
      
      if(hiBbsSeq > 0)
      {
         hiBoard = hiBoardService.boardSelect(hiBbsSeq);
         
         if(hiBoard != null)
         {
            user2 = userService2.userSelect2(cookieUserId);
         }
      }
      
      model.addAttribute("searchType", searchType);   //첫번째 매개변수는 board/replyForm에서 쓸 변수이름, 두번째 매개변수는 이 메소드에서 받은 값
      model.addAttribute("searchValue", searchValue);
      model.addAttribute("curPage", curPage);
      model.addAttribute("hiBoard", hiBoard);
      model.addAttribute("user2", user2);
                  
      return "/board/replyForm";
   }
   
   //게시물 삭제
   @RequestMapping(value="/board/delete", method=RequestMethod.POST)
   @ResponseBody
   public Response<Object> Delete(HttpServletRequest request, HttpServletResponse response)
   {
      String cookieUserId = CookieUtil.getHexValue(request, AUTH_COOKIE_NAME);
      long hiBbsSeq = HttpUtil.get(request, "hiBbsSeq", (long)0);         //View.jsp에서 ajax를 통해 hiBbsSeq를 요청
      
      Response<Object> ajaxResponse = new Response<Object>();
      
      if(hiBbsSeq > 0)
      {
         HiBoard hiBoard = hiBoardService.boardSelect(hiBbsSeq);
         
         if(hiBoard != null)
         {
            if(StringUtil.equals(hiBoard.getUserId(), cookieUserId))   //현재 로그인 한 아이디와 게시판에 등록되어있는 아이디가 같은지 확인
            {
               try
               {
                  if(hiBoardService.boardAnswersCount(hiBoard.getHiBbsSeq()) > 0)
                  {
                     ajaxResponse.setResponse(-999, "Answers exist and be delete");
                  }
                  else
                  {
                                                      
                     if(hiBoardService.boardDelete(hiBoard.getHiBbsSeq()) > 0)
                     {
                        ajaxResponse.setResponse(0, "Seccess");
                     }
                     else
                     {
                        ajaxResponse.setResponse(500, "Internal Server Error");
                     }                  
                  }
               }
               catch(Exception e)
               {
                  logger.error("[HiBoardController] /board/delete Exception", e);
                  ajaxResponse.setResponse(500, "internal Server Error");
               }
            }
            else
            {
               ajaxResponse.setResponse(400, "Not Found");
            }
         }
         else
         {
            ajaxResponse.setResponse(400, "Not Found");
         }
      }
      else
      {
         ajaxResponse.setResponse(400, "Bad Request");
      }
      
      if(logger.isDebugEnabled())
         {
            logger.debug("[HiBoardController] /board/delete response\n" + JsonUtil.toJsonPretty(ajaxResponse));
         }
      
      return ajaxResponse;
      
      
   }
   
   
   
   //게시물 조회
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
      }
   
   
   
   
   //게시물 쓰기
   @RequestMapping(value="/board/writeForm")
   public String writeForm(ModelMap model, HttpServletRequest request, HttpServletResponse response)
   {
      //쿠키값
      String cookieUserId = CookieUtil.getHexValue(request, AUTH_COOKIE_NAME);
      String searchType = HttpUtil.get(request, "searchType");
      String searchValue = HttpUtil.get(request, "searchValue");
      long curPage = HttpUtil.get(request, "curPage", (long)1);
      
      //사용자 정보 조회
      User2 user2 = userService2.userSelect2(cookieUserId);      //사용자 인증단에서 이미 검증되고 넘어온거임
      
      model.addAttribute("searchType", searchType);
      model.addAttribute("searchValue", searchValue);
      model.addAttribute("curPage", curPage);
      model.addAttribute("user2", user2);
      
      return "/board/writeForm";
   }
   
   //게시물 등록
   @RequestMapping(value="/board/writeProc", method=RequestMethod.POST)
   @ResponseBody
   public Response<Object> writeProc(MultipartHttpServletRequest request, HttpServletResponse response)
   {
      String cookieUserId = CookieUtil.getHexValue(request, AUTH_COOKIE_NAME);
      String hiBbsTitle = HttpUtil.get(request, "hiBbsTitle", "");
      String hiBbsContent = HttpUtil.get(request, "hiBbsContent", "");
      FileData fileData = HttpUtil.getFile(request, "hiBbsFile", UPLOAD_SAVE_DIR);
      
      Response<Object> ajaxResponse = new Response<Object>();
      
      if(!StringUtil.isEmpty(hiBbsTitle) && !StringUtil.isEmpty(hiBbsContent))
      {
         HiBoard hiBoard = new HiBoard();
         
         hiBoard.setUserId(cookieUserId);
         hiBoard.setHiBbsTitle(hiBbsTitle);
         hiBoard.setHiBbsContent(hiBbsContent);
         
         if(fileData != null && fileData.getFileSize() > 0)
         {
            HiBoardFile hiBoardFile = new HiBoardFile();
            
            hiBoardFile.setFileName(fileData.getFileName());
            hiBoardFile.setFileOrgName(fileData.getFileOrgName());
            hiBoardFile.setFileExt(fileData.getFileExt());
            hiBoardFile.setFileSize(fileData.getFileSize());
            
            hiBoard.setHiBoardFile(hiBoardFile);
         }
         
         try
         {
            if(hiBoardService.boardInsert(hiBoard) > 0)
            {
               ajaxResponse.setResponse(0, "Success");
            }
            else
            {
               ajaxResponse.setResponse(500, "Internal Server Error");
            }
         }
         catch(Exception e)
         {
            logger.error("[HiBoardController]/board/writeProc Exception", e);
            ajaxResponse.setResponse(500, "Internal Server Error");
         }
      }
      else
      {
         ajaxResponse.setResponse(400, "Bad Request");
      }
      
         if(logger.isDebugEnabled())
         {
            logger.debug("[HiBoardController] /board/writeProc response\n" + JsonUtil.toJsonPretty(ajaxResponse));
         }
      
      return ajaxResponse;
   }
   
   //게시물 리스트
   @RequestMapping(value="/board/list")
   public String list(ModelMap model, HttpServletRequest request, HttpServletResponse response)
   {
      //조회항목(1:작성자조회, 2:제목조회, 3:내용조회)
      String searchType = HttpUtil.get(request, "searchType");
      //조회값
      String searchValue = HttpUtil.get(request, "searchValue");
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
      
      totalCount = hiBoardService.boardListCount2(search);
      
      logger.debug("totalCount : " + totalCount);
      logger.debug("curPage : " + curPage);
      
      if(totalCount > 0)
      {
         paging = new Paging("/board/list", totalCount, LIST_COUNT, PAGE_COUNT, curPage, "curPage");
         
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
      
      return "/board/list";
   }

 //게시물 답변 삭제
   @RequestMapping(value="/board/replyDelete", method=RequestMethod.POST)
   @ResponseBody
   public Response<Object> ReplyDelete(HttpServletRequest request, HttpServletResponse response)
   {
      String cookieUserId = CookieUtil.getHexValue(request, AUTH_COOKIE_NAME);
      long hiBbsSeq = HttpUtil.get(request, "hiBbsSeq", (long)0);  
      //View.jsp에서 ajax를 통해 hiBbsSeq를 요청
      
      Response<Object> ajaxResponse = new Response<Object>();
      
      if(hiBbsSeq > 0)
      {
         HiBoard parentHiBoard = hiBoardService.boardSelect(hiBbsSeq);
         
         if(parentHiBoard != null)
         {
            if(StringUtil.equals(parentHiBoard.getUserId(), cookieUserId))   //현재 로그인 한 아이디와 게시판에 등록되어있는 아이디가 같은지 확인
            {
               try
               {
                 
                                                      
                     if(hiBoardService.boardReplyDelete(parentHiBoard.getHiBbsSeq(), parentHiBoard.getHiBbsOrder()) > 0)
                     {
                        ajaxResponse.setResponse(0, "Seccess");
                     }
                     else
                     {
                        ajaxResponse.setResponse(500, "Internal Server Error");
                     }                  
                  
               }
               catch(Exception e)
               {
                  logger.error("[HiBoardController] /board/replyDelete Exception", e);
                  ajaxResponse.setResponse(500, "internal Server Error");
               }
            }
            else
            {
               ajaxResponse.setResponse(400, "Not Found");
            }
         }
         else
         {
            ajaxResponse.setResponse(400, "Not Found");
         }
      }
      else
      {
         ajaxResponse.setResponse(400, "Bad Request");
      }
      
      if(logger.isDebugEnabled())
         {
            logger.debug("[HiBoardController] /board/replyDelete response\n" + JsonUtil.toJsonPretty(ajaxResponse));
            //logger.debug("댓글 삭제 게시물 번호는 (아마 원래 본글 일꺼야!!) : "+ hiBbsSeq);
         }
      //else {
        //	logger.debug("댓글 삭제 게시물 번호는 : "+ hiBbsSeq);
         //}
      
      return ajaxResponse;
      
      
   }
 
   
}