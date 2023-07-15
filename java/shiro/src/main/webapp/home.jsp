<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="include.jsp" />
<!DOCTYPE html>
<html>
<head>
<title>shiro学习示例</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<style>
body {
  padding: 0 20px;
}
</style>
</head>
<body>
  <h1>shiro学习示例</h1>
  <p>您好<shiro:guest>游客</shiro:guest>
    <shiro:user>
      <%
      request.setAttribute("account", org.apache.shiro.SecurityUtils.getSubject().getPrincipals().oneByType(java.util.Map.class));
    %>
      <c:out value="${account.givenName}" />
    </shiro:user>
    !(
    <shiro:user>
      <a href="<c:url value="/logout"/>">退出</a>
    </shiro:user>
    <shiro:guest>
      <a href="<c:url value="/login.jsp"/>">登陆</a>
    </shiro:guest>
    )
  </p>

  <p>欢迎来到主页</p>
  <shiro:authenticated>
    <p>访问你的<a href="<c:url value="/account"/>">账户页面</a>.</p>
  </shiro:authenticated>
  <shiro:notAuthenticated>
    <p>请您登陆后访问<a href="<c:url value="/account"/>">账户页面</a></p>
  </shiro:notAuthenticated>

  <h2>角色信息</h2>
  <h3>你拥有的角色:</h3>
  <p>
    <shiro:hasRole name="Captains">Captains<br />
    </shiro:hasRole>
    <shiro:hasRole name="Officers">Bad Guys<br />
    </shiro:hasRole>
    <shiro:hasRole name="Enlisted">Enlisted<br />
    </shiro:hasRole>
  </p>

  <h3>你未拥有的角色:</h3>
  <p>
    <shiro:lacksRole name="Captains">Captains<br />
    </shiro:lacksRole>
    <shiro:lacksRole name="Officers">Officers<br />
    </shiro:lacksRole>
    <shiro:lacksRole name="Enlisted">Enlisted<br />
    </shiro:lacksRole>
  </p>

  <h2>权限信息</h2>
  <ul>
    <li>你<shiro:lacksPermission name="ship:NCC-1701-D:command">
        <b>不</b></shiro:lacksPermission>能命令<code>NCC-1701-D</code>星际飞船!
    </li>
    <li>你<shiro:lacksPermission name="user:${account.username}:edit">
        <b>不</b></shiro:lacksPermission>能编辑${account.username}用户!
    </li>
  </ul>
</body>
</html>

