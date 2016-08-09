<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>ATM</title>
	<link href="<c:url value="/resources/css/bootstrap.min.css"/>" rel="stylesheet"/>
	<link href="<c:url value="/resources/css/atmstyle.css"/>" rel="stylesheet" type="text/css"/>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-md-12 col-sm-6 head-block">
				<h2 class="colh2">Welcome to ATM <em>Reliable Bank</em></h2>
			</div>
			<div class="col-md-12 col-sm-6 main-block">
				<center>
					<form action="selection" method="get">
						<h1>To get started click Enterance</h1> 
						<br> 
						<input type="submit" value="Enterance">
					</form>
				</center>
			</div>
			<div class="col-md-12 col-sm-6 footer-block">
				<p>If you have any questions you can always contact customer support. Telephone number: <strong>+1 10 11781600</strong></p>
				<p>With best wishes, your <strong><em>"Reliable Bank"</em></strong>	
			</div>
		</div>
	</div>	
</body>
</html>