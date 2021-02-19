<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/include/head.jsp" %>
<title>고객센터</title>
<script>
$(document).ready(function() {
    
	   $("#modalsend").on("click", function() {

         //값 보내기, 창 닫기
          document.qna.submit();
         self.close();
	         
	   });
	});



</script>
 <style>
        /* The Modal (background) */
        .modal {
            display: none; /* Hidden by default */
            position: fixed; /* Stay in place */
            z-index: 1; /* Sit on top */
            left: 0;
            top: 0;
            width: 100%; /* Full width */
            height: 100%; /* Full height */
            overflow: auto; /* Enable scroll if needed */
            background-color: rgb(0,0,0); /* Fallback color */
            background-color: rgba(0,0,0,0.4); /* Black w/ opacity */
        }
    
        /* Modal Content/Box */
        .modal-content {
            background-color: #fefefe;
            margin: 15% auto; /* 15% from the top and centered */
            padding: 20px;
            border: 1px solid #888;
            width: 30%; /* Could be more or less, depending on screen size */                          
        }
 
</style>
</head>
<body>

<%@ include file="/WEB-INF/views/include/teamNavigation.jsp" %>

    <!-- ======= Works Section ======= -->
    <section class="section site-portfolio">
      <div class="container">
        <div class="row mb-5 align-items-center">
          <div class="col-md-12 col-lg-6 mb-4 mb-lg-0" data-aos="fade-up">
            <a class="nav-link" href="#">
              <img src="/resources/images/travel.png" width="50" height="50" alt="" loading="lazy"></a>
          </div>
          <div class="col-md-12 text-center" data-aos="fade-up" data-aos-delay="100">
            <div id="filters" class="filters">
              <div class="card" style="width: 30rem; height: 30rem;">
  <img src="..." class="card-img-top" alt="...">
  <div class="card-body">
    <h5 class="card-title">고객의 소리</h5>
    <p class="card-text">내용을 입력하세요</p><br><br>
    <a href="/" class="btn btn-primary">보내기</a>
  </div>
</div>
            </div>
          </div>
        </div>
      <hr/>
<!-- @@@@@@@@@@@@@ -->



<script src="https://code.jquery.com/jquery-latest.js"></script> 
 

 
    <!-- The Modal -->
    <div id="myModal" class="modal">
 
      <!-- Modal content -->
      <div class="modal-content">
      <form name="qna" id="qna" method=get" action="/mainHome" target="_blank">
      
                <p style="text-align: center;"><span style="font-size: 14pt;"><b><span style="font-size: 24pt;">QnA</span></b></span></p>
                <p style="text-align: center; line-height: 1.5;"><br /></p>
                <p style="text-align: center; line-height: 1.5;"><span style="font-size: 14pt;">문의사항을 적어주세요</span></p>
                
                <div class="mb-3">
		           <label for="message-text" class="col-form-label">Message:</label>
		           <textarea class="form-control" id="messageText"></textarea>
		        </div>
                
                <div style="cursor:pointer;background-color:#FFDA29;text-align: center;padding-bottom: 10px;padding-top: 10px;" id=modalsend onClick="close_pop();">
                <span class="pop_bt" style="font-size: 13pt;" >
                     전송
                </span>
                </div>
            	<div style="cursor:pointer;background-color:#DDDDDD;text-align: center;padding-bottom: 10px;padding-top: 10px;" onClick="close_pop();">
                <span class="pop_bt" style="font-size: 13pt;" >
                     닫기
                </span>
                </div>
     </form>
            </div>
      </div>
 
    </div>
        <!--End Modal-->
 
 
    <script type="text/javascript">
      
        jQuery(document).ready(function() {
                $('#myModal').show();
        });
        //팝업 Close 기능
        function close_pop(flag) {
             $('#myModal').hide();
        };
        
      </script>


</body>
</html>   