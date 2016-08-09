<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>ATM selection operation</title>
	<link href="<c:url value="/resources/css/bootstrap.min.css"/>" rel="stylesheet"/>
	<link href="<c:url value="/resources/css/atmstyle.css"/>" rel="stylesheet" type="text/css"/>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-md-12 col-sm-6 head-block">
				<h2 class="colh2">ATM <em>Reliable Bank</em></h2>
			</div>
			<div class="col-md-8 col-sm-6 main-block">
				<h3>Operation selection:</h3>
				
				<a href="getcash" class="button-setting">Get cash</a>
				<br>
				<br>
				<a href="putcash" class="button-setting">Put cash</a>
				<br>
				<br>
				<form action="atm" method="post">
					<h4>If you want to finish work with the ATM, press the button "Exit".</h4>
					<input type="submit" value="Exit">
				</form>
			</div>
			<div class="col-md-4 col-sm-6 main-block">
				<form action="selection" method="post">
					<h4>To get the archive of log files, click button <p class="colGetArchive">"Get archive"</p></h4>
					<input type="submit" value="Get archive">
				</form>
			</div>
			<div class="col-md-12 col-sm-6 footer-block">
				<p>If you have any questions you can always contact customer support. Telephone number: <strong>+1 10 11781600</strong></p>
				<p>With best wishes, your <strong><em>"Reliable Bank"</em></strong>				
			</div>
		</div>
	</div>
</body>
</html>