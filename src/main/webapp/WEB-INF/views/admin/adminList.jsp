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
</head>
<body>
<%@ include file="/WEB-INF/views/include/adminNavigation.jsp" %>

   
  <main id="main">
  <br>
   <h2>회원 목록 </h2> <button><a href = "${path}/admin/adminDelete">강제탈퇴</a></button>
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
      </tr>
      </c:forEach>
   </table>
   </main><!-- End #main -->
</body>

</html>