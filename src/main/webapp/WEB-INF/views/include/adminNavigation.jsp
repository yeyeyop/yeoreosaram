<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib2.jsp" %>
<script>
   function except(){
      alert("로그인 후 이용가능합니다.");
   }
</script>
  <nav class="navbar navbar-expand-lg navbar-light admin-navmenu">
    <div class="container-fluid">
    
      <div class="navbar-header">
      <a class="nav-link" href="/mainHome">
      <img src="/resources/images/travel.png" width="50" height="50" alt="" loading="lazy"></a>
      </div>
<%
   if(com.icia.web.util.CookieUtil.getCookie(request, (String)request.getAttribute("AUTH_COOKIE_NAME")) != null)
   {
%>
       <ul class="nav navbar-nav navbar-right">
         <li class="nav-item">
           <a class="nav-link" href="/user/loginOut">로그아웃</a>
         </li>
         <li class="nav-item">
           <a class="nav-link" href="/board/list">동행게시판</a>
         </li>
         <li class="nav-item">
           <a class="nav-link" href="/user/updateForm2">회원관리</a>
         </li>
         <li class="nav-item">
           <a class="nav-link" href="/board/customerList">고객센터</a>
         </li>
       </ul> 
     </div>   
   </nav>
<%
   }
   else
   {
%>
       <ul class="nav navbar-nav navbar-right">  
         <li class="nav-item">
           <a class="nav-link" href="index">로그인</a>
         </li>
         <li class="nav-item">
           <a class="nav-link" href="/board/customerList">고객센터</a>
         </li>
       </ul>
     </div>
   </nav>
   
<%
   }
%>