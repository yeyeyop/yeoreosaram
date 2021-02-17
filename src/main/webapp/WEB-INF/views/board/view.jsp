<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%
   // 개행문자 값을 저장한다.
   //개행문자 값을 newLine에 저장해서 JSTL에서 쓴다는것
   pageContext.setAttribute("newLine", "\n");
%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/include/head.jsp" %>
<script type="text/javascript">
$(document).ready(function() {
<c:choose>
   <c:when test="${empty hiBoard}">
   alert("조회하신 게시물이 존재하지 않습니다.");
   document.bbsForm.action = "/board/list";
   document.bbsForm.submit();
   </c:when>
   <c:otherwise>

   $("#btnList").on("click", function() {
      document.bbsForm.action = "/board/list";
      document.bbsForm.submit();
   });
   
   $("#btnReply").on("click", function() {
      document.bbsForm.action = "/board/replyForm";
      document.bbsForm.submit();
   });
   
   <c:if test="${boardMe eq 'Y'}">
   $("#btnUpdate").on("click", function() {
      document.bbsForm.action = "/board/updateForm";
      document.bbsForm.submit();
   });
   
   $("#btnDelete").on("click", function(){
      if(confirm("게시물을 삭제 하시겠습니까?") == true)
      {
         $.ajax({
            type : "POST",
            url : "/board/delete",
            data : {
               hiBbsSeq : <c:out value="${hiBoard.hiBbsSeq}" />
            },
            datatype : "JSON",
            beforeSend : function(xhr){
                  xhr.setRequestHeader("AJAX", "true");
              },
            success : function(response) {
               // var data = JSON.parse(obj);

               if(response.code == 0)
               {
                  alert("게시물이 삭제되었습니다.");
                  location.href = "/board/list";
               }
               else if(response.code == 400)
               {
                  alert("파라미터 값이 올바르지 않습니다.");
               }
               else if(response.code == 404)
               {
                  alert("게시물을 찾을수 없습니다.");
                  location.href = "/board/list";
               }
               else if(response.code == -999)
                 {
                    alert("답변 게시물이 존재하여 삭제할 수 없습니다.");
                 }
               else
               {
                  alert("게시물이 삭제중 오류가 발생하였습니다.");
               }   
            },
            complete : function(data) 
            {
               // 응답이 종료되면 실행, 잘 사용하지않는다
               icia.common.log(data);
            },
            error : function(xhr, status, error) 
            {
               icia.common.error(error);
            }
         });
      }
   });
   </c:if>
   </c:otherwise>
</c:choose>   
});

</script>
</head>
<body>
<c:if test="${!empty hiBoard}">
<%@ include file="/WEB-INF/views/include/navigation.jsp" %>
<div class="container">
   <h2>게시물 보기</h2>
   <div class="row" style="margin-right:0; margin-left:0;">
      <table class="table">
         <thead>
            <tr class="table-active">
               <th scope="col" style="width:60%">
                  <c:out value="${hiBoard.hiBbsTitle}" /><br/>
                  <c:out value="${hiBoard.userName}" />&nbsp;&nbsp;&nbsp;
                  <a href="mailto:${hiBoard.userEmail}" style="color:#828282;">${hiBoard.userEmail}</a>
   <c:if test="${!empty hiBoard.hiBoardFile}">
                  &nbsp;&nbsp;&nbsp;<a href="/board/download?hiBbsSeq=${hiBoard.hiBoardFile.hiBbsSeq}" style="color:#000;">[첨부파일]</a>
    </c:if>                
               </th>
               <th scope="col" style="width:40%" class="text-right">
                  조회 : <fmt:formatNumber type="number" maxFractionDigits="3" value="${hiBoard.hiBbsReadCnt}" /><br/>
                  ${hiBoard.regDate}
               </th>
            </tr>
         </thead>
         <tbody>
            <tr>
               <td colspan="2"><pre><c:out value="${hiBoard.hiBbsContent}" /></pre></td>
            </tr>
         </tbody>
         <tfoot>
         <tr>
               <td colspan="2"></td>
           </tr>
         </tfoot>
      </table>
   </div>
   
  
     <table class="table table-hover">
      <thead>
      <tr style="background-color: #dee2e6;">
         <th scope="col" class="text-center" style="width:10%">번호</th>
         <th scope="col" class="text-center" style="width:55%">제목</th>
         <th scope="col" class="text-center" style="width:10%">작성자</th>
         <th scope="col" class="text-center" style="width:15%">날짜</th>
         <th scope="col" class="text-center" style="width:10%">조회수</th>
      </tr>
      </thead>
      <tbody>
<c:if test="${!empty replylist}">
   <c:forEach var="hiBoard" items="${replylist}" varStatus="status"> 
   	<c:if test="${hiBoard.hiBbsParent eq hiBoard.hiBbsGroup}"> 
      <tr>
         <td class="text-center">${hiBoard.hiBbsSeq}</td>
         <td>
            <a href="javascript:void(0)" onclick="fn_view(${hiBoard.hiBbsSeq})">
      
         <img src="/resources/images/icon_reply.gif" style="margin-left: ${hiBoard.hiBbsIndent}em;"/>
      
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
   
   
   
   
   
   
   
   <button type="button" id="btnList" class="btn btn-secondary">리스트</button>
   <button type="button" id="btnReply" class="btn btn-secondary">답변</button>
   <c:if test="${boardMe eq 'Y'}">
   <button type="button" id="btnUpdate" class="btn btn-secondary">수정</button>
   <button type="button" id="btnDelete" class="btn btn-secondary">삭제</button>
   </c:if>
   <br/>
   <br/>
</div>
</c:if>
<form name="bbsForm" id="bbsForm" method="post">
   <input type="hidden" name="hiBbsSeq" value="${hiBbsSeq}" />
   <input type="hidden" name="searchType" value="${searchType}" />
   <input type="hidden" name="searchValue" value="${searchValue}" />
   <input type="hidden" name="curPage" value="${curPage}" />
</form>

</body>
</html>