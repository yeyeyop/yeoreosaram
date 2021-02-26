<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib2.jsp" %>
<!DOCTYPE html>
<html>
<%@ include file="/WEB-INF/views/include/head.jsp" %>
<!-- navigation -->
<%@ include file="/WEB-INF/views/include/adminNavigation.jsp" %>
<script type="text/javascript">
$(document).ready(function() {
   
   $("#adminId").focus();
   
   $("#adminId").on("keypress", function(e){
      
      if(e.which == 13)
      {   
         fn_loginCheck();
      }
      
   });
   
   $("#adminPwd").on("keypress", function(e){
      
      if(e.which == 13)
      {
         fn_loginCheck();
      }
      
   });
      
   $("#btnAdmin").on("click", function() {
      
      fn_loginCheck();
      
   });
   
   $("#btnReg").on("click", function() {
      location.href = "/user/regForm2";
   });
   
});

function fn_loginCheck()
{
   if($.trim($("#adminId").val()).length <= 0)
   {
      alert("아이디를 입력하세요.");
      $("#adminId").focus();
      return;
   }
   
   if($.trim($("#adminPwd").val()).length <= 0)
   {
      alert("비밀번호를 입력하세요.");
      $("#adminPwd").focus();
      return;
   }
   
   $.ajax({               <!-- 클라이언트와 서버간에 XML 데이터를 주고받는 기술 -->
   type : "POST",
   url : "/admin/login2",
   data : {
      adminId: $("#adminId").val(),
      adminPwd: $("#adminPwd").val() 
   },
   datatype : "JSON",
   beforeSend : function(xhr){
         xhr.setRequestHeader("AJAX", "true");
     },
   success : function(response) {
      
      if(!icia.common.isEmpty(response))
      {
         icia.common.log(response);
         
         // var data = JSON.parse(obj);
         var code = icia.common.objectValue(response, "code", -500);
         
         if(code == 0)
         {
        	 alert("로그인 완료");
            location.href = "/admin/adminHome"; //관리자페이지?
         }
         else
         {
            if(code == -1)
            {
               alert("비밀번호가 올바르지 않습니다.");
               $("#adminPwd").focus();
            }
            else if(code == 404)
            {
               alert("아이디와 일치하는 사용자 정보가 없습니다.");
               $("#adminId").focus();
            }
            else if(code == 400)
            {
               alert("파라미터 값이 올바르지 않습니다.");
               $("#adminId").focus();
            }
            else
            {
               alert("오류가 발생하였습니다.");
               $("#adminId").focus();
            }   
         }   
      }
      else
      {
         alert("오류가 발생하였습니다.");
         $("#adminId").focus();
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
</script>

<body>
<div class="container">
   <form class="form-signin">
       <h2 class="form-signin-heading m-b3">로그인</h2>
      <label for="adminId" class="sr-only">아이디</label>
      <input type="text" id="adminId" name="adminId" class="form-control" maxlength="20" placeholder="아이디">
      <label for="adminPwd" class="sr-only">비밀번호</label>
      <input type="password" id="adminPwd" name="adminPwd" class="form-control" maxlength="20" placeholder="비밀번호">
        
      <button type="button" id=btnAdmin class="btn btn-lg btn-primary btn-block">로그인</button>
      <div class="caption"><a href="#">Forgot Password?</a></div>
   </form>
</div>
</body>
</html>s