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
import com.icia.web.model.Paging;
import com.icia.web.model.Qna;
import com.icia.web.model.QnaHiBoardFile;
import com.icia.web.model.Response;
import com.icia.web.model.User2;
import com.icia.web.service.QnaService;
import com.icia.web.service.UserService2;
import com.icia.web.util.CookieUtil;
import com.icia.web.util.HttpUtil;
import com.icia.web.util.JsonUtil;

@Controller("qnaController")
public class QnaController {
   
   private static Logger logger = LoggerFactory.getLogger(QnaController.class);
   
      //쿠키명
      @Value("#{env['auth.cookie.name']}")
      private String AUTH_COOKIE_NAME;
      
      //파일경로
      @Value("#{env['upload.save.dir']}")
      private String UPLOAD_SAVE_DIR;
      
      @Autowired
      private QnaService qnaService;
      
      
      @Autowired
      private UserService2 userService2;
      
      private static final int LIST_COUNT = 10;
      private static final int PAGE_COUNT = 10;
      
      //게시물 답변
      @RequestMapping(value="/board/replyProc3", method=RequestMethod.POST)
      @ResponseBody
      public Response<Object> replyProc3(MultipartHttpServletRequest request, HttpServletResponse response)
      {
         String cookieUserId = CookieUtil.getHexValue(request, AUTH_COOKIE_NAME);
         
         long qnaHiBbsSeq = HttpUtil.get(request, "qnaHiBbsSeq", (long)0);
         String qnaHiBbsTitle = HttpUtil.get(request, "qnaHiBbsTitle", "");
         String qnaHiBbsContent = HttpUtil.get(request, "qnaHiBbsContent", "");
         
         FileData fileData = HttpUtil.getFile(request, "qnaHiBbsFile", UPLOAD_SAVE_DIR);
         Response<Object> ajaxResponse = new Response<Object>();
         
         if(qnaHiBbsSeq > 0 && !StringUtil.isEmpty(qnaHiBbsTitle) && !StringUtil.isEmpty(qnaHiBbsContent))
         {
            Qna parentQna = qnaService.qnaSelect(qnaHiBbsSeq);      //댓글은 부모밑에 있기때문에 parent라고 써줌
            
            if(parentQna != null)
            {
               Qna qna = new Qna();
               
               qna.setUserId(cookieUserId);
               qna.setQnaHiBbsTitle(qnaHiBbsTitle);
               qna.setQnaHiBbsContent(qnaHiBbsContent);
               qna.setQnaHiBbsGroup(parentQna.getQnaHiBbsGroup());
               qna.setQnaHiBbsOrder(parentQna.getQnaHiBbsOrder() + 1);
               qna.setQnaHiBbsIndent(parentQna.getQnaHiBbsIndent() + 1);
               qna.setQnaHiBbsParent(qnaHiBbsSeq);
               //////여기 일단 두고 나중에 수정하자
               if(fileData != null && fileData.getFileSize() > 0)
               {
                  QnaHiBoardFile qnaHiBoardFile = new QnaHiBoardFile();
                  
                  qnaHiBoardFile.setQnaFileName(fileData.getFileName());
                  qnaHiBoardFile.setQnaFileOrgName(fileData.getFileOrgName());
                  qnaHiBoardFile.setQnaFileExt(fileData.getFileExt());
                  qnaHiBoardFile.setQnaFileSize(fileData.getFileSize());
                  
                  qna.setQnaHiBoardFile(qnaHiBoardFile);
               }
               
               try
               {
                  if(qnaService.qnaReplyInsert(qna) > 0)
                  {
                     ajaxResponse.setResponse(0, "Success", qna);
                  }
                  else
                  {
                     ajaxResponse.setResponse(500, "Internal Server Error");
                  }
               }
               catch(Exception e)
               {
                  logger.error("[QnaController] /board/replyProc3 Exception", e);
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
               logger.debug("[QnaController] /board/replyProc3 response\n" + JsonUtil.toJsonPretty(ajaxResponse));
            }
               
         return ajaxResponse;
      }
      
    //게시물 답변 폼
      @RequestMapping(value="/board/replyForm3", method=RequestMethod.POST)
      public String replyForm3(ModelMap model, HttpServletRequest request, HttpServletResponse response)
      {
         String cookieUserId = CookieUtil.getHexValue(request, AUTH_COOKIE_NAME);
         
         long qnaHiBbsSeq = HttpUtil.get(request, "qnaHiBbsSeq", (long)0);
         
         String searchType = HttpUtil.get(request, "searchType");
         String searchValue = HttpUtil.get(request, "searchValue");
         long curPage = HttpUtil.get(request, "curPage", (long)1);
         
         Qna qna = null;
         User2 user2 = null;
         
         if(qnaHiBbsSeq > 0)
         {
            qna = qnaService.qnaSelect(qnaHiBbsSeq);
            
            if(qna != null)
            {
               user2 = userService2.userSelect2(cookieUserId);
            }
         }
         
         model.addAttribute("searchType", searchType);   //첫번째 매개변수는 board/replyForm에서 쓸 변수이름, 두번째 매개변수는 이 메소드에서 받은 값
         model.addAttribute("searchValue", searchValue);
         model.addAttribute("curPage", curPage);
         model.addAttribute("qna", qna);
         model.addAttribute("user2", user2);
                     
         return "/board/replyForm3";
      }
      
      //게시물 삭제
      @RequestMapping(value="/board/delete3", method=RequestMethod.POST)
      @ResponseBody
      public Response<Object> Delete3(HttpServletRequest request, HttpServletResponse response)
      {
         String cookieUserId = CookieUtil.getHexValue(request, AUTH_COOKIE_NAME);
         long qnaHiBbsSeq = HttpUtil.get(request, "qnaHiBbsSeq", (long)0);         //View.jsp에서 ajax를 통해 hiBbsSeq를 요청
         
         Response<Object> ajaxResponse = new Response<Object>();
         
         if(qnaHiBbsSeq > 0)
         {
            Qna qna = qnaService.qnaSelect(qnaHiBbsSeq);
            
            if(qna != null)
            {
               if(StringUtil.equals(qna.getUserId(), cookieUserId))   //현재 로그인 한 아이디와 게시판에 등록되어있는 아이디가 같은지 확인
               {
                  try
                  {
                     if(qnaService.qnaAnswersCount(qna.getQnaHiBbsSeq()) > 0)
                     {
                        ajaxResponse.setResponse(-999, "Answers exist and be delete");
                     }
                     else
                     {
                                                         
                        if(qnaService.qnaDelete(qna.getQnaHiBbsSeq()) > 0)
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
                     logger.error("[QnaController] /board/delete3 Exception", e);
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
               logger.debug("[QnaController] /board/delete3 response\n" + JsonUtil.toJsonPretty(ajaxResponse));
            }
         
         return ajaxResponse;
         
      }
      
    //게시물 조회
         @RequestMapping(value="/board/view3")
         public String view3(ModelMap model, HttpServletRequest request, HttpServletResponse response)
         {
            String cookieUserId = CookieUtil.getHexValue(request, AUTH_COOKIE_NAME);
            long qnaHiBbsSeq = HttpUtil.get(request, "qnaHiBbsSeq", (long)0);
            String searchType = HttpUtil.get(request, "searchType", "");
            String searchValue = HttpUtil.get(request, "searchValue", "");
            long curPage = HttpUtil.get(request, "curPage", (long)1);
            //본인글 확인 여부
            String boardMe = "N";
            
            Qna qna = null;
            
            logger.debug("1111111111111111===================================================");
            logger.debug("===== qnaHiBbsSeq [" + qnaHiBbsSeq + "]==============================");
            
            
            if(qnaHiBbsSeq > 0)
            {
               qna = qnaService.qnaView(qnaHiBbsSeq);
               
               if(qna != null && StringUtil.equals(qna.getUserId(), cookieUserId))
               {
                  boardMe = "Y";   //본인글인 경우
               }
            }
            model.addAttribute("qnaHiBbsSeq", qnaHiBbsSeq);
            model.addAttribute("qna", qna);
            model.addAttribute("boardMe", boardMe);
            model.addAttribute("searchType", searchType);
            model.addAttribute("searchValue", searchValue);
            model.addAttribute("curPage", curPage);
         
            return "/board/view3";
         }
         
         //게시물 리스트
         @RequestMapping(value="/board/customerList")
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
            List<Qna> list = null;
            //페이징 객체
            Paging paging = null;
            //조회객체
            Qna search = new Qna();
            
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
            
            totalCount = qnaService.qnaListCount(search);
            
            logger.debug("totalCount : " + totalCount);
            logger.debug("curPage : " + curPage);
            
            if(totalCount > 0)
            {
               paging = new Paging("/board/customerList", totalCount, LIST_COUNT, PAGE_COUNT, curPage, "curPage");
               
               paging.addParam("searchType", searchType);
               paging.addParam("searchValue", searchValue);
               paging.addParam("curPage", curPage);
               
               search.setStartRow(paging.getStartRow());
               search.setEndRow(paging.getEndRow());
               
               list = qnaService.qnaList(search);
            }
            
            model.addAttribute("list", list);
            model.addAttribute("searchType", searchType);
            model.addAttribute("searchValue", searchValue);
            model.addAttribute("curPage", curPage);
            model.addAttribute("paging", paging);
            
            return "/board/customerList";
         }
         
         //게시물 쓰기
         @RequestMapping(value="/board/writeForm2")
         public String writeForm2(ModelMap model, HttpServletRequest request, HttpServletResponse response)
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
            
            return "/board/writeForm2";
         }
         
         //게시물 등록
         @RequestMapping(value="/board/writeProc3", method=RequestMethod.POST)
         @ResponseBody
         public Response<Object> writeProc3(MultipartHttpServletRequest request, HttpServletResponse response)
         {
            String cookieUserId = CookieUtil.getHexValue(request, AUTH_COOKIE_NAME);
            String qnaHiBbsTitle = HttpUtil.get(request, "qnaHiBbsTitle", "");
            String qnaHiBbsContent = HttpUtil.get(request, "qnaHiBbsContent", "");
            FileData fileData = HttpUtil.getFile(request, "qnaHiBbsFile", UPLOAD_SAVE_DIR);
            
            Response<Object> ajaxResponse = new Response<Object>();
            
            if(!StringUtil.isEmpty(qnaHiBbsTitle) && !StringUtil.isEmpty(qnaHiBbsContent))
            {
               Qna qna = new Qna();
               
               qna.setUserId(cookieUserId);
               qna.setQnaHiBbsTitle(qnaHiBbsTitle);
               qna.setQnaHiBbsContent(qnaHiBbsContent);
               
               if(fileData != null && fileData.getFileSize() > 0)
               {
                  QnaHiBoardFile qnaHiBoardFile = new QnaHiBoardFile();
                  
                  qnaHiBoardFile.setQnaFileName(fileData.getFileName());
                  qnaHiBoardFile.setQnaFileOrgName(fileData.getFileOrgName());
                  qnaHiBoardFile.setQnaFileExt(fileData.getFileExt());
                  qnaHiBoardFile.setQnaFileSize(fileData.getFileSize());
                  
                  qna.setQnaHiBoardFile(qnaHiBoardFile);
               }
               
               try
               {
                  if(qnaService.qnaInsert(qna) > 0)
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
                  logger.error("[QnaController]/board/writeProc3 Exception", e);
                  ajaxResponse.setResponse(500, "Internal Server Error");
               }
            }
            else
            {
               ajaxResponse.setResponse(400, "Bad Request");
            }
            
               if(logger.isDebugEnabled())
               {
                  logger.debug("[QnaController] /board/writeProc3 response\n" + JsonUtil.toJsonPretty(ajaxResponse));
               }
            
            return ajaxResponse;
         }
}