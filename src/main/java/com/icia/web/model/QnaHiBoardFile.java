package com.icia.web.model;

import java.io.Serializable;

public class QnaHiBoardFile implements Serializable {

   private static final long serialVersionUID = 1L;

   private long qnaHiBbsSeq;  //게시물 번호
   private short qnaFileSeq;  //파일 번호
   private String qnaFileOrgName;  //원본파일명
   private String qnaFileName;  //파일명
   private String qnaFileExt;  //파일확장자
   private long qnaFileSize;  //파일 크기(byte)
   private String regDate;  //등록일
   
   public QnaHiBoardFile()
   {
      qnaHiBbsSeq = 0;
      qnaFileSeq = 0;
      qnaFileOrgName = "";
      qnaFileName = "";
      qnaFileExt = "";
      qnaFileSize = 0;
      regDate = "";
   }

   public long getQnaHiBbsSeq() {
      return qnaHiBbsSeq;
   }

   public void setQnaHiBbsSeq(long qnaHiBbsSeq) {
      this.qnaHiBbsSeq = qnaHiBbsSeq;
   }

   public short getQnaFileSeq() {
      return qnaFileSeq;
   }

   public void setQnaFileSeq(short qnaFileSeq) {
      this.qnaFileSeq = qnaFileSeq;
   }

   public String getQnaFileOrgName() {
      return qnaFileOrgName;
   }

   public void setQnaFileOrgName(String qnaFileOrgName) {
      this.qnaFileOrgName = qnaFileOrgName;
   }

   public String getQnaFileName() {
      return qnaFileName;
   }

   public void setQnaFileName(String qnaFileName) {
      this.qnaFileName = qnaFileName;
   }

   public String getQnaFileExt() {
      return qnaFileExt;
   }

   public void setQnaFileExt(String qnaFileExt) {
      this.qnaFileExt = qnaFileExt;
   }

   public long getQnaFileSize() {
      return qnaFileSize;
   }

   public void setQnaFileSize(long qnaFileSize) {
      this.qnaFileSize = qnaFileSize;
   }

   public String getRegDate() {
      return regDate;
   }

   public void setRegDate(String regDate) {
      this.regDate = regDate;
   }
   
   
}

