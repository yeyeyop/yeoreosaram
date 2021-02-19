<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib2.jsp" %>
<!-- navigation -->
<%@ include file="/WEB-INF/views/include/teamNavigation2.jsp" %>

<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/include/head.jsp" %>
<script type="text/javascript">
$(document).ready(function() {

   $("#userId2").focus();
   
   $("#btnReg2").on("click", function() {
      
      // 모든 공백 체크 정규식
      var emptCheck = /\s/g;
      // 영문 대소문자, 숫자로만 이루어진 4~12자리 정규식
      var idPwCheck = /^[a-zA-Z0-9]{4,12}$/;
            
      if($.trim($("#userId2").val()).length <= 0)
      {
         alert("사용자 아이디를 입력하세요.");
         $("#userId2").val("");
         $("#userId2").focus();
         return;
      }
      
      if (emptCheck.test($("#userId2").val())) 
      {
         alert("사용자 아이디는 공백을 포함할 수 없습니다.");
         $("#userId2").focus();
         return;
      }
      
      if (!idPwCheck.test($("#userId2").val())) 
      {
         alert("사용자 아이디는 4~12자의 영문 대소문자와 숫자로만 입력하세요");
         $("#userId2").focus();
         return;
      }
      
      if($.trim($("#userPwd1").val()).length <= 0)
      {
         alert("비밀번호를 입력하세요.");
         $("#userPwd1").val("");
         $("#userPwd1").focus();d
         return;
      }
      
      if (emptCheck.test($("#userPwd1").val())) 
      {
         alert("비밀번호는 공백을 포함할 수 없습니다.");
         $("#userPwd1").focus();
         return;
      }
      
      if (!idPwCheck.test($("#userPwd1").val())) 
      {
         alert("비밀번호는 영문 대소문자와 숫자로 4~12자리 입니다.");
         $("#userPwd1").focus();
         return;
      }
      
      if ($("#userPwd1").val() != $("#userPwd3").val()) 
      {
         alert("비밀번호가 일치하지 않습니다.");
         $("#userPwd3").focus();
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
      
      if($.trim($("#gender2").val()).length <= 0)
      {
         alert("사용자 성별을 입력하세요.");
         $("#gender2").val("");
         $("#gender2").focus();
         return;
      }
      
      if($.trim($("#userBirth2").val()).length <= 0)
      {
         alert("사용자 생년월일 8자를 입력하세요.");
         $("#userBirth2").val("");
         $("#userBirth2").focus();
         return;
      }
      
      $("#userPwd2").val($("#userPwd1").val());
      
      $.ajax({
         type : "POST",
         url : "/user/idCheck2",
         data : {
            userId2 : $("#userId2").val()
         },
         datatype : "JSON",
         beforeSend : function(xhr){
               xhr.setRequestHeader("AJAX", "true");
           },
         success : function(response) {
            // var data = JSON.parse(obj);

            if(response.code == 0)
            {
               // 아이디 중복이 없다면 회원가입 처리한다.
               fn_userReg();
            }
            else if(response.code == 100)
            {
               alert("중복된 아이디 입니다.");
               $("#userId2").focus();
            }
            else if(response.code == 400)
            {
               alert("파라미터 값이 올바르지 않습니다.");
               $("#userId2").focus();
            }
            else
            {
               alert("오류가 발생하였습니다.");
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

function fn_userReg()
{
   $.ajax({
      type : "POST",
      url : "/user/regProc2",
      data : {
         userId2: $("#userId2").val(),
         userPwd2: $("#userPwd2").val(),
         userName2: $("#userName2").val(),
         userEmail2: $("#userEmail2").val(),
         regDate2 : $("#regDate2").val(),
         gender2: $("#gender2").val(),
         userBirth2: $("#userBirth2").val()
      },
      datatype : "JSON",
      beforeSend : function(xhr){
            xhr.setRequestHeader("AJAX", "true");
        },
      success : function(response) {
         // var data = JSON.parse(obj);

         if(response.code == 0)
         {
            alert("회원 가입되었습니다.");
            location.href = "/board/list";
         }
         else if(response.code == 100)
         {
            alert("회원 아이디가 중복 되었습니다.");
            $("#userId2").focus();
         }
         else if(response.code == 400)
         {
            alert("파라미터 값이 올바르지 않습니다.");
            $("#userId2").focus();
         }
         else if(response.code == 500)
         {
            alert("회원 가입중 오류가 발생하였습니다.");
            $("#userId2").focus();
         }
         else
         {
            alert("회원 가입중 오류가 발생하였습니다.");
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
}

function fn_validateEmail(value)
{
   var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
   
   return emailReg.test(value);
}
</script>
</head>
<body>
<div class="container">
    <div  class="form-group">
       <h1>회원가입</h1>
    </div>
    <div class="row mt-2">
        <div class="col-12">
            <form id="regForm2" method="post">
                <div class="form-group">
                    <label for="userId2">아이디</label>
                    <input type="text" class="form-control" id="userId2" name="userId2" placeholder="" maxlength="12" />
                </div>
                <div class="form-group">
                    <label for="userPwd2">비밀번호</label>
                    <input type="password" class="form-control" id="userPwd1" name="userPwd1" placeholder="" maxlength="12" />
                </div>
                <div class="form-group">
                    <label for="userPwd3">비밀번호 확인</label>
                    <input type="password" class="form-control" id="userPwd3" name="userPwd3" placeholder="" maxlength="12" />
                </div>
                <div class="form-group">
                    <label for="userName2">이름</label>
                    <input type="text" class="form-control" id="userName2" name="userName2" placeholder="" maxlength="6" />
                </div>
                <div class="form-group">
                    <label for="userEmail2">이메일</label>
                    <input type="text" class="form-control" id="userEmail2" name="userEmail2" placeholder="" maxlength="30" />
                </div>
                <div class="form-group">
                    <label for="gender2">성별</label>
                    </br/>
                    <input type='radio' id="gender2" name="gender2" value="F"/><label for='F'>여성</label>
                    <input type='radio' id="gender2" name="gender2" value="M"/><label for='M'>남성</label>
                </div>
                <div class="form-group">
                    <label for="userBirth2">생년월일</label>
                    <input type="text" class="form-control" id="userBirth2" name="userBirth2" placeholder="8자입력" maxlength="8" />
                </div>
                <input type="hidden" id="userPwd2" name="userPwd2" value="" />
                
                <!-- 등록부분 css 추가 -->
                <div class="form-group">
                   <div class="btn-group">
                      <button type="button" id="btnReg2" class="btn btn-outline-warning">등록</button>
                   </div>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>