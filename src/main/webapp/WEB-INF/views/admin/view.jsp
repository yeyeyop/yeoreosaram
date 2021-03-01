<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib2.jsp" %>
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
   <c:when test="${empty admin}">
   //alert("조회하신 게시물이 존재하지 않습니다.(관리자 페이지에유)");
   document.bbsForm.action = "/admin/view";
   document.bbsForm.submit();
   </c:when>
   <c:otherwise>

   $("#btnList").on("click", function() {
      document.bbsForm.action = "/admin/list";
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
   
   $("#btnReplyDelete ").on("click", function(){

	       if(confirm("댓글을 삭제 하시겠습니까?") == true)
	      {
	         $.ajax({
	            type : "POST",
	            url : "/board/replyDelete",
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
	                  alert("댓글이 삭제되었습니다.");
	                  location.reload = "/board/view";
	                  setTimeout('location.reload()',1000); 
	               }
	               else if(response.code == 400)
	               {
	                  alert("파라미터 값이 올바르지 않습니다.");
	               }
	               else if(response.code == 404)
	               {
	                  alert("댓글을 찾을수 없습니다.");
	                  location.href = "/board/view";
	               }
	               else if(response.code == -999)
	                 {
	                    alert("답변 게시물이 존재하여 삭제할 수 없습니다.");
	                 }
	               else
	               {
	                  alert("댓글 삭제중 오류가 발생하였습니다.");
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

<!--  게시물 리스트 출력 -->
<c:if test="${!empty admin}">
<%@ include file="/WEB-INF/views/include/adminNavigation.jsp" %>
<div class="container">
   <h2>게시물 보기</h2>
   <div class="row" style="margin-right:0; margin-left:0;">
      <table class="table">
         <thead>
            <tr class="table-active" style="color:#939597">
               <th scope="col" style="width:60%">
                  <c:out value="${admin.hiBbsTitle}" /><br/>
                  <c:out value="${admin.userName2}" />&nbsp;&nbsp;&nbsp;
                  <a href="mailto:${admin.userEmail}" style="color:#828282;">${admin.userEmail}</a>
                
               </th>
               <th scope="col" style="width:40%" class="text-right">
                  조회 : <fmt:formatNumber type="number" maxFractionDigits="3" value="${admin.hiBbsReadCnt}" /><br/>
                  ${admin.regDate}
               </th>
            </tr>
         </thead>
         <tbody>
            <tr>
               <td colspan="2"><pre><c:out value="${admin.hiBbsContent}" /></pre></td>
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
         <th scope="col" class="text-center" style="width:75%">댓글</th>
         <th scope="col" class="text-center" style="width:10%">작성자</th>
         <th scope="col" class="text-center" style="width:15%">날짜</th>
      </tr>
      </thead>
      <tbody>
     
<c:if test="${!empty replylist}">
   <c:forEach var="admin" items="${replylist}" varStatus="status"> 
      <c:if test="${admin.hiBbsParent eq admin.hiBbsGroup}"> 
      <tr>
         <td>
         
         <img src="/resources/images/icon_reply.gif" style="margin-left: ${hiBoard.hiBbsIndent}em;"/>
      
               <c:out value="${admin.hiBbsContent}" />
            
         </td>
         <td class="text-center"><c:out value="${admin.userEmail}" /></td>
         <td class="text-center">${admin.regDate}</td>
          <td>${admin.hiBbsSeq}<button id="btnReplyDelete" class="btn btn-secondary">댓글 삭제</button></td>
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
   
   
   
   
   
   
   <!-- btn-group은 색상 -->
   <div class="btn-group">
   <button type="button" id="btnList" class="btn btn-secondary">리스트</button>
   <button type="button" id="btnUpdate" class="btn btn-secondary">수정</button>
   <button type="button" id="btnDelete" class="btn btn-secondary">삭제</button>
   </c:if>
   <br/>
   <br/>
  
	</div>
</div>
<form name="bbsForm" id="bbsForm" method="post">
   <input type="hidden" name="hiBbsSeq" value="${hiBbsSeq}" />
   <input type="hidden" name="searchType" value="${searchType}" />
   <input type="hidden" name="searchValue" value="${searchValue}" />
   <input type="hidden" name="curPage" value="${curPage}" />
</form>
</body>
</html>