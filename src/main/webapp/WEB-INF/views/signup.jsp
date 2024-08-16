<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>
</head>
<style>
td:nth-child(1) {
	text-align:right;
}
</style>
<body>
<form method=post action='/doSignup'>
<table>
<tr><td>아이디</td><td><input type=text name=userid></td></tr>
<tr><td>비밀번호</td><td><input type=password name=passwd></td></tr>
<tr><td>비밀번호확인</td><td><input type=password name=passwd1></td></tr>
<tr><td>실명</td><td><input type=text name=name></td></tr>
<tr><td>생년월일</td><td><input type="date" name=birthday></td></tr>
<tr><td>성별</td><td><input type=radio name=gender value='남성'>남성
					<input type=radio name=gender value="여성">여성</td>
</tr>
<tr><td>모바일번호</td><td><input type=text name=mobile></td></tr>
<tr><td>지역</td><td>
	<select name=region>
		<option value='덕양구'>덕양구</option>
		<option value='일산동구'>일산동구</option>
		<option value='일산서구'>일산서구</option>
	</select>
</td></tr>
<tr>
	<td>관심분야</td><td>
		<input type=checkbox name=favorate value=Java>Java
		<input type=checkbox name=favorate value=Javascript>Javascript
		<input type=checkbox name=favorate value=Python>Python<br>
		<input type=checkbox name=favorate value=MySQL>MySQL
		<input type=checkbox name=favorate value=Oracle>Oracle
		<input type=checkbox name=favorate value=React>React<br>
		<input type=checkbox name=favorate value=Spring>Spring
		<input type=checkbox name=favorate value="Node.js">Node.js
		<input type=checkbox name=favorate value="Next.js">Next.js
	</td>
</tr>
<tr>
	<td colspan=2 style='text-align:center'>
		<input type=submit value='회원가입'>&nbsp;
		<input type=reset value='비우기'>
	</td>
</table>
</form>
</body>
</html>