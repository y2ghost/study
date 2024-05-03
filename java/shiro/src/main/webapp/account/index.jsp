<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page pageEncoding="UTF-8"%>
<jsp:include page="../include.jsp" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>登陆</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<style>
body {
    padding: 0 20px;
}
</style>
</head>
<body>
  <h2>针对已认证用户!</h2>
  <p>本页面模拟给认证用户使用.</p>
  <p>你已经登陆</p>
  <p><a href="<c:url value="/home.jsp"/>">返回主页</a></p>
  <p><a href="<c:url value="/logout"/>">退出登陆</a></p>
</body>
</html>

