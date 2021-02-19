<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String messageText = request.getParameter("messageText");
%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/include/head.jsp" %>
<title>관리자 요청</title>


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
<script>
function (messageText) {
	
	   var content = '<div style="padding:5px;z-index:1; width:200px;">' + messageText + '<br>' ;

}
</script>
</body>
</html>   