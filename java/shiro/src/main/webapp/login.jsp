<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page pageEncoding="UTF-8"%>
<jsp:include page="include.jsp" />
<!DOCTYPE html>
<html>
<head>
<title>登陆</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<style>
body {
  padding-top: 20px;
}
</style>
</head>
<body>
  <div class="container">
    <div class="row">
      <div class="col-md-4 col-md-offset-4">
        <div class="panel panel-default">
          <div class="panel-heading">
            <h3 class="panel-title">请登陆</h3>
          </div>
          <div class="panel-body">
            <form name="loginform" action="" method="POST"
              accept-charset="UTF-8" role="form">
              <fieldset>
                <div class="form-group">
                  <input class="form-control" placeholder="用户名或邮箱"
                    name="username" type="text">
                </div>
                <div class="form-group">
                  <input class="form-control" placeholder="密码"
                    name="password" type="password" value="">
                </div>
                <div class="checkbox">
                  <label> <input name="rememberMe" type="checkbox"
                    value="true">记住我
                  </label>
                </div>
                <input class="btn btn-lg btn-success btn-block" type="submit"
                  value="登陆">
              </fieldset>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</body>
</html>

