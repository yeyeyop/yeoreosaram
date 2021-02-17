<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
   javax.servlet.RequestDispatcher requestDispatcher = request.getRequestDispatcher("/index");
   requestDispatcher.forward(request, response);
%>
<%-- 이름이 /index 일때 어디로 연결되라고 정의가 되어있다는 말임  --%>