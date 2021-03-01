<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<%@ include file="/WEB-INF/views/include/head.jsp" %> 
<head>
<meta charset="UTF-8">

<title>회원탈퇴</title>
<srcipt src="http://ajax.googleapis.com/ajax/lib/jquery/1.11.1/jquery.min.js"></srcipt>

<script>
$(document).ready(function() {
   
    $("#submit").on("click", function() {
      var responseMessage = "<c:out value = "${message}" />";
      
      if(responseMessage != "")
      {
         alert("회원탈퇴 완료!");
         location.href = "/admin/adminList";
      }
      else {
         alert("없는 회원입니다.");
      }
   });
    
    $("#close").on("click",function(){
      location.href = "/admin/adminList";
    });

}); //ready function
</script>

</head>

<body>
<%@ include file="/WEB-INF/views/include/adminNavigation.jsp" %>
<div class="container">
   <span>회원 강제 탈퇴</span>
   <br><br>
   <form action = "adminDeleteForm" method = "post">
      -회원 아이디-<br>
      <input type = "text" name = userId2 placeholder = "탈퇴시킬 회원의 아이디 입력">
      <button type = "submit" name = "submit" id="submit">회원 강제탈퇴</button>
      <input type = "button" id="close" value = "창닫기">
   </form>
</div>
</body>


</html>