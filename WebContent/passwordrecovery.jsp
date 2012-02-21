<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.zkoss.org/jsp/zul" prefix="z" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>消防产品市场准入业务管理系统——密码找回</title>
</head>
<body>
<div align="center">
<img src="./image/files/top_bg.png">
<!--<div width="50%" align="left">-->
<!--		<img src="./image/common/title_login_4.gif">-->
<!--</div>-->
<div align="center" width="50%">
<z:init use="org.zkoss.zkplus.databind.AnnotateDataBinderInit" />
<form action="submit">

<z:page>
<z:window width="500px">
	<z:div align="left">
		<z:image src="./image/common/title_login_4.gif"/>
	</z:div>
</z:window>
<z:window id="jspPasswordModify" apply="openjframework.web.zk.PasswrodModifyController" border="normal" width="300px">
	<z:label value="　　请设置您的新密码，以字母开头，最少6位最多18位。设置完成后便可以通过新密码登录系统，请劳记你的密码！" style="color:#FF0000"/>
	<div align="justify">
		<br>
		<div>
			<z:label value="用户名："/>
			<z:label value="@{jspPasswordModify$composer.username}"/>
		</div>
		<br>
		<div>
			<z:label value="新密码："/>
			<z:textbox id="tbxPwd1" type="password" width="150px" value="@{jspPasswordModify$composer.password }" constraint="no empty,/^[a-zA-Z]\w{5,17}$/:密码必须以字母开头，长度在6-18之间， 只能包含字符、数字和下划线"></z:textbox>
		</div>
		<br>
		<div>
			<z:label value="确　认："/>
			<z:textbox id="tbxPwd2" type="password" width="150px" value="@{jspPasswordModify$composer.password2}" constraint="no empty:请再次输入密码"></z:textbox>
		</div>
	</div>
	<br>
	<z:button id="btnSubmit" label="提交" width="68" height="28" />
</z:window>
</z:page>
</form>
</div>
</div>
</body>
</html>