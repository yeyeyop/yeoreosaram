package com.icia.web.model;

import java.io.Serializable;

public class Qna implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private long qnaHiBbsSeq;		//게시물 번호
	private String userId;	//사용자 아이디
	private long qnaHiBbsGroup;	//게시물 그룹번호
	private int qnaHiBbsOrder;		//게시물 그룹내 순서
	private int qnaHiBbsIndent;		//게시물 들여쓰기
	private String qnaHiBbsTitle;	//게시물 제목
	private String qnaHiBbsContent; //게시물 내용
	private int qnaHiBbsReadCnt;	//게시물 조회수
	private String regDate;		//등록일
	private long qnaHiBbsParent;   //부모 게시물 번호
	
	private String userName ;	//사용자 이름
	private String userEmail ;	//사용자 이메일
	
	private long startRow;		//시작 rownum
	private long endRow;		//끝 rownum
	
	private String searchType ;	//검색타입(1: 이름, 2: 제목, 3: 내용)
	private String searchValue; //검색값
	
	private QnaHiBoardFile qnaHiBoardFile; //첨부파일
	
	public Qna()
	{
		qnaHiBbsSeq = 0;		//게시물 번호
		userId = "";		//사용자 아이디
		qnaHiBbsGroup = 0;	//게시물 그룹번호
		qnaHiBbsOrder =0;		//게시물 그룹내 순서
		qnaHiBbsIndent =0;		//게시물 들여쓰기
		qnaHiBbsTitle = "";	//게시물 제목
		qnaHiBbsContent = ""; //게시물 내용
		qnaHiBbsReadCnt = 0;	//게시물 조회수
		regDate = "";		//등록일
		
		userName ="";	//사용자 이름
		userEmail = "";	//사용자 이메일
		
		startRow = 0;		//시작 rownum
		endRow = 0;		//끝 rownum
		
		searchType = "";	//검색타입(1: 이름, 2: 제목, 3: 내용)
		searchValue = ""; //검색값
		
		qnaHiBoardFile = null; //첨부파일
		qnaHiBbsParent = 0;
	}

	public long getQnaHiBbsSeq() {
		return qnaHiBbsSeq;
	}

	public void setQnaHiBbsSeq(long qnaHiBbsSeq) {
		this.qnaHiBbsSeq = qnaHiBbsSeq;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public long getQnaHiBbsGroup() {
		return qnaHiBbsGroup;
	}

	public void setQnaHiBbsGroup(long qnaHiBbsGroup) {
		this.qnaHiBbsGroup = qnaHiBbsGroup;
	}

	public int getQnaHiBbsOrder() {
		return qnaHiBbsOrder;
	}

	public void setQnaHiBbsOrder(int qnaHiBbsOrder) {
		this.qnaHiBbsOrder = qnaHiBbsOrder;
	}

	public int getQnaHiBbsIndent() {
		return qnaHiBbsIndent;
	}

	public void setQnaHiBbsIndent(int qnaHiBbsIndent) {
		this.qnaHiBbsIndent = qnaHiBbsIndent;
	}

	public String getQnaHiBbsTitle() {
		return qnaHiBbsTitle;
	}

	public void setQnaHiBbsTitle(String qnaHiBbsTitle) {
		this.qnaHiBbsTitle = qnaHiBbsTitle;
	}

	public String getQnaHiBbsContent() {
		return qnaHiBbsContent;
	}

	public void setQnaHiBbsContent(String qnaHiBbsContent) {
		this.qnaHiBbsContent = qnaHiBbsContent;
	}

	public int getQnaHiBbsReadCnt() {
		return qnaHiBbsReadCnt;
	}

	public void setQnaHiBbsReadCnt(int qnaHiBbsReadCnt) {
		this.qnaHiBbsReadCnt = qnaHiBbsReadCnt;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public long getQnaHiBbsParent() {
		return qnaHiBbsParent;
	}

	public void setQnaHiBbsParent(long qnaHiBbsParent) {
		this.qnaHiBbsParent = qnaHiBbsParent;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public long getStartRow() {
		return startRow;
	}

	public void setStartRow(long startRow) {
		this.startRow = startRow;
	}

	public long getEndRow() {
		return endRow;
	}

	public void setEndRow(long endRow) {
		this.endRow = endRow;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getSearchValue() {
		return searchValue;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}

	public QnaHiBoardFile getQnaHiBoardFile() {
		return qnaHiBoardFile;
	}

	public void setQnaHiBoardFile(QnaHiBoardFile qnaHiBoardFile) {
		this.qnaHiBoardFile = qnaHiBoardFile;
	}

	
	
}
