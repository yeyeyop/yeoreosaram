<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/include/head.jsp" %>
<script type="text/javascript">
$(document).ready(function() {
    
   $("#qnaHiBbsTitle").focus();
   
   $("#btnWrite2").on("click", function() {
      
      $("#btnWrite2").prop("disabled", true); // 글쓰기 버튼 비활성화 > 아작스 통신을 하는 도중에 다시 누르지 못하게 하기 위해서
      
      if($.trim($("#qnaHiBbsTitle").val()).length <= 0)
      {
         alert("제목을 입력하세요.");
         $("#qnaHiBbsTitle").val("");
         $("#qnaHiBbsTitle").focus();
         
         $("#btnWrite2").prop("disabled", false);  // 글쓰기 버튼 활성화
         
         return;
      }
      
      if($.trim($("#qnaHiBbsContent").val()).length <= 0)
      {
         alert("내용을 입력하세요.");
         $("#qnaHiBbsContent").val("");
         $("#qnaHiBbsContent").focus();
         
         $("#btnWrite2").prop("disabled", false); // 글쓰기 버튼 활성화
         
         return;
      }
      
      var form = $("#writeForm2")[0];   //문법
      var formData = new FormData(form);
      
      $.ajax({
           type: "POST",
           enctype: 'multipart/form-data',   <!-- 파일 업로드할때 반드시 해줘야함 -->
           url: "/board/writeProc3",
           data: formData,
           processData: false,
           contentType: false,
           cache: false,
           timeout: 600000,
           beforeSend : function(xhr) 
           {
               xhr.setRequestHeader("AJAX", "true");
           },
           success: function (response) 
           {
              if(response.code == 0)
              {
                 alert("게시물이 등록되었습니다.");
                 location.href = "/board/customerList";
                 /*
                 document.bbsForm.action = "/board/list";
               document.bbsForm.submit();
                 */
              }
              else if(response.code == 400)
              {
                 alert("파라미터 값이 올바르지 않습니다.");   
                 $("#btnWrite2").prop("disabled", false);  // 글쓰기 버튼 활성화
              }
              else
              {
                 alert("게시물 등록중 오류가 발생하였습니다.");
                 $("#btnWrite2").prop("disabled", false);  // 글쓰기 버튼 활성화
              }
           },
           error: function (error) 
           {
              icia.common.error(error);
              alert("게시물 등록중 오류가 발생하였습니다.");
              $("#btnWrite2").prop("disabled", false);  // 글쓰기 버튼 활성화
           }
       });
   });
   
   $("#btnList2").on("click", function() {
      document.bbsForm.action = "/board/customerList";
      document.bbsForm.submit();
   });
});
</script>
</head>
<body>
<%@ include file="/WEB-INF/views/include/navigation.jsp" %>
<div class="container">
   <h2>게시물 쓰기</h2>
   <form name="writeForm2" id="writeForm2" method="post" enctype="multipart/form-data">   <!-- 파일 업로드할 때 쓰는 타입 multipart/form-data-->
      <input type="text" name="userName2" id="userName2" maxlength="20" value="${user2.userName2}" style="ime-mode:active;" class="form-control mt-4 mb-2" placeholder="이름을 입력해주세요." readonly />
      <input type="text" name="userEmail2" id="userEmail2" maxlength="30" value="${user2.userEmail2}" style="ime-mode:inactive;" class="form-control mb-2" placeholder="이메일을 입력해주세요." readonly />
      <input type="text" name="qnaHiBbsTitle" id="qnaHiBbsTitle" maxlength="100" style="ime-mode:active;" class="form-control mb-2" placeholder="제목을 입력해주세요." required />
      <div class="form-group">
         <textarea class="form-control" rows="10" name="qnaHiBbsContent" id="qnaHiBbsContent" style="ime-mode:active;" placeholder="내용을 입력해주세요" required></textarea>
      </div>
      <input type="file" id="qnaHiBbsFile" name="qnaHiBbsFile" class="form-control mb-2" placeholder="파일을 선택하세요." required />
      <div class="form-group row">
         <div class="col-sm-12">
            <button type="button" id="btnWrite2" class="btn btn-primary" title="저장">저장</button>
            <button type="button" id="btnList2" class="btn btn-secondary" title="리스트">리스트</button>
         </div>
      </div>
   </form>
   <form name="bbsForm" id="bbsForm" method="post">
      <input type="hidden" name="searchType" value="${searchType}" />
      <input type="hidden" name="searchValue" value="${searchValue}" />
      <input type="hidden" name="curPage" value="${curPage}" />
   </form>
</div>
</body>
</html>