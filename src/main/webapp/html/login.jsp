<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>请登录</title>

</head>
<body>
<h1>请登录</h1>


ip:  ${ip} <br>
port: ${port} <br>

<form action="/login" method="post">
    <%--<input type="text" name="token"/>--%>
    <input type="submit" value="登录">
</form>

</body>
</html>
