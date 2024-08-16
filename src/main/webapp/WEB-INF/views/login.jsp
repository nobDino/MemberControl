<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<form method="post" action="/doLogin">
<table>
<tr><td>아이디</td><td><input type=text name=userid id=userid></td>
<tr><td>비밀번호</td><td><input type=password name=passwd id=passwd></td>
<tr><td colspan=2 style='text-align:center'>
</table>
<input type=submit value='로긴' id=btnLogin>&nbsp;<input type=reset value='비우기'>
 </form>
</body>
</html>