<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib2.jsp" %>    
<!DOCTYPE html>
<html>
<%@ include file="/WEB-INF/views/include/head.jsp" %> 
<title>동행 게시판</title>
<script type="text/javascript">
$(document).ready(function() {
    
   $("#btnWrite").on("click", function() {
      
      document.bbsForm.hiBbsSeq.value = "";
      document.bbsForm.action = "/board/writeForm";
      document.bbsForm.submit();
      
   });
   
   $("#btnSearch").on("click", function() {
      
      document.bbsForm.hiBbsSeq.value = "";
      document.bbsForm.searchType.value = $("#_searchType").val();
      document.bbsForm.searchValue.value = $("#_searchValue").val();
      document.bbsForm.curPage.value = "1";
      document.bbsForm.action = "/board/list";
      document.bbsForm.submit();
      
   });
});

function fn_view(bbsSeq)
{
   document.bbsForm.hiBbsSeq.value = bbsSeq;
   document.bbsForm.action = "/board/view";
   document.bbsForm.submit();
}

function fn_list(curPage)
{
   document.bbsForm.hiBbsSeq.value = "";
   document.bbsForm.curPage.value = curPage;
   document.bbsForm.action = "/admin/list";
   document.bbsForm.submit();
}

function popup(){
    var url = "/calendar/calendar.jsp";
    var name = "popup test";
    var option = "width = 500, height = 300, top = 100, left = 200, location = no"
    window.open(url, name, option);
}

</script>
<body>
<%@ include file="/WEB-INF/views/include/adminNavigation.jsp" %>
<div class="container">
   
   <div class="d-flex">
      <div style="width:50%;">
         <h2>게시판</h2>
      </div>
      <div class="ml-auto input-group" style="width:50%;">
         <select name="_searchType" id="_searchType" class="custom-select" style="width:auto;">
            <option value="">조회 항목</option>
            <option value="1" <c:if test="${searchType eq '1'}">selected</c:if>>작성자</option>
            <option value="2" <c:if test="${searchType eq '2'}">selected</c:if>>제목</option>
            <option value="3" <c:if test="${searchType eq '3'}">selected</c:if>>내용</option>
         </select>
         <input type="text" name="_searchValue" id="_searchValue" value="${searchValue}" class="form-control mx-1" maxlength="20" style="width:auto;ime-mode:active;" placeholder="조회값을 입력하세요." />
         <div class="btn-group">
            <button type="button" id="btnSearch" class="btn btn-secondary mb-3 mx-1">조회</button>
         </div>
      </div>
    </div>
    
   <table class="table table-hover">
      <thead>
      <tr style="background-color: #F5dF4D;">
         <th scope="col" class="text-center" style="width:10%">번호</th>
         <th scope="col" class="text-center" style="width:55%">제목</th>
         <th scope="col" class="text-center" style="width:10%">작성자</th>
         <th scope="col" class="text-center" style="width:15%">날짜</th>
         <th scope="col" class="text-center" style="width:10%">조회수</th>
      </tr>
      </thead>
      <tbody>
<c:if test="${!empty list}">   
   <c:forEach var="hiBoard" items="${list}" varStatus="status">  
         <c:if test="${hiBoard.hiBbsIndent == 0}"> 
      <tr>
         <td class="text-center">${hiBoard.hiBbsSeq}</td>
         <td>
            <a href="javascript:void(0)" onclick="fn_view(${hiBoard.hiBbsSeq})">



               <c:out value="${hiBoard.hiBbsTitle}" />
            </a>
         </td>
         <td class="text-center"><c:out value="${hiBoard.userName}" /></td>
         <td class="text-center">${hiBoard.regDate}</td>
         <td class="text-center"><fmt:formatNumber type="number" maxFractionDigits="3" value="${hiBoard.hiBbsReadCnt}" /></td>
      </tr>
            </c:if>
   </c:forEach>
</c:if>
      </tbody>
      <tfoot>
      <tr>
            <td colspan="5"></td>
        </tr>
      </tfoot>
   </table>
   <nav>
      <ul class="pagination justify-content-center">
<c:if test="${!empty paging}">
   <c:if test="${paging.prevBlockPage gt 0 }">
         <li class="page-item"><a class="page-link" href="javascript:void(0)" onclick="fn_list(${paging.prevBlockPage})">이전블럭</a></li>
   </c:if>
   
   <c:forEach var="i" begin="${paging.startPage}" end="${paging.endPage}">
      <c:choose>
         <c:when test="${i ne curPage}">
         <li class="page-item"><a class="page-link" href="javascript:void(0)" onclick="fn_list(${i})">${i}</a></li>
         </c:when>
         <c:otherwise>
         <li class="page-item active"><a class="page-link" href="javascript:void(0)" style="cursor:default;">${i}</a></li>
         </c:otherwise>
      </c:choose>
   </c:forEach>
   <c:if test="${paging.nextBlockPage gt 0}">
         <li class="page-item"><a class="page-link" href="javascript:void(0)" onclick="fn_list(${paging.nextBlockPage})">다음블럭</a></li>
   </c:if>
</c:if>   
      </ul>
   </nav>
<%
   if(com.icia.web.util.CookieUtil.getCookie(request, (String)request.getAttribute("AUTH_COOKIE_NAME")) != null)
   {
%>      
   <div class="btn-group">
      <button type="button" id="btnWrite" class="btn btn-secondary mb-3">글쓰기</button>
   </div>
<%
   }
   else
   {
      
   }
%>  
   <form name="bbsForm" id="bbsForm" method="post">
      <input type="hidden" name="hiBbsSeq" value="" />
      <input type="hidden" name="searchType" value="${searchType}" />
      <input type="hidden" name="searchValue" value="${searchValue}" />
      <input type="hidden" name="curPage" value="${curPage}" />
   </form>
</div>

</body>
</html>