package com.icia.web.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.icia.common.util.FileUtil;
import com.icia.web.dao.HiBoardDao;
import com.icia.web.model.HiBoard;
import com.icia.web.model.HiBoardFile;


@Service("hiBoardService")
public class HiBoardService 
{
   private static Logger logger = LoggerFactory.getLogger(HiBoardService.class);
   
   //파일 저장 디렉토리
   @Value("#{env['upload.save.dir']}")
   private String UPLOAD_SAVE_DIR;
   
   @Autowired
   private HiBoardDao hiBoardDao;
   
   
   //게시물 삭제(파일이 있는 경우 같이 삭제)
   @Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
   public int boardDelete(long hiBbsSeq) throws Exception
   {
      int count = 0;
      
      HiBoard hiBoard = boardView(hiBbsSeq);
      
      if(hiBoard != null)
      {
         count = hiBoardDao.boardDelete(hiBbsSeq);
         
         if(count > 0)
         {
            HiBoardFile hiBoardFile = hiBoard.getHiBoardFile();
            
            if(hiBoardFile != null) //첨부파일이 있다는 의미
            {
               if(hiBoardDao.boardFileDelete(hiBbsSeq) > 0)
               {
                  logger.debug("=======================delete file : " + UPLOAD_SAVE_DIR + FileUtil.getFileSeparator() + hiBoardFile.getFileName());
                  FileUtil.deleteFile(UPLOAD_SAVE_DIR + FileUtil.getFileSeparator() + hiBoardFile.getFileName());
                  
               }
            }
         }
      }
      
      return count;
   }
   
   //총 게시물 수
   public long boardListCount(HiBoard hiBoard)
   {
      long count = 0;
      
      try
      {
         count = hiBoardDao.boardListCount(hiBoard);
      }
      catch(Exception e)
      {
         logger.error("[HiBoardService] boardListCount Exception", e);
      }
      
      return count;
   }
   
   
   
   
   public long boardListCount2(HiBoard hiBoard)
   {
      long count = 0;
      
      try
      {
         count = hiBoardDao.boardListCount2(hiBoard);
      }
      catch(Exception e)
      {
         logger.error("[HiBoardService] boardListCount Exception", e);
      }
      
      return count;
   }
   
   
   
   
   
   
   
   
   
   
   //게시물 리스트
   public List<HiBoard> boardList(HiBoard hiBoard)
   {
      List<HiBoard> list = null;
      
      try
      {
         list = hiBoardDao.boardList(hiBoard);
      }
      catch(Exception e)
      {
         logger.error("[HiBoardService] boardList Exception", e);
      }
      
      return  list;
   }
   
   

   
   
   
   
   
   
   
   //게시물 등록      hiBoardDao.boardInsert(hiBoard)가 오류없이 진행되어도 
   //hiBoardDao.boardFileInsert(hiBoardFile)여기서 오류가 나면 hiBoardDao.boardInsert(hiBoard)까지 롤백한다는 의미
   @Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
   public int boardInsert(HiBoard hiBoard) throws Exception
   {
      int count = hiBoardDao.boardInsert(hiBoard);
      
      //게시물 등록이 되었으면, 첨부파일이 있다면 첨부파일 등록
      if(count > 0 && hiBoard.getHiBoardFile() != null)
      {
         HiBoardFile hiBoardFile = hiBoard.getHiBoardFile();
         
         hiBoardFile.setHiBbsSeq(hiBoard.getHiBbsSeq());
         hiBoardFile.setFileSeq((short)1);
         
         hiBoardDao.boardFileInsert(hiBoardFile);
      }
      
      return count;
   }
   
   
   //게시물 조회
   public HiBoard boardSelect(long hiBbsSeq)
   {
      HiBoard hiBoard = null;
      
      try
      {
         hiBoard = hiBoardDao.boardSelect(hiBbsSeq);
      }
      catch(Exception e)
      {
         logger.error("[HiBoardService] boardSelect Exception", e);
      }
      
      return hiBoard;
   }
   
   
   
   //게시물 보깅
   public HiBoard boardView(long hiBbsSeq)
   {
      HiBoard hiBoard = null;
      
      try
      {
         hiBoard = hiBoardDao.boardSelect(hiBbsSeq);
         
         if(hiBoard != null)
         {
            //조회수 증가 처리
            hiBoardDao.boardReadCntPlus(hiBbsSeq);
            
            HiBoardFile hiBoardFile = hiBoardDao.boardFileSelect(hiBoard.getHiBbsSeq());
            
            if(hiBoardFile != null)
            {
               hiBoard.setHiBoardFile(hiBoardFile);
            }
         }
      }
      catch(Exception e)
      {
         logger.error("[HiBoardService] boardView Exception", e);
      }
      
      return hiBoard;
   }
   
   
   @Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
   public int boardReplyInsert(HiBoard hiBoard) throws Exception
   {
      hiBoardDao.boardGroupOrderUpdate(hiBoard);
      
      int count = hiBoardDao.boardReplyInsert(hiBoard);
      
      if(count > 0 && hiBoard.getHiBoardFile() != null)   //파일이 있을 경우
      {
         HiBoardFile hiBoardFile = hiBoard.getHiBoardFile();
         
         hiBoardFile.setHiBbsSeq(hiBoard.getHiBbsSeq());
         hiBoardFile.setFileSeq((short)1);
         
         hiBoardDao.boardFileInsert(hiBoardFile);
      }
      
      return count;
   }
   
   //게시물 수정
   @Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
   public int boardUpdate(HiBoard hiBoard) throws Exception
   {
      int count = hiBoardDao.boardUpdate(hiBoard);
      
      if(count > 0 && hiBoard.getHiBoardFile() != null)
      {
         HiBoardFile delHiBoardFile = hiBoardDao.boardFileSelect(hiBoard.getHiBbsSeq());
         
         //기존 파일이 있으면 삭제
         if(delHiBoardFile != null)
         {
            logger.debug("delete file info" + UPLOAD_SAVE_DIR + FileUtil.getFileSeparator() + delHiBoardFile.getFileName());
            FileUtil.deleteFile(UPLOAD_SAVE_DIR + FileUtil.getFileSeparator() + delHiBoardFile.getFileName());  
            
            hiBoardDao.boardFileDelete(hiBoard.getHiBbsSeq());
         }
         HiBoardFile hiBoardFile = hiBoard.getHiBoardFile();
         
         hiBoardFile.setHiBbsSeq(hiBoard.getHiBbsSeq());
         hiBoardFile.setFileSeq((short)1);
         
         hiBoardDao.boardFileInsert(hiBoard.getHiBoardFile());
      }
      
      return count;
   }
   
   //게시물 삭제시 답변글 수 체크
   public int boardAnswersCount(long hiBbsSeq)
   {
      int count = 0;
      
      try
      {
         count = hiBoardDao.boardAnswersCount(hiBbsSeq);
      }
      catch(Exception e)
      {
         logger.error("[HiBoardService] boardAnswersCount Exception", e);
      }
      
      return count;
   }
   
   
   //게시물 답변 리스트
   public List<HiBoard> boardReplyList(HiBoard hiBoard)
   {
      List<HiBoard> replylist = null;
      
      try
      { 
         replylist = hiBoardDao.boardReplyList(hiBoard);
      }
      catch(Exception e)
      {
         logger.error("[HiBoardService] boardList Exception", e);
      }
      
      return  replylist;
   }
   
   //게시물 답변 삭제(파일이 있는 경우 같이 삭제)
   @Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
   public int boardReplyDelete(long hiBbsSeq) throws Exception
   {
      int count = 0;
      
      HiBoard parentHiBoard = boardView(hiBbsSeq);
      
      if(parentHiBoard != null)
      {
         count = hiBoardDao.boardReplyDelete(hiBbsSeq);
        
      }
      
      return count;
   }
}