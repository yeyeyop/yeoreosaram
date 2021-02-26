<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.icia.common.util.StringUtil"%>
<%@ page import="com.icia.web.util.HttpUtil"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<%@ include file="/WEB-INF/views/include/head.jsp" %> 
<head>
<meta http-equiv = "Content-Type" Content = "text/html; charset = UTF-8">
<script src = "http://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<title>회원 목록</title>
<script>
$(document).ready(function(){
   
   
   $("#deleteBtn").click(function(){
      
      //확인 대화상자
      if(confirm("삭제하시겠습니까?"))
      {
         
        //alert("dd");
        
         document.form1.action = "${path}/admin/delete";
         document.form1.submit();
         
         alert("삭제완료");
         
         
         $.ajax({
             type : "POST",
             url : "/user/adminDelete",
             data : {
                qnaHiBbsSeq : <c:out value="${qna.qnaHiBbsSeq}" />
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
                   location.href = "/user/adminList";
                }
                else if(response.code == 400)
                {
                   alert("파라미터 값이 올바르지 않습니다.");
                }
                else if(response.code == 404)
                {
                   alert("게시물을 찾을수 없습니다.");
                   location.href = "/board/customerList";
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
          
       }
         
      }
   });
});

</script>

</head>
<body>
<%@ include file="/WEB-INF/views/include/adminNavigation.jsp" %>

   
  <main id="main">
  <br>
   <h2>회원 목록</h2>
  <br>
  
    <table style="width:80%" >
      <tr style="background-color: cyan">
         <th>아이디</th><th>비밀번호</th><th>회원명</th><th>이메일</th>
      </tr>
      
      <c:forEach var = "row" items = "${list}">
      <tr>
         <td>${row.userId2}</td>
         <td>${row.userPwd2}</td>
         <td>${row.userName2}</td>
         <td>${row.userEmail2}</td>
         <td><button type="button" id="deletebtn" class="btn btn-secondary">삭제</button></td>
      </tr>
      </c:forEach>
   </table>
   </main><!-- End #main -->
</body>

</html>