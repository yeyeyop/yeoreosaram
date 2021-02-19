<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib2.jsp" %>

<!DOCTYPE html>
<html>
<%@ include file="/WEB-INF/views/include/head.jsp" %>
<script type="text/javascript">
$(document).ready(function() {

   $("#btnUpdate").on("click", function() {
      
      // 모든 공백 체크 정규식
      var emptCheck = /\s/g;
      // 영문 대소문자, 숫자로만 이루어진 4~12자리 정규식
      var idPwCheck = /^[a-zA-Z0-9]{4,12}$/;
            
      if($.trim($("#userPwd4").val()).length <= 0)
      {
         alert("비밀번호를 입력하세요.");
         $("#userPwd4").val("");
         $("#userPwd4").focus();
         return;
      }
      
      if (emptCheck.test($("#userPwd4").val())) 
      {
         alert("비밀번호는 공백을 포함할 수 없습니다.");
         $("#userPwd4").focus();
         return;
      }
      
      if (!idPwCheck.test($("#userPwd4").val())) 
      {
         alert("비밀번호는 영문 대소문자와 숫자로 4~12자리 입니다.");
         $("#userPwd4").focus();
         return;
      }
      
      if ($("#userPwd4").val() != $("#userPwd5").val()) 
      {
         alert("비밀번호가 일치하지 않습니다.");
         $("#userPwd5").focus();
         return;
      }
      
      if($.trim($("#userName2").val()).length <= 0)
      {
         alert("사용자 이름을 입력하세요.");
         $("#userName2").val("");
         $("#userName2").focus();
         return;
      }
      
      if(!fn_validateEmail($("#userEmail2").val()))
      {
         alert("사용자 이메일 형식이 올바르지 않습니다.");
         $("#userEmail2").focus();
         return;   
      }
      
      $("#userPwd2").val($("#userPwd4").val());
      
      $.ajax({
         type : "POST",
         url : "/user/updateProc2",
         data : {
            userId2: $("#userId2").val(),
            userPwd2: $("#userPwd2").val(),
            userName2: $("#userName2").val(),
            userEmail2: $("#userEmail2").val()
         },
         datatype : "JSON",
         beforeSend : function(xhr){
               xhr.setRequestHeader("AJAX", "true");
           },
         success : function(response) {
            // var data = JSON.parse(obj);

            if(response.code == 0)
            {
               alert("회원 정보가 수정되었습니다.");
               location.href = "/board/list";
            }
            else if(response.code == 400)
            {
               alert("파라미터 값이 올바르지 않습니다.");
               $("#userId2").focus();
            }
            else if(response.code == 404)
            {
               alert("회원 정보가 없습니다.");
               location.href = "/";
            }
            else if(response.code == 500)
            {
               alert("회원정보 수정 중 오류가 발생하였습니다.");
               $("#userId2").focus();
            }
            else
            {
               alert("회원정보 수정 중 오류가 발생하였습니다.");
               $("#userId2").focus();
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
   });
});

function fn_validateEmail(value)
{
   var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
   
   return emailReg.test(value);
}
</script>
<body>
<%@ include file="/WEB-INF/views/include/teamNavigation.jsp" %>
<div class="container">
    <div  class="form-group">
       <h1>회원정보수정</h1>
    </div>
    <div class="row mt-2">
        <div class="col-12">
            <form>
                <div class="form-group">
                    <label for="username">사용자 아이디</label>
                    ${user2.userId2}
                </div>
                <div class="form-group">
                    <label for="username2">비밀번호</label>
                    <input type="password" class="form-control" id="userPwd4" name="userPwd4" value="${user2.userPwd2}" placeholder="비밀번호" maxlength="12" />
                </div>
                <div class="form-group">
                    <label for="username2">비밀번호 확인</label>
                    <input type="password" class="form-control" id="userPwd5" name="userPwd5" value="${user2.userPwd2}" placeholder="비밀번호 확인" maxlength="12" />
                </div>
                <div class="form-group">
                    <label for="username2">사용자 이름</label>
                    <input type="text" class="form-control" id="userName2" name="userName2" value="${user2.userName2}" placeholder="사용자 이름" maxlength="15" />
                </div>
                <div class="form-group">
                    <label for="username2">사용자 이메일</label>
                    <input type="text" class="form-control" id="userEmail2" name="userEmail2" value="${user2.userEmail2}" placeholder="사용자 이메일" maxlength="30" />
                </div>
                <input type="hidden" id="userId2" name="userId2" value="${user2.userId2}" />
                <input type="hidden" id="userPwd2" name="userPwd2" value="" />
                <div class="form-group">
                   <div class="btn-group">
                <button type="button" id="btnUpdate" class="btn btn-primary">수정</button>
                	</div>
                </div>
                
            </form>
        </div>
    </div>
</div>
</body>
</html>