<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String messageText = request.getParameter("messageText");
%>
<%String title = request.getParameter("title");%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/include/head.jsp" %>
<title>관리자 요청</title>
<script>
function select2(){
	if(check1)
	{
	  const table = document.getElementById('demo1');
	  const table = document.getElementsByTagName
	  const newRow = table.insertRow(-1);
	  
	  const newCell1 = newRow.insertCell(0);
	  newCell1.innerText = title;
	}
}
function displayInfowindow(marker, title) {
	
	   var content = '<div style="padding:5px;z-index:1; width:200px;">' + title + '<br>' +
	    '<button id="btn1">일정 추가</button></div>';
	    infowindow.setContent(content);
	    infowindow.open(map, marker);
	    document.getElementById('btn1').onclick = function() {select2()};
	    document.getElementById('day1').onclick = function() {day1()};
	    document.getElementById('day2').onclick = function() {day2()};
	    document.getElementById('day3').onclick = function() {day3()};
	    document.getElementById('day4').onclick = function() {day4()};
	    document.getElementById('day5').onclick = function() {day5()};
	    document.getElementById('day6').onclick = function() {day6()};
	    document.getElementById('day7').onclick = function() {day7()};
	    document.getElementById('day8').onclick = function() {day8()};
	    document.getElementById('day9').onclick = function() {day9()};
	    document.getElementById('day10').onclick = function() {day10()};
	   
	    
	    function select2() {
	       
	       if(check1)
	       {
	         const table = document.getElementById('demo1');
	         const newRow = table.insertRow(-1);
	         
	         const newCell1 = newRow.insertCell(0);
	         newCell1.innerText = title;
	       }
	         
	       else if(check2)
	       {
	         const table = document.getElementById('demo2');
	         const newRow = table.insertRow(-1);
	         
	         const newCell1 = newRow.insertCell(0);
	         newCell1.innerText = title;       
	       }
	         
	       else if(check3)
	       {
	         const table = document.getElementById('demo3');
	         const newRow = table.insertRow(-1);
	         
	         const newCell1 = newRow.insertCell(0);
	         newCell1.innerText = title; 
	       }
	       
	       else if(check4)
	       {
	         const table = document.getElementById('demo4');
	         const newRow = table.insertRow(-1);
	         
	         const newCell1 = newRow.insertCell(0);
	         newCell1.innerText = title; 
	       }
	       
	       else if(check5)
	       {
	         const table = document.getElementById('demo5');
	         const newRow = table.insertRow(-1);
	         
	         const newCell1 = newRow.insertCell(0);
	         newCell1.innerText = title; 
	       }
	       
	       else if(check6)
	       {
	         const table = document.getElementById('demo6');
	         const newRow = table.insertRow(-1);
	         
	         const newCell1 = newRow.insertCell(0);
	         newCell1.innerText = title; 
	       }
	       
	       else if(check7)
	       {
	         const table = document.getElementById('demo7');
	         const newRow = table.insertRow(-1);
	         
	         const newCell1 = newRow.insertCell(0);
	         newCell1.innerText = title; 
	       }
	       
	       else if(check8)
	       {
	         const table = document.getElementById('demo8');
	         const newRow = table.insertRow(-1);
	         
	         const newCell1 = newRow.insertCell(0);
	         newCell1.innerText = title; 
	       }
	       
	       else if(check9)
	       {
	         const table = document.getElementById('demo9');
	         const newRow = table.insertRow(-1);
	         
	         const newCell1 = newRow.insertCell(0);
	         newCell1.innerText = title; 
	       }
	       
	       else if(check10)
	       {
	         const table = document.getElementById('demo10');
	         const newRow = table.insertRow(-1);
	         
	         const newCell1 = newRow.insertCell(0);
	         newCell1.innerText = title; 
	       }
	
	    }
}
</script>

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