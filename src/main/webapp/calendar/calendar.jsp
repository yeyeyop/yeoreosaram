<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.icia.web.util.HttpUtil" %>
<%! 
String fromDate = null;
String toDate = null;
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>달력 테스트</title>
<link rel="stylesheet" href="http://code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css"/>

<%
int toDate1 = HttpUtil.get(request, "toDate" , 0);
int fromDate1 = HttpUtil.get(request, "fromDate", 0);

if((toDate1 - fromDate1) + 1 > 10)
{
%>
   alert("x");
<%
}
%>

<script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
<script src="http://code.jquery.com/ui/1.11.4/jquery-ui.min.js"></script>
<script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/i18n/datepicker-ko.js"></script>
<script>
    $(function() {
        
        //오늘 날짜를 출력
        $("#today").text(new Date().toLocaleDateString());

        $.datepicker.setDefaults($.datepicker.regional['ko']); 
        
        // 시작일(fromDate)은 종료일(toDate) 이후 날짜 선택 불가
        // 종료일(toDate)은 시작일(fromDate) 이전 날짜 선택 불가

        //시작일
        $('#fromDate').datepicker({
            showOn: "both",                     // 달력을 표시할 타이밍 (both: focus or button)
            buttonImage: "pngegg.png", 
            buttonImageOnly : true,             
            buttonText: "날짜선택",
            dateFormat: "yymmdd",             
            changeMonth: true,                  //월을 이동하기 위한 선택상자 표시여부
            minDate: 0,                         //오늘 이전 날짜 선택 불가
            onClose: function( selectedDate ) {    
                // 시작일(fromDate) datepicker가 닫힐때
                // 종료일(toDate)의 선택할수있는 최소 날짜(minDate)를 선택한 시작일로 지정
                $("#toDate").datepicker( "option", "minDate", selectedDate );
            }                
        });

        //종료일
        $('#toDate').datepicker({
            showOn: "both", 
            buttonImage: "pngegg.png", 
            buttonImageOnly : true,
            buttonText: "날짜선택",
            dateFormat: "yymmdd",
            changeMonth: true,
            minDate: 0,
            onClose: function( selectedDate ) {
                $("#fromDate").datepicker( "option", "maxDate", selectedDate );
            }                
        });
        
        $('#dateValue').on('click', function(){
        	
           if(($('#toDate').val() - $('#fromDate').val()) + 1 <= 10)
             { 
             //종료일 시작일 두칸 다 비워져 있는 함수 시작
                 if(($('#toDate').val() == 0) || ($('#formDate').val() == 0)){
                    
                 //ㄴ종료일 todate 칸만 비워져 있는 함수 시작
                     if($('#fromDate').val()){//시작일에 값 넣기
                         alert("날짜선택");
                          return $('#toDate').focus();
                          $("#toDate").datepicker( "option", "minDate", selectedDate );
                  }
                 
                 //ㄴ종료일만 비워져 있는 함수 완료 
		         alert("날짜를 입력해주세요.");
		         return $('#fromDate').focus();
		         $("#today").text(new Date().toLocaleDateString());
		                   
                  }
             //두칸 다 비워져 있는 함수 완료
             
             //값 보내기, 창 닫기
               document.plan.submit();
                self.close();
             }
              
           else
             {
                 //시작일 포커스
                $('#fromDate').focus();
                alert("x");
             }
                
        });
    });
    
   
</script>
</head>
<body>
   <table>
   <form name="plan" id="plan" method="post" action="/join3.jsp" target="_blank">
      <tr>
         <td>오늘 날짜</td>
         <td> : <span id="today"></td>   
      </tr>
      <tr>
      
         <td><label for="fromDate">시작일</label></td>
         <td><input type="text" name="fromDate" id="fromDate"></td>
         <td>~</td>
      </tr>   
      <tr>
         <td><label for="toDate">종료일</label></td>
         <td><input type="text" name="toDate" id="toDate"><br></td>   
      </tr>   
      <tr>
         <td><input type="button" name="dateValue" id="dateValue" value="계획짜기" ></td>
      </tr>
   </form>
   </table>
</body>
</html>